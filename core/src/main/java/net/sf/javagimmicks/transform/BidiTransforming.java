package net.sf.javagimmicks.transform;

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
   public BidiFunction<F, T> getTransformerBidiFunction();
}
