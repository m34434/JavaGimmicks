package net.sf.javagimmicks.collections;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import net.sf.javagimmicks.collections.decorators.AbstractQueueDecorator;

public class AutoDroppingQueueDecorator<E> extends AbstractQueueDecorator<E>
{
   private static final long serialVersionUID = 8775576852585456548L;

   private final int _maxSize;
   private final int _dropCount;

   public AutoDroppingQueueDecorator(final Queue<E> decorated, final int maxSize, final int dropCount)
   {
      super(decorated);

      if (maxSize < 1)
      {
         throw new IllegalArgumentException("Max size must be 1 or greater!");
      }

      if (dropCount < 1)
      {
         throw new IllegalArgumentException("Skip count must be 1 or greater!");
      }

      if (dropCount > maxSize)
      {
         throw new IllegalArgumentException("Skip count mustn't be greater than max size!");
      }

      _maxSize = maxSize;
      _dropCount = dropCount;
   }

   public AutoDroppingQueueDecorator(final Queue<E> internalQueue, final int maxSize)
   {
      this(internalQueue, maxSize, maxSize);
   }

   public AutoDroppingQueueDecorator(final int maxSize, final int skipCount)
   {
      this(new LinkedList<E>(), maxSize, skipCount);
   }

   public AutoDroppingQueueDecorator(final int maxSize)
   {
      this(maxSize, maxSize);
   }

   @Override
   public boolean add(final E o)
   {
      final boolean changed = size() == _maxSize;

      if (changed)
      {
         if (_dropCount == _maxSize)
         {
            clear();
         }
         else
         {
            for (int i = _dropCount; i > 0; --i)
            {
               remove();
            }
         }
      }

      return getDecorated().add(o) || changed;
   }

   @Override
   public boolean addAll(final Collection<? extends E> c)
   {
      boolean changed = false;

      for (final E element : c)
      {
         changed |= add(element);
      }

      return changed;
   }

   @Override
   public boolean offer(final E o)
   {
      return add(o);
   }
}
