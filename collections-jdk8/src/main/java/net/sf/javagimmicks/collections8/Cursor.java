package net.sf.javagimmicks.collections8;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * Defines operations on a data structure that has a well-definied order of
 * elements but no count, index, beginning or end (e.g. an infinite sequence of
 * numbers).
 * <p>
 * A {@link Cursor} is quite similar, but also different in some points to
 * {@link Iterator} or {@link ListIterator}. Respectively, there are methods
 * like {@link #next()} or {@link #previous()} but nothing like
 * <code>hasNext()</code> or <code>hasPrevious()</code> as there is no end.
 * <p>
 * There is also one big difference to {@link Iterator} and {@link ListIterator}
 * - the {@link Cursor} does not reside between the elements but on them.
 * Respectively there is a {@link #get()} method that returns the element that
 * currently lies under the {@link Cursor}.
 * <p>
 * Besides {@link #remove()} and {@link #set(Object)} there are also methods to
 * insert new elements after or before the {@link Cursor}'s current location.
 * <p>
 * It is also possible to jump forward/backward by more than one element.
 * 
 * @param <E>
 *           the type of elements this {@link Cursor} operates on
 */
public interface Cursor<E> extends Supplier<E>
{

   /**
    * Moves the {@link Cursor} forward to the next element and returns it
    * 
    * @return the element after the {@link Cursor}'s location
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   E next() throws NoSuchElementException;

   /**
    * Moves the {@link Cursor} forward by any number of elements and returns the
    * one the finally lies under the {@link Cursor}.
    * <p>
    * If a negative number is given, the {@link Cursor} will simply move
    * backward.
    * 
    * @param count
    *           the number of positions to move forward
    * 
    * @return the resulting element after moving the {@link Cursor} forward by
    *         the given number of positions
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   default E next(final int count)
   {
      if (count < 0)
      {
         return previous(-count);
      }

      if (count == 0)
      {
         return get();
      }

      E result = null;
      for (int i = 0; i < count; ++i)
      {
         result = next();
      }

      return result;
   }

   /**
    * Moves the {@link Cursor} backward to the previous element and returns it
    * 
    * @return the element before the {@link Cursor}'s location
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   E previous() throws NoSuchElementException;

   /**
    * Moves the {@link Cursor} backward by any number of elements and returns
    * the one the finally lies under the {@link Cursor}.
    * <p>
    * If a negative number is given, the {@link Cursor} will simply move
    * forward.
    * 
    * @param count
    *           the number of positions to move backward
    * @return the resulting element after moving the {@link Cursor} backward by
    *         the given number of positions
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   default E previous(final int count)
   {
      if (count < 0)
      {
         return next(-count);
      }

      if (count == 0)
      {
         return get();
      }

      E result = null;
      for (int i = 0; i < count; ++i)
      {
         result = previous();
      }

      return result;
   }

   /**
    * Returns the element that currently lies under the {@link Cursor}.
    * 
    * @return the element that currently lies under the {@link Cursor}
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   @Override
   E get() throws NoSuchElementException;

   /**
    * Replaces the element that currently lies under the {@link Cursor} with a
    * given one and returns the replaced one.
    * 
    * @param value
    *           to new value to set
    * @return the element that previously lied under the {@link Cursor}
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   E set(E value);

   /**
    * Inserts a new element after the one that currently lies under the
    * {@link Cursor} into the underlying data structure keeping the
    * {@link Cursor} on it's current location.
    * <p>
    * If the data structure was empty before the call, the {@link Cursor} moves
    * over the inserted element.
    * 
    * @param value
    *           the value to insert
    */
   void insertAfter(E value);

   /**
    * Inserts a new element before the one that currently lies under the
    * {@link Cursor} into the underlying data structure keeping the
    * {@link Cursor} on it's current location.
    * <p>
    * If the data structure was empty before the call, the {@link Cursor} moves
    * over the inserted element.
    * 
    * @param value
    *           the value to insert
    */
   void insertBefore(E value);

   /**
    * Inserts a number of elements given as a {@link Iterable} after the one
    * that currently lies under the {@link Cursor} into the underlying data
    * structure keeping the {@link Cursor} on it's current location.
    * 
    * @param collection
    *           the {@link Iterable} of values to insert
    */
   default void insertAfter(final Iterable<? extends E> collection)
   {
      int count = 0;
      for (final E value : collection)
      {
         insertAfter(value);
         next();

         ++count;
      }

      previous(count);
   }

   /**
    * Inserts a number of elements given as a {@link Iterable} before the one
    * that currently lies under the {@link Cursor} into the underlying data
    * structure keeping the {@link Cursor} on it's current location.
    * 
    * @param collection
    *           the {@link Iterable} of values to insert
    */
   default void insertBefore(final Iterable<? extends E> collection)
   {
      for (final E value : collection)
      {
         insertBefore(value);
      }
   }

   /**
    * Removes and returns the value that currently lies under the {@link Cursor}
    * .
    * 
    * @return the removed value that lied under the {@link Cursor}.
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   E remove() throws NoSuchElementException;

   /**
    * Creates and returns a new {@link Cursor} at the current {@link Cursor}'s
    * position
    * 
    * @return the new {@link Cursor}
    */
   Cursor<E> cursor();
}