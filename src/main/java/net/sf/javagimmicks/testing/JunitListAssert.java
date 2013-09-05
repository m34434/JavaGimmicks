package net.sf.javagimmicks.testing;

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

public class JunitListAssert
{
   public static <E> void assertListEquals(String message, List<? extends E> actual, E... expected)
   {
      assertArrayEquals(message, expected, actual.toArray());
   }

   public static <E> void assertListEquals(List<? extends E> actual, E... expected)
   {
      assertArrayEquals(expected, actual.toArray());
   }
}
