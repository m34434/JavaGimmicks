package net.sf.javagimmicks.transform;

/**
 * A basic helper for dealing with {@link Transformer}s, {@link BidiTransformer}
 * s and {@link Transforming} and {@link BidiTransforming} instances.
 */
public class Transformers
{
   private Transformers()
   {}

   /**
    * Creates a new pseudo-{@link Transformer} that always returns the original
    * value.
    * 
    * @param <E>
    *           the type of elements the {@link Transformer} should handle
    * @return a new pseudo-{@link Transformer} that always returns the original
    *         value
    */
   public static <E> Transformer<E, E> identityTransformer()
   {
      return identityBidiTransformer();
   }

   /**
    * Creates a new pseudo-{@link BidiTransformer} that always returns the
    * original value.
    * 
    * @param <E>
    *           the type of elements the {@link BidiTransformer} should handle
    * @return a new pseudo-{@link BidiTransformer} that always returns the
    *         original value
    */
   public static <E> BidiTransformer<E, E> identityBidiTransformer()
   {
      return new BidiTransformer<E, E>()
      {
         @Override
         public E transform(final E source)
         {
            return source;
         }

         @Override
         public BidiTransformer<E, E> invert()
         {
            return this;
         }

         @Override
         public E transformBack(final E source)
         {
            return source;
         }
      };
   }

   /**
    * Creates a new inverted version of a given {@link BidiTransformer}
    * (exchanges {@link BidiTransformer#transform(Object)} and
    * {@link BidiTransformer#transformBack(Object)}).
    * 
    * @param transformer
    *           the {@link BidiTransformer} to invert
    * @param <F>
    *           the "from" or source type
    * @param <T>
    *           the "to" or target type
    * @return a inverted version of the given {@link BidiTransformer}
    */
   public static <F, T> BidiTransformer<T, F> invert(final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformer<T, F>()
      {
         @Override
         public T transformBack(final F source)
         {
            return transformer.transform(source);
         }

         @Override
         public F transform(final T source)
         {
            return transformer.transformBack(source);
         }

         @Override
         public BidiTransformer<F, T> invert()
         {
            return transformer;
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
    * @param <F>
    *           the "from" or source type
    * @param <T>
    *           the "to" or target type
    * @return a resulting {@link BidiTransformer} combined from the two given
    *         {@link Transformer}s
    */
   public static <F, T> BidiTransformer<F, T> bidiTransformerFromTransformers(
         final Transformer<F, T> forwardTransformer,
         final Transformer<T, F> backTransformer)
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
   public static Transformer<?, ?> getTransformer(final Object transforming)
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
