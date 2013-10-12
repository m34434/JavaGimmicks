package net.sf.javagimmicks.math;

import java.util.Comparator;

import net.sf.javagimmicks.math.comparator.ByteComparator;
import net.sf.javagimmicks.math.comparator.LongComparator;

/**
 * A shortcut helper for comparing non-{@code int}s in a {@link Comparator}
 * style. Internally uses {@link Comparator}s from
 * {@link net.sf.javagimmicks.comparator} package.
 */
public class NumberCompareUtils
{
   private NumberCompareUtils()
   {}

   /**
    * Compares two {@code long}s in {@link Comparator} style.
    * 
    * @param n1
    *           the first {@code long} to compare
    * @param n2
    *           the second {@code long} to compare
    * @return the comparison result as {@code int}
    * @see LongComparator
    */
   public static int compareLong(final long n1, final long n2)
   {
      return new LongComparator().compare(n1, n2);
   }

   /**
    * Compares two {@code byte}s in {@link Comparator} style.
    * 
    * @param n1
    *           the first {@code byte} to compare
    * @param n2
    *           the second {@code byte} to compare
    * @return the comparison result as {@code int}
    * @see ByteComparator
    */
   public static int compareByte(final byte n1, final byte n2)
   {
      return new ByteComparator().compare(n1, n2);
   }
}
