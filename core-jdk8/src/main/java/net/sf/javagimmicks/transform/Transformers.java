package net.sf.javagimmicks.transform;

import java.util.function.Function;

import javax.xml.transform.Transformer;

/**
 * A basic helper for dealing with {@link Function}s, {@link BidiFunction} s and
 * {@link Transforming} and {@link BidiTransforming} instances.
 */
public class Transformers
{
   private Transformers()
   {}

   /**
    * Creates a new inverted version of a given {@link BidiFunction} (exchanges
    * {@link BidiTransformer#transform(Object)} and
    * {@link BidiTransformer#applyReverse(Object)}).
    * 
    * @param function
    *           the {@link BidiFunction} to invert
    * @return a inverted version of the given {@link BidiFunction}
    */
   public static <F, T> BidiFunction<T, F> invert(final BidiFunction<F, T> function)
   {
      return new BidiFunction<T, F>()
      {
         @Override
         public T applyReverse(final F source)
         {
            return function.apply(source);
         }

         @Override
         public F apply(final T source)
         {
            return function.applyReverse(source);
         }

         @Override
         public BidiFunction<F, T> invert()
         {
            return function;
         }
      };
   }

   /**
    * Creates a new {@link BidiTransformer} out of two given {@link Transformer}
    * s - one for each direction.
    * 
    * @param forwardTransformer
    *           the forward {@link Transformer}
    * @param backTransformer
    *           the backward {@link Transformer}
    * @return a resulting {@link BidiTransformer} combined from the two given
    *         {@link Transformer}s
    */
   public static <F, T> BidiTransformer<F, T> bidiTransformerFromTransformers(
         final Function<F, T> forwardTransformer,
         final Function<T, F> backTransformer)
   {
      return new DualTransformerBidiTransformer<F, T>(forwardTransformer, backTransformer);
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
    * Returns the {@link Transformer} of a given object if it is
    * {@link Transforming}.
    * 
    * @param transforming
    *           the object to drag the {@link Transformer} out from
    * @return the {@link Transformer} contained in the given object
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

      return ((Transforming<?, ?>) transforming).getTransformer();
   }

   /**
    * Returns the {@link BidiTransformer} of a given object if it is
    * {@link BidiTransforming}.
    * 
    * @param transforming
    *           the object to drag the {@link BidiTransformer} out from
    * @return the {@link BidiTransformer} contained in the given object
    * @throws IllegalArgumentException
    *            if the given object is not a {@link BidiTransforming} instance
    * @see #isBidiTransforming(Object)
    */
   public static BidiTransformer<?, ?> getBidiTransformer(final Object transforming)
   {
      if (!isBidiTransforming(transforming))
      {
         throw new IllegalArgumentException("Object is not bidi-transforming!");
      }

      return ((BidiTransforming<?, ?>) transforming).getBidiTransformer();
   }
}
