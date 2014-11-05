package net.sf.javagimmicks.transform;

import net.sf.javagimmicks.util.Function;

/**
 * A basic helper for dealing with {@link Function}s, {@link BidiFunction}
 * s and {@link Transforming} and {@link BidiTransforming} instances.
 */
public class Functions
{
   private Functions()
   {}

   /**
    * Creates a new pseudo-{@link Function} that always returns the original
    * value.
    * 
    * @param <E>
    *           the type of elements the {@link Function} should handle
    * @return a new pseudo-{@link Function} that always returns the original
    *         value
    */
   public static <E> Function<E, E> identityTransformer()
   {
      return identityBidiTransformer();
   }

   /**
    * Creates a new pseudo-{@link BidiFunction} that always returns the
    * original value.
    * 
    * @param <E>
    *           the type of elements the {@link BidiFunction} should handle
    * @return a new pseudo-{@link BidiFunction} that always returns the
    *         original value
    */
   public static <E> BidiFunction<E, E> identityBidiTransformer()
   {
      return new BidiFunction<E, E>()
      {
         @Override
         public E apply(final E source)
         {
            return source;
         }

         @Override
         public BidiFunction<E, E> invert()
         {
            return this;
         }

         @Override
         public E applyReverse(final E source)
         {
            return source;
         }
      };
   }

   /**
    * Creates a new inverted version of a given {@link BidiFunction}
    * (exchanges {@link BidiFunction#apply(Object)} and
    * {@link BidiFunction#applyReverse(Object)}).
    * 
    * @param transformer
    *           the {@link BidiFunction} to invert
    * @param <F>
    *           the "from" or source type
    * @param <T>
    *           the "to" or target type
    * @return a inverted version of the given {@link BidiFunction}
    */
   public static <F, T> BidiFunction<T, F> invert(final BidiFunction<F, T> transformer)
   {
      return new BidiFunction<T, F>()
      {
         @Override
         public T applyReverse(final F source)
         {
            return transformer.apply(source);
         }

         @Override
         public F apply(final T source)
         {
            return transformer.applyReverse(source);
         }

         @Override
         public BidiFunction<F, T> invert()
         {
            return transformer;
         }
      };
   }

   /**
    * Creates a new {@link BidiFunction} out of two given {@link Function}
    * s - one for each direction.
    * 
    * @param forwardTransformer
    *           the forward {@link Function}
    * @param backTransformer
    *           the backward {@link Function}
    * @param <F>
    *           the "from" or source type
    * @param <T>
    *           the "to" or target type
    * @return a resulting {@link BidiFunction} combined from the two given
    *         {@link Function}s
    */
   public static <F, T> BidiFunction<F, T> bidiTransformerFromTransformers(
         final Function<F, T> forwardTransformer,
         final Function<T, F> backTransformer)
   {
      return new DualFunctionBidiFunction<F, T>(forwardTransformer, backTransformer);
   }

   /**
    * Checks if a given object is transforming (if it is an instance of
    * {@link Transforming}).
    * 
    * @param o
    *           the object to check
    * @return if the object is {@link Transforming}
    */
   public static boolean isTransforming(final Object o)
   {
      return o instanceof Transforming<?, ?>;
   }

   /**
    * Checks if a given object is bidi-transforming (if it is an instance of
    * {@link BidiTransforming}).
    * 
    * @param o
    *           the object to check
    * @return if the object is {@link BidiTransforming}
    */
   public static boolean isBidiTransforming(final Object o)
   {
      return o instanceof BidiTransforming<?, ?>;
   }

   /**
    * Returns the {@link Function} of a given object if it is
    * {@link Transforming}.
    * 
    * @param transforming
    *           the object to drag the {@link Function} out from
    * @return the {@link Function} contained in the given object
    * @throws IllegalArgumentException
    *            if the given object is not a {@link Transforming} instance
    * @see #isTransforming(Object)
    */
   public static Function<?, ?> getTransformer(final Object transforming)
   {
      if (!isTransforming(transforming))
      {
         throw new IllegalArgumentException("Object is not transforming!");
      }

      return ((Transforming<?, ?>) transforming).getTransformerFunction();
   }

   /**
    * Returns the {@link BidiFunction} of a given object if it is
    * {@link BidiTransforming}.
    * 
    * @param transforming
    *           the object to drag the {@link BidiFunction} out from
    * @return the {@link BidiFunction} contained in the given object
    * @throws IllegalArgumentException
    *            if the given object is not a {@link BidiTransforming} instance
    * @see #isBidiTransforming(Object)
    */
   public static BidiFunction<?, ?> getBidiTransformer(final Object transforming)
   {
      if (!isBidiTransforming(transforming))
      {
         throw new IllegalArgumentException("Object is not bidi-transforming!");
      }

      return ((BidiTransforming<?, ?>) transforming).getTransformerBidiFunction();
   }
}
