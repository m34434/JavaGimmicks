package net.sf.javagimmicks.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * An abstract implementation of {@link Ring} following the design of
 * {@link AbstractCollection} reducing to effort for a developer to provide a
 * full implementation to implementing {@link #size()} and {@link #cursor()}.
 * <p>
 * <b>Important note:</b> This also contains an abstract implementation of
 * {@link RingCursor} that can (and should) be used by any {@link Ring}
 * implementations based on {@link AbstractRing}. It extends the existing
 * capabilities of {@link AbstractRingCursor} by some issues like concurrent
 * modification handling.
 */
public abstract class AbstractRing<E> extends AbstractCollection<E> implements Ring<E>
{
   protected int _modCount = Integer.MIN_VALUE;

   @Override
   public boolean add(final E value)
   {
      // Get the initial RingCursor add the element before the current position
      // (this means at the 'end' of the ring)
      cursor().insertBefore(value);

      return true;
   }

   @Override
   public boolean addAll(final Collection<? extends E> collection)
   {
      // Get the initial RingCursor add the elements before the current position
      // (this means at the 'end' of the ring)
      cursor().insertBefore(collection);

      return true;
   }

   @Override
   public Iterator<E> iterator()
   {
      return cursor().iterator();
   }

   @Override
   public String toString()
   {
      return cursor().toString();
   }

   protected static abstract class BasicRingCursor<E, R extends AbstractRing<E>> implements RingCursor<E>
   {
      protected final R _ring;
      protected int _expectedModCount;

      protected BasicRingCursor(final R ring)
      {
         _ring = ring;
         _expectedModCount = _ring._modCount;
      }

      @Override
      public int size()
      {
         return _ring.size();
      }

      protected void checkForModification()
      {
         if (_ring._modCount != _expectedModCount)
         {
            throw new ConcurrentModificationException();
         }
      }
   }

}
