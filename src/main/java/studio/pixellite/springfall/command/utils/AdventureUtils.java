package studio.pixellite.springfall.command.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

/**
 * A utility class for interacting with adventure.
 */
public class AdventureUtils {

  /**
   * Deserializes the given ampersand string into a component.
   *
   * @param string the string to deserialize
   * @return the new component
   */
  public static Component deserialize(String string) {
    return LegacyComponentSerializer.legacyAmpersand().deserialize(string);
  }

  // ensure this class cannot be instantiated
  private AdventureUtils() {}
}
