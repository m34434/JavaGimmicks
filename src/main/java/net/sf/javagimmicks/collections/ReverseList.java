package net.sf.javagimmicks.collections;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A decorator around {@link List} implementations the reverses the element
 * order.
 */
public class ReverseList<E> extends AbstractList<E>
{
   /**
    * Creates a new instance for the given {@link List}
    * 
    * @param list
    *           the {@link List} whose element to reverse
    * @return the decorated instance with reverse element order
    */
   public static <E> ReverseList<E> decorate(final List<E> list)
   {
      return new ReverseList<E>(list);
   }

   protected final List<E> _internalList;

   protected ReverseList(final List<E> decorated)
   {
      _internalList = decorated;
   }

   /**
    * Returns the decorated {@link List}
    * 
    * @return the decorated {@link List}
    */
   public List<E> getInternalList()
   {
      return _internalList;
   }

   @Override
   public boolean add(final E e)
   {
      _internalList.add(0, e);
      return true;
   }

   @Override
   public void add(final int index, final E element)
   {
      _internalList.add(getRevertedIndex(index) + 1, element);
   }

   @Override
   public void clear()
   {
      _internalList.clear();
   }

   @Override
   public boolean contains(final Object o)
   {
      return _internalList.contains(o);
   }

   @Override
   public boolean containsAll(final Collection<?> c)
   {
      return _internalList.containsAll(c);
   }

   @Override
   public E get(final int index)
   {
      return _internalList.get(getRevertedIndex(index));
   }

   @Override
   public int indexOf(final Object o)
   {
      return _internalList.lastIndexOf(o);
   }

   @Override
   public boolean isEmpty()
   {
      return _internalList.isEmpty();
   }

   @Override
   public Iterator<E> iterator()
   {
      return listIterator();
   }

   @Override
   public int lastIndexOf(final Object o)
   {
      return _internalList.indexOf(o);
   }

   @Override
   public ListIterator<E> listIterator()
   {
      return listIterator(0);
   }

   @Override
   public ListIterator<E> listIterator(final int index)
   {
      return new RevertingListIteratorDecorator(_internalList.listIterator(getRevertedIndex(index) + 1));
   }

   @Override
   public E remove(final int index)
   {
      return _internalList.remove(getRevertedIndex(index));
   }

   @Override
   public boolean remove(final Object o)
   {
      return _internalList.remove(o);
   }

   @Override
   public boolean removeAll(final Collection<?> c)
   {
      return _internalList.removeAll(c);
   }

   @Override
   public boolean retainAll(final Collection<?> c)
   {
      return _internalList.retainAll(c);
   }

   @Override
   public E set(final int index, final E element)
   {
      return _internalList.set(getRevertedIndex(index), element);
   }

   @Override
   public int size()
   {
      return _internalList.size();
   }

   @Override
   public List<E> subList(final int fromIndex, final int toIndex)
   {
      return new ReverseList<E>(_internalList.subList(getRevertedIndex(toIndex) + 1, getRevertedIndex(fromIndex) + 1));
   }

   protected int getRevertedIndex(final int index)
   {
      return size() - index - 1;
   }

   protected class RevertingListIteratorDecorator implements ListIterator<E>
   {
      protected final ListIterator<E> _internalIterator;

      public RevertingListIteratorDecorator(final ListIterator<E> decorated)
      {
         _internalIterator = decorated;
      }

      @Override
      public void add(final E e)
      {
         _internalIterator.add(e);
         _internalIterator.previous();
      }

      @Override
      public boolean hasNext()
      {
         return _internalIterator.hasPrevious();
      }

      @Override
      public boolean hasPrevious()
      {
         return _internalIterator.hasNext();
      }

      @Override
      public E next()
      {
         return _internalIterator.previous();
      }

      @Override
      public int nextIndex()
      {
         return getRevertedIndex(_internalIterator.previousIndex());
      }

      @Override
      public E previous()
      {
         return _internalIterator.next();
      }

      @Override
      public int previousIndex()
      {
         return getRevertedIndex(_internalIterator.nextIndex());
      }

      @Override
      public void remove()
      {
         _internalIterator.remove();
      }

      @Override
      public void set(final E e)
      {
         _internalIterator.set(e);
      }
   }
}
