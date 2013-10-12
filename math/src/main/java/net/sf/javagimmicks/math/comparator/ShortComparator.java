package net.sf.javagimmicks.math.comparator;

import java.util.Comparator;

/**
 * A {@link Comparator} that compares {@link Short} instances.
 */
public class ShortComparator implements Comparator<Short>
{
   @Override
   public int compare(final Short o1, final Short o2)
   {
      return o1.compareTo(o2);
   }
}
