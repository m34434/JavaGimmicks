package net.sf.javagimmicks.collections.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.junit.Test;

import net.sf.javagimmicks.collections.builder.CollectionBuilder;
import net.sf.javagimmicks.collections.builder.MapBuilder;

public class DualMapMappingsTest
{
   private static final TreeSet<Integer> refSetA = CollectionBuilder.create(new TreeSet<Integer>())
      .add(1)
      .add(2)
      .add(3)
      .add(4).toCollection();

   private static final TreeSet<Integer> refSetB = CollectionBuilder.<Integer>createTreeSet()
      .add(2)
      .add(4).toCollection();
   
   private static final TreeSet<Integer> refSetC = CollectionBuilder.<Integer>createTreeSet()
      .add(3)
      .add(4).toCollection();
   
   private static final TreeSet<Integer> refSetD = CollectionBuilder.<Integer>createTreeSet()
      .add(4).toCollection();
   
   private static final HashMap<String, Set<Integer>> refMapLeft = MapBuilder.<String, Set<Integer>>createHashMap()
      .put("A", refSetA)
      .put("B", refSetB)
      .put("C", refSetC)
      .put("D", refSetD)
      .toMap();

   private static final HashSet<String> refSet1 = CollectionBuilder.<String>createHashSet()
      .add("A").toCollection();
   
   private static final HashSet<String> refSet2 = CollectionBuilder.<String>createHashSet()
      .add("A")
      .add("B").toCollection();
   
   private static final HashSet<String> refSet3 = CollectionBuilder.<String>createHashSet()
      .add("A")
      .add("C").toCollection();
   
   private static final HashSet<String> refSet4 = CollectionBuilder.<String>createHashSet()
      .add("A")
      .add("B")
      .add("C")
      .add("D").toCollection();
   
   private static final HashMap<Integer, Set<String>> refMapRight = MapBuilder.<Integer, Set<String>>createHashMap()
      .put(1, refSet1)
      .put(2, refSet2)
      .put(3, refSet3)
      .put(4, refSet4)
      .toMap();

   private Mappings<String, Integer> createMappings()
   {
      return DualMapMappings.createHashTreeInstance();
   }
   
   @Test
   public void testPut()
   {
      Mappings<String, Integer> mappings = createMappings();
      
      mappings.put("A", 1);
      assertEquals(1, mappings.size());
      assertTrue(mappings.containsLeft("A"));
      assertTrue(mappings.containsRight(1));
      assertTrue(mappings.contains("A", 1));
      
      mappings.putRight("A", Arrays.asList(new Integer[]{2, 3}));
      assertEquals(3, mappings.size());
      assertTrue(mappings.containsLeft("A"));
      assertTrue(mappings.containsRight(2));
      assertTrue(mappings.containsRight(3));
      assertTrue(mappings.contains("A", 2));
      assertTrue(mappings.contains("A", 3));
      
      mappings.putLeft(4, Arrays.asList(new String[]{"A", "B", "C", "D"}));
      assertEquals(7, mappings.size());
      assertTrue(mappings.containsLeft("A"));
      assertTrue(mappings.containsLeft("B"));
      assertTrue(mappings.containsLeft("C"));
      assertTrue(mappings.containsLeft("D"));
      assertTrue(mappings.containsRight(4));
      assertTrue(mappings.contains("A", 4));
      assertTrue(mappings.contains("B", 4));
      assertTrue(mappings.contains("C", 4));
      assertTrue(mappings.contains("D", 4));
      
      Mappings<Integer, String> inverseMappings = mappings.getInverseMappings();
      inverseMappings.put(1, "A");
      inverseMappings.putRight(2, Arrays.asList(new String[]{"A", "B"}));
      inverseMappings.putLeft("C", Arrays.asList(new Integer[]{3, 4}));
      
      assertEquals(refMapLeft, mappings.getLeftMap());
      assertEquals(refMapRight, mappings.getRightMap());

      assertEquals(refSetA, mappings.getRight("A"));
      assertEquals(refSetB, mappings.getRight("B"));
      assertEquals(refSetC, mappings.getRight("C"));
      assertEquals(refSetD, mappings.getRight("D"));

      assertEquals(refSet1, mappings.getLeft(1));
      assertEquals(refSet2, mappings.getLeft(2));
      assertEquals(refSet3, mappings.getLeft(3));
      assertEquals(refSet4, mappings.getLeft(4));
   }
   
   @Test
   public void testInverseMappings()
   {
      Mappings<String, Integer> mappings = createMappings();
      mappings.put("a", 1);
      mappings.put("a", 2);
      mappings.put("b", 2);
      mappings.put("b", 3);
      
      Mappings<Integer,String> inverseMappings = mappings.getInverseMappings();
      
      assertSame(mappings, inverseMappings.getInverseMappings());
      
      assertEquals(mappings.getLeftMap(), inverseMappings.getRightMap());
      assertEquals(mappings.getRightMap(), inverseMappings.getLeftMap());
      
      assertEquals(mappings.getLeft(1), inverseMappings.getRight(1));
      assertEquals(mappings.getLeft(2), inverseMappings.getRight(2));
      assertEquals(mappings.getLeft(3), inverseMappings.getRight(3));

      assertEquals(mappings.getRight("a"), inverseMappings.getLeft("a"));
      assertEquals(mappings.getRight("b"), inverseMappings.getLeft("b"));
      
      assertTrue(mappings.contains("b", 2));
      inverseMappings.remove(2, "b");
      assertFalse(mappings.contains("b", 2));
      
      assertFalse(mappings.getLeft(2).isEmpty());
      inverseMappings.removeRight(2);
      assertNull(mappings.getLeft(2));
   }
   
   @Test
   public void testAddAfterRemoveLeft()
   {
      Mappings<String, Integer> mappings = createMappings();
      mappings.put("A", 1);
      
      Set<Integer> values1 = mappings.getRight("A");
      mappings.removeRight("A");
      
      assertTrue(values1.isEmpty());
      assertTrue(mappings.isEmpty());
      
      mappings.put("A", 2);
      
      try
      {
         values1.add(99);
         fail(IllegalStateException.class.getName() + " expected!");
      }
      catch(IllegalStateException ex)
      {
      }
   }
   
   @Test
   public void testAddAfterRemoveRight()
   {
      Mappings<String, Integer> mappings = createMappings();
      mappings.put("A", 1);
      
      Set<String> values1 = mappings.getLeft(1);
      mappings.removeRight("A");

      assertTrue(values1.isEmpty());
      assertTrue(mappings.isEmpty());
      
      mappings.put("A", 2);
      
      try
      {
         values1.add("B");
         fail(IllegalStateException.class.getName() + " expected!");
      }
      catch(IllegalStateException ex)
      {
      }
   }

   @Test
   public void testClearValueSetLeft()
   {
      Mappings<String, Integer> mappings = createMappings();
      mappings.put("A", 1);
      
      Set<String> values1 = mappings.getLeft(1);
      values1.clear();
      
      assertFalse(mappings.containsRight(1));
      assertFalse(mappings.containsLeft("A"));
      
      try
      {
         values1.add("Z");
         fail(IllegalStateException.class.getName() + " expected!");
      }
      catch(IllegalStateException ex)
      {
      }
   }

   @Test
   public void testClearValueSetRight()
   {
      Mappings<String, Integer> mappings = createMappings();
      mappings.put("A", 1);
      
      Set<Integer> valuesA = mappings.getRight("A");
      valuesA.clear();
      
      assertFalse(mappings.containsRight(1));
      assertFalse(mappings.containsLeft("A"));
      
      try
      {
         valuesA.add(99);
         fail(IllegalStateException.class.getName() + " expected!");
      }
      catch(IllegalStateException ex)
      {
      }
   }
   
   @Test
   public void testUpdateEntry()
   {
      Mappings<String, Integer> mappings = createMappings();
      mappings.put("A", 1);
      
      Entry<String, Set<Integer>> entry = mappings.getLeftMap().entrySet().iterator().next();
      
      try
      {
         final Set<Integer> emptySet = Collections.emptySet();
         entry.setValue(emptySet);
         fail(IllegalArgumentException.class.getName() + " expected!");
      }
      catch(IllegalArgumentException ex)
      {
         
      }

      try
      {
         entry.setValue(null);
         fail(IllegalArgumentException.class.getName() + " expected!");
      }
      catch(IllegalArgumentException ex)
      {
         
      }
      
      Set<Integer> oldSet = entry.setValue(Collections.singleton(2));
      
      assertEquals(Collections.singleton(1), oldSet);
      assertTrue(mappings.contains("A", 2));
   }
}
