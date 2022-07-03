package studio.pixellite.springfall.command.module.player;

import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.jetbrains.annotations.NotNull;

public class EnderChestModule implements TerminableModule {
  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPlayer()
            .assertPermission("pixellite.command.enderchest")
            .handler(c -> {
              c.sender().openInventory(c.sender().getEnderChest());
              Players.msg(c.sender(), "&3Opening your ender chest...");
            })
            .registerAndBind(consumer, "enderchest", "echest");
  }
}
