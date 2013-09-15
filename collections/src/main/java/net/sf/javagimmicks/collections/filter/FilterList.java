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

public class FilterList<E> extends AbstractEventList<E>
{
   private static final long serialVersionUID = 8662426678003785337L;

   protected final List<Reference<FilteredList>> _filteredLists = new ArrayList<Reference<FilteredList>>();
   
   protected boolean _childrenReadOnly;
   
   public FilterList(List<E> internalList, boolean childrenReadOnly)
   {
      super(internalList);
      
      setChildrenReadOnly(childrenReadOnly);
   }
   
   public FilterList(List<E> internalList)
   {
      this(internalList, true);
   }
   
   public FilterList(boolean childrenReadOnly)
   {
      this(new ArrayList<E>(), childrenReadOnly);
   }
   
   public FilterList()
   {
      this(true);
   }
   
   public List<E> createFilteredList(Filter<E> filter)
   {
      final FilteredList result = new FilteredList(filter);
      
      for(ListIterator<E> iterElements = getDecorated().listIterator(); iterElements.hasNext();)
      {
         if(filter.accepts(iterElements.next()))
         {
            result._realIndeces.add(iterElements.previousIndex());
         }
      }
      
      _filteredLists.add(new WeakReference<FilteredList>(result));
      
      return result;
   }
   
   public boolean isChildrenReadOnly()
   {
      return _childrenReadOnly;
   }

   public void setChildrenReadOnly(boolean childrenReadOnly)
   {
      _childrenReadOnly = childrenReadOnly;
   }

   public List<FilteredList> getActiveFilteredLists()
   {
      return Collections.unmodifiableList(getAliveFilterLists());
   }

   @Override
   protected void fireElementsAdded(int index, Collection<? extends E> elements)
   {
      for(FilteredList filteredList : getAliveFilterLists())
      {
         for(E element : elements)
         {
            doAdd(filteredList, index, filteredList._filter.accepts(element));
         }
      }
   }

   @Override
   protected void fireElementRemoved(int index, E element)
   {
      for(FilteredList filteredList : getAliveFilterLists())
      {
         doRemove(filteredList, index);
      }
   }

   @Override
   protected void fireElementUpdated(int index, E oldElement, E newElement)
   {
      for(FilteredList filteredList : getAliveFilterLists())
      {
         boolean acceptsOld = filteredList._filter.accepts(oldElement);
         boolean acceptsNew = filteredList._filter.accepts(newElement);
         
         if(acceptsOld == acceptsNew)
         {
            continue;
         }
         else
         {
            int realIndex = findStartIndex(filteredList, index);
            
            if(acceptsOld)
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

   private int findStartIndex(FilteredList filteredList, int index)
   {
      for(ListIterator<Integer> iterIndex = filteredList._realIndeces.listIterator(); iterIndex.hasNext();)
      {
         int realIndex = iterIndex.next();
         if(realIndex < index)
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
      
      for(Iterator<Reference<FilteredList>> iter = _filteredLists.iterator(); iter.hasNext();)
      {
         final FilteredList filteredList = iter.next().get();
         
         if(filteredList == null)
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

   private void doAdd(FilteredList filteredList, int index, boolean accepts)
   {
      int startIndex = findStartIndex(filteredList, index);
      
      if(startIndex >= 0)
      {
         for(ListIterator<Integer> iterIndex = filteredList._realIndeces.listIterator(startIndex); iterIndex.hasNext();)
         {
            iterIndex.set(iterIndex.next() + 1);
         }
      }
      
      if(accepts)
      {
         if(startIndex == -1)
         {
            filteredList._realIndeces.add(index);
         }
         else
         {
            filteredList._realIndeces.add(startIndex, index);
         }
      }
   }

   private void doRemove(FilteredList filteredList, int index)
   {
      int startIndex = findStartIndex(filteredList, index);
      
      if(startIndex >= 0)
      {
         for(ListIterator<Integer> iterIndex = filteredList._realIndeces.listIterator(startIndex); iterIndex.hasNext();)
         {
            int realIndex = iterIndex.next();

            if(realIndex == index)
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

   public class FilteredList extends AbstractList<E>
   {
      protected final List<Integer> _realIndeces = new ArrayList<Integer>();
      protected final Filter<E> _filter;

      protected FilteredList(Filter<E> filter)
      {
         _filter = filter;
      }
      
      public FilterList<E> getFilterList()
      {
         return FilterList.this;
      }

      @Override
      public boolean add(E e)
      {
         if(_childrenReadOnly)
         {
            throw new UnsupportedOperationException();
         }
         
         if(!_filter.accepts(e))
         {
            String string = "Cannot add an element to a " + getClass().getSimpleName() + " which is not accepted by the filter!";
            throw new IllegalArgumentException(string);
         }
         
         FilterList.this.add(e);
         
         return true;
      }
      
      @Override
      public E set(int index, E element)
      {
         if(_childrenReadOnly)
         {
            throw new UnsupportedOperationException();
         }
         
         if(!_filter.accepts(element))
         {
            String string = "Cannot set an element in a " + getClass().getSimpleName() + " which is not accepted by the filter!";
            throw new IllegalArgumentException(string);
         }
         
         E result = FilterList.this.set(getRealIndex(index), element);
         
         return result;
      }

      @Override
      public E get(int index)
      {
         return FilterList.this.get(getRealIndex(index));
      }

      @Override
      public int size()
      {
         return _realIndeces.size();
      }

      @Override
      public E remove(int index)
      {
         return FilterList.this.remove(getRealIndex(index));
      }
      
      private int getRealIndex(int index)
      {
         return _realIndeces.get(index);
      }
   }
}
