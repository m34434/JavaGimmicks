package net.sf.javagimmicks.collections8.decorators;

import java.util.NavigableSet;

/**
 * A basic class for unmodifiable {@link NavigableSet} decorators that simply
 * forwards all read-calls to an internal delegate instance.
 */
public abstract class AbstractUnmodifiableNavigableSetDecorator<E> extends AbstractUnmodifiableSortedSetDecorator<E>
      implements NavigableSet<E>
{
   private static final long serialVersionUID = -2004886060529930248L;

   protected AbstractUnmodifiableNavigableSetDecorator(final NavigableSet<E> decorated)
   {
      super(decorated);
   }

   @Override
   public E ceiling(final E e)
   {
      return getDecorated().ceiling(e);
   }

   @Override
   public E floor(final E e)
   {
      return getDecorated().floor(e);
   }

   @Override
   public E higher(final E e)
   {
      return getDecorated().higher(e);
   }

   @Override
   public E lower(final E e)
   {
      return getDecorated().lower(e);
   }

   @Override
   public E pollFirst()
   {
      return getDecorated().pollFirst();
   }

   @Override
   public E pollLast()
   {
      return getDecorated().pollLast();
   }

   @Override
   protected NavigableSet<E> getDecorated()
   {
      return (NavigableSet<E>) super.getDecorated();
   }
}
