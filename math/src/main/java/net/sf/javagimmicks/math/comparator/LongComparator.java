package net.sf.javagimmicks.math.comparator;

import java.util.Comparator;

/**
 * A {@link Comparator} that compares {@link Long} instances.
 */
public class LongComparator implements Comparator<Long>
{
   @Override
   public int compare(final Long o1, final Long o2)
   {
      return o1.compareTo(o2);
   }
}
