package net.sf.javagimmicks.collections8;

import java.util.Collection;
import java.util.Iterator;

/**
 * Defines a {@link Collection}-like data structure that organizes elements
 * within a ring structure, so there is a well-defined order but no index,
 * beginning or end.
 * <p>
 * The interface fully extends the {@link Collection} interface but additionally
 * extends {@link RingCursorProvider} that allows to create a {@link RingCursor}
 * for it which is a special kind of iterator that defines no beginning or end
 * but operations to modify or traverse the underlying data structure.
 * 
 * @param <E>
 *           the type of elements this {@link Ring} can contain
 */
public interface Ring<E> extends Collection<E>, RingCursorProvider<E>
{
   @Override
   default boolean add(final E value)
   {
      // Get the initial RingCursor add the element before the current position
      // (this means at the 'end' of the ring)
      cursor().insertBefore(value);

      return true;
   }

   @Override
   default boolean addAll(final Collection<? extends E> collection)
   {
      // Get the initial RingCursor add the elements before the current position
      // (this means at the 'end' of the ring)
      cursor().insertBefore(collection);

      return true;
   }

   @Override
   default Iterator<E> iterator()
   {
      return cursor().iterator();
   }
}
