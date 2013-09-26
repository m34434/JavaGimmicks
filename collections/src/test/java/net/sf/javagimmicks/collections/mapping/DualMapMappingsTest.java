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
      assertTrue(mappings.containsLeftKey("A"));
      assertTrue(mappings.containsRightKey(1));
      assertTrue(mappings.contains("A", 1));
      
      mappings.putRightValuesFor("A", Arrays.asList(new Integer[]{2, 3}));
      assertEquals(3, mappings.size());
      assertTrue(mappings.containsLeftKey("A"));
      assertTrue(mappings.containsRightKey(2));
      assertTrue(mappings.containsRightKey(3));
      assertTrue(mappings.contains("A", 2));
      assertTrue(mappings.contains("A", 3));
      
      mappings.putLeftValuesFor(4, Arrays.asList(new String[]{"A", "B", "C", "D"}));
      assertEquals(7, mappings.size());
      assertTrue(mappings.containsLeftKey("A"));
      assertTrue(mappings.containsLeftKey("B"));
      assertTrue(mappings.containsLeftKey("C"));
      assertTrue(mappings.containsLeftKey("D"));
      assertTrue(mappings.containsRightKey(4));
      assertTrue(mappings.contains("A", 4));
      assertTrue(mappings.contains("B", 4));
      assertTrue(mappings.contains("C", 4));
      assertTrue(mappings.contains("D", 4));
      
      Mappings<Integer, String> inverseMappings = mappings.getInverseMappings();
      inverseMappings.put(1, "A");
      inverseMappings.putRightValuesFor(2, Arrays.asList(new String[]{"A", "B"}));
      inverseMappings.putLeftValuesFor("C", Arrays.asList(new Integer[]{3, 4}));
      
      assertEquals(refMapLeft, mappings.getLeftView());
      assertEquals(refMapRight, mappings.getRightView());

      assertEquals(refSetA, mappings.getRightValuesFor("A"));
      assertEquals(refSetB, mappings.getRightValuesFor("B"));
      assertEquals(refSetC, mappings.getRightValuesFor("C"));
      assertEquals(refSetD, mappings.getRightValuesFor("D"));

      assertEquals(refSet1, mappings.getLeftValuesFor(1));
      assertEquals(refSet2, mappings.getLeftValuesFor(2));
      assertEquals(refSet3, mappings.getLeftValuesFor(3));
      assertEquals(refSet4, mappings.getLeftValuesFor(4));
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
      
      assertEquals(mappings.getLeftView(), inverseMappings.getRightView());
      assertEquals(mappings.getRightView(), inverseMappings.getLeftView());
      
      assertEquals(mappings.getLeftValuesFor(1), inverseMappings.getRightValuesFor(1));
      assertEquals(mappings.getLeftValuesFor(2), inverseMappings.getRightValuesFor(2));
      assertEquals(mappings.getLeftValuesFor(3), inverseMappings.getRightValuesFor(3));

      assertEquals(mappings.getRightValuesFor("a"), inverseMappings.getLeftValuesFor("a"));
      assertEquals(mappings.getRightValuesFor("b"), inverseMappings.getLeftValuesFor("b"));
      
      assertTrue(mappings.contains("b", 2));
      inverseMappings.remove(2, "b");
      assertFalse(mappings.contains("b", 2));
      
      assertFalse(mappings.getLeftValuesFor(2).isEmpty());
      inverseMappings.removeLeftKey(2);
      assertNull(mappings.getLeftValuesFor(2));
   }
   
   @Test
   public void testAddAfterRemoveLeft()
   {
      Mappings<String, Integer> mappings = createMappings();
      mappings.put("A", 1);
      
      Set<Integer> values1 = mappings.getRightValuesFor("A");
      mappings.removeLeftKey("A");
      
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
      
      Set<String> values1 = mappings.getLeftValuesFor(1);
      mappings.removeLeftKey("A");

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
      
      Set<String> values1 = mappings.getLeftValuesFor(1);
      values1.clear();
      
      assertFalse(mappings.containsRightKey(1));
      assertFalse(mappings.containsLeftKey("A"));
      
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
      
      Set<Integer> valuesA = mappings.getRightValuesFor("A");
      valuesA.clear();
      
      assertFalse(mappings.containsRightKey(1));
      assertFalse(mappings.containsLeftKey("A"));
      
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
      
      Entry<String, Set<Integer>> entry = mappings.getLeftView().entrySet().iterator().next();
      
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
