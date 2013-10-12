package net.sf.javagimmicks.math.comparator;

import java.util.Comparator;

/**
 * A {@link Comparator} that compares {@link Byte} instances.
 */
public class ByteComparator implements Comparator<Byte>
{
   @Override
   public int compare(final Byte o1, final Byte o2)
   {
      return o1.compareTo(o2);
   }
}
