package studio.pixellite.springfall.command.module.staff;

import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.pixellite.springfall.command.CommandPlugin;
import studio.pixellite.springfall.command.utils.StringUtils;

public class TeleportModule implements TerminableModule {
  private final CommandPlugin plugin;

  public TeleportModule(CommandPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPlayer()
            .assertPermission("pixellite.command.teleportsilent")
            .assertUsage("<location>")
            .handler(c -> {
              Player player = c.sender();

              if(c.args().size() == 1) {
                String targetName = c.arg(0).parseOrFail(String.class);
                Player target = Bukkit.getPlayer(targetName);

                if(target == null) {
                  Players.msg(player, "&cThat player isn't online.");
                  return;
                }

                player.teleport(target);

                if(plugin.getPrimaryGroupTracker().comparePrimaryGroups(player, target)) {
                  Players.msg(player, "&3Silently teleporting to &b" + target.getName() + "'s &3location.");
                } else {
                  Players.msg(player, "&3Teleporting to &b" + target.getName() + "'s location.");
                  Players.msg(target, "&b" + player.getName() + " &3has teleported to your location.");
                }

                return;
              }

              if(c.args().size() == 3) {
                double x = c.arg(0).parseOrFail(Double.class);
                double y = c.arg(1).parseOrFail(Double.class);
                double z = c.arg(2).parseOrFail(Double.class);
                Location playerLoc = player.getLocation();

                player.teleport(new Location(player.getWorld(),
                        x,
                        y,
                        z,
                        playerLoc.getYaw(),
                        playerLoc.getPitch()));

                Players.msg(player, StringUtils.concat("&3Teleporting you to &b",
                        Double.toString(x),
                        ", ",
                        Double.toString(y),
                        ", ",
                        Double.toString(z),
                        "&3."));
              }
            })
            .registerAndBind(consumer, "tp", "teleport");

    Commands.create()
            .assertPlayer()
            .assertPermission("pixellite.command.teleport.here")
            .assertUsage("<player>")
            .handler(c -> {
              Player player = c.sender();

              String targetName = c.arg(0).parseOrFail(String.class);
              Player target = Bukkit.getPlayer(targetName);

              if(target == null) {
                Players.msg(player, "&cThat player isn't online.");
                return;
              }

              target.teleport(player);

              if(plugin.getPrimaryGroupTracker().comparePrimaryGroups(player, target)) {
                Players.msg(player, "&3Silently teleporting &b" + target.getName() + " &3to your location.");
              } else {
                Players.msg(target, "&3Teleporting to &b" + target.getName() + "'s &3location.");
                Players.msg(player, "&3Teleporting &b" + target.getName() + " &3to your location.");
              }
            })
            .registerAndBind(consumer, "tphere", "teleporthere");
  }
}
