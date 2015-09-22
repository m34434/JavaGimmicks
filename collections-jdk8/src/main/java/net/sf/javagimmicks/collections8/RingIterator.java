package net.sf.javagimmicks.collections8;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An implementation of {@link Iterator} that wraps around a given
 * {@link RingCursor}.
 * 
 * @param <E>
 *           the type of elements this {@link RingIterator} operates on
 */
public class RingIterator<E> implements Iterator<E>
{
   private final RingCursor<E> _ringCursor;
   private final int _size;

   private int _counter = 0;
   private boolean _removeCalled = true;

   /**
    * Constructs a new instance around the given {@link RingCursor}.
    * 
    * @param ringCursor
    *           the {@link RingCursor} over which's elements should be iterated
    */
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