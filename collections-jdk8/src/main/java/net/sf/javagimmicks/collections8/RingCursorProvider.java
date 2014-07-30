package net.sf.javagimmicks.collections8;

/**
 * Extends {@link Iterable} by defining an additional method to create a
 * {@link RingCursor} which is a special kind of iterator that defines no
 * beginning or end but operations to modify or traverse the underlying data
 * structure.
 * 
 * @param <E>
 *           the type of elements this {@link RingCursorProvider} works on
 * @see RingCursor
 */
public interface RingCursorProvider<E> extends Iterable<E>
{
   /**
    * Returns a new {@link RingCursor} for the current data structure
    * 
    * @return a new {@link RingCursor}
    */
   public RingCursor<E> cursor();
}
