package studio.pixellite.springfall.command.sqlite;

import me.lucko.helper.terminable.Terminable;
import org.bukkit.Bukkit;
import studio.pixellite.springfall.command.CommandPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Used to generate the primary SQLite database file.
 */
public class SQLiteConnectionManager implements Terminable {
  /** The primary plugin instance. */
  private final CommandPlugin plugin;

  /** The path to the write file. */
  private final Path writeFile;

  /** The backing connection instance. For thread-safety, access to this should be synchronized. */
  private final Connection connection;

  public SQLiteConnectionManager(CommandPlugin plugin) {
    this.plugin = plugin;
    this.writeFile = generateFile();
    this.connection = initConnection();
  }

  public void close() {
    try {
      connection.close();
    } catch (SQLException e) {
      // ignored
    }
  }

  /**
   * Generates the database file. Should be run once at the initialization of the plugin.
   *
   * <p>THIS METHOD IS NOT THREAD SAFE. AND SHOULD BE RUN SYNCHRONOUSLY UNDER ALL
   * CIRCUMSTANCES.</p>
   *
   * @return the path to the write file
   */
  private Path generateFile() {
    Path path = Path.of(plugin.getDataFolder() + "/database.db");
    File file = path.toFile();

    // create the actual file
    if(!file.exists()) {
      try {
        if(file.createNewFile()) {
          plugin.getLogger().info("Created a new database file.");
        }
      } catch (IOException e) {
        e.printStackTrace();

        // DB file could not be generated, disable the plugin
        plugin.getLogger().severe("The database file could not be generated. Disabling.");
        Bukkit.getPluginManager().disablePlugin(plugin);
      }
    }

    return path;
  }

  /**
   * Initializes the connection.
   *
   * @return the new connection
   * @throws IllegalStateException if the connection object is already established
   */
  private Connection initConnection() {
    if(connection != null) {
      throw new IllegalStateException("The connection is already established!");
    }

    Connection conn;

    try {
      String url = "jdbc:sqlite:" + writeFile.toString();
      conn = DriverManager.getConnection(url);
    } catch (SQLException e) {
      // DB file could not be generated, disable the plugin
      plugin.getLogger().severe("The database connection could not be established. Disabling.");
      Bukkit.getPluginManager().disablePlugin(plugin);
      throw new RuntimeException(e);
    }

    return conn;
  }

  public Connection getConnection() {
    return connection;
  }
}
