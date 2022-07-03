package studio.pixellite.springfall.command;

import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bukkit.Bukkit;
import studio.pixellite.springfall.command.config.Configuration;
import studio.pixellite.springfall.command.group.PrimaryGroupTracker;
import studio.pixellite.springfall.command.group.impl.LPPrimaryGroupTracker;
import studio.pixellite.springfall.command.group.impl.SimplePrimaryGroupTracker;
import studio.pixellite.springfall.command.module.joinleave.JoinLeaveModule;
import studio.pixellite.springfall.command.module.player.AnvilModule;
import studio.pixellite.springfall.command.module.player.EnderChestModule;
import studio.pixellite.springfall.command.module.player.FlyModule;
import studio.pixellite.springfall.command.module.player.GamemodeShortcutModule;
import studio.pixellite.springfall.command.module.staff.BroadcastModule;
import studio.pixellite.springfall.command.module.staff.StaffModeModule;
import studio.pixellite.springfall.command.module.staff.SudoModule;
import studio.pixellite.springfall.command.module.staff.TeleportModule;
import studio.pixellite.springfall.command.sqlite.SQLite;
import studio.pixellite.springfall.command.user.UserManager;

/**
 * The primary plugin instance
 */
public class CommandPlugin extends ExtendedJavaPlugin {
  private Configuration configuration;
  private UserManager userManager;
  private PrimaryGroupTracker primaryGroupTracker;
  private SQLite sqLite;

  @Override
  protected void enable() {
    // setup configuration
    saveDefaultConfig();
    configuration = new Configuration(this);

    // setup database
    sqLite = new SQLite(this);
    sqLite.init();

    // setup user manager
    userManager = new UserManager(this);

    // setup group tracker
    setupPrimaryGroupTracker();

    // bind modules
    bindModules();
  }

  @Override
  protected void disable() {
    sqLite.close();
  }

  protected void setupPrimaryGroupTracker() {
    if(Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
      primaryGroupTracker = new LPPrimaryGroupTracker();
    } else {
      primaryGroupTracker = new SimplePrimaryGroupTracker();
    }
  }

  protected void bindModules() {
    bindModule(new JoinLeaveModule(this));
    bindModule(new AnvilModule());
    bindModule(new EnderChestModule());
    bindModule(new FlyModule(this));
    bindModule(new GamemodeShortcutModule());
    bindModule(new TeleportModule(this));
    bindModule(new SudoModule());
    bindModule(new StaffModeModule(this));
    bindModule(new BroadcastModule());
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  public UserManager getUserManager() {
    return userManager;
  }

  public PrimaryGroupTracker getPrimaryGroupTracker() {
    return primaryGroupTracker;
  }

  public SQLite getSqLite() {
    return sqLite;
  }
}
