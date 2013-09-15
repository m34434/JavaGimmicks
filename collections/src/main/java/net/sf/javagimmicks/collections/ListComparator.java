package net.sf.javagimmicks.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sf.javagimmicks.util.ComparableComparator;

/**
 * A {@link Comparator} implementation able to compare {@link List}s based on a
 * given compare logic on the element level.
 * 
 * @param <E>
 *           the type of elements to compare
 */
@SuppressWarnings("unchecked")
public class ListComparator<E> implements Comparator<List<E>>
{
   private final Comparator<E> _elementComparator;

   /**
    * A reusable instance able to compare {@link List}s of {@link Comparable}
    * elements
    */
   @SuppressWarnings("rawtypes")
   public static ListComparator<? extends Comparable<?>> COMPARABLE_INSTANCE = new ListComparator(
         ComparableComparator.INSTANCE);

   /**
    * Creates a new instance based on a given element {@link Comparator}
    * 
    * @param elementComparator
    *           the element {@link Comparator} to use for comparing
    */
   public ListComparator(final Comparator<E> elementComparator)
   {
      _elementComparator = elementComparator;
   }

   public static <E extends Comparable<? super E>> ListComparator<E> getComparableInstance()
   {
      return (ListComparator<E>) COMPARABLE_INSTANCE;
   }

   @Override
   public int compare(final List<E> list1, final List<E> list2)
   {
      final Iterator<E> iterator1 = list1.iterator();
      final Iterator<E> iterator2 = list2.iterator();

      while (iterator1.hasNext() && iterator2.hasNext())
      {
         final E element1 = iterator1.next();
         final E element2 = iterator2.next();

         final int iCompareResult = _elementComparator.compare(element1, element2);

         if (iCompareResult != 0)
         {
            return iCompareResult;
         }
      }

      if (iterator1.hasNext())
      {
         return -1;
      }
      else if (iterator2.hasNext())
      {
         return 1;
      }
      else
      {
         return 0;
      }
   }
}
