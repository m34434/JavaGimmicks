package net.sf.javagimmicks.math.sequence;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An abstract helper for creating {@link NumberSequence} instances that cache
 * already calculated values for later use.
 */
public abstract class CachedNumberSequence<N extends Number> implements NumberSequence<N>
{
   private final ReadWriteLock _cacheLock = new ReentrantReadWriteLock();
   private final Map<BigInteger, N> _cache = new TreeMap<BigInteger, N>();

   protected CachedNumberSequence()
   {}

   abstract protected N compute(BigInteger index);

   @Override
   public final N get(final BigInteger index)
   {
      N result = getCached(index);
      if (result == null)
      {
         _cacheLock.writeLock().lock();
         try
         {
            result = _cache.get(index);
            if (result == null)
            {
               result = compute(index);
               _cache.put(index, result);
            }
         }
         finally
         {
            _cacheLock.writeLock().unlock();
         }
      }

      return result;
   }

   protected final N getCached(final BigInteger index)
   {
      _cacheLock.readLock().lock();
      try
      {
         return _cache.get(index);
      }
      finally
      {
         _cacheLock.readLock().unlock();
      }
   }

   protected final void putToCache(final BigInteger index, final N value)
   {
      _cacheLock.writeLock().lock();
      try
      {
         _cache.put(index, value);
      }
      finally
      {
         _cacheLock.writeLock().unlock();
      }
   }
}
