package studio.pixellite.springfall.command.user;

import java.util.UUID;

/**
 * An immutable object that exports a copy of a user's data. Used for SQL storage.
 */
public class ExportedUserData {
  private final UUID uniqueId;
  private final boolean flightEnabled;

  protected ExportedUserData(User user) {
    this.uniqueId = user.getUniqueId();
    this.flightEnabled = user.getPersistentTriggerStore().isFlightEnabled();
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public boolean isFlightEnabled() {
    return flightEnabled;
  }
}
