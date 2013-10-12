package net.sf.javagimmicks.math.sequence;

import static java.math.BigInteger.ONE;

import java.math.BigInteger;

/**
 * An implementation of {@link NumberSequence} for the <a
 * href="http://en.wikipedia.org/wiki/Factorial">factorial</a> sequence.
 */
public class FactorialSequence extends InductiveNumberSequence<BigInteger>
{
   /**
    * Returns a caches singleton instance of {@link FactorialSequence}.
    * 
    * @return a caches singleton instance of {@link FactorialSequence}
    */
   public static FactorialSequence get()
   {
      return NumberSequences.get(FactorialSequence.class);
   }

   /**
    * Returns the sequence value at the given index from the singleton
    * {@link FactorialSequence} instance (see {@link #get()}).
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
   public FactorialSequence()
   {
      super(ONE);
   }

   @Override
   protected BigInteger computeInductive(final BigInteger index)
   {
      if (isStartIndex(index))
      {
         return ONE;
      }

      return getCached(index.subtract(ONE)).multiply(index);
   }
}
