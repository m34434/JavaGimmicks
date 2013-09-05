package net.sf.javagimmicks.collections;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReverseList<E> extends AbstractList<E>
{  
   public static <E> ReverseList<E> decorate(List<E> list)
   {
      return new ReverseList<E>(list);
   }
   
   protected final List<E> _internalList;

   protected ReverseList(List<E> decorated)
   {
      _internalList = decorated;
   }
   
   public List<E> getInternalList()
   {
      return _internalList;
   }

   @Override
   public boolean add(E e)
   {
      _internalList.add(0, e);
      return true;
   }

   @Override
   public void add(int index, E element)
   {
      _internalList.add(getRevertedIndex(index) + 1, element);
   }

   @Override
   public void clear()
   {
      _internalList.clear();
   }

   @Override
   public boolean contains(Object o)
   {
      return _internalList.contains(o);
   }

   @Override
   public boolean containsAll(Collection<?> c)
   {
      return _internalList.containsAll(c);
   }

   @Override
   public E get(int index)
   {
      return _internalList.get(getRevertedIndex(index));
   }

   @Override
   public int indexOf(Object o)
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
   public int lastIndexOf(Object o)
   {
      return _internalList.indexOf(o);
   }

   @Override
   public ListIterator<E> listIterator()
   {
      return listIterator(0);
   }

   @Override
   public ListIterator<E> listIterator(int index)
   {
      return new RevertingListIteratorDecorator(_internalList.listIterator(getRevertedIndex(index) + 1));
   }

   @Override
   public E remove(int index)
   {
      return _internalList.remove(getRevertedIndex(index));
   }

   @Override
   public boolean remove(Object o)
   {
      return _internalList.remove(o);
   }

   @Override
   public boolean removeAll(Collection<?> c)
   {
      return _internalList.removeAll(c);
   }

   public boolean retainAll(Collection<?> c)
   {
      return _internalList.retainAll(c);
   }

   @Override
   public E set(int index, E element)
   {
      return _internalList.set(getRevertedIndex(index), element);
   }

   @Override
   public int size()
   {
      return _internalList.size();
   }

   @Override
   public List<E> subList(int fromIndex, int toIndex)
   {
      return new ReverseList<E>(_internalList.subList(getRevertedIndex(toIndex) + 1, getRevertedIndex(fromIndex) + 1));
   }

   protected int getRevertedIndex(int index)
   {
      return size() - index - 1;
   }
   
   protected class RevertingListIteratorDecorator implements ListIterator<E>
   {  
      protected final ListIterator<E> _internalIterator;

      public RevertingListIteratorDecorator(ListIterator<E> decorated)
      {
         _internalIterator = decorated;
      }

      public void add(E e)
      {
         _internalIterator.add(e);
         _internalIterator.previous();
      }

      public boolean hasNext()
      {
         return _internalIterator.hasPrevious();
      }

      public boolean hasPrevious()
      {
         return _internalIterator.hasNext();
      }

      public E next()
      {
         return _internalIterator.previous();
      }

      public int nextIndex()
      {
         return getRevertedIndex(_internalIterator.previousIndex());
      }

      public E previous()
      {
         return _internalIterator.next();
      }

      public int previousIndex()
      {
         return getRevertedIndex(_internalIterator.nextIndex());
      }

      public void remove()
      {
         _internalIterator.remove();
      }

      public void set(E e)
      {
         _internalIterator.set(e);
      }
   }
}
