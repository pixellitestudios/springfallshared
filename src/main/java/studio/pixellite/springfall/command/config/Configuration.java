package studio.pixellite.springfall.command.config;

import studio.pixellite.springfall.command.CommandPlugin;

/**
 * The primary plugin configuration.
 */
public class Configuration extends AbstractConfiguration {
  // stored values are effectively final
  private boolean joinMessagesEnabled;
  private String joinMessage;
  private String leaveMessage;

  public Configuration(CommandPlugin plugin) {
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
