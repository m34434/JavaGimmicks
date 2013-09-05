package net.sf.javagimmicks.collections;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import net.sf.javagimmicks.collections.decorators.AbstractQueueDecorator;

public class AutoSkippingQueueDecorator<E> extends AbstractQueueDecorator<E>
{
   private static final long serialVersionUID = 8775576852585456548L;

   private final int _maxSize;
   private final int _skipCount;
   
   public AutoSkippingQueueDecorator(Queue<E> decorated, int maxSize, int skipCount)
   {
      super(decorated);
      
      if(maxSize < 1)
      {
         throw new IllegalArgumentException("Max size must be 1 or greater!");
      }
      
      if(skipCount < 1)
      {
         throw new IllegalArgumentException("Skip count must be 1 or greater!");
      }
      
      if(skipCount > maxSize)
      {
         throw new IllegalArgumentException("Skip count mustn't be greater than max size!");
      }
      
      _maxSize = maxSize;
      _skipCount = skipCount;
   }

   public AutoSkippingQueueDecorator(Queue<E> internalQueue, int maxSize)
   {
      this(internalQueue, maxSize, maxSize);
   }
   
   public AutoSkippingQueueDecorator(int maxSize, int skipCount)
   {
      this(new LinkedList<E>(), maxSize, skipCount);
   }
   
   public AutoSkippingQueueDecorator(int maxSize)
   {
      this(maxSize, maxSize);
   }
   
   public boolean add(E o)
   {
      boolean changed = size() == _maxSize;
      
      if(changed)
      {
         if(_skipCount == _maxSize)
         {
            clear();
         }
         else
         {
            for(int i = _skipCount; i > 0; --i)
            {
               remove();
            }
         }
      }
      
      return getDecorated().add(o) || changed;
   }

   public boolean addAll(Collection<? extends E> c)
   {
      boolean changed = false;
      
      for(E element : c)
      {
         changed |= add(element);
      }
      
      return changed;
   }

   public boolean offer(E o)
   {
      return add(o);
   }
}
