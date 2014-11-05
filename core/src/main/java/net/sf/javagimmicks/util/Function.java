package net.sf.javagimmicks.util;

/**
 * A simple interface for classes that can transform objects into another value
 * and/or format.
 * 
 * @param <F>
 *           the source object type
 * @param <T>
 *           the target object type
 */
public interface Function<F, T>
{
   /**
    * Transforms a given object into another value and/or type
    * 
    * @param source
    *           the source object
    * @return the transformed object
    */
   public T apply(F source);
}
