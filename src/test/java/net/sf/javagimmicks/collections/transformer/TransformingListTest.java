package net.sf.javagimmicks.collections.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.junit.Test;

public class TransformingListTest extends AbstractTransformingCollectionTest
{
   protected static final int[] REMOVE_INDECES = new int[]{5, 3, 4, 0, 1, 1, 0};
   
   @Test
   public void testListIterator()
   {
      testListIterator(getTestPair());
      testListIterator(getBidiTestPair());
   }
   
   @Test
   public void testGet()
   {
      testGet(getTestPair());
      testGet(getBidiTestPair());
   }
   
   @Test
   public void testIndexOf()
   {
      testIndexOf(getTestPair());
      testIndexOf(getBidiTestPair());
   }
   
   @Test
   public void testRemove()
   {
      testRemove(getTestPair());
      testRemove(getBidiTestPair());
   }
   
   @Test
   public void testSet()
   {
      try
      {
         testSet(getTestPair());
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch (UnsupportedOperationException e)
      {
      }
      
      testSet(getBidiTestPair());
   }
   
   @Test
   public void testAddByIndex()
   {
      try
      {
         testAddByIndex(getTestPair());
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch (UnsupportedOperationException e)
      {
      }
      
      testAddByIndex(getBidiTestPair());
   }
   
   @Test
   public void testSubListRead()
   {
      testSubListRead(getTestPair());
      testSubListRead(getBidiTestPair());
   }
   
   @Test
   public void testSubListWrite()
   {
      try
      {
         testSubListWrite(getTestPair());
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch (UnsupportedOperationException e)
      {
      }
      
      testSubListWrite(getBidiTestPair());
   }
   
   @Override
   protected List<String> buildCollection()
   {
      return new ArrayList<String>();
   }

   @Override
   protected List<Integer> decorate(Collection<String> base)
   {
      return TransformerUtils.decorate((List<String>)base, getTransformer());
   }

   @Override
   protected List<Integer> decorateBidi(Collection<String> base)
   {
      return TransformerUtils.decorate((List<String>)base, getBidiTransformer());
   }

   @Override
   protected ListTestPair getTestPair()
   {
      List<String> base = buildCollection();
      base.addAll(BASE_DATA);
      return new ListTestPair(base, decorate(base));
   }

   @Override
   protected ListTestPair getBidiTestPair()
   {
      List<String> base = buildCollection();
      base.addAll(BASE_DATA);
      return new ListTestPair(base, decorateBidi(base));
   }
   
   protected void testListIterator(ListTestPair testPair)
   {
      ListIterator<String> baseIterator = testPair.getBase().listIterator();
      ListIterator<Integer> transformedIterator = testPair.getTransformed().listIterator();
      
      while(baseIterator.hasNext())
      {
         assertTrue(transformedIterator.hasNext());
         
         assertEquals(baseIterator.previousIndex(), transformedIterator.previousIndex());
         assertEquals(baseIterator.nextIndex(), transformedIterator.nextIndex());
         assertEquals(transform(baseIterator.next()), transformedIterator.next());
      }

      while(baseIterator.hasPrevious())
      {
         assertTrue(transformedIterator.hasPrevious());
         
         assertEquals(baseIterator.previousIndex(), transformedIterator.previousIndex());
         assertEquals(baseIterator.nextIndex(), transformedIterator.nextIndex());
         assertEquals(transform(baseIterator.previous()), transformedIterator.previous());
      }
   }
   
   protected void testGet(ListTestPair testPair)
   {
      for(int i = 0; i < testPair.getBase().size(); ++i)
      {
         assertEquals(transform(testPair.getBase().get(i)), testPair.getTransformed().get(i));
      }
   }
   
   protected void testIndexOf(ListTestPair testPair)
   {
      for(String string : testPair.getBase())
      {
         Integer integer = transform(string);
         
         assertEquals(testPair.getBase().indexOf(string), testPair.getTransformed().indexOf(integer));
         assertEquals(testPair.getBase().lastIndexOf(string), testPair.getTransformed().lastIndexOf(integer));
      }
   }
   
   protected void testRemove(ListTestPair testPair)
   {
      for(int index : REMOVE_INDECES)
      {
         String string = testPair.getBase().get(index);
         Integer integer = testPair.getTransformed().remove(index);
         
         assertEquals(transform(string), integer);
         
         assertFalse(testPair.getBase().contains(string));
         assertFalse(testPair.getTransformed().contains(integer));
      }
   }
   
   protected void testSet(ListTestPair testPair)
   {
      Random random = new Random();
      
      int size = testPair.getTransformed().size();
      
      for(int i = 0; i < size; ++i)
      {
         Integer integer = random.nextInt();
         String string = _transformer.transformBack(integer);
         
         Integer oldInteger = testPair.getTransformed().get(i);
         
         assertEquals(oldInteger, testPair.getTransformed().set(i, integer));
         
         assertEquals(string, testPair.getBase().get(i));
      }
   }
   
   protected void testAddByIndex(ListTestPair testPair)
   {
      for(ListIterator<Integer> addIterator = CONTAINS_NONE_DATA.listIterator(); addIterator.hasNext();)
      {
         testPair.getTransformed().add(addIterator.nextIndex(), addIterator.next());
      }
      
      for(ListIterator<Integer> addIterator = CONTAINS_NONE_DATA.listIterator(); addIterator.hasNext();)
      {
         int index = addIterator.nextIndex();
         Integer integer = addIterator.next();
         String string = _transformer.transformBack(integer);
         
         assertEquals(string, testPair.getBase().get(index));
      }
   }
   
   protected void testSubListRead(ListTestPair testPair)
   {
      int size = testPair.getBase().size();
      if(size < 3)
      {
         return;
      }
      
      int newSize = size - 1;
      List<String> baseSubList = testPair.getBase().subList(1, newSize);
      List<Integer> transformedSubList = testPair.getTransformed().subList(1, newSize);
      
      ListTestPair subTestPair = new ListTestPair(baseSubList, transformedSubList);
      
      testSize(subTestPair);
      testIterator(subTestPair);
      testListIterator(subTestPair);
      testGet(subTestPair);
      testIndexOf(subTestPair);
      testSubListRead(subTestPair);
   }

   protected void testSubListWrite(ListTestPair testPair)
   {
      final List<String> baseList = testPair.getBase();
      final List<Integer> transformedList = testPair.getTransformed();
      final int size = baseList.size();
      
      final int toIndex = size - 1;
      final List<String> baseSubList = baseList.subList(1, toIndex);
      final List<Integer> transformedSubList = transformedList.subList(1, toIndex);
      
      Random random = new Random();
      
      // Remove operation
      int index = random.nextInt(transformedSubList.size());
      String removedString = baseSubList.get(index);
      
      assertTrue(baseList.contains(removedString));
      assertTrue(baseSubList.contains(removedString));

      Integer removedInteger = transformedSubList.remove(index);
      
      assertEquals(removedString, _transformer.transformBack(removedInteger));
      
      assertEquals(baseList.contains(removedString), transformedList.contains(removedInteger));
      assertFalse(transformedSubList.contains(removedInteger));
      
      // Add operation
      index = random.nextInt(transformedSubList.size());
      Integer integerToAdd = random.nextInt();
      String stringToAdd = _transformer.transformBack(integerToAdd);
      
      assertEquals(baseList.contains(removedString), transformedList.contains(removedInteger));

      transformedSubList.add(index, integerToAdd);
      
      assertEquals(baseList.contains(removedString), transformedList.contains(removedInteger));
      assertEquals(stringToAdd, baseList.get(index + 1));
      
      // Problem: When creating two sublists of one list and modifying one of them
      // makes the unmodified sublist corrupt (ConcurrentModificationException)
//      testSubListWrite(new ListTestPair(baseSubList, transformedSubList));
   }

   protected class ListTestPair extends TestPair
   {
      public ListTestPair(List<String> base, List<Integer> transformed)
      {
         super(base, transformed);
      }

      @Override
      public List<String> getBase()
      {
         return (List<String>) super.getBase();
      }

      @Override
      public List<Integer> getTransformed()
      {
         return (List<Integer>) super.getTransformed();
      }
   }
}
