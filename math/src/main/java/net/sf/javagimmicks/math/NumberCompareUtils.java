package net.sf.javagimmicks.math;

import net.sf.javagimmicks.math.comparator.LongComparator;


public class NumberCompareUtils
{
   public static LongComparator getLongComparator()
   {
      return new LongComparator();
   }
   
   public static int compareLong(long long1, long long2)
   {
      return getLongComparator().compare(long1, long2);
   }
}
