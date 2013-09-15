package net.sf.javagimmicks.collections;

import java.util.NoSuchElementException;

/**
 * An implementation of {@link Ring} that internally operates on a double-linked
 * list implementation basis.
 */
public class LinkedRing<E> extends AbstractRing<E>
{
   private int _size = 0;
   private RingElement<E> _hook = null;

   @Override
   public int size()
   {
      return _size;
   }

   @Override
   public RingCursor<E> cursor()
   {
      return new LinkedRingCursor<E>(this, _hook);
   }

   private static class RingElement<E>
   {
      private RingElement<E> _previous;
      private RingElement<E> _next;

      private E _value;
   }

   private static class LinkedRingCursor<E> extends BasicRingCursor<E, LinkedRing<E>>
   {
      private RingElement<E> _current;

      private LinkedRingCursor(final LinkedRing<E> ring, final RingElement<E> current)
      {
         super(ring);
         _current = current;
      }

      @Override
      public E get()
      {
         if (_current == null)
         {
            throw new NoSuchElementException("Ring is empty");
         }

         return _current._value;
      }

      @Override
      public void insertAfter(final E value)
      {
         final RingElement<E> next = _current != null ? _current._next : null;

         insert(value, _current, next);
      }

      @Override
      public void insertBefore(final E value)
      {
         final RingElement<E> previous = _current != null ? _current._previous : null;

         insert(value, previous, _current);
      }

      @Override
      public E next()
      {
         checkForModification();

         if (_current == null)
         {
            throw new NoSuchElementException("Ring is empty");
         }

         _current = _current._next;

         return get();
      }

      @Override
      public E previous()
      {
         checkForModification();

         if (_current == null)
         {
            throw new NoSuchElementException("Ring is empty");
         }

         _current = _current._previous;

         return get();
      }

      @Override
      public E remove()
      {
         checkForModification();

         // Get the ring size
         final int ringSize = _ring._size;

         // If nothing to remove, throw an exception
         if (ringSize == 0)
         {
            throw new NoSuchElementException();
         }

         final E result = _current._value;

         if (ringSize == 1)
         {
            // If only one element is remaining, clean up everything
            _current._next = null;
            _current._previous = null;
            _current = null;
            _ring._hook = null;
         }
         else
         {
            // Get the next and previous element
            final RingElement<E> nextElement = _current._next;
            final RingElement<E> previousElement = _current._previous;

            // Re-assign their links
            nextElement._previous = previousElement;
            previousElement._next = nextElement;

            // Clean links of element to remove
            _current._next = null;
            _current._previous = null;

            // Relink the hook (if necessary) and the current RingCursor
            // position
            if (_ring._hook == _current)
            {
               _ring._hook = nextElement;
            }
            _current = nextElement;
         }

         // Decrease ring size
         --_ring._size;

         // Update modification counters
         ++_ring._modCount;
         ++_expectedModCount;

         return result;
      }

      @Override
      public RingCursor<E> cursor()
      {
         return new LinkedRingCursor<E>(_ring, _current);
      }

      private void insert(final E value, final RingElement<E> previous, final RingElement<E> next)
      {
         checkForModification();

         // Create the new element and set it's value
         final RingElement<E> newElement = new RingElement<E>();
         newElement._value = value;

         if (previous == null || next == null)
         {
            // This is the first element, so link it to itself
            newElement._previous = newElement;
            newElement._next = newElement;

            // Also update the current element and the hook of the ring
            _current = newElement;
            _ring._hook = newElement;
         }
         else
         {
            // Set links of new element
            newElement._previous = previous;
            newElement._next = next;

            // Set links to new element
            previous._next = newElement;
            next._previous = newElement;
         }

         // Increase ring size
         ++_ring._size;

         // Update modification counters
         ++_ring._modCount;
         ++_expectedModCount;
      }
   }
}
