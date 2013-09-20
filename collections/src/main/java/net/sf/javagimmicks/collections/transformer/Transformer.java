package net.sf.javagimmicks.collections.transformer;

/**
 * A simple interface for class that can transform objects into another value
 * and/or format.
 * 
 * @param <F>
 *           the source object type
 * @param <T>
 *           the target object type
 */
public interface Transformer<F, T>
{
   /**
    * Transforms a given object into another value and/or type
    * 
    * @param source
    *           the source object
    * @return the transformed object
    */
   public T transform(F source);
}
