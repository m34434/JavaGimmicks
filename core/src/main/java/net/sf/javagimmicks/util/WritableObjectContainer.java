package net.sf.javagimmicks.util;

/**
 * This interface extends {@link ObjectContainer} by providing additional
 * methods that allow write access to the contained object.
 * 
 * @param <E>
 *           the type of object the container can carry
 */
public interface WritableObjectContainer<E> extends ObjectContainer<E>
{
   /**
    * Returns if an already set object instance may be replaced by another one
    * 
    * @return if an already set object instance my be replaced by another one
    * @see #set(Object)
    */
   boolean isAllowOverwrite();

   /**
    * Registers an instance in the container
    * 
    * @param instance
    *           the instance to register
    * @throws IllegalStateException
    *            if the given instances is not <code>null</code> and there is
    *            already another instance registered and
    *            {@link #isAllowOverwrite()} is <code>true</code>
    * @see #isAllowOverwrite()
    */
   void set(E instance) throws IllegalStateException;

   /**
    * Removes the instance (if any) from the container
    * 
    * @return the previously registered instance or <code>null</code> if none
    *         was registered
    */
   E remove();

}