package studio.pixellite.springfall.command.user;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.lucko.helper.Events;
import me.lucko.helper.text3.Text;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import studio.pixellite.springfall.command.CommandPlugin;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages, loads, and unloads user objects.
 */
public class UserManager {
  /** The backing map instance for this manager. */
  private final ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();

  /** The primary plugin instance. */
  private final CommandPlugin plugin;

  /** A cache that stores recently disconnected user objects. This is to save time & effort in the
   * event that a user leaves then immediately rejoins. */
  private final Cache<UUID, User> recentlyDisconnected = Caffeine.newBuilder()
          .expireAfterWrite(Duration.ofMinutes(2))
          .build();

  public UserManager(CommandPlugin plugin) {
    this.plugin = plugin;
    new ConnectionListener().initListeners();
  }

  /**
   * Registers a user if there is not already one present in the cache.
   *
   * @param user the user to register
   */
  public void registerUser(User user) {
    users.putIfAbsent(user.getUniqueId(), user);
  }

  /**
   * Gets a user from the cache.
   *
   * @param uniqueId the unique id of this user
   * @return an optional wrapping the user, empty if nothing was found
   */
  public Optional<User> getUser(UUID uniqueId) {
    return Optional.ofNullable(users.get(uniqueId));
  }

  /**
   * Gets a user from the cache.
   *
   * @param uniqueId the unique id of this user
   * @return the user, null if nothing was found
   */
  public User getUserOrNull(UUID uniqueId) {
    return users.get(uniqueId);
  }

  /**
   * Removes a user from this cache.
   *
   * @param uniqueId the unique id of this user
   * @return the removed user object
   */
  public User unregisterUser(UUID uniqueId) {
    return users.remove(uniqueId);
  }

  /**
   * Checks to see if a user is registered in the cache.
   *
   * @param uniqueId the unique id of the user
   * @return if the user is registered in the cache
   */
  public boolean isRegistered(UUID uniqueId) {
    return users.containsKey(uniqueId);
  }

  /**
   * A listener subclass for handling the loading and unloading of user objects.
   */
  private final class ConnectionListener {
    public void initListeners() {
      // listening on highest priority to allow for other plugins to handle player logins first
      Events.subscribe(AsyncPlayerPreLoginEvent.class, EventPriority.HIGHEST)
              .handler(this::handleAsnycLogin)
              .bindWith(plugin);

      Events.subscribe(PlayerLoginEvent.class, EventPriority.HIGHEST)
              .handler(this::handlePlayerLogin)
              .bindWith(plugin);

      Events.subscribe(PlayerQuitEvent.class)
              .handler(this::handlePlayerQuit)
              .bindWith(plugin);
    }

    private void handleAsnycLogin(AsyncPlayerPreLoginEvent e) {
      // ensure that the event wasn't cancelled by another plugin
      if(e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
        return;
      }

      // get the player's associated user object.
      User user = recentlyDisconnected.getIfPresent(e.getUniqueId());

      if(user == null) {
        // load the player's user object from the database, creating one if needed, because
        // we aren't carrying any time-sensitive or otherwise important data (and other data
        // is updated on change), we have no need to save the user back to the database if a
        // new one is created
        //
        // joining as to prevent the login from continuing until the user's data is loaded
        user = plugin.getSqLite().loadOrMakeUser(e.getUniqueId(), e.getName()).join();
      }

      // add the user to the manager
      registerUser(user);
    }

    @SuppressWarnings("deprecation")
    private void handlePlayerLogin(PlayerLoginEvent e) {
      // check to ensure that the user object successfully loaded, if it didn't cancel the
      // event and tell the joining player that something went wrong.
      User user = getUserOrNull(e.getPlayer().getUniqueId());

      if(user == null) {
        e.setKickMessage(Text.colorize("&cThere was an error loading some of your player data." +
                " Please rejoin or contact an administrator."));
        e.setResult(PlayerLoginEvent.Result.KICK_FULL);
        return;
      }

      if(e.getResult() != PlayerLoginEvent.Result.ALLOWED) {
        // deny and cleanup data, for some reason a plugin cancelled the event between async login
        // and here
        unregisterUser(user.getUniqueId());
      }
    }

    private void handlePlayerQuit(PlayerQuitEvent e) {
      // get the user object
      User user = getUserOrNull(e.getPlayer().getUniqueId());

      // in the off-chance the object is null, return
      if(user == null) {
        return;
      }

      // perform one last data save and then remove them from the manager's cache
      plugin.getSqLite().saveUser(user);
      unregisterUser(user.getUniqueId());
      recentlyDisconnected.put(user.getUniqueId(), unregisterUser(user.getUniqueId()));
    }
  }
}