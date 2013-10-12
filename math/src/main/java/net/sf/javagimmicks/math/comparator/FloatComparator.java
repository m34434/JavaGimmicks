package net.sf.javagimmicks.math.comparator;

import java.util.Comparator;

/**
 * A {@link Comparator} that compares {@link Integer} instances.
 */
public class FloatComparator implements Comparator<Integer>
{
   @Override
   public int compare(final Integer o1, final Integer o2)
   {
      return o1.compareTo(o2);
   }
}
