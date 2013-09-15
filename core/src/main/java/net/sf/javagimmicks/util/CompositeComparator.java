package net.sf.javagimmicks.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CompositeComparator<E> implements Comparator<E>
{
   private final List<Comparator<E>> _comparators;
   
   public CompositeComparator(List<Comparator<E>> comparators)
   {
      _comparators = new ArrayList<Comparator<E>>(comparators);
   }

   public int compare(E o1, E o2)
   {
      for(Comparator<E> comparator : _comparators)
      {
         int compareResult = comparator.compare(o1, o2);
         
         if(compareResult != 0)
         {
            return compareResult;
         }
      }
      
      return 0;
   }
}
