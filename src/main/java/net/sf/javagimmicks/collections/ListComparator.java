package net.sf.javagimmicks.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sf.javagimmicks.util.ComparableComparator;

@SuppressWarnings("unchecked")
public class ListComparator<E> implements Comparator<List<E>>
{
   private final Comparator<E> _elementComparator;

   @SuppressWarnings("rawtypes")
   public static ListComparator<? extends Comparable<?>> COMPARABLE_INSTANCE = new ListComparator(ComparableComparator.INSTANCE);

   public ListComparator(Comparator<E> elementComparator)
   {
      _elementComparator = elementComparator;
   }
   
   public static <E extends Comparable<? super E>> ListComparator<E> getComparableInstance()
   {
      return (ListComparator<E>) COMPARABLE_INSTANCE;
   }
   
   public int compare(List<E> list1, List<E> list2)
   {
      Iterator<E> iterator1 = list1.iterator();
      Iterator<E> iterator2 = list2.iterator();
      
      while(iterator1.hasNext() && iterator2.hasNext())
      {
         E element1 = iterator1.next();
         E element2 = iterator2.next();
         
         int iCompareResult = _elementComparator.compare(element1, element2);
         
         if(iCompareResult != 0)
         {
            return iCompareResult;
         }
      }
      
      if(iterator1.hasNext())
      {
         return -1;
      }
      else if(iterator2.hasNext())
      {
         return 1;
      }
      else
      {
         return 0;
      }
   }
}
