package net.sf.javagimmicks.collections;

/**
 * Extends {@link Iterable} by defining an additional method to create a
 * {@link Traverser} which is a special kind of iterator that defines not
 * beginning or end but operations to modify or traverse the underlying data
 * structure.
 * 
 * @param <E>
 *           the type of elements this {@link Traversable} works on
 * @see Traverser
 */
public interface Traversable<E> extends Iterable<E>
{
   /**
    * Return a new {@link Traverser} for the current data structure
    * 
    * @return a new {@link Traverser}
    */
   public Traverser<E> traverser();
}
