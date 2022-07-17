package studio.pixellite.shared.group.impl;

import org.bukkit.entity.Player;
import studio.pixellite.shared.group.PrimaryGroupTracker;

/**
 * A simple implementation of {@link PrimaryGroupTracker}.
 */
public class SimplePrimaryGroupTracker implements PrimaryGroupTracker {
  @Override
  public String getPrimaryGroupRaw(Player player) {
    return "default";
  }

  @Override
  public String getPrimaryGroupFormatted(Player player) {
    return "Default";
  }

  @Override
  public boolean comparePrimaryGroups(Player player1, Player player2) {
    return false;
  }

  @Override
  public void setPermission(Player player, String permission, boolean value) {
    // do nothing
  }
}
