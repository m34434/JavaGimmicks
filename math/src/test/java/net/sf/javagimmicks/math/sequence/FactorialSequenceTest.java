package net.sf.javagimmicks.math.sequence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.Test;

public class FactorialSequenceTest
{
   @Test
   public void test()
   {
      final FactorialSequence s = new FactorialSequence();

      s.get(BigInteger.valueOf(1000L));

      assertEquals(new BigInteger("3628800"), s.get(new BigInteger("10")));
      assertEquals(new BigInteger("362880"), s.get(new BigInteger("9")));
      assertEquals(new BigInteger("40320"), s.get(new BigInteger("8")));
      assertEquals(new BigInteger("5040"), s.get(new BigInteger("7")));
      assertEquals(new BigInteger("720"), s.get(new BigInteger("6")));
      assertEquals(new BigInteger("120"), s.get(new BigInteger("5")));
      assertEquals(new BigInteger("24"), s.get(new BigInteger("4")));
      assertEquals(new BigInteger("6"), s.get(new BigInteger("3")));
      assertEquals(new BigInteger("2"), s.get(new BigInteger("2")));
      assertEquals(new BigInteger("1"), s.get(new BigInteger("1")));

      try
      {
         s.get(new BigInteger("0"));
         fail(IndexOutOfBoundsException.class.getName() + " expected!");
      }
      catch (final IndexOutOfBoundsException ignore)
      {
      }
   }
}
