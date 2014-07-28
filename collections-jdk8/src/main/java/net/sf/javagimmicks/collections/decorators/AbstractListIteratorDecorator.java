package net.sf.javagimmicks.collections.decorators;

import java.util.ListIterator;

/**
 * A basic class for {@link ListIterator} decorators that simply forwards all
 * calls to an internal delegate instance.
 */
public abstract class AbstractListIteratorDecorator<E> extends AbstractIteratorDecorator<E> implements ListIterator<E>
{
   protected AbstractListIteratorDecorator(final ListIterator<E> decorated)
   {
      super(decorated);
   }

   @Override
   public void add(final E e)
   {
      getDecorated().add(e);
   }

   @Override
   public boolean hasPrevious()
   {
      return getDecorated().hasPrevious();
   }

   @Override
   public int nextIndex()
   {
      return getDecorated().nextIndex();
   }

   @Override
   public E previous()
   {
      return getDecorated().previous();
   }

   @Override
   public int previousIndex()
   {
      return getDecorated().previousIndex();
   }

   @Override
   public void set(final E e)
   {
      getDecorated().set(e);
   }

   @Override
   protected ListIterator<E> getDecorated()
   {
      return (ListIterator<E>) super.getDecorated();
   }
}
