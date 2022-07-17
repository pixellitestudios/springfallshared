package studio.pixellite.shared.user;

import studio.pixellite.shared.utils.AtomicUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A class built solely to store {@link AtomicBoolean} values for a user that are persistently
 * stored within a database.
 *
 * <p>Properties and methods within this object should be entirely atomic and thread-safe.</p>
 */
public class PersistentTriggerStore {
  private final AtomicBoolean flightEnabled = new AtomicBoolean(false);

  /*
   * Flight enabled methods
   */

  public void setFlightEnabled(boolean flightEnabled) {
    this.flightEnabled.set(flightEnabled);
  }

  public boolean isFlightEnabled() {
    return flightEnabled.get();
  }

  public boolean updateFlightEnabled() {
    return AtomicUtils.negate(flightEnabled);
  }
}
