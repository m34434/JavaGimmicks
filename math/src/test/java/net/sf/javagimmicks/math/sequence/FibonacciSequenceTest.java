package net.sf.javagimmicks.math.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.Test;

public class FibonacciSequenceTest
{
   @Test
   public void test()
   {
      final FibonacciSequence s = new FibonacciSequence();

      s.get(BigInteger.valueOf(1000L));

      assertEquals(new BigInteger("55"), s.get(new BigInteger("10")));
      assertEquals(new BigInteger("34"), s.get(new BigInteger("9")));
      assertEquals(new BigInteger("21"), s.get(new BigInteger("8")));
      assertEquals(new BigInteger("13"), s.get(new BigInteger("7")));
      assertEquals(new BigInteger("8"), s.get(new BigInteger("6")));
      assertEquals(new BigInteger("5"), s.get(new BigInteger("5")));
      assertEquals(new BigInteger("3"), s.get(new BigInteger("4")));
      assertEquals(new BigInteger("2"), s.get(new BigInteger("3")));
      assertEquals(new BigInteger("1"), s.get(new BigInteger("2")));
      assertEquals(new BigInteger("1"), s.get(new BigInteger("1")));
      assertEquals(new BigInteger("0"), s.get(new BigInteger("0")));

      try
      {
         s.get(new BigInteger("-1"));
         fail(IndexOutOfBoundsException.class.getName() + " expected!");
      }
      catch (final IndexOutOfBoundsException ignore)
      {
      }
   }
}
