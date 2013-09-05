package net.sf.javagimmicks.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import net.sf.javagimmicks.collections.decorators.AbstractListDecorator;
import net.sf.javagimmicks.collections.decorators.AbstractListIteratorDecorator;
import net.sf.javagimmicks.util.ComparableComparator;

public class SortedListUtils
{
   public static <T extends Comparable<? super T>> int getInsertIndex(List<T> list, T element)
   {
      int index = Collections.binarySearch(list, element);

      if (index < 0)
      {
         index = -index - 1;
      }

      return index;
   }

   public static <T> int getInsertIndex(List<T> list, T element, Comparator<? super T> comparator)
   {
      int index = Collections.binarySearch(list, element, comparator);

      if (index < 0)
      {
         index = -index - 1;
      }

      return index;
   }

   public static <T extends Comparable<? super T>> void addSorted(List<T> list, T element)
   {
      list.add(getInsertIndex(list, element), element);
   }

   public static <T> void addSorted(List<T> list, T element, Comparator<? super T> comparator)
   {
      list.add(getInsertIndex(list, element, comparator), element);
   }

   @SuppressWarnings("unchecked")
   public static <T extends Comparable<? super T>> List<T> decorate(List<T> list)
   {
      ComparableComparator<T> comparableComparator = (ComparableComparator<T>) ComparableComparator.INSTANCE;
      return new SortedListDecorator<T>(list, comparableComparator);
   }

   public static <T> List<T> decorate(List<T> list, Comparator<? super T> comparator)
   {
      return new SortedListDecorator<T>(list, comparator);
   }

   public static <T> void resort(List<T> list)
   {
      if (!(list instanceof SortedListDecorator<?>))
      {
         throw new IllegalArgumentException("Provided list is not a decorated sorted list!");
      }

      SortedListDecorator<T> sortedList = (SortedListDecorator<T>) list;
      Collections.sort(sortedList.getDecorated(), sortedList._comparator);
   }

   public static <T> List<T> exchangeComparator(List<T> list, Comparator<? super T> oComparator)
   {
      if (!(list instanceof SortedListDecorator<?>))
      {
         throw new IllegalArgumentException("Provided list is not a decorated sorted list!");
      }

      SortedListDecorator<T> sortedList = (SortedListDecorator<T>) list;

      return new SortedListDecorator<T>(sortedList.getDecorated(), oComparator);
   }

   protected static final class SortedListDecorator<E> extends AbstractListDecorator<E>
   {
      private static final long serialVersionUID = 8756998247400676692L;

      private static final String MSG_ERROR_INDEX = "Cannot insert by index into a sorted list!";

      protected final Comparator<? super E> _comparator;

      protected SortedListDecorator(final List<E> decorated, Comparator<? super E> comparator)
      {
         super(decorated);
         _comparator = comparator;

         Collections.sort(decorated, _comparator);
      }

      public boolean add(E element)
      {
         addSorted(getDecorated(), element, _comparator);

         return true;
      }

      public void add(int index, E element)
      {
         throw new UnsupportedOperationException(MSG_ERROR_INDEX);
      }

      public boolean addAll(Collection<? extends E> collection)
      {
         for (E element : collection)
         {
            add(element);
         }

         return true;
      }

      public boolean addAll(int index, Collection<? extends E> c)
      {
         throw new UnsupportedOperationException(MSG_ERROR_INDEX);
      }

      public ListIterator<E> listIterator()
      {
         return new SortedListIteratorDecorator(getDecorated().listIterator());
      }

      public ListIterator<E> listIterator(int index)
      {
         return new SortedListIteratorDecorator(getDecorated().listIterator(index));
      }

      public E set(int index, E element)
      {
         throw new UnsupportedOperationException("Cannot set values by index in a sorted list!");
      }

      public SortedListDecorator<E> subList(int fromIndex, int toIndex)
      {
         throw new UnsupportedOperationException("Cannot create sub lists of a sorted list!");
      }

      protected class SortedListIteratorDecorator extends AbstractListIteratorDecorator<E>
      {
         public SortedListIteratorDecorator(final ListIterator<E> decorated)
         {
            super(decorated);
         }

         public void add(E o)
         {
            throw new UnsupportedOperationException("Cannot add to a sorted list via a ListIterator!");
         }

         public void set(E o)
         {
            throw new UnsupportedOperationException("Cannot set values in a a sorted list via a ListIterator!");
         }
      }
   }

}