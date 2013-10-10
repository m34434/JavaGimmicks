package net.sf.javagimmicks.math.sequence;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public abstract class InductiveNumberSequence<N extends Number> implements NumberSequence<N>
{
   private final Map<BigInteger, N> _cache = Collections.synchronizedMap(new TreeMap<BigInteger, N>());

   private final BigInteger _startIndex;

   private BigInteger _cacheIndex;

   protected InductiveNumberSequence(final BigInteger startIndex)
   {
      if (startIndex == null)
      {
         throw new IllegalArgumentException("Start index cannot be null!");
      }
      _startIndex = startIndex;
   }

   abstract protected N computeFirst();

   abstract protected N computeNext(N previous, BigInteger currentIndex);

   @Override
   public N get(final BigInteger index)
   {
      if (index.compareTo(_startIndex) < 0)
      {
         throw new IndexOutOfBoundsException(String.format("Index must be %1$s or greater!", _startIndex));
      }

      N result = _cache.get(index);
      if (result == null)
      {
         synchronized (_cache)
         {
            result = _cache.get(index);
            if (result == null)
            {
               if (_cacheIndex == null)
               {
                  _cacheIndex = _startIndex;
                  _cache.put(_cacheIndex, computeFirst());
               }
               result = _cache.get(_cacheIndex);

               BigInteger currentIndex = _cacheIndex;
               while (currentIndex.compareTo(index) < 0)
               {
                  currentIndex = currentIndex.add(BigInteger.ONE);
                  result = computeNext(result, currentIndex);

                  _cache.put(currentIndex, result);
               }

               _cacheIndex = index;
            }
         }
      }

      return result;
   }
}
