package studio.pixellite.springfall.command.config;

import com.google.common.reflect.TypeToken;
import me.lucko.helper.config.ConfigurationNode;
import me.lucko.helper.config.objectmapping.ObjectMappingException;
import studio.pixellite.springfall.command.CommandPlugin;
import studio.pixellite.springfall.command.config.resolver.ConfigurateResolver;

import java.util.List;

public abstract class AbstractConfiguration {
  private final ConfigurationNode node;

  protected AbstractConfiguration(CommandPlugin plugin, String path) {
    this.node = ConfigurateResolver.resolveNode(plugin, path);
  }

  protected String getString(Object... path) {
    return node.getNode(path).getString("default");
  }

  protected int getInt(Object... path) {
    return node.getNode(path).getInt();
  }

  protected double getDouble(Object... path) {
    return node.getNode(path).getDouble();
  }

  protected float getFloat(Object... path) {
    return node.getNode(path).getFloat();
  }

  protected boolean getBoolean(Object... path) {
    return node.getNode(path).getBoolean();
  }

  @SuppressWarnings("UnstableApiUsage")
  protected <T> List<T> getList(Class<T> clazz, Object... path) {
    try {
      return node.getNode(path).getList(TypeToken.of(clazz));
    } catch (ObjectMappingException e) {
      throw new RuntimeException(e);
    }
  }
}
