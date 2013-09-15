package net.sf.javagimmicks.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import net.sf.javagimmicks.collections.decorators.AbstractListDecorator;
import net.sf.javagimmicks.collections.decorators.AbstractListIteratorDecorator;
import net.sf.javagimmicks.util.ComparableComparator;

/**
 * Provided various utility methods for sorting {@link List}s
 */
public class SortedListUtils
{
   /**
    * Determines for a given (already sorted) {@link List} and a given new
    * {@link Comparable} element the insert index so that the sorting order
    * remains valid
    * 
    * @param list
    *           the {@link List} where to insert the new element
    * @param element
    *           the new element to insert
    * @return the respective insert index to keep the sorting order valid
    */
   public static <T extends Comparable<? super T>> int getInsertIndex(final List<T> list, final T element)
   {
      int index = Collections.binarySearch(list, element);

      if (index < 0)
      {
         index = -index - 1;
      }

      return index;
   }

   /**
    * Determines for a given (already sorted) {@link List} and a given new
    * element the insert index so that the sorting order determined by the given
    * {@link Comparator} remains valid
    * 
    * @param list
    *           the {@link List} where to insert the new element
    * @param element
    *           the new element to insert
    * @param comparator
    *           the {@link Comparator} that determines the sorting order
    * @return the respective insert index to keep the sorting order valid
    */
   public static <T> int getInsertIndex(final List<T> list, final T element, final Comparator<? super T> comparator)
   {
      int index = Collections.binarySearch(list, element, comparator);

      if (index < 0)
      {
         index = -index - 1;
      }

      return index;
   }

   /**
    * Adds a given new {@link Comparable} element to a given (already sorted)
    * {@link List} at the right location so that the sorting order remains valid
    * 
    * @param list
    *           the {@link List} where to insert the new element
    * @param element
    *           the new element to insert
    */
   public static <T extends Comparable<? super T>> void addSorted(final List<T> list, final T element)
   {
      list.add(getInsertIndex(list, element), element);
   }

   /**
    * Adds a given new element to a given (already sorted) {@link List} at the
    * right location so that the sorting order determined by the given
    * {@link Comparator} remains valid
    * 
    * @param list
    *           the {@link List} where to insert the new element
    * @param element
    *           the new element to insert
    * @param comparator
    *           the {@link Comparator} that determines the sorting order
    */
   public static <T> void addSorted(final List<T> list, final T element, final Comparator<? super T> comparator)
   {
      list.add(getInsertIndex(list, element, comparator), element);
   }

   /**
    * Decorates a given {@link List} of {@link Comparable} elements with a
    * sorting facade that will sort the decorated {@link List} upon creation and
    * from then will insert all new elements automatically a the right location
    * so that the sorting order remains valid
    * 
    * @param list
    *           the {@link List} to decorate
    * @return the sorting facade {@link List}
    */
   @SuppressWarnings("unchecked")
   public static <T extends Comparable<? super T>> List<T> decorate(final List<T> list)
   {
      final ComparableComparator<T> comparableComparator = (ComparableComparator<T>) ComparableComparator.INSTANCE;
      return new SortedListDecorator<T>(list, comparableComparator);
   }

   /**
    * Decorates a given {@link List} with a sorting facade that will sort the
    * decorated {@link List} upon creation and from then will insert all new
    * elements automatically a the right location so that the sorting order
    * remains valid.
    * <p>
    * The sorting order must be provided by a given {@link Comparator}
    * 
    * @param list
    *           the {@link List} to decorate
    * @param comparator
    *           the {@link Comparator} that determines the sorting order
    * @return the sorting facade {@link List}
    */
   public static <T> List<T> decorate(final List<T> list, final Comparator<? super T> comparator)
   {
      return new SortedListDecorator<T>(list, comparator);
   }

   /**
    * Resorts a given {@link List} that was created by {@link #decorate(List)}
    * or {@link #decorate(List, Comparator)}.
    * 
    * @param list
    *           the decorated {@link List} to resort
    */
   public static <T> void resort(final List<T> list)
   {
      if (!(list instanceof SortedListDecorator<?>))
      {
         throw new IllegalArgumentException("Provided list is not a decorated sorted list!");
      }

      final SortedListDecorator<T> sortedList = (SortedListDecorator<T>) list;
      Collections.sort(sortedList.getDecorated(), sortedList._comparator);
   }

   /**
    * Changes the {@link Comparator} in a given {@link List} that was created by
    * {@link #decorate(List)} or {@link #decorate(List, Comparator)} and resorts
    * it afterwards
    * 
    * @param list
    *           the decorated {@link List} where to exchange the
    *           {@link Comparator}
    * @param comparator
    *           the new {@link Comparator} for the {@link List}
    */
   public static <T> List<T> exchangeComparator(final List<T> list, final Comparator<? super T> comparator)
   {
      if (!(list instanceof SortedListDecorator<?>))
      {
         throw new IllegalArgumentException("Provided list is not a decorated sorted list!");
      }

      final SortedListDecorator<T> sortedList = (SortedListDecorator<T>) list;

      return new SortedListDecorator<T>(sortedList.getDecorated(), comparator);
   }

   protected static final class SortedListDecorator<E> extends AbstractListDecorator<E>
   {
      private static final long serialVersionUID = 8756998247400676692L;

      private static final String MSG_ERROR_INDEX = "Cannot insert by index into a sorted list!";

      protected final Comparator<? super E> _comparator;

      protected SortedListDecorator(final List<E> decorated, final Comparator<? super E> comparator)
      {
         super(decorated);
         _comparator = comparator;

         Collections.sort(decorated, _comparator);
      }

      @Override
      public boolean add(final E element)
      {
         addSorted(getDecorated(), element, _comparator);

         return true;
      }

      @Override
      public void add(final int index, final E element)
      {
         throw new UnsupportedOperationException(MSG_ERROR_INDEX);
      }

      @Override
      public boolean addAll(final Collection<? extends E> collection)
      {
         for (final E element : collection)
         {
            add(element);
         }

         return true;
      }

      @Override
      public boolean addAll(final int index, final Collection<? extends E> c)
      {
         throw new UnsupportedOperationException(MSG_ERROR_INDEX);
      }

      @Override
      public ListIterator<E> listIterator()
      {
         return new SortedListIteratorDecorator(getDecorated().listIterator());
      }

      @Override
      public ListIterator<E> listIterator(final int index)
      {
         return new SortedListIteratorDecorator(getDecorated().listIterator(index));
      }

      @Override
      public E set(final int index, final E element)
      {
         throw new UnsupportedOperationException("Cannot set values by index in a sorted list!");
      }

      @Override
      public SortedListDecorator<E> subList(final int fromIndex, final int toIndex)
      {
         throw new UnsupportedOperationException("Cannot create sub lists of a sorted list!");
      }

      protected class SortedListIteratorDecorator extends AbstractListIteratorDecorator<E>
      {
         public SortedListIteratorDecorator(final ListIterator<E> decorated)
         {
            super(decorated);
         }

         @Override
         public void add(final E o)
         {
            throw new UnsupportedOperationException("Cannot add to a sorted list via a ListIterator!");
         }

         @Override
         public void set(final E o)
         {
            throw new UnsupportedOperationException("Cannot set values in a a sorted list via a ListIterator!");
         }
      }
   }

}