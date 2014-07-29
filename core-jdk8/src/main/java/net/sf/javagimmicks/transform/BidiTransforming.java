package net.sf.javagimmicks.transform;

import java.util.function.Function;

/**
 * An interface for objects that carry a {@link BidiFunction} for internally
 * transforming objects.
 * 
 * @param <F>
 *           the source object type of the contained {@link BidiFunction}
 * @param <T>
 *           the target object type of the contained {@link BidiFunction}
 */
public interface BidiTransforming<F, T> extends Transforming<F, T>
{
   /**
    * Returns the internal {@link BidiFunction}
    * 
    * @return the internal {@link BidiFunction}
    */
   public BidiFunction<F, T> getBidiTransformer();

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
    * Returns the {@link BidiFunction} of a given object if it is
    * {@link BidiTransforming}.
    * 
    * @param transforming
    *           the object to drag the {@link Function} out from
    * @return the {@link Function} contained in the given object
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

      return ((BidiTransforming<?, ?>) transforming).getBidiTransformer();
   }
}
