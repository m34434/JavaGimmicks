package net.sf.javagimmicks.collections8.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import net.sf.javagimmicks.transform8.BidiFunction;

import org.junit.Before;
import org.junit.Test;

public abstract class AbstractTransformingCollectionTest
{  
   protected static final Random RANDOM = new Random();
   protected static final List<String> BASE_DATA = Arrays.asList(new String[] {"1", "3", "5", "7", "9", "11", "13"});
   protected static final List<Integer> REMOVE_DATA1 = Arrays.asList(new Integer[] {3, 5, 7, 11});
   protected static final List<Integer> REMOVE_DATA2 = Arrays.asList(new Integer[] {1, 9, 13});
   protected static final List<Integer> CONTAINS_SOME_DATA = Arrays.asList(new Integer[] {1, 2, 9, 13});
   protected static final List<Integer> CONTAINS_NONE_DATA = Arrays.asList(new Integer[] {2, 4, 6, 8, 10});

   protected BidiFunction<String, Integer> _transformer;
   
   @Before
   public void setUp()
   {
      _transformer = new BidiFunction<String, Integer>()
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
   }   
   
   abstract protected Collection<String> buildCollection();
   abstract protected Collection<Integer> decorate(Collection<String> base);
   abstract protected Collection<Integer> decorateBidi(Collection<String> base);

   @Test
   public void testTransformer()
   {
      BidiFunction<Integer, String> invertedTransformer = _transformer.invert();
      
      String s1 = "1";
      assertEquals(s1, transformBack(transform(s1)));
      assertEquals(s1, invertedTransformer.apply(invertedTransformer.applyReverse(s1)));
      
      int i1 = 1;
      assertEquals(i1, transform(transformBack(i1)).intValue());
      assertEquals(i1, invertedTransformer.applyReverse(invertedTransformer.apply(i1)).intValue());
   }
   
   @Test
   public void testSize()
   {
      testSize(getTestPair());
      testSize(getBidiTestPair());
   }
   
   @Test
   public void testIterator()
   {
      testIterator(getTestPair());
      testIterator(getBidiTestPair());
   }
   
   @Test
   public void testContains()
   {
      testContains(getTestPair());
      testContains(getBidiTestPair());
   }
   
   @Test
   public void testContainsReferenceData()
   {
      testContainsReferenceData(getTestPair());
      testContainsReferenceData(getBidiTestPair());
   }
   
   @Test
   public void testRemove()
   {
      testRemove(getTestPair());
      testRemove(getBidiTestPair());
   }
   
   @Test
   public void testRemoveAll()
   {
      testRemoveAll(getTestPair());
      testRemoveAll(getBidiTestPair());
   }
   
   @Test
   public void testRetainAll()
   {
      testRetainAll(getTestPair());
      testRetainAll(getBidiTestPair());
   }
   
   @Test
   public void testAdd()
   {
      try
      {
         testAdd(getTestPair());
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch (UnsupportedOperationException e)
      {
      }
      
      testAdd(getBidiTestPair());
   }
   
   @Test
   public void testAddAll()
   {
      try
      {
         testAddAll(getTestPair());
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch (UnsupportedOperationException e)
      {
      }

      testAddAll(getBidiTestPair());
   }
   
   protected TestPair getTestPair()
   {
      Collection<String> base = buildCollection();
      base.addAll(BASE_DATA);
      return new TestPair(base, decorate(base));
   }
   
   protected TestPair getBidiTestPair()
   {
      Collection<String> base = buildCollection();
      base.addAll(BASE_DATA);
      return new TestPair(base, decorateBidi(base));
   }
   
   protected final Function<String, Integer> getTransformer()
   {
      return _transformer;
   }
   
   protected final BidiFunction<String, Integer> getBidiTransformer()
   {
      return _transformer;
   }
   
   protected Integer transform(String value)
   {
      return _transformer.apply(value);
   }
   
   protected String transformBack(Integer value)
   {
      return _transformer.applyReverse(value);
   }
   
   protected static void testEmpty(TestPair testPair, boolean empty)
   {
      assertEquals(empty, testPair.getBase().isEmpty());
      assertEquals(empty, testPair.getTransformed().isEmpty());
   }
   
   protected static void testSize(TestPair testPair)
   {
      assertEquals(testPair.getBase().size(), testPair.getTransformed().size());
   }
   
   protected void testIterator(TestPair testPair)
   {
      Iterator<String> baseIterator = testPair.getBase().iterator();
      Iterator<Integer> transformedIterator = testPair.getTransformed().iterator();
      
      while(baseIterator.hasNext())
      {
         assertTrue(transformedIterator.hasNext());
         
         assertEquals(transform(baseIterator.next()), transformedIterator.next());
      }
   }
   
   protected void testContains(TestPair testPair)
   {
      for(String string : testPair.getBase())
      {
         assertTrue(testPair.getTransformed().contains(transform(string)));
      }
   }
   
   protected void testContainsReferenceData(TestPair testPair)
   {
      for(Integer integer : CONTAINS_NONE_DATA)
      {
         assertFalse(testPair.getTransformed().contains(integer));
      }
      
      assertTrue(testPair.getTransformed().containsAll(REMOVE_DATA1));
      assertTrue(testPair.getTransformed().containsAll(REMOVE_DATA2));
      assertFalse(testPair.getTransformed().containsAll(CONTAINS_SOME_DATA));
      assertFalse(testPair.getTransformed().containsAll(CONTAINS_NONE_DATA));
   }
   
   protected void testRemove(TestPair testPair)
   {
      testRemove(testPair, REMOVE_DATA1);
      testRemove(testPair, REMOVE_DATA2);
      
      testEmpty(testPair, true);
   }
   
   protected void testRemove(TestPair testPair, Collection<Integer> toRemove)
   {
      for(Integer integer : toRemove)
      {
         String string = transformBack(integer);
         
         assertTrue(testPair.getBase().contains(string));
         assertTrue(testPair.getTransformed().contains(integer));
         
         assertTrue(testPair.getTransformed().remove(integer));

         assertFalse(testPair.getBase().contains(string));
         assertFalse(testPair.getTransformed().contains(integer));

         assertFalse(testPair.getTransformed().remove(integer));
         
         testSize(testPair);
      }
   }
   
   protected void testRemoveAll(TestPair testPair)
   {
      assertTrue(testPair.getTransformed().removeAll(REMOVE_DATA1));
      
      assertFalse(testPair.getTransformed().containsAll(REMOVE_DATA1));
      assertTrue(testPair.getTransformed().containsAll(REMOVE_DATA2));
      
      testSize(testPair);
      testIterator(testPair);
      
      assertTrue(testPair.getTransformed().removeAll(REMOVE_DATA2));

      testEmpty(testPair, true);

      assertFalse(testPair.getTransformed().containsAll(REMOVE_DATA1));
      assertFalse(testPair.getTransformed().containsAll(REMOVE_DATA2));
   }
   
   protected void testRetainAll(TestPair testPair)
   {
      assertTrue(testPair.getTransformed().retainAll(REMOVE_DATA1));
      
      assertTrue(testPair.getTransformed().containsAll(REMOVE_DATA1));
      assertFalse(testPair.getTransformed().containsAll(REMOVE_DATA2));
      
      testSize(testPair);
      testIterator(testPair);
      
      assertTrue(testPair.getTransformed().retainAll(REMOVE_DATA2));

      testEmpty(testPair, true);

      assertFalse(testPair.getTransformed().containsAll(REMOVE_DATA1));
      assertFalse(testPair.getTransformed().containsAll(REMOVE_DATA2));
   }
   
   protected void testAdd(TestPair testPair)
   {
      for(Integer integer : CONTAINS_NONE_DATA)
      {
         String string = transformBack(integer);
         
         assertFalse(testPair.getBase().contains(string));
         assertFalse(testPair.getTransformed().contains(integer));
         
         assertTrue(testPair.getTransformed().add(integer));         

         assertTrue(testPair.getBase().contains(string));
         assertTrue(testPair.getTransformed().contains(integer));
      }
      
      testSize(testPair);
      testIterator(testPair);
   }

   protected void testAddAll(TestPair testPair)
   {
      assertTrue(testPair.getTransformed().addAll(CONTAINS_NONE_DATA));
      assertTrue(testPair.getTransformed().containsAll(CONTAINS_NONE_DATA));
      
      testSize(testPair);
      testIterator(testPair);
   }

   protected static class TestPair
   {
      protected final Collection<String> _base;
      protected final Collection<Integer> _transformed;

      public TestPair(Collection<String> base, Collection<Integer> transformed)
      {
         _base = base;
         _transformed = transformed;
      }

      public Collection<String> getBase()
      {
         return _base;
      }

      public Collection<Integer> getTransformed()
      {
         return _transformed;
      }
   }
}
