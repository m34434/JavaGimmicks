package net.sf.javagimmicks.swing.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;

/**
 * A {@link ListModel} implementation that is also a {@link List}.
 */
public class ListModelAdapter<E> extends AbstractListModel implements List<E>
{
   private static final long serialVersionUID = -8453375309510933002L;

   protected final List<E> _internalList;

   /**
    * Creates a new instance wrapping a given {@link List}.
    * 
    * @param internalList
    *           the {@link List} to use internally for element management
    */
   public ListModelAdapter(final List<E> internalList)
   {
      _internalList = internalList;
   }

   /**
    * Creates a new instance using a new {@link ArrayList} internally.
    */
   public ListModelAdapter()
   {
      this(new ArrayList<E>());
   }

   @Override
   public Object getElementAt(final int index)
   {
      return _internalList.get(index);
   }

   @Override
   public int getSize()
   {
      return _internalList.size();
   }

   @Override
   public boolean add(final E element)
   {
      add(size(), element);

      return true;
   }

   @Override
   public void add(final int index, final E element)
   {
      _internalList.add(index, element);

      fireIntervalAdded(this, index, index);
   }

   @Override
   public boolean addAll(final Collection<? extends E> c)
   {
      addAll(size(), c);

      return true;
   }

   @Override
   public boolean addAll(final int index, final Collection<? extends E> c)
   {
      final boolean result = _internalList.addAll(index, c);

      fireIntervalAdded(this, index, index + c.size() - 1);

      return result;
   }

   @Override
   public void clear()
   {
      final int sizeBefore = size();

      _internalList.clear();

      fireIntervalRemoved(this, 0, sizeBefore - 1);
   }

   @Override
   public E remove(final int iIndex)
   {
      final E result = _internalList.remove(iIndex);

      fireIntervalRemoved(this, iIndex, iIndex);

      return result;
   }

   @Override
   public boolean remove(final Object o)
   {
      final int index = indexOf(o);

      if (index < 0)
      {
         return false;
      }

      remove(index);

      return true;
   }

   @Override
   public E set(final int index, final E element)
   {
      final E result = _internalList.set(index, element);

      fireContentsChanged(this, index, index);

      return result;
   }

   @Override
   public boolean removeAll(final Collection<?> c)
   {
      boolean bChanged = false;

      for (final Object oElement : c)
      {
         bChanged |= remove(oElement);
      }

      return bChanged;
   }

   @Override
   public boolean retainAll(final Collection<?> c)
   {
      boolean bResult = false;

      for (final Iterator<E> iterElements = iterator(); iterElements.hasNext();)
      {
         final E oElement = iterElements.next();

         if (!c.contains(oElement))
         {
            bResult = true;
            iterElements.remove();
         }
      }

      return bResult;
   }

   @Override
   public ListModelAdapter<E> subList(final int from, final int to)
   {
      return new SubListDecorator<E>(this, from, to);
   }

   @Override
   public Iterator<E> iterator()
   {
      return listIterator();
   }

   @Override
   public ListIterator<E> listIterator()
   {
      return new ListIteratorDecorator(_internalList.listIterator());
   }

   @Override
   public ListIterator<E> listIterator(final int index)
   {
      return new ListIteratorDecorator(_internalList.listIterator(index));
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
      return _internalList.get(index);
   }

   @Override
   public int indexOf(final Object o)
   {
      return _internalList.indexOf(o);
   }

   @Override
   public boolean isEmpty()
   {
      return _internalList.isEmpty();
   }

   @Override
   public int lastIndexOf(final Object o)
   {
      return _internalList.lastIndexOf(o);
   }

   @Override
   public int size()
   {
      return _internalList.size();
   }

   @Override
   public Object[] toArray()
   {
      return _internalList.toArray();
   }

   @Override
   public <T> T[] toArray(final T[] a)
   {
      return _internalList.toArray(a);
   }

   @Override
   public boolean equals(final Object o)
   {
      if (!(o instanceof ListModelAdapter<?>))
      {
         return false;
      }

      final ListModelAdapter<?> other = (ListModelAdapter<?>) o;

      return this._internalList.equals(other._internalList);
   }

   @Override
   public int hashCode()
   {
      final int listHashCode = _internalList.hashCode();

      return listHashCode + (1 << 15);
   }

   protected class ListIteratorDecorator implements ListIterator<E>
   {
      protected final ListIterator<E> _internalIterator;
      protected int _lastIndex;

      public ListIteratorDecorator(final ListIterator<E> internalIterator)
      {
         _internalIterator = internalIterator;
      }

      @Override
      public void add(final E element)
      {
         _internalIterator.add(element);

         final int index = _internalIterator.previousIndex();
         fireIntervalAdded(ListModelAdapter.this, index, index);
      }

      @Override
      public void remove()
      {
         _internalIterator.remove();

         final int index = _internalIterator.nextIndex();
         fireIntervalRemoved(this, index, index);
      }

      @Override
      public void set(final E element)
      {
         _internalIterator.set(element);

         fireContentsChanged(ListModelAdapter.this, _lastIndex, _lastIndex);
      }

      @Override
      public E next()
      {
         final E result = _internalIterator.next();

         _lastIndex = _internalIterator.previousIndex();

         return result;
      }

      @Override
      public E previous()
      {
         final E result = _internalIterator.previous();

         _lastIndex = _internalIterator.nextIndex();

         return result;
      }

      @Override
      public boolean hasNext()
      {
         return _internalIterator.hasNext();
      }

      @Override
      public boolean hasPrevious()
      {
         return _internalIterator.hasPrevious();
      }

      @Override
      public int nextIndex()
      {
         return _internalIterator.nextIndex();
      }

      @Override
      public int previousIndex()
      {
         return _internalIterator.previousIndex();
      }
   }

   protected static class SubListDecorator<E> extends ListModelAdapter<E>
   {
      private static final long serialVersionUID = -3760181638551658142L;

      protected final int _offset;
      protected final ListModelAdapter<E> _parent;

      protected SubListDecorator(final ListModelAdapter<E> parent, final int fromIndex, final int toIndex)
      {
         super(parent._internalList.subList(fromIndex, toIndex));

         _parent = parent;
         _offset = fromIndex;
      }

      @Override
      protected void fireContentsChanged(final Object source, final int index0, final int index1)
      {
         super.fireContentsChanged(source, index0, index1);

         if (source == this && index0 >= 0 && index1 >= 0)
         {
            _parent.fireContentsChanged(_parent, _offset + index0, _offset + index1);
         }
      }

      @Override
      protected void fireIntervalAdded(final Object source, final int index0, final int index1)
      {
         super.fireIntervalAdded(source, index0, index1);

         if (source == this)
         {
            _parent.fireIntervalAdded(_parent, _offset + index0, _offset + index1);
         }
      }

      @Override
      protected void fireIntervalRemoved(final Object source, final int index0, final int index1)
      {
         super.fireIntervalRemoved(source, index0, index1);

         if (source == this)
         {
            _parent.fireIntervalRemoved(_parent, _offset + index0, _offset + index1);
         }
      }
   }
}
