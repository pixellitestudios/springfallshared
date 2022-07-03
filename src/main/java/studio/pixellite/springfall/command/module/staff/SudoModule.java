package studio.pixellite.springfall.command.module.staff;

import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SudoModule implements TerminableModule {
  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPermission("pixellite.command.sudo")
            .assertUsage("<player> <command>")
            .handler(c -> {
              Player target = Bukkit.getPlayer(c.arg(0).parseOrFail(String.class));
              String command = c.arg(1).parseOrFail(String.class);

              if(target == null) {
                Players.msg(c.sender(), "&cThat player is not online.");
                return;
              }

              Players.msg(c.sender(), "&3Forcing &b" + target.getName() + " &3to run command &b" + command + ".");
              target.performCommand(command);
            })
            .registerAndBind(consumer, "sudo");
  }
}
