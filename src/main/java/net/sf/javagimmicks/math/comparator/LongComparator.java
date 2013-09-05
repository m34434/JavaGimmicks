package net.sf.javagimmicks.math.comparator;

import java.util.Comparator;

public class LongComparator implements Comparator<Long>
{
   public int compare(Long o1, Long o2)
   {
      return o1.compareTo(o2);
   }
}
