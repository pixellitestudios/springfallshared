package studio.pixellite.springfall.command.module.player;

import me.lucko.helper.Commands;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GamemodeShortcutModule implements TerminableModule {
  private static final String PERMISSION = "minecraft.command.gamemode";

  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPlayer()
            .assertPermission(PERMISSION)
            .handler(c -> changeGamemode(c.sender(), GameMode.CREATIVE))
            .registerAndBind(consumer, "gmc");

    Commands.create()
            .assertPlayer()
            .assertPermission(PERMISSION)
            .handler(c -> changeGamemode(c.sender(), GameMode.ADVENTURE))
            .registerAndBind(consumer, "gma");

    Commands.create()
            .assertPlayer()
            .assertPermission(PERMISSION)
            .handler(c -> changeGamemode(c.sender(), GameMode.SPECTATOR))
            .registerAndBind(consumer, "gmsp");

    Commands.create()
            .assertPlayer()
            .assertPermission(PERMISSION)
            .handler(c -> changeGamemode(c.sender(), GameMode.SURVIVAL))
            .registerAndBind(consumer, "gms");
  }

  private void changeGamemode(Player player, GameMode gameMode) {
    player.setGameMode(gameMode);
    Players.msg(player, "&3Set your gamemode to &b" + gameMode);
  }
}
