package net.sf.javagimmicks.collections8.composite;

import static net.sf.javagimmicks.util.MoreCollectors.summingLongToBigInteger;

import java.math.BigInteger;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class CompositeSpliterator<E> implements Spliterator<E>
{
   protected final LinkedList<Spliterator<E>> _spliterators;

   protected int _currentIndex = -1;
   protected final int _characteristics;

   CompositeSpliterator(final LinkedList<Spliterator<E>> spliterators)
   {
      _spliterators = new LinkedList<>(spliterators);

      _characteristics = calculateCharacteristics();
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

         result |= spliterator.tryAdvance(action);
      }

      return result;
   }

   @Override
   public Spliterator<E> trySplit()
   {
      final Spliterator<E> firstSpliterator = _spliterators.getFirst();

      // If the first Spliterator can split, return the result
      final Spliterator<E> splitTry = firstSpliterator.trySplit();
      if (splitTry != null)
      {
         return splitTry;
      }

      // If it cannot split, return the WHOLE Spliterator, but remove it from
      // the current one
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
      return _characteristics;
   }

   private int calculateCharacteristics()
   {
      int result = 0;

      // We cannot guarantee "DISTINCT", since we would have to cross-check all
      // elements

      result = checkAndApplyIfAll(result, CONCURRENT);
      result = checkAndApplyIfAll(result, IMMUTABLE);
      result = checkAndApplyIfAll(result, NONNULL);
      result = checkAndApplyIfAll(result, ORDERED);
      result = checkAndApplyIfAll(result, SIZED);
      result = checkAndApplyIfAll(result, SORTED);
      result = checkAndApplyIfAll(result, SUBSIZED);

      return result;
   }

   private int checkAndApplyIfAll(int base, final int characteristics)
   {
      if (_spliterators.stream().allMatch(s -> s.hasCharacteristics(characteristics)))
      {
         base |= characteristics;
      }

      return base;
   }
}