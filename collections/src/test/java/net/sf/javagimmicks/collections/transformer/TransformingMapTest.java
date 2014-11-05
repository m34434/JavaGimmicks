package net.sf.javagimmicks.collections.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.sf.javagimmicks.transform.AbstractBidiFunction;
import net.sf.javagimmicks.transform.BidiFunction;
import net.sf.javagimmicks.transform.Functions;
import net.sf.javagimmicks.util.Function;

import org.junit.Before;
import org.junit.Test;

public class TransformingMapTest
{  
   protected BidiFunction<String, Integer> _keyTransformer;
   protected BidiFunction<Integer, String> _valueTransformer;
   
   @Before
   public void setUp()
   {
      _keyTransformer = new AbstractBidiFunction<String, Integer>()
      {
         public String applyReverse(Integer source)
         {
            return String.valueOf(source.intValue());
         }

         public Integer apply(String source)
         {
            return Integer.parseInt(source);
         }
      };
   
      _valueTransformer = _keyTransformer.invert();
   }
   
   @Test
   public void testTransformer()
   {
      BidiFunction<Integer, String> invertedTransformer = _keyTransformer.invert();
      
      String s1 = "1";
      assertEquals(s1, _keyTransformer.applyReverse(_keyTransformer.apply(s1)));
      assertEquals(s1, invertedTransformer.apply(invertedTransformer.applyReverse(s1)));
      
      int i1 = 1;
      assertEquals(i1, _keyTransformer.apply(_keyTransformer.applyReverse(i1)).intValue());
      assertEquals(i1, invertedTransformer.applyReverse(invertedTransformer.apply(i1)).intValue());
   }
   
   @Test
   public void testKeyBased()
   {
      final Map<String, Integer> base = buildTestMap();
      final Map<Integer, Integer> transformed = TransformerUtils.decorateKeyBased(base, getKeyTransformer());
      
      testKeyBasedCommon(base, transformed);

      try
      {
         transformed.put(5, 50);
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch(UnsupportedOperationException ex)
      {
         
      }
   }
   
   @Test
   public void testKeyBasedBidi()
   {
      final Map<String, Integer> base = buildTestMap();
      final Map<Integer, Integer> transformed = TransformerUtils.decorateKeyBased(base, _keyTransformer);

      testKeyBasedCommon(base, transformed);
      
      transformed.put(5, 50);
      assertEquals(50, transformed.get(5).intValue());
      assertEquals(50, base.get("5").intValue());
   }
   
   @Test
   public void testValueBased()
   {
      final Map<String, Integer> base = buildTestMap();
      final Map<String, String> transformed = TransformerUtils.decorateValueBased(base, getValueTransformer());
      
      testValueBasedCommon(base, transformed);

      try
      {
         transformed.put("5", "50");
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch(UnsupportedOperationException ex)
      {
         
      }
   }
   
   @Test
   public void testValueBasedBidi()
   {
      final Map<String, Integer> base = buildTestMap();
      final Map<String, String> transformed = TransformerUtils.decorateValueBased(base, _valueTransformer);

      testValueBasedCommon(base, transformed);
      
      transformed.put("5", "50");
      assertEquals("50", transformed.get("5"));
      assertEquals(50, base.get("5").intValue());
   }
   
   @Test
   public void testBoth()
   {
      final Map<String, Integer> base = buildTestMap();
      final Map<Integer, String> transformed = TransformerUtils.decorate(base, getKeyTransformer(), getValueTransformer());
      
      testBothCommon(base, transformed);

      try
      {
         transformed.put(5, "50");
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch(UnsupportedOperationException ex)
      {
         
      }
   }
   
   @Test
   public void testBothKeyBidi()
   {
      final Map<String, Integer> base = buildTestMap();
      final Map<Integer, String> transformed = TransformerUtils.decorate(base, _keyTransformer, getValueTransformer());
      
      testBothCommon(base, transformed);

      try
      {
         transformed.put(5, "50");
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch(UnsupportedOperationException ex)
      {
         
      }
   }
   
   @Test
   public void testBothValueBidi()
   {
      final Map<String, Integer> base = buildTestMap();
      final Map<Integer, String> transformed = TransformerUtils.decorate(base, getKeyTransformer(), _valueTransformer);
      
      testBothCommon(base, transformed);

      try
      {
         transformed.put(5, "50");
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch(UnsupportedOperationException ex)
      {
         
      }
   }
   
   @Test
   public void testBothBidi()
   {
      final Map<String, Integer> base = buildTestMap();
      final Map<Integer, String> transformed = TransformerUtils.decorate(base, _keyTransformer, _valueTransformer);
      
      testBothCommon(base, transformed);

      transformed.put(5, "50");
      assertEquals("50", transformed.get(5));
      assertEquals(50, base.get("5").intValue());
   }
   
   protected void testKeyBasedCommon(Map<String, Integer> base, Map<Integer, Integer> transformed)
   {
      assertEquals(base.isEmpty(), transformed.isEmpty());
      assertEquals(base.size(), transformed.size());
      
      assertTrue(transformed.containsKey(1));
      assertFalse(transformed.containsKey("1"));
      
      assertEquals(20, transformed.get(2).intValue());
      assertNull(transformed.get("2"));

      final Set<Integer> transformedKeySet = TransformerUtils.decorate(base.keySet(), _keyTransformer);
      assertEquals(transformed.keySet().size(), transformedKeySet.size());
      assertTrue(transformed.keySet().containsAll(transformedKeySet));
      
      assertEquals(transformed.values().size(), base.values().size());
      assertTrue(transformed.values().containsAll(base.values()));
      
      assertTrue(transformed.containsKey(3));
      assertTrue(transformed.containsValue(30));
      assertEquals(30, transformed.remove(3).intValue());
      assertFalse(transformed.containsKey(3));
      assertFalse(transformed.containsValue(30));
      
      Function<Integer, Integer> valueTransformer = Functions.identityTransformer();
      testEntrySets(base.entrySet(), transformed.entrySet(), _keyTransformer, valueTransformer);
   }
   
   protected void testValueBasedCommon(Map<String, Integer> base, Map<String, String> transformed)
   {
      assertEquals(base.isEmpty(), transformed.isEmpty());
      assertEquals(base.size(), transformed.size());
      
      assertTrue(transformed.containsValue("10"));
      assertFalse(transformed.containsValue(10));
      
      assertEquals("20", transformed.get("2"));

      final Collection<String> transformedValueCollection = TransformerUtils.decorate(base.values(), _valueTransformer);
      assertEquals(transformed.values().size(), transformedValueCollection.size());
      assertTrue(transformed.values().containsAll(transformedValueCollection));
      
      assertEquals(transformed.keySet().size(), base.keySet().size());
      assertTrue(transformed.keySet().containsAll(base.keySet()));
      
      assertTrue(transformed.containsKey("3"));
      assertTrue(transformed.containsValue("30"));
      assertEquals("30", transformed.remove("3"));
      assertFalse(transformed.containsKey("3"));
      assertFalse(transformed.containsValue("30"));
      
      Function<String, String> keyTransformer = Functions.identityTransformer();
      testEntrySets(base.entrySet(), transformed.entrySet(), keyTransformer, _valueTransformer);
   }
   
   protected void testBothCommon(Map<String, Integer> base, Map<Integer, String> transformed)
   {
      assertEquals(base.isEmpty(), transformed.isEmpty());
      assertEquals(base.size(), transformed.size());
      
      assertTrue(transformed.containsKey(1));
      assertFalse(transformed.containsKey("1"));

      assertTrue(transformed.containsValue("10"));
      assertFalse(transformed.containsValue(10));
      
      assertEquals("20", transformed.get(2));

      final Set<Integer> transformedKeySet = TransformerUtils.decorate(base.keySet(), _keyTransformer);
      assertEquals(transformed.keySet().size(), transformedKeySet.size());
      assertTrue(transformed.keySet().containsAll(transformedKeySet));
      
      final Collection<String> transformedValueCollection = TransformerUtils.decorate(base.values(), _valueTransformer);
      assertEquals(transformed.values().size(), transformedValueCollection.size());
      assertTrue(transformed.values().containsAll(transformedValueCollection));
      
      assertTrue(transformed.containsKey(3));
      assertTrue(transformed.containsValue("30"));
      assertEquals("30", transformed.remove(3));
      assertFalse(transformed.containsKey(3));
      assertFalse(transformed.containsValue("30"));
      
      testEntrySets(base.entrySet(), transformed.entrySet(), _keyTransformer, _valueTransformer);
   }
   
   protected Function<String, Integer> getKeyTransformer()
   {
      return _keyTransformer;
   }
   
   protected Function<Integer, String> getValueTransformer()
   {
      return _valueTransformer;
   }
   
   protected static <KF, KT, VF, VT> void testEntrySets(Set<Entry<KF, VF>> setF, Set<Entry<KT, VT>> setT, Function<KF, KT> keyTransformer, Function<VF, VT> valueTransformer)
   {
      Iterator<Entry<KF, VF>> iterF = setF.iterator();
      Iterator<Entry<KT, VT>> iterT = setT.iterator();
      
      while(iterF.hasNext())
      {
         assertTrue(iterT.hasNext());
         
         Entry<KF, VF> entryF = iterF.next();
         Entry<KT, VT> entryT = iterT.next();
         
         assertEquals(keyTransformer.apply(entryF.getKey()), entryT.getKey());
         assertEquals(valueTransformer.apply(entryF.getValue()), entryT.getValue());
      }
   }
   
   protected static Map<String, Integer> buildTestMap()
   {
      Map<String, Integer> result = new TreeMap<String, Integer>();
      result.put("1", 10);
      result.put("2", 20);
      result.put("3", 30);
      result.put("4", 40);
      
      return result;
   }
}
