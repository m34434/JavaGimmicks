package net.sf.javagimmicks.collections8.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.sf.javagimmicks.collections8.builder.MapBuilder;
import net.sf.javagimmicks.collections8.mapping.DualMapValueMappings;
import net.sf.javagimmicks.collections8.mapping.ValueMappings;

import org.junit.Test;

public class DualMapValueMappingsTest
{
   private ValueMappings<String, Integer, String> createMappings()
   {
      return DualMapValueMappings.createHashTreeInstance();
   }
   
   @Test
   public void testPut()
   {
      ValueMappings<String, Integer, String> mappings = createMappings();
      
      mappings.put("A", 1, "A1");
      assertEquals(1, mappings.size());
      assertEquals("A1", mappings.get("A", 1));
      assertTrue(mappings.containsLeftKey("A"));
      assertTrue(mappings.containsRightKey(1));
      assertTrue(mappings.containsMapping("A", 1));
      
      mappings.putAllForLeftKey("A", MapBuilder.create(new HashMap<Integer, String>())
         .put(2, "A2")
         .put(3, "A3").get());
      assertEquals(3, mappings.size());
      assertEquals("A2", mappings.get("A", 2));
      assertEquals("A3", mappings.get("A", 3));
      assertTrue(mappings.containsLeftKey("A"));
      assertTrue(mappings.containsRightKey(2));
      assertTrue(mappings.containsRightKey(3));
      assertTrue(mappings.containsMapping("A", 2));
      assertTrue(mappings.containsMapping("A", 3));
      
      mappings.putAllForRightKey(4, MapBuilder.create(new HashMap<String, String>())
            .put("A", "A4")
            .put("B", "B4")
            .put("C", "C4")
            .put("D", "D4").get());
      assertEquals(7, mappings.size());
      assertEquals("A4", mappings.get("A", 4));
      assertEquals("B4", mappings.get("B", 4));
      assertEquals("C4", mappings.get("C", 4));
      assertEquals("D4", mappings.get("D", 4));
      assertTrue(mappings.containsLeftKey("A"));
      assertTrue(mappings.containsLeftKey("B"));
      assertTrue(mappings.containsLeftKey("C"));
      assertTrue(mappings.containsLeftKey("D"));
      assertTrue(mappings.containsRightKey(4));
      assertTrue(mappings.containsMapping("A", 4));
      assertTrue(mappings.containsMapping("B", 4));
      assertTrue(mappings.containsMapping("C", 4));
      assertTrue(mappings.containsMapping("D", 4));
      
      ValueMappings<Integer, String, String> inverseMappings = mappings.invert();
      inverseMappings.put(1, "A", "A1");
      inverseMappings.putAllForLeftKey(2, MapBuilder.create(new HashMap<String, String>())
            .put("A", "A2")
            .put("B", "B2").get());
      inverseMappings.putAllForRightKey("C", MapBuilder.create(new HashMap<Integer, String>())
            .put(3, "C3")
            .put(4, "C4").get());
      
      // Build left reference map
      TreeMap<Integer, String> mapA = MapBuilder.create(new TreeMap<Integer, String>())
         .put(1, "A1")
         .put(2, "A2")
         .put(3, "A3")
         .put(4, "A4").get();
      
      TreeMap<Integer, String> mapB = MapBuilder.create(new TreeMap<Integer, String>())
         .put(2, "B2")
         .put(4, "B4").get();
      
      TreeMap<Integer, String> mapC = MapBuilder.create(new TreeMap<Integer, String>())
         .put(3, "C3")
         .put(4, "C4").get();
      
      TreeMap<Integer, String> mapD = MapBuilder.create(new TreeMap<Integer, String>())
         .put(4, "D4").get();
      
      HashMap<String, Map<Integer, String>> leftReferenceMap = MapBuilder.create(new HashMap<String, Map<Integer, String>>())
         .put("A", mapA)
         .put("B", mapB)
         .put("C", mapC)
         .put("D", mapD)
         .get();

      // Build right reference map
      HashMap<String, String> map1 = MapBuilder.create(new HashMap<String, String>())
         .put("A", "A1").get();
      
      HashMap<String, String> map2 = MapBuilder.create(new HashMap<String, String>())
         .put("A", "A2")
         .put("B", "B2").get();
      
      HashMap<String, String> map3 = MapBuilder.create(new HashMap<String, String>())
         .put("A", "A3")
         .put("C", "C3").get();
   
      HashMap<String, String> map4 = MapBuilder.create(new HashMap<String, String>())
         .put("A", "A4")
         .put("B", "B4")
         .put("C", "C4")
         .put("D", "D4").get();
      
      HashMap<Integer, Map<String, String>> rightReferenceMap = MapBuilder.create(new HashMap<Integer, Map<String, String>>())
         .put(1, map1)
         .put(2, map2)
         .put(3, map3)
         .put(4, map4)
         .get();
      
      assertEquals(leftReferenceMap, mappings.getLeftView());
      assertEquals(rightReferenceMap, mappings.getRightView());

      assertEquals(mapA, mappings.getAllForLeftKey("A"));
      assertEquals(mapB, mappings.getAllForLeftKey("B"));
      assertEquals(mapC, mappings.getAllForLeftKey("C"));
      assertEquals(mapD, mappings.getAllForLeftKey("D"));

      assertEquals(map1, mappings.getAllForRightKey(1));
      assertEquals(map2, mappings.getAllForRightKey(2));
      assertEquals(map3, mappings.getAllForRightKey(3));
      assertEquals(map4, mappings.getAllForRightKey(4));
      
      System.out.println(mappings.getMappingSet());
   }
   
   @Test
   public void testAddAfterRemoveLeft()
   {
      ValueMappings<String, Integer, String> mappings = createMappings();
      mappings.put("A", 1, "A1");
      
      Map<Integer, String> innerA = mappings.getAllForLeftKey("A");
      mappings.removeLeftKey("A");
      
      assertTrue(innerA.isEmpty());
      assertTrue(mappings.isEmpty());
      
      mappings.put("A", 2, "A2");
      
      try
      {
         innerA.put(99, "A99");
         fail(IllegalStateException.class.getName() + " expected!");
      }
      catch(IllegalStateException ex)
      {
      }
   }
   
   @Test
   public void testAddAfterRemoveRight()
   {
      ValueMappings<String, Integer, String> mappings = createMappings();
      mappings.put("A", 1, "A1");
      
      Map<String, String> inner1 = mappings.getAllForRightKey(1);
      mappings.removeLeftKey("A");

      assertTrue(inner1.isEmpty());
      assertTrue(mappings.isEmpty());
      
      mappings.put("A", 2, "A2");
      
      try
      {
         inner1.put("B", "B1");
         fail(IllegalStateException.class.getName() + " expected!");
      }
      catch(IllegalStateException ex)
      {
      }
   }

   @Test
   public void testClearInnerMapLeft()
   {
      ValueMappings<String, Integer, String> mappings = createMappings();
      mappings.put("A", 1, "A1");
      
      Map<String, String> inner1 = mappings.getAllForRightKey(1);
      inner1.clear();
      
      assertFalse(mappings.containsRightKey(1));
      assertFalse(mappings.containsLeftKey("A"));
      
      try
      {
         inner1.put("Z", "Z1");
         fail(IllegalStateException.class.getName() + " expected!");
      }
      catch(IllegalStateException ex)
      {
      }
   }

   @Test
   public void testClearInnerMapRight()
   {
      ValueMappings<String, Integer, String> mappings = createMappings();
      mappings.put("A", 1, "A1");
      
      Map<Integer, String> innerA = mappings.getAllForLeftKey("A");
      innerA.clear();
      
      assertFalse(mappings.containsRightKey(1));
      assertFalse(mappings.containsLeftKey("A"));
      
      try
      {
         innerA.put(99, "A99");
         fail(IllegalStateException.class.getName() + " expected!");
      }
      catch(IllegalStateException ex)
      {
      }
   }
   
   @Test
   public void testUpdateEntry()
   {
      ValueMappings<String, Integer, String> mappings = createMappings();
      mappings.put("A", 1, "A1");
      
      Entry<String, Map<Integer, String>> entry = mappings.getLeftView().entrySet().iterator().next();
      
      try
      {
         final Map<Integer, String> emptyMap = Collections.emptyMap();
         entry.setValue(emptyMap);
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
      
      Map<Integer, String> oldSet = entry.setValue(Collections.singletonMap(2, "A2"));
      
      assertEquals(Collections.singletonMap(1, "A1"), oldSet);
      assertTrue(mappings.containsMapping("A", 2));
      assertEquals("A2", mappings.get("A", 2));
   }
}
