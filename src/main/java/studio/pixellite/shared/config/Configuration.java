package studio.pixellite.shared.config;

import studio.pixellite.shared.SharedPlugin;

/**
 * The primary plugin configuration.
 */
public class Configuration extends AbstractConfiguration {
  // stored values are effectively final
  private boolean joinMessagesEnabled;
  private String joinMessage;
  private String leaveMessage;

  public Configuration(SharedPlugin plugin) {
    super(plugin, "config.yml");

    // obtain config values
    getValues();
  }

  /**
   * Gets and saves all the configuration values to this instance.
   */
  private void getValues() {
    this.joinMessagesEnabled = getBoolean("join-messages", "enabled");
    this.joinMessage = getString("join-messages", "join");
    this.leaveMessage = getString("join-messages", "leave");
  }

  public boolean isJoinMessagesEnabled() {
    return joinMessagesEnabled;
  }

  public String getJoinMessage() {
    return joinMessage;
  }

  public String getLeaveMessage() {
    return leaveMessage;
  }
}
