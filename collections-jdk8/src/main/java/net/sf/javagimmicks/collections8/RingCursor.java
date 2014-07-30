package net.sf.javagimmicks.collections8;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A special version of {@link Cursor} that operates on a "closed" data
 * structure with a finite number of elements. As it still has no beginning or
 * end, this must be a cyclic data structure (represented by the {@link Ring}
 * interface).
 * <p>
 * It provides additional operations to get information about the underlying
 * {@link Ring} like {@link #size()} or {@link #isEmpty()} as well as bridge
 * methods to convenient data structures from the <code>java.util</code>
 * package.
 * 
 * @param <E>
 *           the type of elements this {@link RingCursor} operates on
 */
public interface RingCursor<E> extends RingCursorProvider<E>, Cursor<E>
{
   /**
    * Moves the {@link RingCursor} forward by any number of elements and returns
    * the one the finally lies under the {@link RingCursor}.
    * <p>
    * If the number of positions to move is bigger than the size of the
    * {@link RingCursor} it will simply circle through the data structure and
    * start over from the original position.
    * 
    * @param count
    *           the number of positions to move forward
    * 
    * @return the resulting element after moving the cursor forward by the given
    *         number of positions
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    * @see Cursor#next(int)
    */
   @Override
   default E next(int count)
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
    * Moves the {@link RingCursor} backward by any number of elements and
    * returns the one the finally lies under the {@link RingCursor}.
    * <p>
    * If the number of positions to move is bigger than the size of the
    * {@link RingCursor} it will simply circle through the data structure and
    * start over from the original position.
    * 
    * @param count
    *           the number of positions to move backward
    * @return the resulting element after moving the {@link Cursor} backward by
    *         the given number of positions
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    * @see Cursor#previous(int)
    */
   @Override
   default E previous(int count)
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

   /**
    * Creates and returns a new {@link RingCursor} on the underlying data
    * structure pointing the current {@link RingCursor}'s position.
    * 
    * @return the resulting {@link RingCursor}
    */
   @Override
   RingCursor<E> cursor();

   /**
    * Transforms the underlying data structure into a {@link List} with the same
    * order that starts at the current cursor's position.
    * 
    * @return the respective {@link List}
    */
   default List<E> toList()
   {
      final ArrayList<E> result = new ArrayList<E>(size());
      for (final E element : this)
      {
         result.add(element);
      }

      return result;
   }

   /**
    * Returns the size of the underlying data structure.
    * 
    * @return the size of the underlying data structure
    */
   int size();

   /**
    * Returns if the underlying data is empty.
    * 
    * @return if the underlying data is empty
    */
   default boolean isEmpty()
   {
      return size() == 0;
   }

   default E set(final E value)
   {
      if (isEmpty())
      {
         throw new NoSuchElementException("There is no element to replace");
      }

      insertAfter(value);
      return remove();
   }

   default Iterator<E> iterator()
   {
      return new RingIterator<E>(cursor());
   }
}
