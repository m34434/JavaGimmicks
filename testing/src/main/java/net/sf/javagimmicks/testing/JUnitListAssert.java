package net.sf.javagimmicks.testing;

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

/**
 * Contains utility methods the help to make assertions against {@link List}s by
 * providing the expected values as a variable argument list.
 */
public class JUnitListAssert
{
   private JUnitListAssert()
   {}

   /**
    * Compares the given {@link List} against a variable argument list of
    * expected values.
    * 
    * @param message
    *           the error message to show if the assertion fails
    * @param actual
    *           the actual values as {@link List}
    * @param expected
    *           the expected values as variable argument list
    * @param <E>
    *           the type of elements of the given {@link List} and argument list
    */
   public static <E> void assertListEquals(final String message, final List<? extends E> actual, final E... expected)
   {
      assertArrayEquals(message, expected, actual.toArray());
   }

   /**
    * Compares the given {@link List} against a variable argument list of
    * expected values.
    * 
    * @param actual
    *           the actual values as {@link List}
    * @param expected
    *           the expected values as variable argument list
    * @param <E>
    *           the type of elements of the given {@link List} and argument list
    */
   public static <E> void assertListEquals(final List<? extends E> actual, final E... expected)
   {
      assertArrayEquals(expected, actual.toArray());
   }
}
