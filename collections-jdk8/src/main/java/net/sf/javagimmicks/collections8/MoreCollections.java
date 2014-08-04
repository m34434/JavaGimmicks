package net.sf.javagimmicks.collections8;

import java.util.Collection;
import java.util.Collections;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.sf.javagimmicks.util.Pair;

/**
 * Provides some {@link Collection} helper functions in addition to
 * {@link Collections}.
 */
public class MoreCollections
{
   private MoreCollections()
   {}

   /**
    * Performs a classic "cross-product" (non-parallel)
    * {@link Iterable#forEach(java.util.function.Consumer)} operation on two
    * given {@link Iterable}s invoking the given {@link BiConsumer} for each
    * combination of elements.
    * 
    * @param aElements
    *           the first level {@link Iterable} to iterate over
    * @param bElements
    *           the second level {@link Iterable} to iterate over
    * @param action
    *           the {@link BiConsumer} which will be called with each pair of
    *           elements of the two given {@link Iterable}s
    * @param <A>
    *           the type of elements of the first level {@link Iterable}
    * @param <B>
    *           the type of elements of the second level {@link Iterable}
    */
   public static <A, B> void crossProductForEach(final Iterable<? extends A> aElements,
         final Iterable<? extends B> bElements,
         final BiConsumer<? super A, ? super B> action)
   {
      aElements.forEach(aElement -> //
            bElements.forEach(bElement -> //
                  action.accept(aElement, bElement)));
   }

   public static <A, B> Stream<Pair<A, B>> crossProductStream(final Collection<? extends A> aCollection,
         final Collection<? extends B> bCollection)
   {
      return StreamSupport.stream(new CrossProductSpliterator<A, B>(aCollection, bCollection), false);
   }

   public static <A, B> Stream<Pair<A, B>> crossProductParallelStream(final Collection<? extends A> aCollection,
         final Collection<? extends B> bCollection)
   {
      return StreamSupport.stream(new CrossProductSpliterator<A, B>(aCollection, bCollection), true);
   }
}
