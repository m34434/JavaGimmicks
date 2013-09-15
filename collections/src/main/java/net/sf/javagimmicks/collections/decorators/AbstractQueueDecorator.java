package net.sf.javagimmicks.collections.decorators;

import java.util.Queue;

/**
 * A basic class for {@link Queue} decorators
 * that simply forwards all calls to an internal
 * delegate instance.
 */
public abstract class AbstractQueueDecorator<E> extends AbstractCollectionDecorator<E> implements Queue<E>
{
   private static final long serialVersionUID = -4415496990793072540L;

   protected AbstractQueueDecorator(Queue<E> decorated)
   {
      super(decorated);
   }
   
   /**
    * Returns the decorated instance (the delegate)
    */
   public Queue<E> getDecorated()
   {
      return (Queue<E>)super.getDecorated();
   }

   public E element()
   {
      return getDecorated().element();
   }

   public boolean offer(E e)
   {
      return getDecorated().offer(e);
   }

   public E peek()
   {
      return getDecorated().peek();
   }

   public E poll()
   {
      return getDecorated().poll();
   }

   public E remove()
   {
      return getDecorated().remove();
   }
}
