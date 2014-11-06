package net.sf.javagimmicks.collections8.composite;

import static net.sf.javagimmicks.collections8.MoreCollectors.summingLongToBigInteger;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class CompositeSpliterator<E> implements Spliterator<E>
{
   protected final LinkedList<Spliterator<E>> _spliterators;
   protected Integer _characteristics;

   CompositeSpliterator(final LinkedList<Spliterator<E>> spliterators)
   {
      _spliterators = new LinkedList<>(spliterators);
   }

   static <E, C extends Collection<E>> CompositeSpliterator<E> fromCollectionList(final List<C> collections)
   {
      return new CompositeSpliterator<E>(collections.stream().map(c -> c.spliterator())
            .collect(Collectors.toCollection(LinkedList::new)));
   }

   @Override
   public boolean tryAdvance(final Consumer<? super E> action)
   {
      for (final Iterator<Spliterator<E>> spliteratorIter = _spliterators.iterator(); spliteratorIter.hasNext();)
      {
         final Spliterator<E> spliterator = spliteratorIter.next();

         if (spliterator.tryAdvance(action))
         {
            return true;
         }

         spliteratorIter.remove();
      }

      return false;
   }

   @Override
   public Spliterator<E> trySplit()
   {
      final int spliteratorsCount = _spliterators.size();
      if (spliteratorsCount == 0)
      {
         // Update characteristics
         _characteristics = 0;

         return null;
      }

      if (spliteratorsCount == 1)
      {
         final Spliterator<E> theLastSpliterator = _spliterators.getFirst();
         final Spliterator<E> result = theLastSpliterator.trySplit();

         // Update characteristics
         _characteristics = theLastSpliterator.characteristics();

         return result;
      }

      // Cut in the middle (not regarding sizes)
      final int middle = _spliterators.size() / 2;

      // Create new List of Spliterators simply by using and cloning a SubList
      final List<Spliterator<E>> splitSpliteratorsSubList = _spliterators.subList(0, middle);
      final LinkedList<Spliterator<E>> splitSpliteratorsNewList = new LinkedList<Spliterator<E>>(
            splitSpliteratorsSubList);

      // Clear the SubList which effectively removes its elements from this
      // Spliterator
      splitSpliteratorsSubList.clear();

      // Reset characteristics
      _characteristics = null;

      return splitSpliteratorsNewList.size() > 1 ? new CompositeSpliterator<E>(splitSpliteratorsNewList)
            : splitSpliteratorsNewList.getFirst();
   }

   @Override
   public long estimateSize()
   {
      // Sum up all estimations of remaining spliterator into a BigInteger
      final BigInteger sum = _spliterators.stream().collect(
            summingLongToBigInteger(s -> s.estimateSize()));

      // Return the sum with an upper limit of Long.MAX_VALUE
      return sum.min(BigInteger.valueOf(Long.MAX_VALUE)).longValue();
   }

   @Override
   public int characteristics()
   {
      if (_characteristics == null)
      {
         _characteristics = calculateCharacteristics();
      }

      return _characteristics;
   }

   private int calculateCharacteristics()
   {
      // We cannot guarantee "DISTINCT" or "SORTED", since we would have to
      // cross-check all elements

      // Walk over all Spliterator and merge the characteristics for each into a
      // Map
      final Map<Integer, Boolean> m = new HashMap<>();
      _spliterators.forEach(s -> characteristicsStream().forEach(
            c -> m.merge(c, s.hasCharacteristics(c), (a, b) -> a && b)));

      // Predicate/map/reduce to the final characteristics (skip all "false"
      // characteristics and merge them via "|" operator
      final Optional<Integer> result = m.entrySet().stream().filter(e -> e.getValue()).map(e -> e.getKey())
            .reduce((a, b) -> a | b);

      return result.orElse(0);
   }

   private static IntStream characteristicsStream()
   {
      return IntStream.of(ORDERED, NONNULL, SIZED, SUBSIZED, IMMUTABLE, CONCURRENT);
   }
}