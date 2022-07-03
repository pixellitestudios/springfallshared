package studio.pixellite.springfall.command.group.impl;

import me.lucko.helper.Services;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.entity.Player;
import studio.pixellite.springfall.command.group.PrimaryGroupTracker;
import studio.pixellite.springfall.command.utils.StringUtils;

/**
 * An implementation of {@link PrimaryGroupTracker} using LuckPerms.
 *
 * <p>While the majority of the LuckPerms api may be thread safe, the individual actions
 * inside this thread are not.</p>
 */
public class LPPrimaryGroupTracker implements PrimaryGroupTracker {
  private final LuckPerms luckPerms = Services.get(LuckPerms.class).orElseThrow();

  @Override
  public String getPrimaryGroupRaw(Player player) {
    return luckPerms.getPlayerAdapter(Player.class).getUser(player).getPrimaryGroup();
  }

  @Override
  public String getPrimaryGroupFormatted(Player player) {
    return StringUtils.capitalizeFirst(getPrimaryGroupRaw(player));
  }

  @Override
  public boolean comparePrimaryGroups(Player player1, Player player2) {
    User user1 = luckPerms.getPlayerAdapter(Player.class).getUser(player1);
    User user2 = luckPerms.getPlayerAdapter(Player.class).getUser(player2);

    Group group1 = luckPerms.getGroupManager().getGroup(user1.getPrimaryGroup());
    Group group2 = luckPerms.getGroupManager().getGroup(user2.getPrimaryGroup());

    if(group1 == null || group2 == null) {
      return false;
    }

    return group1.getWeight().orElse(0) > group2.getWeight().orElse(0);
  }

  @Override
  public void setPermission(Player player, String permission, boolean value) {
    if(value) {
      luckPerms.getUserManager().modifyUser(player.getUniqueId(), user ->
              user.data().add(PermissionNode.builder(permission).build())
      );
      return;
    }

    luckPerms.getUserManager().modifyUser(player.getUniqueId(), user ->
            user.data().remove(PermissionNode.builder(permission).build())
    );
  }
}
