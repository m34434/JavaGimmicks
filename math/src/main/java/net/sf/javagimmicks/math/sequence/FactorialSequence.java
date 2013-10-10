package net.sf.javagimmicks.math.sequence;

import java.math.BigInteger;

public class FactorialSequence extends InductiveNumberSequence<BigInteger>
{
   public FactorialSequence()
   {
      super(BigInteger.ONE);
   }

   @Override
   protected BigInteger computeFirst()
   {
      return BigInteger.ONE;
   }

   @Override
   protected BigInteger computeNext(final BigInteger previous, final BigInteger currentIndex)
   {
      return previous.multiply(currentIndex);
   }
}
