package net.sf.javagimmicks.collections;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * An implementation of {@link Ring} that internally operates on an
 * {@link ArrayList}.
 */
public class ArrayRing<E> extends AbstractRing<E>
{
   private final ArrayList<E> _backingList;

   /**
    * Constructs a new instance with a default {@link ArrayList} in the
    * background.
    */
   public ArrayRing()
   {
      _backingList = new ArrayList<E>();
   }

   /**
    * Constructs a new instance with a {@link ArrayList} in the background that
    * has the given initial capacity.
    * 
    * @see ArrayList#ArrayList(int)
    */
   public ArrayRing(final int initialCapacity)
   {
      _backingList = new ArrayList<E>(initialCapacity);
   }

   @Override
   public int size()
   {
      return _backingList.size();
   }

   @Override
   public RingCursor<E> cursor()
   {
      return new ArrayRingCursor<E>(this, 0);
   }

   /**
    * Adjusts the underlying {@link ArrayList} to have the minimum given
    * capacity.
    * 
    * @param minCapacity
    *           the new minimal capacity
    * @see {@link ArrayList#ensureCapacity(int)}
    */
   public void ensureCapacity(final int minCapacity)
   {
      _backingList.ensureCapacity(minCapacity);
   }

   private static class ArrayRingCursor<E> extends BasicRingCursor<E, ArrayRing<E>>
   {
      private int _position;

      private ArrayRingCursor(final ArrayRing<E> ring, final int position)
      {
         super(ring);

         _position = position;
      }

      @Override
      public E get()
      {
         checkForModification();

         if (_ring._backingList.isEmpty())
         {
            throw new NoSuchElementException();
         }

         return _ring._backingList.get(_position);
      }

      @Override
      public void insertAfter(final E value)
      {
         checkForModification();

         if (_ring.isEmpty())
         {
            _ring._backingList.add(value);
            _position = 0;
         }
         else
         {
            _ring._backingList.add(_position + 1, value);
         }

         ++_ring._modCount;
         ++_expectedModCount;
      }

      @Override
      public void insertBefore(final E value)
      {
         checkForModification();

         if (_position == 0)
         {
            _ring._backingList.add(value);
         }
         else
         {
            _ring._backingList.add(_position, value);
            ++_position;
         }

         ++_ring._modCount;
         ++_expectedModCount;
      }

      @Override
      public E next()
      {
         checkForModification();

         if (_ring.isEmpty())
         {
            throw new NoSuchElementException("Ring is empty");
         }

         ++_position;

         if (_position == _ring.size())
         {
            _position = 0;
         }

         return get();
      }

      @Override
      public E previous()
      {
         checkForModification();

         if (_ring.isEmpty())
         {
            throw new NoSuchElementException("Ring is empty");
         }

         --_position;

         if (_position == -1)
         {
            _position += _ring.size();
         }

         return get();
      }

      @Override
      public E remove()
      {
         checkForModification();

         if (_ring.isEmpty())
         {
            throw new NoSuchElementException("Ring is empty");
         }

         final E result = _ring._backingList.remove(_position);
         if (_position >= _ring._backingList.size())
         {
            --_position;
         }

         ++_ring._modCount;
         ++_expectedModCount;

         return result;
      }

      @Override
      public RingCursor<E> cursor()
      {
         return new ArrayRingCursor<E>(_ring, _position);
      }
   }
}
