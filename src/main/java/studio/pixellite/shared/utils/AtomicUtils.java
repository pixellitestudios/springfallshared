package studio.pixellite.shared.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for working with atomics.
 */
public final class AtomicUtils {
  /**
   * Atomically negates the given atomic boolean.
   *
   * @param atomic the atomic boolean to negate
   * @return the updated value
   */
  public static boolean negate(AtomicBoolean atomic) {
    while(true) {
      boolean current = atomic.get();
      boolean next = !current;

      if(atomic.compareAndSet(current, next)) {
        return next;
      }
    }
  }

  /**
   * Subtracts the given amount from the atomic integer.
   *
   * @param atomic the atomic to subtract from
   * @param amount the amount to subtract
   * @return the new amount
   */
  public static int subtract(AtomicInteger atomic, int amount) {
    while(true) {
      int current = atomic.get();
      int next = current - amount;

      if(atomic.compareAndSet(current, next)) {
        return next;
      }
    }
  }

  // prevent this class from be instantiated
  private AtomicUtils() {}
}
