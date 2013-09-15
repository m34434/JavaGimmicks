package net.sf.javagimmicks.collections;

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
   E next(int count) throws NoSuchElementException;

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
   E previous(int count) throws NoSuchElementException;

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
   List<E> toList();

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
   boolean isEmpty();
}
