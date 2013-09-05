package net.sf.javagimmicks.collections;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

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
public interface Cursor<E>
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
   E next(int count) throws NoSuchElementException;

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
   E previous(int count) throws NoSuchElementException;

   /**
    * Returns the element that currently lies under the {@link Cursor}.
    * 
    * @return the element that currently lies under the {@link Cursor}
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
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
   void insertAfter(Iterable<? extends E> collection);

   /**
    * Inserts a number of elements given as a {@link Iterable} before the one
    * that currently lies under the {@link Cursor} into the underlying data
    * structure keeping the {@link Cursor} on it's current location.
    * 
    * @param collection
    *           the {@link Iterable} of values to insert
    */
   void insertBefore(Iterable<? extends E> collection);

   /**
    * Removes and returns the value that currently lies under the {@link Cursor}
    * .
    * 
    * @return the removed value that lied under the {@link Cursor}.
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   E remove() throws NoSuchElementException;

}