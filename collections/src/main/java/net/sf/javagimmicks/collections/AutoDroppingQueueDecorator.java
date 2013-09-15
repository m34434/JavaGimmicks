package net.sf.javagimmicks.collections;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import net.sf.javagimmicks.collections.decorators.AbstractQueueDecorator;

/**
 * A decorator for a given {@link Queue} that limits the maximum number of
 * entries within there to a given number by dropping a given number of entries
 * from it's end if the internal {@link Queue} is full.
 * <p>
 * A use case for this might be processing status messages in a
 * producer/consumer scenario from a long-running operation where each status
 * updates the previous one (so the previous one is obsolete and can be
 * dropped).
 */
public class AutoDroppingQueueDecorator<E> extends AbstractQueueDecorator<E>
{
   private static final long serialVersionUID = 8775576852585456548L;

   private final int _maxSize;
   private final int _dropCount;

   /**
    * Create a new instance around the given {@link Queue} with a given size
    * limit and drop count.
    * 
    * @param decorated
    *           the {@link Queue} to decorate
    * @param maxSize
    *           the size limit for the decorator
    * @param dropCount
    *           the numer of entries to drop if the size threshold is reached
    */
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

   /**
    * Create a new instance around the given {@link Queue} with a given size
    * limit that drops all entries when full.
    * 
    * @param decorated
    *           the {@link Queue} to decorate
    * @param maxSize
    *           the size limit for the decorator
    */
   public AutoDroppingQueueDecorator(final Queue<E> internalQueue, final int maxSize)
   {
      this(internalQueue, maxSize, maxSize);
   }

   /**
    * Create a new instance wrapping an internal {@link LinkedList} with a given
    * size limit and drop count.
    * 
    * @param maxSize
    *           the size limit for the decorator
    * @param dropCount
    *           the numer of entries to drop if the size threshold is reached
    */
   public AutoDroppingQueueDecorator(final int maxSize, final int skipCount)
   {
      this(new LinkedList<E>(), maxSize, skipCount);
   }

   /**
    * Create a new instance wrapping an internal {@link LinkedList} with a given
    * size limit that drops all entries when full.
    * 
    * @param maxSize
    *           the size limit for the decorator
    */
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
