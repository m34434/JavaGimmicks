package net.sf.javagimmicks.collections8.composite;

import static net.sf.javagimmicks.util.MoreCollectors.summingLongToBigInteger;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
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
      boolean result = false;

      // Advance through all remaining Spliterators continuously removing them
      // from this Composite
      while (!_spliterators.isEmpty())
      {
         final Spliterator<E> spliterator = _spliterators.removeFirst();

         while (spliterator.tryAdvance(action))
         {
            result = true;
         }
      }

      return result;
   }

   @Override
   public Spliterator<E> trySplit()
   {
      final Spliterator<E> firstSpliterator = _spliterators.getFirst();
      final int firstSpliteratorCharacteristics = firstSpliterator.characteristics();

      // If the first Spliterator can split, return the result
      final Spliterator<E> splitTry = firstSpliterator.trySplit();
      if (splitTry != null)
      {
         // Re-check if characteristics have changed - if yes, we have to
         // re-calculate
         if (firstSpliteratorCharacteristics != firstSpliterator.characteristics())
         {
            _characteristics = null;
         }
         return splitTry;
      }

      // If it cannot split
      // - enforce re-calc of characteristics
      // - return the WHOLE Spliterator, but remove it from the current one
      _characteristics = null;
      return _spliterators.removeFirst();
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
      _spliterators.forEach(s -> IntStream.of(ORDERED, NONNULL, SIZED, SUBSIZED, IMMUTABLE, CONCURRENT).forEach(
            c -> m.put(c, m.getOrDefault(c, true) && s.hasCharacteristics(c))));

      // Map/reduce to the final characteristics
      final Optional<Integer> result = m.entrySet().stream().filter(e -> e.getValue()).map(e -> e.getKey())
            .reduce((a, b) -> a | b);

      return result.orElse(0);
   }
}