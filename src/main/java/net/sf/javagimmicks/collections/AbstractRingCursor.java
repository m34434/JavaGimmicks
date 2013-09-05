package net.sf.javagimmicks.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An abstract implementation of {@link RingCursor} that provides default
 * implementations for some derivable methods.
 * <p>
 * These are:
 * <ul>
 * <li>{@link #insertAfter(Iterable)}</li>
 * <li>{@link #insertBefore(Iterable)}</li>
 * <li>{@link #next(int)}</li>
 * <li>{@link #previous(int)}</li>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #set(Object)}</li>
 * <li>{@link #iterator()}</li>
 * <li>{@link #toList()}</li>
 * <li>{@link #toString()}</li>
 * </ul>
 */
public abstract class AbstractRingCursor<E> extends AbstractCursor<E> implements RingCursor<E>
{
   /**
    * A re-implementation of {@link AbstractCursor#next(int)} that optimizes the
    * internal logic by leveraging advances {@link Ring} features.
    */
   @Override
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

      final int size = size();
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

   /**
    * A re-implementation of {@link AbstractCursor#previous(int)} that optimizes
    * the internal logic by leveraging advances {@link Ring} features.
    */
   @Override
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

      final int size = size();
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

   @Override
   public boolean isEmpty()
   {
      return size() == 0;
   }

   @Override
   public E set(final E value)
   {
      if (isEmpty())
      {
         throw new NoSuchElementException("There is no element to replace");
      }

      insertAfter(value);
      return remove();
   }

   @Override
   public Iterator<E> iterator()
   {
      return new RingIterator<E>(cursor());
   }

   @Override
   public List<E> toList()
   {
      final ArrayList<E> result = new ArrayList<E>(size());
      for (final E element : this)
      {
         result.add(element);
      }

      return result;
   }

   @Override
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

      public RingIterator(final RingCursor<E> ringCursor)
      {
         _ringCursor = ringCursor;
         _ringCursor.previous();
         _size = ringCursor.size();
      }

      @Override
      public boolean hasNext()
      {
         return _counter != _size;
      }

      @Override
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

      @Override
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
