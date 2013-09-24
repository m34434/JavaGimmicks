package net.sf.javagimmicks.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * A {@link Comparator} implementation that wraps a list of delegate
 * {@link Comparator}s and applies them in the given order when comparing
 * objects.
 * 
 * @param <E>
 */
public class CompositeComparator<E> implements Comparator<E>
{
   private final List<Comparator<E>> _comparators;

   /**
    * Creates a new instance for the given delegate {@link Comparator}s
    * 
    * @param comparators
    *           the delegate {@link Comparator}s to use internally
    */
   public CompositeComparator(final List<Comparator<E>> comparators)
   {
      _comparators = new ArrayList<Comparator<E>>(comparators);
   }

   /**
    * Creates a new instance for the given delegate {@link Comparator}s
    * 
    * @param comparators
    *           the delegate {@link Comparator}s to use internally
    */
   public CompositeComparator(final Comparator<E>... comparators)
   {
      this(Arrays.asList(comparators));
   }

   @Override
   public int compare(final E o1, final E o2)
   {
      for (final Comparator<E> comparator : _comparators)
      {
         final int compareResult = comparator.compare(o1, o2);

         if (compareResult != 0)
         {
            return compareResult;
         }
      }

      return 0;
   }
}
