package studio.pixellite.springfall.command.module.staff;

import me.lucko.helper.Commands;
import me.lucko.helper.Events;
import me.lucko.helper.event.filter.EventFilters;
import me.lucko.helper.metadata.Metadata;
import me.lucko.helper.metadata.MetadataKey;
import me.lucko.helper.metadata.MetadataMap;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import studio.pixellite.springfall.command.CommandPlugin;
import studio.pixellite.springfall.command.user.User;

public class StaffModeModule implements TerminableModule {
  /** A metadata key containing all of the players currently in staff mode. */
  private static final MetadataKey<GameMode> IN_STAFF_MODE =
          MetadataKey.create("staff-mode-active", GameMode.class);

  /** A metadata key of players who were previously hidden. */
  private static final MetadataKey<Boolean> PREVIOUSLY_HIDDEN =
          MetadataKey.create("staff-mode-previously-hidden", Boolean.class);

  private static final String HIDDEN_PERMISSION = "pixellite.hidden";

  private final CommandPlugin plugin;

  public StaffModeModule(CommandPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public void setup(@NotNull TerminableConsumer consumer) {
    Commands.create()
            .assertPlayer()
            .assertPermission("pixellite.commands.staffmode")
            .handler(c -> {
              MetadataMap metadata = Metadata.provideForPlayer(c.sender());

              if(metadata.has(IN_STAFF_MODE)) {
                disableStaffMode(c.sender(), metadata);
              } else {
                enableStaffMode(c.sender(), metadata);
              }
            })
            .registerAndBind(consumer, "staffmode", "sm");

    Events.subscribe(PlayerQuitEvent.class)
            .filter(EventFilters.playerHasMetadata(IN_STAFF_MODE))
            .handler(e ->
              // if the player is in staff mode, exit staff mode
              disableStaffMode(e.getPlayer(), Metadata.provideForPlayer(e.getPlayer()))
            ).bindWith(consumer);
  }

  private void enableStaffMode(Player player, MetadataMap metadata) {
    for(Player p : Bukkit.getOnlinePlayers()) {
      if(!p.hasPermission("pixellite.staff")) {
        p.hidePlayer(plugin, player);
      }
    }

    metadata.put(IN_STAFF_MODE, player.getGameMode());

    if(player.hasPermission(HIDDEN_PERMISSION)) {
      metadata.put(PREVIOUSLY_HIDDEN, true);
    } else {
      plugin.getPrimaryGroupTracker().setPermission(player, HIDDEN_PERMISSION, true);
    }

    player.setGameMode(GameMode.SPECTATOR);
    Players.msg(player, "&aEnabled staff mode. You are now invisible to non-staff players.");
  }

  private void disableStaffMode(Player player, MetadataMap metadata) {
    User user = plugin.getUserManager().getUserOrNull(player.getUniqueId());

    for(Player p : Bukkit.getOnlinePlayers()) {
      p.showPlayer(plugin, player);
    }

    // update the player's gamemode
    GameMode previousGamemode = metadata.getOrDefault(IN_STAFF_MODE, GameMode.SURVIVAL);
    player.setGameMode(previousGamemode);

    // set the player's fly state if necessary
    if(user != null && user.getPersistentTriggerStore().isFlightEnabled()) {
      player.setAllowFlight(true);
      player.setFlying(true);
    }

    // check if we need to update the player's hidden permission
    if(!metadata.has(PREVIOUSLY_HIDDEN)) {
      plugin.getPrimaryGroupTracker().setPermission(player, HIDDEN_PERMISSION, false);
    }

    // cleanup metadata
    metadata.remove(IN_STAFF_MODE);
    metadata.remove(PREVIOUSLY_HIDDEN);
  }
}
