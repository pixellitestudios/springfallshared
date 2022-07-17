package studio.pixellite.shared;

import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bukkit.Bukkit;
import studio.pixellite.shared.group.impl.SimplePrimaryGroupTracker;
import studio.pixellite.shared.module.joinleave.JoinLeaveModule;
import studio.pixellite.shared.module.player.AnvilModule;
import studio.pixellite.shared.module.player.EnderChestModule;
import studio.pixellite.shared.module.player.FlyModule;
import studio.pixellite.shared.module.player.GamemodeShortcutModule;
import studio.pixellite.shared.module.staff.BroadcastModule;
import studio.pixellite.shared.module.staff.StaffModeModule;
import studio.pixellite.shared.module.staff.SudoModule;
import studio.pixellite.shared.module.staff.TeleportModule;
import studio.pixellite.shared.sqlite.SQLite;
import studio.pixellite.shared.config.Configuration;
import studio.pixellite.shared.group.PrimaryGroupTracker;
import studio.pixellite.shared.group.impl.LPPrimaryGroupTracker;
import studio.pixellite.shared.user.UserManager;

/**
 * The primary plugin instance
 */
public class SharedPlugin extends ExtendedJavaPlugin {
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
