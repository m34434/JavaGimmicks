package net.sf.javagimmicks.math.comparator;

import java.util.Comparator;

public class ByteComparator implements Comparator<Byte>
{
   public int compare(Byte o1, Byte o2)
   {
      return o1.compareTo(o2);
   }
}
