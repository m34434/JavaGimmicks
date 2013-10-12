package net.sf.javagimmicks.math.sequence;

import static java.math.BigInteger.ONE;

import java.math.BigInteger;

public class FactorialSequence extends InductiveNumberSequence<BigInteger>
{
   public static FactorialSequence get()
   {
      return NumberSequences.get(FactorialSequence.class);
   }

   public static BigInteger getValue(final BigInteger index)
   {
      return get().get(index);
   }

   protected FactorialSequence()
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
