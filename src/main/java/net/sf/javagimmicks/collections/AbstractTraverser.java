package net.sf.javagimmicks.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractTraverser<E> implements Traverser<E>
{
   public boolean isEmpty()
   {
      return size() == 0;
   }

   public void insertAfter(Collection<? extends E> collection)
   {
      for (E value : collection)
      {
         insertAfter(value);
         next();
      }

      previous(collection.size());
   }

   public void insertBefore(Collection<? extends E> collection)
   {
      for (E value : collection)
      {
         insertBefore(value);
      }
   }

   public E next(int count)
   {
      if (count < 0)
      {
         return previous(-count);
      }

      if (count == 0)
      {
         return get();
      }

      int size = size();
      count = count % size;

      if (count > size / 2)
      {
         return previous(size - count);
      }
      else
      {
         E result = null;

         for (int i = 0; i < count; ++i)
         {
            result = next();
         }

         return result;
      }
   }

   public E previous(int count)
   {
      if (count < 0)
      {
         return next(-count);
      }

      if (count == 0)
      {
         return get();
      }

      int size = size();
      count = count % size;

      if (count > size / 2)
      {
         return next(size - count);
      }
      else
      {
         E result = null;

         for (int i = 0; i < count; ++i)
         {
            result = previous();
         }

         return result;
      }
   }

   public E set(E value)
   {
      if(isEmpty())
      {
         throw new NoSuchElementException("There is no element to replace");
      }
      
      insertAfter(value);
      return remove();
   }

   public Iterator<E> iterator()
   {
      return new RingIterator<E>(traverser());
   }

   public List<E> toList()
   {
      ArrayList<E> result = new ArrayList<E>(size());
      for (E element : this)
      {
         result.add(element);
      }

      return result;
   }

   public String toString()
   {
      return toList().toString();
   }

   protected static class RingIterator<E> implements Iterator<E>
   {
      private final Traverser<E> _traverser;
      private final int _size;

      private int _counter = 0;
      private boolean _removeCalled = true;

      public RingIterator(Traverser<E> traverser)
      {
         _traverser = traverser;
         _traverser.previous();
         _size = traverser.size();
      }

      public boolean hasNext()
      {
         return _counter != _size;
      }

      public E next()
      {
         if (!hasNext())
         {
            throw new NoSuchElementException();
         }

         _removeCalled = false;
         ++_counter;

         return _traverser.next();
      }

      public void remove()
      {
         if (_removeCalled)
         {
            throw new IllegalStateException(
                  "next() has not yet been called since last remove() call or creation of this iterator!");
         }

         _removeCalled = true;

         _traverser.remove();

         if (!_traverser.isEmpty())
         {
            _traverser.previous();
         }
      }
   }
}
