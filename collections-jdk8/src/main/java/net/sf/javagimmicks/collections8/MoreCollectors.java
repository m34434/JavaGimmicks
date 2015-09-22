package net.sf.javagimmicks.collections8;

import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Collectors;

/**
 * Provides some {@link Collector} helper functions in addition to
 * {@link Collectors}.
 */
public class MoreCollectors
{
   private MoreCollectors()
   {}

   /**
    * Constructs a {@link Collector} that maps elements to {@link BigInteger}s
    * using the given {@link Function} and sums them up.
    * 
    * @param toBigIntegerFunction
    *           a {@link Function} to map elements to {@link BigInteger}
    *           instances
    * @return the sum of all mapped {@link BigInteger} values
    */
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

   /**
    * Constructs a {@link Collector} that maps elements to {@code long} values
    * using the given {@link ToLongFunction} and sums them up as
    * {@link BigInteger}.
    * 
    * @param toLongFunction
    *           a {@link ToLongFunction} to map elements to {@code long} values
    * @return the sum of all mapped {@code long} values as {@link BigInteger}
    */
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

   /**
    * Constructs a {@link Collector} that maps elements to {@code int} values
    * using the given {@link ToIntFunction} and sums them up as
    * {@link BigInteger}.
    * 
    * @param toIntFunction
    *           a {@link ToIntFunction} to map elements to {@code int} values
    * @return the sum of all mapped {@code int} values as {@link BigInteger}
    */
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
