package studio.pixellite.springfall.command.module.player;

import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.jetbrains.annotations.NotNull;

public class AnvilModule implements TerminableModule {
  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPlayer()
            .assertPermission("pixellite.command.anvil")
            .handler(c -> {
              c.sender().openAnvil(null, true);
              Players.msg(c.sender(), "&3Opening an anvil...");
            })
            .registerAndBind(consumer, "anvil");
  }
}
