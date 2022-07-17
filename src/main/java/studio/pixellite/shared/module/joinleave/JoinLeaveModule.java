package studio.pixellite.shared.module.joinleave;

import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import net.kyori.adventure.text.Component;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import studio.pixellite.shared.SharedPlugin;
import studio.pixellite.shared.utils.AdventureUtils;

public class JoinLeaveModule implements TerminableModule {
  private final SharedPlugin plugin;

  public JoinLeaveModule(SharedPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Events.subscribe(PlayerJoinEvent.class)
            .handler(e -> {
              if(plugin.getConfiguration().isJoinMessagesEnabled()) {
                e.joinMessage(AdventureUtils.deserialize(plugin.getConfiguration()
                        .getJoinMessage()
                        .replace("{player}", e.getPlayer().getName())));
              } else {
                e.joinMessage(Component.text(""));
              }
            }).bindWith(consumer);

    Events.subscribe(PlayerQuitEvent.class)
            .handler(e -> {
              if(plugin.getConfiguration().isJoinMessagesEnabled()) {
                e.quitMessage(AdventureUtils.deserialize(plugin.getConfiguration()
                        .getLeaveMessage()
                        .replace("{player}", e.getPlayer().getName())));
              } else {
                e.quitMessage(Component.text(""));
              }
            }).bindWith(consumer);
  }
}
