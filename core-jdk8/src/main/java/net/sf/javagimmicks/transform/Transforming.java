package net.sf.javagimmicks.transform;

import java.util.function.Function;

/**
 * An interface for objects that carry a {@link Function} for internally
 * transforming objects.
 * 
 * @param <F>
 *           the source object type of the contained {@link Function}
 * @param <T>
 *           the target object type of the contained {@link Function}
 */
public interface Transforming<F, T>
{
   /**
    * Returns the internal {@link Function}
    * 
    * @return the internal {@link Function}
    */
   public Function<F, T> getTransformer();
}
