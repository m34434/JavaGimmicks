package net.sf.javagimmicks.collections.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import net.sf.javagimmicks.lang.Transformer;

class CompositeListIterator<E> implements ListIterator<E>
{
   protected final ListIterator<? extends ListIterator<E>> _iterators;
   
   protected ListIterator<E> _previousIterator;
   protected ListIterator<E> _currentIterator;
   protected ListIterator<E> _nextIterator;
   
   protected int _index;
   
   CompositeListIterator(List<? extends ListIterator<E>> iterators)
   {
      _index = -1;
      _iterators = iterators.listIterator();
   }
   
   CompositeListIterator(List<? extends List<E>> lists, int startIndex)
   {
      _index = startIndex - 1;
      
      // Build a list of the ListIterators of all handed Lists
      final List<ListIterator<E>> iteratorList = new ArrayList<ListIterator<E>>(lists.size());
      
      // Remember the index of the List determined by the startIndex
      int listIndex = -1;
      
      // Iterator over the Lists
      for(ListIterator<? extends List<E>> listsIterator = lists.listIterator(); listsIterator.hasNext();)
      {
         // Get the current List and size
         final List<E> currentList = listsIterator.next();         
         final int currentListSize = currentList.size();
         
         // Prepare a variable for the current ListIterator
         final ListIterator<E> currentListIterator;
         
         // Case 1: We are BEHIND the starting List
         if(startIndex == -1)
         {
            // Get the default ListIterator (starting at 0)
            currentListIterator = currentList.listIterator();
         }
         
         // Case 2: We are INSIDE of the starting List
         else if(currentListSize >= startIndex)
         {
            // Get the ListIterator for the right position
            currentListIterator = currentList.listIterator(startIndex);
            
            _currentIterator = currentListIterator;
            
            startIndex = -1;
            listIndex = listsIterator.previousIndex();
         }
         
         // Case 3: we are BEFORE the starting List
         else
         {
            // Get a ListIterator starting at the end of the list
            currentListIterator = currentList.listIterator(currentListSize);
            
            // Don't forget to update the current start index
            startIndex -= currentListSize;
         }

         // Fill the current ListIterator in the List of ListIterators
         iteratorList.add(currentListIterator);
      }
      
      // Get the internal ListIterator of ListIterators from the just built list
      _iterators = iteratorList.listIterator(listIndex);
      
      // Enforce the right internal state of the ListIterator
      _iterators.next();
   }
   
   static class ListIteratorExtractor<E, C extends List<E>> implements Transformer<C, ListIterator<E>>
   {
      public ListIterator<E> transform(C source)
      {
         return source.listIterator();
      }
   };

   
   public boolean hasPrevious()
   {
      if(_currentIterator != null && _currentIterator.hasPrevious())
      {
         return true;
      }
      
      findPreviousIterator();
      return _previousIterator != null && _previousIterator.hasPrevious();
   }

   public boolean hasNext()
   {
      if(_currentIterator != null && _currentIterator.hasNext())
      {
         return true;
      }
      
      findNextIterator();
      return _nextIterator != null && _nextIterator.hasNext();
   }
   
   public E previous()
   {
      if(!hasPrevious())
      {
         throw new NoSuchElementException();
      }
      
      return movePrevious();
   }

   public E next()
   {
      if(!hasNext())
      {
         throw new NoSuchElementException();
      }
      
      return moveNext();
   }
   
   public int previousIndex()
   {
      return _index;
   }

   public int nextIndex()
   {
      return _index + 1;
   }

   public void remove()
   {
      if(_currentIterator == null)
      {
         throw new IllegalStateException();
      }
      
      _currentIterator.remove();
   }
   
   public void add(E e)
   {
      if(_currentIterator == null)
      {
         throw new IllegalStateException();
      }
      
      _currentIterator.add(e);
   }

   public void set(E e)
   {
      if(_currentIterator == null)
      {
         throw new IllegalStateException();
      }
      
      _currentIterator.set(e);
   }

   protected void findPreviousIterator()
   {
      if(_previousIterator != null)
      {
         return;
      }
      
      while(_iterators.hasPrevious())
      {
          _previousIterator = _iterators.previous();
          
          if(_previousIterator.hasPrevious())
          {
             break;
          }
      }
   }

   protected void findNextIterator()
   {
      if(_nextIterator != null)
      {
         return;
      }
      
      while(_iterators.hasNext())
      {
          _nextIterator = _iterators.next();
          
          if(_nextIterator.hasNext())
          {
             break;
          }
      }
   }

   protected E movePrevious()
   {
      if(_previousIterator != null)
      {
         _currentIterator = _previousIterator;
         _previousIterator = null;
      }
      
      --_index;
      
      return _currentIterator.previous();
   }

   protected E moveNext()
   {
      if(_nextIterator != null)
      {
         _currentIterator = _nextIterator;
         _nextIterator = null;
      }

      ++_index;
      
      return _currentIterator.next();
   }
}