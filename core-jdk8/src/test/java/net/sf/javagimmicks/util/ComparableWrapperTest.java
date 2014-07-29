package net.sf.javagimmicks.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.function.Supplier;

import org.junit.Test;

public class ComparableWrapperTest
{
   @Test
   public void testWithComparableInterface()
   {
      final ComparableWrapper<StringWrapper, StringWrapperComparable> wrapper = ComparableWrapper.create(
            StringWrapper.class, StringWrapperComparable.class, COMPARATOR);

      final StringWrapperComparable w1 = wrapper.wrap(() -> "1");
      final StringWrapperComparable w2 = wrapper.wrap(() -> "2");
      final StringWrapperComparable w3 = wrapper.wrap(() -> "3");

      assertEquals("1", w1.get());
      assertEquals("2", w2.get());
      assertEquals("3", w3.get());

      assertEquals(0, w1.compareTo(w1));

      assertTrue(w1.compareTo(w2) < 0);
      assertTrue(w1.compareTo(w3) < 0);
      assertTrue(w2.compareTo(w3) < 0);

      assertTrue(w2.compareTo(w1) > 0);
      assertTrue(w3.compareTo(w1) > 0);
      assertTrue(w3.compareTo(w2) > 0);
   }

   @SuppressWarnings("unchecked")
   @Test
   public void testWithoutComparableInterface()
   {
      final ComparableWrapper<StringWrapper, StringWrapper> wrapper = ComparableWrapper.create(
            StringWrapper.class, COMPARATOR);

      final StringWrapper w1 = wrapper.wrap(() -> "1");
      final StringWrapper w2 = wrapper.wrap(() -> "2");
      final StringWrapper w3 = wrapper.wrap(() -> "3");

      assertEquals("1", w1.get());
      assertEquals("2", w2.get());
      assertEquals("3", w3.get());

      assertEquals(0, ((Comparable<StringWrapper>) w1).compareTo(w1));

      assertTrue(((Comparable<StringWrapper>) w1).compareTo(w2) < 0);
      assertTrue(((Comparable<StringWrapper>) w1).compareTo(w3) < 0);
      assertTrue(((Comparable<StringWrapper>) w2).compareTo(w3) < 0);

      assertTrue(((Comparable<StringWrapper>) w2).compareTo(w1) > 0);
      assertTrue(((Comparable<StringWrapper>) w3).compareTo(w1) > 0);
      assertTrue(((Comparable<StringWrapper>) w3).compareTo(w2) > 0);
   }

   @FunctionalInterface
   private static interface StringWrapper extends Supplier<String>
   {}

   private static interface StringWrapperComparable extends StringWrapper, Comparable<StringWrapperComparable>
   {}

   private static final Comparator<StringWrapper> COMPARATOR = (o1, o2) ->
   {
      if (o1 == null && o2 == null)
      {
         return 0;
      }

      if (o1 == null)
      {
         return 1;
      }

      if (o2 == null)
      {
         return -1;
      }

      return o1.get().compareTo(o2.get());
   };
}
