package net.sf.javagimmicks.lang;

/**
 * An interface for objects that carry a {@link Transformer} for internally
 * transforming objects.
 * 
 * @param <F>
 *           the source object type of the contained {@link Transformer}
 * @param <T>
 *           the target object type of the contained {@link Transformer}
 */
public interface Transforming<F, T>
{
   /**
    * Returns the internal {@link Transformer}
    * 
    * @return the internal {@link Transformer}
    */
   public Transformer<F, T> getTransformer();
}
