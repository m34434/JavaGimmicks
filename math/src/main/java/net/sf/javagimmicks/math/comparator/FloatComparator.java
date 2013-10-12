package net.sf.javagimmicks.math.comparator;

import java.util.Comparator;

/**
 * A {@link Comparator} that compares {@link Float} instances.
 */
public class FloatComparator implements Comparator<Float>
{
   @Override
   public int compare(final Float o1, final Float o2)
   {
      return o1.compareTo(o2);
   }
}
