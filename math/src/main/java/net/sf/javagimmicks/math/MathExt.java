package net.sf.javagimmicks.math;

import java.math.BigInteger;

import net.sf.javagimmicks.math.sequence.FactorialSequence;

/**
 * Provides some extension to {@link Math} by providing additional utility
 */
public class MathExt
{
   private static final FactorialSequence _factorials = new FactorialSequence();

   private MathExt()
   {}

   /**
    * Calculates the <a
    * href="http://en.wikipedia.org/wiki/Factorial">factorial</a> for a given
    * number.
    * 
    * @param base
    *           the base number of which to calculate the factorial
    * @return the resulting factorial
    */
   public static BigInteger factorial(final BigInteger base)
   {
      return _factorials.get(base);
   }

   /**
    * Calculates the <a
    * href="http://en.wikipedia.org/wiki/Factorial">factorial</a> for a given
    * number.
    * 
    * @param base
    *           the base number of which to calculate the factorial
    * @return the resulting factorial
    */
   public static BigInteger factorial(final long base)
   {
      return factorial(BigInteger.valueOf(base));
   }

   /**
    * Calculates the <a
    * href="http://en.wikipedia.org/wiki/Binomial_coefficient">binomial
    * coefficient</a> for a given <b>n</b> and <b>k</b> number.
    * 
    * @param n
    *           the <b>n</b> part of the binomial coefficient
    * @param k
    *           the <b>k</b> part of the binomial coefficient
    * @return the resulting binomial coefficient
    */
   public static BigInteger binomial(final BigInteger n, final BigInteger k)
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
         return binomial(n, nMinusK);
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