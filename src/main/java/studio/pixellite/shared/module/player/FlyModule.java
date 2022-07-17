package studio.pixellite.shared.module.player;

import me.lucko.helper.Commands;
import me.lucko.helper.Events;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import studio.pixellite.shared.SharedPlugin;
import studio.pixellite.shared.user.User;

public class FlyModule implements TerminableModule {
  private final SharedPlugin plugin;

  public FlyModule(SharedPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPlayer()
            .assertPermission("pixellite.command.fly")
            .handler(c -> {
              User user = plugin.getUserManager().getUserOrNull(c.sender().getUniqueId());

              if(user == null) {
                return;
              }

              boolean updatedFlight = user.getPersistentTriggerStore().updateFlightEnabled();
              c.sender().setAllowFlight(updatedFlight);
              plugin.getSqLite().saveUser(user);

              if(updatedFlight) {
                Players.msg(c.sender(), "&3Flight enabled.");
              } else {
                Players.msg(c.sender(), "&3Flight disabled.");
              }
            })
            .registerAndBind(consumer, "fly");

    Events.subscribe(PlayerJoinEvent.class)
            .handler(e -> {
              User user = plugin.getUserManager().getUserOrNull(e.getPlayer().getUniqueId());
              boolean flightEnabled = user.getPersistentTriggerStore().isFlightEnabled();

              if(flightEnabled) {
                e.getPlayer().setAllowFlight(true);
              }
            }).bindWith(consumer);
  }
}
