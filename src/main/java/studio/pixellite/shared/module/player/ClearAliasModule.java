package studio.pixellite.shared.module.player;

import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.jetbrains.annotations.NotNull;

public class ClearAliasModule implements TerminableModule {
  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPlayer()
            .assertPermission("pixellite.command.alias.clear")
            .handler(c -> {
              c.sender().getInventory().clear();
              Players.msg(c.sender(), "&3Cleared your inventory.");
            })
            .registerAndBind(consumer, "c");
  }
}
