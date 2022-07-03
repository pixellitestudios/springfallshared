package studio.pixellite.springfall.command.utils;

import java.util.List;

public class StringUtils {
  /**
   * Joins a set of strings in a list together into one single string.
   *
   * @param list the list to join
   * @return the joined string
   */
  public static String joinList(List<String> list) {
    return String.join(" ", list);
  }

  /**
   * Joins a set of strings in a list together into one single string
   * starting from a specific index
   *
   * @param list the list to join
   * @param startingIndex the starting index
   * @return the joined string
   */
  public static String joinList(List<String> list, int startingIndex) {
    return String.join(" ", list.subList(startingIndex, list.size()));
  }

  /**
   * Capitalizes the first letter in a string.
   *
   * @param string the string to capitalize
   * @return the capitalized string
   */
  public static String capitalizeFirst(String string) {
    return string.substring(0, 1).toUpperCase() + string.substring(1);
  }

  /**
   * Uses {@link StringBuilder} to concatenate multiple
   * strings together.
   *
   * @param strings the strings to concatenate
   * @return the concatenated string
   */
  public static String concat(String... strings) {
    StringBuilder builder = new StringBuilder();

    for(String string : strings) {
      builder.append(string);
    }

    return builder.toString();
  }

  // Ensure that this class cannot be constructed
  private StringUtils() {}
}
