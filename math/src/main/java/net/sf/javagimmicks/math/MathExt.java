package net.sf.javagimmicks.math;

import java.math.BigInteger;

import net.sf.javagimmicks.math.sequence.FactorialSequence;

public class MathExt
{
   private static final FactorialSequence _factorials = new FactorialSequence();

   private MathExt()
   {}

   public static BigInteger factorial(final BigInteger base)
   {
      return _factorials.get(base);
   }

   public static BigInteger factorial(final long base)
   {
      return factorial(BigInteger.valueOf(base));
   }

   public static BigInteger binominal(final BigInteger n, final BigInteger k)
   {
      if (n == null || k == null)
      {
         throw new IllegalArgumentException("At least one argument was null!");
      }

      if (n.compareTo(k) < 0)
      {
         throw new IllegalArgumentException("k was greater than n!");
      }

      final BigInteger nMinusK = n.subtract(k);
      if (nMinusK.compareTo(k) > 0)
      {
         return binominal(n, nMinusK);
      }

      BigInteger result = BigInteger.ONE;

      for (BigInteger i = k.add(BigInteger.ONE); i.compareTo(n) <= 0; i = i.add(BigInteger.ONE))
      {
         result = result.multiply(i);
      }

      result = result.divide(factorial(nMinusK));

      return result;
   }
}