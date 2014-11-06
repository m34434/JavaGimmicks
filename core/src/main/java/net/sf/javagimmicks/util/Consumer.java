package net.sf.javagimmicks.util;

/**
 * A generic interface for objects that can consumer other objects.
 * 
 * @param <E>
 *           the type of object(s) to consume
 */
public interface Consumer<E>
{
   /**
    * Consumes the given object.
    * 
    * @param instance
    *           the object to consume
    */
   void accept(E instance);
}
