package net.sf.javagimmicks.collections.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import net.sf.javagimmicks.util.ComparableComparator;

public class TransformingSortedSetTest extends AbstractTransformingCollectionTest
{
   @Test
   public void testFirst()
   {
      testFirst(getTestPair());
      testFirst(getBidiTestPair());
   }
   
   @Test
   public void testLast()
   {
      testLast(getTestPair());
      testLast(getBidiTestPair());
   }
   
   @Test
   public void testComparator()
   {
      try
      {
         testComparator(getTestPair());
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch(UnsupportedOperationException ex)
      {
         
      }
      testComparator(getBidiTestPair());
   }
   
   @Test
   public void testHeadSet()
   {
      try
      {
         testHeadSet(getTestPair());
         fail(UnsupportedOperationException.class.getName() + " expected!");
      }
      catch(UnsupportedOperationException ex)
      {
         
      }
      testHeadSet(getBidiTestPair());
   }
   
   protected void testFirst(SortedSetTestPair testPair)
   {
      assertEquals(transform(testPair.getBase().first()), testPair.getTransformed().first());
   }
   
   protected void testLast(SortedSetTestPair testPair)
   {
      assertEquals(transform(testPair.getBase().last()), testPair.getTransformed().last());
   }
   
   @SuppressWarnings("unchecked")
   protected void testComparator(SortedSetTestPair testPair)
   {
      // As we passed no comparator to the TreeSet constructor, we must create an artificial one now for the base
      Comparator<String> stringComparator = (Comparator<String>) ComparableComparator.INSTANCE;
      Comparator<? super Integer> baseComparator = TransformerUtils.decorate(stringComparator, getBidiTransformer().invert());

      // The transforming SortedSet should be able to handle the 'null' comparator from its internal base
      Comparator<? super Integer> transformedComparator = testPair.getTransformed().comparator();
      
      testCompare(baseComparator, transformedComparator, 0, 0);
      testCompare(baseComparator, transformedComparator, 0, 1);
      testCompare(baseComparator, transformedComparator, 1, 0);

      testCompare(baseComparator, transformedComparator, 1, 1);
      testCompare(baseComparator, transformedComparator, 1, 2);
      testCompare(baseComparator, transformedComparator, 2, 1);

      testCompare(baseComparator, transformedComparator, 0, -1);
      testCompare(baseComparator, transformedComparator, -1, 0);

      testCompare(baseComparator, transformedComparator, -1, -1);
      testCompare(baseComparator, transformedComparator, -1, -2);
      testCompare(baseComparator, transformedComparator, -2, -1);

      testCompare(baseComparator, transformedComparator, 1, -1);
      testCompare(baseComparator, transformedComparator, -1, 1);
   }
   
   protected void testHeadSet(SortedSetTestPair testPair)
   {
      SortedSet<String> baseSortedSet = testPair.getBase();
      
      if(baseSortedSet.size() < 3)
      {
         return;
      }
      
      String toElementString = BASE_DATA.get(baseSortedSet.size() - 2);
      Integer toElementInteger = getTransformer().transform(toElementString);
      
      SortedSet<String> base = baseSortedSet.headSet(toElementString);
      SortedSet<Integer> transformed = testPair.getTransformed().headSet(toElementInteger);

      SortedSetTestPair headSetTestPair = new SortedSetTestPair(base, transformed);
      
      testSize(headSetTestPair);
      testIterator(headSetTestPair);
      testContains(headSetTestPair);
   }
   
   @Override
   protected SortedSet<String> buildCollection()
   {
      return new TreeSet<String>();
   }

   @Override
   protected SortedSet<Integer> decorate(Collection<String> base)
   {
      return TransformerUtils.decorate((SortedSet<String>)base, getTransformer());
   }

   @Override
   protected SortedSet<Integer> decorateBidi(Collection<String> base)
   {
      return TransformerUtils.decorate((SortedSet<String>)base, getBidiTransformer());
   }
   
   @Override
   protected SortedSetTestPair getBidiTestPair()
   {
      SortedSet<String> base = buildCollection();
      base.addAll(BASE_DATA);
      return new SortedSetTestPair(base, decorateBidi(base));
   }

   @Override
   protected SortedSetTestPair getTestPair()
   {
      SortedSet<String> base = buildCollection();
      base.addAll(BASE_DATA);
      return new SortedSetTestPair(base, decorate(base));
   }

   protected static <E> void testCompare(Comparator<? super E> c1, Comparator<? super E> c2, E e1, E e2)
   {
      assertEquals(c1.compare(e1, e2), c2.compare(e1, e2));
   }

   protected static class SortedSetTestPair extends TestPair
   {
      public SortedSetTestPair(SortedSet<String> base, SortedSet<Integer> transformed)
      {
         super(base, transformed);
      }

      @Override
      public SortedSet<String> getBase()
      {
         return (SortedSet<String>) super.getBase();
      }

      @Override
      public SortedSet<Integer> getTransformed()
      {
         return (SortedSet<Integer>) super.getTransformed();
      }
      
   }
}
