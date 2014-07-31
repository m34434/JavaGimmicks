package net.sf.javagimmicks.collections8.composite;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Test;

public class CompositeSpliteratorTest
{
   private static final int DIM = 200;

   private List<Integer> _referenceList;

   private <C extends Collection<Integer>> C setup(final Supplier<C> collectionSupplier,
         final Function<List<C>, C> compositeBuilder)
   {
      _referenceList = new ArrayList<>(DIM * DIM);

      final List<C> collections = new ArrayList<>(DIM);

      IntStream.range(0, DIM).forEach(i ->
      {
         final C collection = collectionSupplier.get();

         IntStream.range(0, DIM).forEach(j ->
         {
            final int count = i * DIM + j;
            collection.add(count);
            _referenceList.add(count);
         });

         collections.add(collection);
      });

      return compositeBuilder.apply(collections);
   }

   @After
   public void teardown()
   {
      _referenceList.clear();
      _referenceList = null;
   }

   @Test
   public void testArrayListParallelStream()
   {
      final List<Integer> list = setup(() -> {
         final List<Integer> result = new ArrayList<Integer>(DIM);
         return result;
      }, l -> CompositeUtils.list(l));

      final Spliterator<Integer> s = list.spliterator();
      assertTrue(s instanceof CompositeSpliterator<?>);

      assertTrue(s.hasCharacteristics(Spliterator.ORDERED));
      assertTrue(s.hasCharacteristics(Spliterator.SIZED));
      assertTrue(s.hasCharacteristics(Spliterator.SUBSIZED));
      assertFalse(s.hasCharacteristics(Spliterator.IMMUTABLE));
      assertFalse(s.hasCharacteristics(Spliterator.NONNULL));
      assertFalse(s.hasCharacteristics(Spliterator.CONCURRENT));

      assertEquals(DIM * DIM, s.getExactSizeIfKnown());

      final List<Integer> resultList = list.parallelStream().collect(toList());

      assertEquals(_referenceList, resultList);
   }

   @Test
   public void testHashSetParallelStream()
   {
      final Collection<Integer> list = setup(() -> {
         final Collection<Integer> result = new HashSet<Integer>();
         return result;
      }, l -> CompositeUtils.collection(l));

      final Spliterator<Integer> s = list.spliterator();
      assertTrue(s instanceof CompositeSpliterator<?>);

      assertTrue(s.hasCharacteristics(Spliterator.SIZED));
      assertFalse(s.hasCharacteristics(Spliterator.SUBSIZED));
      assertFalse(s.hasCharacteristics(Spliterator.ORDERED));
      assertFalse(s.hasCharacteristics(Spliterator.IMMUTABLE));
      assertFalse(s.hasCharacteristics(Spliterator.NONNULL));
      assertFalse(s.hasCharacteristics(Spliterator.CONCURRENT));

      assertEquals(DIM * DIM, s.getExactSizeIfKnown());

      final List<Integer> resultList = list.parallelStream().collect(toList());

      assertEquals(_referenceList.size(), resultList.size());
      assertTrue(resultList.containsAll(_referenceList));
   }

   @Test
   public void testArrayListWithOneHashSetParallelStream()
   {
      final Collection<Integer> list = setup(() -> {
         final Collection<Integer> result = new ArrayList<Integer>(DIM);
         return result;
      }, l -> {
         // Replace last list with a HashSet
            l.add(new HashSet<>(l.remove(l.size() - 1)));
            return CompositeUtils.collection(l);
         });

      final Spliterator<Integer> s = list.spliterator();
      assertTrue(s instanceof CompositeSpliterator<?>);

      assertTrue(s.hasCharacteristics(Spliterator.SIZED));
      assertFalse(s.hasCharacteristics(Spliterator.SUBSIZED));
      assertFalse(s.hasCharacteristics(Spliterator.ORDERED));
      assertFalse(s.hasCharacteristics(Spliterator.IMMUTABLE));
      assertFalse(s.hasCharacteristics(Spliterator.NONNULL));
      assertFalse(s.hasCharacteristics(Spliterator.CONCURRENT));

      assertEquals(DIM * DIM, s.getExactSizeIfKnown());

      final List<Integer> resultList = list.parallelStream().collect(toList());

      assertEquals(_referenceList.size(), resultList.size());
      assertTrue(resultList.containsAll(_referenceList));
   }
}
