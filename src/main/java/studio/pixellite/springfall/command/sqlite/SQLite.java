package studio.pixellite.springfall.command.sqlite;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.lucko.helper.terminable.Terminable;
import me.lucko.helper.utils.UndashedUuids;
import studio.pixellite.springfall.command.CommandPlugin;
import studio.pixellite.springfall.command.user.ExportedUserData;
import studio.pixellite.springfall.command.user.User;
import studio.pixellite.springfall.command.utils.StringUtils;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Interacts with the plugin's SQLite database.
 *
 * <p>Under normal circumstances using a traditional MySQL and/or MariaDB database would be
 * a much more efficient and performant solution. However, considering that the plugin is storing
 * a very limited amount of data and the server-only nature of the data, using a local database
 * is a more sensible solution putting aside the performance losses with the one-write only nature
 * of drivers such as SQLite.</p>
 *
 * <p>TODO: Move implementation to H2.</p>
 */
public class SQLite implements Terminable {
  private static final String USER_SCHEMA = StringUtils.concat("CREATE TABLE IF NOT EXISTS ",
          "users (",
          "uuid BINARY(16) PRIMARY KEY, ",
          "flight_enabled BOOLEAN NOT NULL)");

  // ignore column limit
  private static final String GET_USER = "SELECT flight_enabled FROM users WHERE uuid=?";
  private static final String INSERT_USER = "INSERT INTO users (uuid, flight_enabled) VALUES(?, ?) ON CONFLICT DO UPDATE SET flight_enabled=?";

  /** The primary plugin instance. */
  private final CommandPlugin plugin;

  /** Manages the primary connection to this database. */
  private final SQLiteConnectionManager manager;

  /** The worker thread for completing SQLite tasks. */
  private final ExecutorService worker;

  public SQLite(CommandPlugin plugin) {
    this.plugin = plugin;
    this.manager = new SQLiteConnectionManager(plugin);
    this.worker = Executors.newCachedThreadPool(new ThreadFactoryBuilder()
            .setDaemon(true)
            .setNameFormat("pixelliteconviencecommands-sqlite-worker")
            .build());
  }

  /**
   * Quietly prints to the console a SQL exception.
   *
   * @param exception the exception to print
   */
  private void handleErrorQuietly(SQLException exception) {
    plugin.getLogger().severe("SQL ERROR: " + exception.getMessage());
  }

  /**
   * Closes the database by closing any subsequent resources for it.
   */
  @Override
  public void close() {
    worker.shutdown();
    manager.close();
  }

  /**
   * Initializes the database by applying the schema.
   */
  public void init() {
    worker.execute(() -> {
      try (Statement stmt = manager.getConnection().createStatement()) {
        stmt.execute(USER_SCHEMA);
      } catch (SQLException e) {
        handleErrorQuietly(e);
      }
    });
  }

  /**
   * Loads a user, making a new one if nothing was found.
   *
   * @param uniqueId the unique id of this user
   * @param username the name of this user
   * @return a future wrapping the new user object
   */
  public CompletableFuture<User> loadOrMakeUser(UUID uniqueId, String username) {
    return CompletableFuture.supplyAsync(() -> {
      User.Builder builder = User.of(uniqueId, username);

      // get user's raw data
      try (PreparedStatement stmt = manager.getConnection().prepareStatement(GET_USER)) {
        stmt.setString(1, UndashedUuids.toString(uniqueId));

        try(ResultSet rs = stmt.executeQuery()) {
          if(rs.next()) {
            boolean flightEnabled = rs.getBoolean("flight_enabled");
            builder.flightEnabled(flightEnabled);
          }
        }
      } catch (SQLException e) {
        handleErrorQuietly(e);
      }

      User user = builder.build();
      saveUser(user);
      return user;
    }, worker);
  }

  /**
   * Saves a user to the database.
   *
   * @param user the user to save
   * @return a future
   */
  public CompletableFuture<Void> saveUser(User user) {
    return CompletableFuture.runAsync(() -> {
      // save raw player data
      try (PreparedStatement stmt = manager.getConnection().prepareStatement(INSERT_USER)) {
        ExportedUserData data = user.export();

        stmt.setString(1, UndashedUuids.toString(data.getUniqueId()));
        stmt.setBoolean(2, data.isFlightEnabled());
        stmt.setBoolean(3, data.isFlightEnabled());

        stmt.executeUpdate();
      } catch (SQLException e) {
        handleErrorQuietly(e);
      }
    }, worker);
  }
}
