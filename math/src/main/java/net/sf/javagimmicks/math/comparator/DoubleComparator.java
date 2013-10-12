package net.sf.javagimmicks.math.comparator;

import java.util.Comparator;

/**
 * A {@link Comparator} that compares {@link Double} instances.
 */
public class DoubleComparator implements Comparator<Double>
{
   @Override
   public int compare(final Double o1, final Double o2)
   {
      return o1.compareTo(o2);
   }
}
