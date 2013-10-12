package net.sf.javagimmicks.math;

import java.util.Comparator;

import net.sf.javagimmicks.math.comparator.BooleanComparator;
import net.sf.javagimmicks.math.comparator.ByteComparator;
import net.sf.javagimmicks.math.comparator.DoubleComparator;
import net.sf.javagimmicks.math.comparator.FloatComparator;
import net.sf.javagimmicks.math.comparator.LongComparator;
import net.sf.javagimmicks.math.comparator.ShortComparator;

/**
 * A shortcut helper for comparing non-{@code int}s in a {@link Comparator}
 * style. Internally uses {@link Comparator}s from
 * {@link net.sf.javagimmicks.math.comparator} package.
 */
public class NumberCompareUtils
{
   private NumberCompareUtils()
   {}

   /**
    * Compares two {@code boolean}s in {@link Comparator} style.
    * 
    * @param n1
    *           the first {@code boolean} to compare
    * @param n2
    *           the second {@code boolean} to compare
    * @return the comparison result as {@code int}
    * @see ByteComparator
    */
   public static int compare(final boolean n1, final boolean n2)
   {
      return new BooleanComparator().compare(n1, n2);
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
   public static int compare(final byte n1, final byte n2)
   {
      return new ByteComparator().compare(n1, n2);
   }

   /**
    * Compares two {@code short}s in {@link Comparator} style.
    * 
    * @param n1
    *           the first {@code short} to compare
    * @param n2
    *           the second {@code short} to compare
    * @return the comparison result as {@code int}
    * @see ShortComparator
    */
   public static int compare(final short n1, final short n2)
   {
      return new ShortComparator().compare(n1, n2);
   }

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
   public static int compare(final long n1, final long n2)
   {
      return new LongComparator().compare(n1, n2);
   }

   /**
    * Compares two {@code float}s in {@link Comparator} style.
    * 
    * @param n1
    *           the first {@code float} to compare
    * @param n2
    *           the second {@code float} to compare
    * @return the comparison result as {@code int}
    * @see FloatComparator
    */
   public static int compare(final float n1, final float n2)
   {
      return new FloatComparator().compare(n1, n2);
   }

   /**
    * Compares two {@code double}s in {@link Comparator} style.
    * 
    * @param n1
    *           the first {@code double} to compare
    * @param n2
    *           the second {@code double} to compare
    * @return the comparison result as {@code int}
    * @see ShortComparator
    */
   public static int compare(final double n1, final double n2)
   {
      return new DoubleComparator().compare(n1, n2);
   }
}
