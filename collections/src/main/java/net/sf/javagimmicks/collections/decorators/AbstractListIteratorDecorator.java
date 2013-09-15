package net.sf.javagimmicks.collections.decorators;

import java.util.ListIterator;

/**
 * A basic class for {@link ListIterator} decorators
 * that simply forwards all calls to an internal
 * delegate instance.
 */
public abstract class AbstractListIteratorDecorator<E> extends AbstractIteratorDecorator<E> implements ListIterator<E>
{
   protected AbstractListIteratorDecorator(ListIterator<E> decorated)
   {
      super(decorated);
   }

   /**
    * Returns the decorated instance (the delegate)
    */
   @Override
   public ListIterator<E> getDecorated()
   {
      return (ListIterator<E>)super.getDecorated();
   }

   public void add(E e)
   {
      getDecorated().add(e);
   }

   public boolean hasPrevious()
   {
      return getDecorated().hasPrevious();
   }

   public int nextIndex()
   {
      return getDecorated().nextIndex();
   }

   public E previous()
   {
      return getDecorated().previous();
   }

   public int previousIndex()
   {
      return getDecorated().previousIndex();
   }

   public void set(E e)
   {
      getDecorated().set(e);
   }
}
