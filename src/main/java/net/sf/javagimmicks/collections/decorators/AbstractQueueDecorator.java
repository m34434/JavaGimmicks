package net.sf.javagimmicks.collections.decorators;

import java.util.Queue;

public abstract class AbstractQueueDecorator<E> extends AbstractCollectionDecorator<E> implements Queue<E>
{
   private static final long serialVersionUID = -4415496990793072540L;

   protected AbstractQueueDecorator(Queue<E> decorated)
   {
      super(decorated);
   }
   
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
