package net.sf.javagimmicks.collections.decorators;

import java.util.Queue;

/**
 * A basic class for {@link Queue} decorators that simply forwards all calls to
 * an internal delegate instance.
 */
public abstract class AbstractQueueDecorator<E> extends AbstractCollectionDecorator<E> implements Queue<E>
{
   private static final long serialVersionUID = -4415496990793072540L;

   protected AbstractQueueDecorator(final Queue<E> decorated)
   {
      super(decorated);
   }

   @Override
   public E element()
   {
      return getDecorated().element();
   }

   @Override
   public boolean offer(final E e)
   {
      return getDecorated().offer(e);
   }

   @Override
   public E peek()
   {
      return getDecorated().peek();
   }

   @Override
   public E poll()
   {
      return getDecorated().poll();
   }

   @Override
   public E remove()
   {
      return getDecorated().remove();
   }

   @Override
   protected Queue<E> getDecorated()
   {
      return (Queue<E>) super.getDecorated();
   }
}
