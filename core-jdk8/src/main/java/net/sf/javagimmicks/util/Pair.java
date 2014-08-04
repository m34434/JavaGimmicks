package net.sf.javagimmicks.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Represents a pair of two typed values.
 * 
 * @param <A>
 *           the type of the first element
 * @param <B>
 *           the type of the second element
 */
public interface Pair<A, B>
{
   /**
    * Creates a new instance.
    * 
    * @param a
    *           the first element
    * @param b
    *           the second element
    * @param <A>
    *           the type of the first element
    * @param <B>
    *           the type of the second element
    * @return the new {@link Pair} instance
    */
   static <A, B> Pair<A, B> of(final A a, final B b)
   {
      return new PairImpl<A, B>(a, b);
   }

   /**
    * Returns the first element.
    * 
    * @return the first element
    */
   A getA();

   /**
    * Returns the second element.
    * 
    * @return the second element
    */
   B getB();

   /**
    * Feeds a given {@link BiConsumer} with the two contained values.
    * 
    * @param consumer
    *           the {@link BiConsumer} to feed with the contained values
    */
   public default void feed(final BiConsumer<? super A, ? super B> consumer)
   {
      consumer.accept(getA(), getB());
   }

   /**
    * Feeds two given {@link Consumer}s with the two contained values.
    * 
    * @param consumerA
    *           the {@link Consumer} for the first value
    * @param consumerB
    *           the {@link Consumer} for the second value
    */
   public default void feed(final Consumer<? super A> consumerA, final Consumer<? super B> consumerB)
   {
      consumerA.accept(getA());
      consumerB.accept(getB());
   }

   /**
    * Applies the two contained values to a given {@link BiFunction}.
    * 
    * @param function
    *           the {@link BiFunction} where to apply the contained values
    * @param <T>
    *           the return type of the {@link BiFunction}
    * @return the result of applying the {@link BiFunction}
    */
   public default <T> T apply(final BiFunction<? super A, ? super B, ? extends T> function)
   {
      return function.apply(getA(), getB());
   }
}
