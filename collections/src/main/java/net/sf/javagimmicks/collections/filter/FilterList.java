package net.sf.javagimmicks.collections.filter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sf.javagimmicks.collections.event.AbstractEventList;
import net.sf.javagimmicks.lang.Filter;

/**
 * A decorating implementation of {@link List} that allows to create filtered
 * views of itself - child {@link List} that only contain elements from the
 * parent that are accepted by a given {@link Filter}.
 */
public class FilterList<E> extends AbstractEventList<E>
{
   private static final long serialVersionUID = 8662426678003785337L;

   protected final List<Reference<FilteredList>> _filteredLists = new ArrayList<Reference<FilteredList>>();

   protected boolean _childrenReadOnly;

   /**
    * Wraps a new instance around the given backing {@link List}.
    * 
    * @param internalList
    *           the {@link List} to wrap
    * @param childrenReadOnly
    *           if created child {@link List} should be read-only or writable
    */
   public FilterList(final List<E> internalList, final boolean childrenReadOnly)
   {
      super(internalList);

      setChildrenReadOnly(childrenReadOnly);
   }

   /**
    * Wraps a new instance around the given backing {@link List}. Child
    * {@link List}s will be read-only
    * 
    * @param internalList
    *           the {@link List} to wrap
    */
   public FilterList(final List<E> internalList)
   {
      this(internalList, true);
   }

   /**
    * Creates a new instance based an internal {@link ArrayList}.
    * 
    * @param childrenReadOnly
    *           if created child {@link List} should be read-only or writable
    */
   public FilterList(final boolean childrenReadOnly)
   {
      this(new ArrayList<E>(), childrenReadOnly);
   }

   /**
    * Creates a new instance based an internal {@link ArrayList}. Child
    * {@link List}s will be read-only
    */
   public FilterList()
   {
      this(true);
   }

   /**
    * Creates a new filtered child {@link List} (of type {@link FilteredList})
    * based on the given {@link Filter}. Please have a look at
    * {@link FilteredList FilteredList documentation} for more details about the
    * behaviour of filtered child {@link List}s.
    * 
    * @param filter
    *           the {@link Filter} that determines which elements should be
    *           contained within the child {@link FilteredList}
    * @return a filtered view of this instance with type {@link FilteredList}
    * @see FilteredList
    */
   public FilteredList createFilteredList(final Filter<E> filter)
   {
      final FilteredList result = new FilteredList(filter);

      for (final ListIterator<E> iterElements = getDecorated().listIterator(); iterElements.hasNext();)
      {
         if (filter.accepts(iterElements.next()))
         {
            result._realIndeces.add(iterElements.previousIndex());
         }
      }

      _filteredLists.add(new WeakReference<FilteredList>(result));

      return result;
   }

   /**
    * Returns if created filtered child {@link List}s are immutable.
    * 
    * @return if created filtered child {@link List}s are immutable
    */
   public boolean isChildrenReadOnly()
   {
      return _childrenReadOnly;
   }

   /**
    * Sets if created filtered child {@link List}s are immutable
    * 
    * @param childrenReadOnly
    *           if created filtered child {@link List}s are immutable
    */
   public void setChildrenReadOnly(final boolean childrenReadOnly)
   {
      _childrenReadOnly = childrenReadOnly;
   }

   /**
    * Returns a {@link List} of "active" {@link FilteredList} children. A child
    * {@link FilteredList} is considered "active" if it is referenced by at
    * least on {@link Thread}. Inactive {@link FilteredList} will be cleaned up
    * from time to time.
    * 
    * @return the {@link List} of active {@link FilteredList} children
    */
   public List<FilteredList> getActiveFilteredLists()
   {
      return Collections.unmodifiableList(getAliveFilterLists());
   }

   @Override
   protected void fireElementsAdded(final int index, final Collection<? extends E> elements)
   {
      for (final FilteredList filteredList : getAliveFilterLists())
      {
         for (final E element : elements)
         {
            doAdd(filteredList, index, filteredList._filter.accepts(element));
         }
      }
   }

   @Override
   protected void fireElementRemoved(final int index, final E element)
   {
      for (final FilteredList filteredList : getAliveFilterLists())
      {
         doRemove(filteredList, index);
      }
   }

   @Override
   protected void fireElementUpdated(final int index, final E oldElement, final E newElement)
   {
      for (final FilteredList filteredList : getAliveFilterLists())
      {
         final boolean acceptsOld = filteredList._filter.accepts(oldElement);
         final boolean acceptsNew = filteredList._filter.accepts(newElement);

         if (acceptsOld == acceptsNew)
         {
            continue;
         }
         else
         {
            final int realIndex = findStartIndex(filteredList, index);

            if (acceptsOld)
            {
               filteredList._realIndeces.remove(realIndex);
            }
            else
            {
               filteredList._realIndeces.add(realIndex, index);
            }
         }
      }
   }

   private int findStartIndex(final FilteredList filteredList, final int index)
   {
      for (final ListIterator<Integer> iterIndex = filteredList._realIndeces.listIterator(); iterIndex.hasNext();)
      {
         final int realIndex = iterIndex.next();
         if (realIndex < index)
         {
            continue;
         }

         return iterIndex.previousIndex();
      }

      return -1;
   }

   private List<FilteredList> getAliveFilterLists()
   {
      final List<FilteredList> result = new ArrayList<FilteredList>(_filteredLists.size());

      for (final Iterator<Reference<FilteredList>> iter = _filteredLists.iterator(); iter.hasNext();)
      {
         final FilteredList filteredList = iter.next().get();

         if (filteredList == null)
         {
            iter.remove();
         }
         else
         {
            result.add(filteredList);
         }
      }

      return result;
   }

   private void doAdd(final FilteredList filteredList, final int index, final boolean accepts)
   {
      final int startIndex = findStartIndex(filteredList, index);

      if (startIndex >= 0)
      {
         for (final ListIterator<Integer> iterIndex = filteredList._realIndeces.listIterator(startIndex); iterIndex
               .hasNext();)
         {
            iterIndex.set(iterIndex.next() + 1);
         }
      }

      if (accepts)
      {
         if (startIndex == -1)
         {
            filteredList._realIndeces.add(index);
         }
         else
         {
            filteredList._realIndeces.add(startIndex, index);
         }
      }
   }

   private void doRemove(final FilteredList filteredList, final int index)
   {
      final int startIndex = findStartIndex(filteredList, index);

      if (startIndex >= 0)
      {
         for (final ListIterator<Integer> iterIndex = filteredList._realIndeces.listIterator(startIndex); iterIndex
               .hasNext();)
         {
            final int realIndex = iterIndex.next();

            if (realIndex == index)
            {
               iterIndex.remove();
            }
            else
            {
               iterIndex.set(realIndex - 1);
            }
         }
      }
   }

   /**
    * Represents a child {@link List} of a {@link FilterList}.
    * <p>
    * Please note that instances do not support the {@link #add(int, Object)}
    * and {@link #addAll(int, Collection)} operations.
    * <p>
    * Furthermore {@link #add(Object)}, {@link #set(int, Object)},
    * {@link #remove(int)}, {@link #remove(Object)} and {@link #clear()} will
    * throw an {@link UnsupportedOperationException} if the parent's
    * {@link FilterList#isChildrenReadOnly()} resolves to {@code true}.
    */
   public class FilteredList extends AbstractList<E>
   {
      protected final List<Integer> _realIndeces = new ArrayList<Integer>();
      protected final Filter<E> _filter;

      protected FilteredList(final Filter<E> filter)
      {
         _filter = filter;
      }

      /**
       * Returns the parent {@link FilterList}.
       * 
       * @return the parent {@link FilterList}
       */
      public FilterList<E> getFilterList()
      {
         return FilterList.this;
      }

      @Override
      public boolean add(final E e)
      {
         if (_childrenReadOnly)
         {
            throw new UnsupportedOperationException();
         }

         if (!_filter.accepts(e))
         {
            final String string = "Cannot add an element to a " + getClass().getSimpleName()
                  + " which is not accepted by the filter!";
            throw new IllegalArgumentException(string);
         }

         FilterList.this.add(e);

         return true;
      }

      @Override
      public E set(final int index, final E element)
      {
         if (_childrenReadOnly)
         {
            throw new UnsupportedOperationException();
         }

         if (!_filter.accepts(element))
         {
            final String string = "Cannot set an element in a " + getClass().getSimpleName()
                  + " which is not accepted by the filter!";
            throw new IllegalArgumentException(string);
         }

         final E result = FilterList.this.set(getRealIndex(index), element);

         return result;
      }

      @Override
      public E get(final int index)
      {
         return FilterList.this.get(getRealIndex(index));
      }

      @Override
      public int size()
      {
         return _realIndeces.size();
      }

      @Override
      public E remove(final int index)
      {
         if (_childrenReadOnly)
         {
            throw new UnsupportedOperationException();
         }

         return FilterList.this.remove(getRealIndex(index));
      }

      private int getRealIndex(final int index)
      {
         return _realIndeces.get(index);
      }
   }
}
