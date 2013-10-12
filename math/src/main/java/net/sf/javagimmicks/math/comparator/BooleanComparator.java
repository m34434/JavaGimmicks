package net.sf.javagimmicks.math.comparator;

import java.util.Comparator;

/**
 * A {@link Comparator} that compares {@link Boolean} instances.
 */
public class BooleanComparator implements Comparator<Boolean>
{
   @Override
   public int compare(final Boolean o1, final Boolean o2)
   {
      return o1.compareTo(o2);
   }
}
