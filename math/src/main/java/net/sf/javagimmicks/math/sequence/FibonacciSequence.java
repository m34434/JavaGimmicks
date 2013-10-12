package net.sf.javagimmicks.math.sequence;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;

/**
 * An implementation of {@link NumberSequence} for the <a
 * href="http://en.wikipedia.org/wiki/Fibonacci_number">Fibonacci</a> sequence.
 */
public class FibonacciSequence extends InductiveNumberSequence<BigInteger>
{
   /**
    * Returns a caches singleton instance of {@link FibonacciSequence}.
    * 
    * @return a caches singleton instance of {@link FibonacciSequence}
    */
   public static FibonacciSequence get()
   {
      return NumberSequences.get(FibonacciSequence.class);
   }

   /**
    * Returns the sequence value at the given index from the singleton
    * {@link FibonacciSequence} instance (see {@link #get()}).
    * 
    * @param index
    *           the index of the value to retrieve
    * @return the resulting value
    */
   public static BigInteger getValue(final BigInteger index)
   {
      return get().get(index);
   }

   /**
    * Creates a new instance.
    */
   public FibonacciSequence()
   {
      super(ZERO);
   }

   @Override
   protected BigInteger computeInductive(final BigInteger index)
   {
      if (ZERO.equals(index))
      {
         return ZERO;
      }
      else if (ONE.equals(index))
      {
         return ONE;
      }

      final BigInteger index1 = index.subtract(ONE);
      final BigInteger index2 = index1.subtract(ONE);

      return getCached(index1).add(getCached(index2));
   }
}
