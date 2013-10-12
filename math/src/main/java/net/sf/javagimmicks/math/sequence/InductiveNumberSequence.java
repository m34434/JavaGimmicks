package net.sf.javagimmicks.math.sequence;

import static java.math.BigInteger.ONE;

import java.math.BigInteger;

/**
 * An abstract helper for creating {@link NumberSequence}s that uses caching
 * (based on {@link CachedNumberSequence}) on one hand side and ensures within
 * each call of {@link #computeInductive(BigInteger)} that all previous value
 * (with a lower index) are already computed and can be retrieved safely via
 * {@link #getCached(BigInteger)}. This is pretty useful for sequences that are
 * <b>inductive</b> - i.e. they need previous values for calculation of the
 * current value.
 */
public abstract class InductiveNumberSequence<N extends Number> extends CachedNumberSequence<N>
{
   private final BigInteger _startIndex;

   private BigInteger _cacheIndex;

   protected InductiveNumberSequence(final BigInteger startIndex)
   {
      if (startIndex == null)
      {
         throw new IllegalArgumentException("Start index cannot be null!");
      }
      _startIndex = startIndex;
      _cacheIndex = _startIndex.subtract(ONE);
   }

   abstract protected N computeInductive(BigInteger currentIndex);

   protected final boolean isStartIndex(final BigInteger index)
   {
      return _startIndex.equals(index);
   }

   protected final BigInteger getStartIndex()
   {
      return _startIndex;
   }

   @Override
   protected final N compute(final BigInteger index)
   {
      if (index.compareTo(_startIndex) < 0)
      {
         throw new IndexOutOfBoundsException(String.format("Index must be %1$s or greater!", _startIndex));
      }

      N result = null;
      for (BigInteger currentIndex = _cacheIndex.add(ONE); currentIndex.compareTo(index) <= 0; currentIndex = currentIndex
            .add(ONE))
      {
         result = computeInductive(currentIndex);
         putToCache(currentIndex, result);
      }

      return result;
   }
}
