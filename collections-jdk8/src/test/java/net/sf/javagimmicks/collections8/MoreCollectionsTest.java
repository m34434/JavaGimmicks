package net.sf.javagimmicks.collections8;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import net.sf.javagimmicks.util8.Pair;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MoreCollectionsTest
{
   private static final List<String> _string = Arrays.asList("a", "b", "c");
   private static final List<Integer> _integers = Arrays.asList(1, 2, 3);

   private static final List<Pair<String, Integer>> _stringsTimesIntegers;

   static
   {
      _stringsTimesIntegers = new ArrayList<>(_string.size() * _integers.size());
      for (final String string : _string)
      {
         for (final Integer integer : _integers)
         {
            _stringsTimesIntegers.add(Pair.of(string, integer));
         }
      }
   }

   @Test
   public void testCrossProductStream()
   {
      testCrossProductStream(MoreCollections.crossProductStream(_string, _integers));
   }

   @Test
   public void testCrossProductParllelStream()
   {
      testCrossProductStream(MoreCollections.crossProductParallelStream(_string, _integers));
   }

   @Test
   public void testCrossProductForEach()
   {
      final List<Pair<String, Integer>> results = new ArrayList<Pair<String, Integer>>(_stringsTimesIntegers.size());

      MoreCollections.crossProductForEach(_string, _integers, (a, b) -> results.add(Pair.of(a, b)));

      Assert.assertEquals(_stringsTimesIntegers, results);
   }

   @SuppressWarnings({ "unchecked", "rawtypes" })
   private void testCrossProductStream(final Stream<Pair<String, Integer>> stream)
   {
      final Consumer<Pair<String, Integer>> consumer = mock(Consumer.class);

      stream.forEachOrdered(consumer);

      final ArgumentCaptor<Pair> capturedPairs = ArgumentCaptor.forClass(Pair.class);
      verify(consumer, times(_stringsTimesIntegers.size())).accept(capturedPairs.capture());

      Assert.assertEquals(_stringsTimesIntegers, capturedPairs.getAllValues());
   }
}
