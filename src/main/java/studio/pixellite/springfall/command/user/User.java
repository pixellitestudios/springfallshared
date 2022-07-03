package studio.pixellite.springfall.command.user;

import me.lucko.helper.terminable.module.TerminableModule;

import java.util.UUID;

/**
 * Represents an online user.
 *
 * <p>This class and it's subsequent properties are thread safe. That being said, All interactions
 * with this class' properties and <b>physical player entities</b> should be handled synchronously
 * and outside of this class' parameters (such as within an {@link TerminableModule}.</p>
 *
 * <p>Construction of this object is to be handled within the {@link Builder} subclass. Typically
 * speaking, construction only needs to take place when a user is loaded from the database, or
 * on a player's first connect generation.</p>
 */
public class User {
  /** The builder subclass for generating User objects. */
  public static final class Builder {
    private final UUID uniqueId;
    private final String username;

    private final PersistentTriggerStore persistentTriggerStore = new PersistentTriggerStore();

    public Builder(UUID uniqueId, String username) {
      this.uniqueId = uniqueId;
      this.username = username;
    }

    // if a value for if flight is enabled is not entered, this will default
    // to 0.
    public Builder flightEnabled(boolean flightEnabled) {
      persistentTriggerStore.setFlightEnabled(flightEnabled);
      return this;
    }

    public User build() {
      return new User(this);
    }
  }

  /**
   * Returns a new builder for a new user object.
   *
   * <p>Parameters passed through into this method should match with the player entity that
   * this user is emulating.</p>
   *
   * @param uniqueId the user's unique Id
   * @param username the user's username
   * @return the new builder object
   * @throws IllegalArgumentException if the username is invalid
   */
  public static Builder of(UUID uniqueId, String username) {
    if(username.length() > 16) {
      throw new IllegalArgumentException("Invalid username (more than 16 chars)");
    }
    return new Builder(uniqueId, username);
  }

  private final UUID uniqueId;
  private final String username;
  private final PersistentTriggerStore persistentTriggerStore;

  private User(Builder builder) {
    this.uniqueId = builder.uniqueId;
    this.username = builder.username;
    this.persistentTriggerStore = builder.persistentTriggerStore;
  }

  /**
   * Exports this user's data to an immutable export object. This object is not mutable and does
   * not represent a user's active state of data.
   *
   * @return the copy of the user's data
   */
  public ExportedUserData export() {
    return new ExportedUserData(this);
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public String getUsername() {
    return username;
  }

  public PersistentTriggerStore getPersistentTriggerStore() {
    return persistentTriggerStore;
  }
}