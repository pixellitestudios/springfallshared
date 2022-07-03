package studio.pixellite.springfall.command.group;

import org.bukkit.entity.Player;

/**
 * An abstraction for tracking a player's primary group. Implementations may vary depending on
 * the permissions plugin installed.
 */
public interface PrimaryGroupTracker {
  /**
   * Gets the raw name of the given player's primary group.
   *
   * @param player the player
   * @return the player's primary group name
   */
  String getPrimaryGroupRaw(Player player);

  /**
   * Gets the formatted name of the player's primary group. Internally uses
   * {@link #getPrimaryGroupRaw(Player)}.
   *
   * @param player the player
   * @return the player's primary group name in formatted form
   */
  String getPrimaryGroupFormatted(Player player);

  /**
   * Compares two players' primary groups to see if one has a higher weight than the other.
   *
   * @param player1 the primary player to compare
   * @param player2 the secondary player  to compare
   * @return if player1 has a higher weight than player2.
   */
  boolean comparePrimaryGroups(Player player1, Player player2);

  /**
   * Sets a permission for a player.
   *
   * @param player the player to set the permission for
   * @param permission the permission to set
   * @param value the boolean value to set the permission to
   */
  void setPermission(Player player, String permission, boolean value);
}
