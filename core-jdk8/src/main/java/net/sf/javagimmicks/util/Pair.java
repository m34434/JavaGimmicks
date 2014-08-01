package net.sf.javagimmicks.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface Pair<A, B>
{
   static <A, B> Pair<A, B> of(final A a, final B b)
   {
      return new PairImpl<A, B>(a, b);
   }

   A getA();

   B getB();

   public default void feed(final BiConsumer<? super A, ? super B> consumer)
   {
      consumer.accept(getA(), getB());
   }

   public default void feed(final Consumer<? super A> consumerA, final Consumer<? super B> consumerB)
   {
      consumerA.accept(getA());
      consumerB.accept(getB());
   }

   public default <T> T apply(final BiFunction<? super A, ? super B, ? extends T> function)
   {
      return function.apply(getA(), getB());
   }
}
