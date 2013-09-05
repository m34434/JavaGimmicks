package net.sf.javagimmicks.collections.mapping;

import static org.junit.Assert.*;

import net.sf.javagimmicks.collections.mapping.Mappings.Mapping;

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
      final Mapping<Integer, String> inverseMappingA = mappingA.getInverseMapping();
      
      assertSame(mappingA, inverseMappingA.getInverseMapping());
      assertSame(mappingA.getLeft(), inverseMappingA.getRight());
      assertSame(mappingA.getRight(), inverseMappingA.getLeft());
      
      assertEquals(inverseMappingA.getLeft(), inverseMappingAReference.getLeft());
      assertEquals(inverseMappingA.getRight(), inverseMappingAReference.getRight());
      assertEquals(inverseMappingA, inverseMappingAReference);
   }
   
   @Test
   public void testToString()
   {
      // Just call it - content is not evaluated
      assertEquals("[a, 1]", createMapping("a", 1).toString());
   }
   
   private static <L, R> DefaultMapping<L, R> createMapping(L left, R right)
   {
      return new DefaultMapping<L, R>(left, right);
   }
}
