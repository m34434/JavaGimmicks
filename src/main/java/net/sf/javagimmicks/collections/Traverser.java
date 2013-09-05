package net.sf.javagimmicks.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Defines operations on a data structure that has a well-definied number and
 * order of elements but no index or beginning or end (like {@link Ring}).
 * <p>
 * The underlying data structure is <i>traversed</i> by a {@link Traverser}
 * using a cursor like it is the case for {@link Iterator} or
 * {@link ListIterator}. Respectively, there are methods like {@link #next()} or
 * {@link #previous()} but nothing like <code>hasNext()</code> or
 * <code>hasPrevious()</code> as there is no end.
 * <p>
 * There is also one big difference to {@link Iterator} and {@link ListIterator}
 * - the cursor does not reside between the elements but on them. Respectively
 * there is a {@link #get()} method that returns the element that currently lies
 * under the cursor.
 * <p>
 * Besides {@link #remove()} and {@link #set(Object)} there are also methods to
 * insert new elements after or before the cursor's current location.
 * <p>
 * It is also possible to jump forward/backward by more than one element.
 * 
 * @param <E>
 *           the type of elements this {@link Traverser} operates on
 */
public interface Traverser<E> extends Traversable<E>
{
   /**
    * Moves the cursor forward to the next element and returns it
    * 
    * @return the element after the cursor's location
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   public E next() throws NoSuchElementException;

   /**
    * Moves the cursor forward by any number of elements and returns the one the
    * finally lies under the cursor.
    * <p>
    * If the number of positions to move is bigger than the size of the
    * {@link Traverser} the cursor will simply circle through the data structure
    * and start over from the "beginning".
    * <p>
    * If a negative number is given, the cursor will simply move backward.
    * 
    * @param count
    *           the number of positions to move forward
    * 
    * @return the resulting element after moving the cursor forward by the given
    *         number of positions
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   public E next(int count) throws NoSuchElementException;

   /**
    * Moves the cursor backward to the previous element and returns it
    * 
    * @return the element before the cursor's location
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   public E previous() throws NoSuchElementException;

   /**
    * Moves the cursor backward by any number of elements and returns the one
    * the finally lies under the cursor.
    * <p>
    * If the number of positions to move is bigger than the size of the
    * {@link Traverser} the cursor will simply circle through the data structure
    * and start over from the "beginning".
    * <p>
    * If a negative number is given, the cursor will simply move forward.
    * 
    * @param count
    *           the number of positions to move backward
    * @return the resulting element after moving the cursor backward by the
    *         given number of positions
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   public E previous(int count) throws NoSuchElementException;

   /**
    * Returns the element that currently lies under the cursor.
    * 
    * @return the element that currently lies under the cursor
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   public E get() throws NoSuchElementException;

   /**
    * Replaces the element that currently lies under the cursor with a given one
    * and returns the replaced one.
    * 
    * @param value
    *           to new value to set
    * @return the element that previously lied under the cursor
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   public E set(E value);

   /**
    * Inserts a new element after the one that currently lies under the cursor
    * into the underlying data structure keeping the cursor on it's current
    * location.
    * <p>
    * If the data structure was empty before the call, the cursor moves over the
    * inserted element.
    * 
    * @param value
    *           the value to insert
    */
   public void insertAfter(E value);

   /**
    * Inserts a new element before the one that currently lies under the cursor
    * into the underlying data structure keeping the cursor on it's current
    * location.
    * <p>
    * If the data structure was empty before the call, the cursor moves over the
    * inserted element.
    * 
    * @param value
    *           the value to insert
    */
   public void insertBefore(E value);

   /**
    * Inserts a number of elements given as a {@link Collection} after the one
    * that currently lies under the cursor into the underlying data structure
    * keeping the cursor on it's current location.
    * 
    * @param collection
    *           the {@link Collection} of values to insert
    */
   public void insertAfter(Collection<? extends E> collection);

   /**
    * Inserts a number of elements given as a {@link Collection} before the one
    * that currently lies under the cursor into the underlying data structure
    * keeping the cursor on it's current location.
    * 
    * @param collection
    *           the {@link Collection} of values to insert
    */
   public void insertBefore(Collection<? extends E> collection);

   /**
    * Removes and returns the value that currently lies under the cursor.
    * 
    * @return the remove value that lied under the cursor.
    * @throws NoSuchElementException
    *            if the underlying data structure is empty
    */
   public E remove() throws NoSuchElementException;

   /**
    * Creates and returns a new {@link Traverser} on the underlying data
    * structure with the cursor pointing the current {@link Traverser}'s
    * position.
    * 
    * @return the resulting {@link Traverser}
    */
   public Traverser<E> traverser();

   /**
    * Transforms the underlying data structure into a {@link List} with the same
    * order that starts at the current cursor's position.
    * 
    * @return the respective {@link List}
    */
   public List<E> toList();

   /**
    * Returns the size of the underlying data structure.
    * 
    * @return the size of the underlying data structure
    */
   public int size();

   /**
    * Returns if the underlying data is empty.
    * 
    * @return if the underlying data is empty
    */
   public boolean isEmpty();
}
