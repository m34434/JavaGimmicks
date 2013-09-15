package net.sf.javagimmicks.collections;

/**
 * An abstract implementation of {@link Cursor} that provides default
 * implementations for some derivable methods.
 * <p>
 * These are:
 * <ul>
 * <li>{@link #insertAfter(Iterable)}</li>
 * <li>{@link #insertBefore(Iterable)}</li>
 * <li>{@link #next(int)}</li>
 * <li>{@link #previous(int)}</li>
 * </ul>
 */
public abstract class AbstractCursor<E> implements Cursor<E>
{
   @Override
   public void insertAfter(final Iterable<? extends E> collection)
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

   @Override
   public void insertBefore(final Iterable<? extends E> collection)
   {
      for (final E value : collection)
      {
         insertBefore(value);
      }
   }

   @Override
   public E next(final int count)
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

   @Override
   public E previous(final int count)
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
}