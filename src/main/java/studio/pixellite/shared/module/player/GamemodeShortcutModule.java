package studio.pixellite.shared.module.player;

import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GamemodeShortcutModule implements TerminableModule {
  private static final String PERMISSION = "minecraft.command.gamemode";

  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPlayer()
            .assertPermission(PERMISSION)
            .handler(c -> changeGamemode(c.sender(), "creative"))
            .registerAndBind(consumer, "gmc");

    Commands.create()
            .assertPlayer()
            .assertPermission(PERMISSION)
            .handler(c -> changeGamemode(c.sender(), "adventure"))
            .registerAndBind(consumer, "gma");

    Commands.create()
            .assertPlayer()
            .assertPermission(PERMISSION)
            .handler(c -> changeGamemode(c.sender(), "spectator"))
            .registerAndBind(consumer, "gmsp");

    Commands.create()
            .assertPlayer()
            .assertPermission(PERMISSION)
            .handler(c -> changeGamemode(c.sender(), "survival"))
            .registerAndBind(consumer, "gms");
  }

  private void changeGamemode(Player player, String gameMode) {
    player.performCommand("gamemode " + gameMode);
  }
}
