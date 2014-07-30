package net.sf.javagimmicks.util;

/**
 * This interface specifies an object that can contain and provide another
 * object (of a given type).
 * 
 * @param <E>
 *           the type of object the container can carry
 */
public interface ObjectContainer<E>
{
   /**
    * Retrieves the registered instance (if present)
    * 
    * @return the resulting instance or <code>null</code> if none is registered
    */
   E get();
}