package net.sf.javagimmicks.math;

import net.sf.javagimmicks.math.comparator.ByteComparator;
import net.sf.javagimmicks.math.comparator.LongComparator;

public class NumberCompareUtils
{
   private NumberCompareUtils()
   {}

   public static int compareLong(final long long1, final long long2)
   {
      return new LongComparator().compare(long1, long2);
   }

   public static int compareBye(final byte byte1, final byte byte2)
   {
      return new ByteComparator().compare(byte1, byte2);
   }
}
