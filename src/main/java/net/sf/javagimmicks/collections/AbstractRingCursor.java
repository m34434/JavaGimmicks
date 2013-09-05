package net.sf.javagimmicks.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractRingCursor<E> extends AbstractCursor<E> implements RingCursor<E>
{
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

   public boolean isEmpty()
   {
      return size() == 0;
   }

   public E set(E value)
   {
      if (isEmpty())
      {
         throw new NoSuchElementException("There is no element to replace");
      }

      insertAfter(value);
      return remove();
   }

   public Iterator<E> iterator()
   {
      return new RingIterator<E>(cursor());
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
      private final RingCursor<E> _ringCursor;
      private final int _size;

      private int _counter = 0;
      private boolean _removeCalled = true;

      public RingIterator(RingCursor<E> ringCursor)
      {
         _ringCursor = ringCursor;
         _ringCursor.previous();
         _size = ringCursor.size();
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

         return _ringCursor.next();
      }

      public void remove()
      {
         if (_removeCalled)
         {
            throw new IllegalStateException(
                  "next() has not yet been called since last remove() call or creation of this iterator!");
         }

         _removeCalled = true;

         _ringCursor.remove();

         if (!_ringCursor.isEmpty())
         {
            _ringCursor.previous();
         }
      }
   }
}
