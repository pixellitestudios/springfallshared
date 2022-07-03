package studio.pixellite.springfall.command.module.staff;

import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import studio.pixellite.springfall.command.utils.StringUtils;

public class BroadcastModule implements TerminableModule {
  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPermission("pixellite.command.broadcast")
            .assertUsage("<message>")
            .handler(c -> {
              String message = StringUtils.joinList(c.args());

              for(Player player : Bukkit.getOnlinePlayers()) {
                Players.msg(player, "&c[Broadcast] &f" + message);
              }
            })
            .registerAndBind(consumer, "broadcast", "bc");
  }
}
