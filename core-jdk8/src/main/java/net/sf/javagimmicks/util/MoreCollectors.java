package net.sf.javagimmicks.util;

import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

public class MoreCollectors
{
   private MoreCollectors()
   {}

   public static <T> Collector<T, ?, BigInteger> summingBigInteger(
         final Function<? super T, BigInteger> toBigIntegerFunction)
   {
      return Collector.of(() -> new BigInteger[] { BigInteger.ZERO },//
            (c, t) -> c[0] = c[0].add(toBigIntegerFunction.apply(t)),//
            (a, b) -> {
               a[0] = a[0].add(b[0]);
               return a;
            },//
            a -> a[0], Characteristics.UNORDERED);
   }

   public static <T> Collector<T, ?, BigInteger> summingLongToBigInteger(
         final ToLongFunction<T> toLongFunction)
   {
      return Collector.of(() -> new BigInteger[] { BigInteger.ZERO },//
            (c, t) -> c[0] = c[0].add(BigInteger.valueOf(toLongFunction.applyAsLong(t))),//
            (a, b) -> {
               a[0] = a[0].add(b[0]);
               return a;
            },//
            a -> a[0], Characteristics.UNORDERED);
   }

   public static <T> Collector<T, ?, BigInteger> summingIntToBigInteger(
         final ToIntFunction<T> toIntFunction)
   {
      return Collector.of(() -> new BigInteger[] { BigInteger.ZERO },//
            (c, t) -> c[0] = c[0].add(BigInteger.valueOf(toIntFunction.applyAsInt(t))),//
            (a, b) -> {
               a[0] = a[0].add(b[0]);
               return a;
            },//
            a -> a[0], Characteristics.UNORDERED);
   }
}
