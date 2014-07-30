package net.sf.javagimmicks.collections8.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import net.sf.javagimmicks.collections8.mapping.DefaultMapping;
import net.sf.javagimmicks.collections8.mapping.Mappings.Mapping;

import org.junit.Test;

public class DefaultMappingTest
{
   private static final DefaultMapping<String, Integer> mappingA = createMapping("a", 1);
   private static final DefaultMapping<String, Integer> mappingB = createMapping("a", 1);
   private static final DefaultMapping<String, Integer> mappingC = createMapping("b", 1);
   private static final DefaultMapping<String, Integer> mappingD = createMapping("a", 2);

   private static final DefaultMapping<Integer, String> inverseMappingAReference = createMapping(1, "a");

   @Test
   public void testEquals()
   {
      assertTrue(mappingA.equals(mappingA));

      assertTrue(mappingA.equals(mappingB));
      assertTrue(mappingB.equals(mappingA));

      assertFalse(mappingA.equals(mappingC));
      assertFalse(mappingA.equals(mappingD));

      assertFalse(mappingA.equals(null));
      assertFalse(mappingA.equals("a"));
      assertFalse(mappingA.equals(1));
   }

   @Test
   public void testHashCode()
   {
      assertTrue(mappingA.hashCode() == mappingB.hashCode());
      assertFalse(mappingA.hashCode() == mappingC.hashCode());
      assertFalse(mappingA.hashCode() == mappingD.hashCode());
   }

   @Test
   public void testInverseMapping()
   {
      final Mapping<Integer, String> inverseMappingA = mappingA.invert();

      assertSame(mappingA, inverseMappingA.invert());
      assertSame(mappingA.getLeftKey(), inverseMappingA.getRightKey());
      assertSame(mappingA.getRightKey(), inverseMappingA.getLeftKey());

      assertEquals(inverseMappingA.getLeftKey(), inverseMappingAReference.getLeftKey());
      assertEquals(inverseMappingA.getRightKey(), inverseMappingAReference.getRightKey());
      assertEquals(inverseMappingA, inverseMappingAReference);
   }

   @Test
   public void testToString()
   {
      // Just call it - content is not evaluated
      assertEquals("[a, 1]", createMapping("a", 1).toString());
   }

   private static <L, R> DefaultMapping<L, R> createMapping(final L left, final R right)
   {
      return new DefaultMapping<L, R>(left, right);
   }
}
