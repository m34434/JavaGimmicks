package net.sf.javagimmicks.collections8.composite;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;

import org.junit.Test;

public class CompositeSpliteratorTest
{
   private static final int DIM = 500;

   private <C extends Collection<Integer>> C setup(final Collection<Integer> reference,
         final Supplier<C> collectionSupplier,
         final Function<List<C>, C> compositeBuilder)
   {
      final List<C> collections = new ArrayList<>(DIM);

      IntStream.range(0, DIM).forEach(i ->
      {
         final C collection = collectionSupplier.get();

         IntStream.range(0, DIM).forEach(j ->
         {
            final int count = i * DIM + j;
            collection.add(count);
            reference.add(count);
         });

         collections.add(collection);
      });

      return compositeBuilder.apply(collections);
   }

   @Test
   public void testArrayListParallelStream()
   {
      final List<Integer> referenceList = new ArrayList<Integer>(DIM * DIM);

      final List<Integer> list = setup(referenceList, () -> {
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

      final List<Integer> resultList = streamIntoCollection(list, toList());

      assertEquals(referenceList, resultList);
   }

   @Test
   public void testHashSetParallelStream()
   {
      final Set<Integer> referenceSet = new HashSet<>();

      final Collection<Integer> list = setup(referenceSet, () -> {
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

      final Set<Integer> resultSet = streamIntoCollection(list, toSet());

      assertEquals(referenceSet, resultSet);
   }

   @Test
   public void testArrayListWithOneHashSetParallelStream()
   {
      final Set<Integer> referenceSet = new HashSet<>();

      final Collection<Integer> list = setup(referenceSet, () -> {
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

      final Set<Integer> resultSet = streamIntoCollection(list, toSet());

      assertEquals(referenceSet, resultSet);
   }

   private <C extends Collection<Integer>> C streamIntoCollection(final Collection<Integer> list,
         final Collector<Integer, ?, C> collector)
   {
      long start = System.currentTimeMillis();
      final C resultList = list.parallelStream().collect(collector);
      System.out.printf("Parallel stream collect took %s milliseconds%n", System.currentTimeMillis() - start);

      start = System.currentTimeMillis();
      list.parallelStream().collect(toList());
      System.out.printf("Sequential stream collect took %s milliseconds%n", System.currentTimeMillis() - start);

      return resultList;
   }
}
