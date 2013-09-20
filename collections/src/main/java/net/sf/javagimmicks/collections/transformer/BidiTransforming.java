package net.sf.javagimmicks.collections.transformer;

/**
 * An interface for objects that carry a {@link BidiTransformer} for internally
 * transforming objects.
 * 
 * @param <F>
 *           the source object type of the contained {@link BidiTransformer}
 * @param <T>
 *           the target object type of the contained {@link BidiTransformer}
 */
public interface BidiTransforming<F, T> extends Transforming<F, T>
{
   /**
    * Returns the internal {@link BidiTransformer}
    * 
    * @return the internal {@link BidiTransformer}
    */
   public BidiTransformer<F, T> getBidiTransformer();
}
