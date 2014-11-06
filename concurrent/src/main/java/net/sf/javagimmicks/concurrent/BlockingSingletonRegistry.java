package net.sf.javagimmicks.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.sf.javagimmicks.util.WritableObjectContainer;

/**
 * This class serves as a registry that allows (de-)registration of objects
 * following the singleton pattern and provides blocking getter methods for
 * them. This means that getter calls will not return until the desired
 * singleton instance has been registered in this container (or a timeout or
 * interruption has occurred).
 * <p>
 * This might be useful in server environments where the container (e.g. a CDI
 * container) takes care about instantiation (at some time) but clients want or
 * need to access the the instances from without the containers context (e.g.
 * via plain old static getter calls).
 * <p>
 * The class can be directly instantiated using the default constructor or a
 * static default instance can be used.
 * <p>
 * This class intensively uses {@link BlockingObjectContainer} internally
 * 
 * @see BlockingObjectContainer
 */
public class BlockingSingletonRegistry
{
   private static BlockingSingletonRegistry _defaultInstance;

   /**
    * Returns a static default (singleton) instance of the
    * {@link BlockingSingletonRegistry}
    * 
    * @return the default instance
    */
   public static BlockingSingletonRegistry getDefault()
   {
      if (_defaultInstance != null)
      {
         return _defaultInstance;
      }

      synchronized (BlockingSingletonRegistry.class)
      {
         if (_defaultInstance == null)
         {
            _defaultInstance = new BlockingSingletonRegistry();
         }

         return _defaultInstance;
      }
   }

   private final Map<Class<?>, BlockingObjectContainer<?>> _theContainers = Collections
         .synchronizedMap(new HashMap<Class<?>, BlockingObjectContainer<?>>());

   /**
    * Registers a new singleton instance in the registry
    * 
    * @param instance
    *           the singleton instance to register
    * @param <T>
    *           the type of the singleton object to register
    * @throws IllegalStateException
    *            if there is already another instance of the given class
    *            registered (it's not a singleton)
    */
   public <T> void set(final T instance) throws IllegalStateException
   {
      if (instance == null)
      {
         return;
      }

      // Get the class of the singleton instance
      @SuppressWarnings("unchecked")
      final Class<T> clazz = (Class<T>) instance.getClass();

      // Get ore create a Container for the class and set the instance
      getOrCreateContainer(clazz).accept(instance);
   }

   /**
    * Removes the singleton of the given class from the registry
    * 
    * @param <T>
    *           the type of the singleton object to remove
    * @param clazz
    *           the singleton class whose instance should be removed
    */
   public <T> void remove(final Class<T> clazz)
   {
      if (clazz == null)
      {
         return;
      }

      // Get the matching container ...
      @SuppressWarnings("unchecked")
      final WritableObjectContainer<T> container = (WritableObjectContainer<T>) _theContainers.remove(clazz);

      // ... and remove the singleton from there if there was one
      if (container != null)
      {
         container.remove();
      }
   }

   /**
    * Remove the given singleton instance from the registry
    * 
    * @param instance
    *           the singleton instance that should be removed
    * @param <T>
    *           the type of the singleton object to remove
    * @throws IllegalArgumentException
    *            if another one than the registered singleton instance should be
    *            removed
    */
   public <T> void remove(final T instance)
   {
      if (instance == null)
      {
         return;
      }

      // Get the class of the singleton instance
      @SuppressWarnings("unchecked")
      final Class<T> clazz = (Class<T>) instance.getClass();

      synchronized (_theContainers)
      {
         // Get the matching container ...
         @SuppressWarnings("unchecked")
         final BlockingObjectContainer<T> container = (BlockingObjectContainer<T>) _theContainers.get(clazz);

         // ... and abort if there is none
         if (container == null)
         {
            return;
         }

         // Check, if the object to remove is the same as the registered one
         final Object existingInstance = container.getNoWait();
         if (existingInstance != null && existingInstance != instance)
         {
            throw new IllegalArgumentException("There is another instance of " + clazz.getName()
                  + " registered! Are you sure it is a singleton?");
         }

         // Checks were okay, so unregister the instance and remove the
         // container
         container.remove();
         _theContainers.remove(clazz);
      }
   }

   /**
    * Retrieves the singleton instance of the given class waiting
    * uninterruptibly until it is registered (i.e. the waiting Thread will NOT
    * react to {@link Thread#interrupt()} calls)
    * <p>
    * Attention: this call blocks FOREVER - so you REALLY should take care that
    * the requested instance is finally registered
    * 
    * @param clazz
    *           the class whose singleton instance should be retrieved
    * @param <T>
    *           the type of the singleton object to retrieve
    * @return the resulting singleton instance of the given class
    */
   public <T> T get(final Class<T> clazz)
   {
      return getOrCreateContainer(clazz).get();
   }

   /**
    * Retrieves the singleton instance of the given class waiting interruptibly
    * until it is registered (i.e. the waiting Thread will react to
    * {@link Thread#interrupt()} calls)
    * <p>
    * Attention: this call block FOREVER (if not interrupted) - so you REALLY
    * should take care that the requested instance is finally registered
    * 
    * @param clazz
    *           the class whose singleton instance should be retrieved
    * @param <T>
    *           the type of the singleton object to retrieve
    * @return the resulting singleton instance of the given class
    * @throws InterruptedException
    *            if the waiting Thread is interrupted while waiting
    */
   public <T> T getInterruptibly(final Class<T> clazz) throws InterruptedException
   {
      return getOrCreateContainer(clazz).getInterruptibly();
   }

   /**
    * Retrieves the singleton instance of the given class waiting interruptibly
    * (i.e. the waiting Thread will react to {@link Thread#interrupt()} calls)
    * until it is registered or the given timeout has elapsed
    * 
    * @param clazz
    *           the class whose singleton instance should be retrieved
    * @param time
    *           the number of {@link TimeUnit}s to wait at until the call
    *           returns
    * @param timeUnit
    *           the {@link TimeUnit} of the given time amount to wait
    * @param <T>
    *           the type of the singleton object to retrieve
    * @return the resulting singleton instance of the given class
    * @throws InterruptedException
    *            if the waiting Thread is interrupted while waiting
    */
   public <T> T get(final Class<T> clazz, final long time, final TimeUnit timeUnit) throws InterruptedException
   {
      return getOrCreateContainer(clazz).get(time, timeUnit);
   }

   /**
    * Retrieves the singleton instance of the given class if currently
    * registered without potentially waiting for it (i.e. the call will always
    * return immediately)
    * 
    * @param clazz
    *           the class whose singleton instance should be retrieved
    * @param <T>
    *           the type of the singleton object to retrieve
    * @return the registered singleton instance or <code>null</code> if non is
    *         registered
    */
   public <T> T getNoWait(final Class<T> clazz)
   {
      return getOrCreateContainer(clazz).getNoWait();
   }

   @SuppressWarnings("unchecked")
   protected <E> BlockingObjectContainer<E> getOrCreateContainer(final Class<E> clazz)
   {
      // Get the potentially registered container
      BlockingObjectContainer<E> result = (BlockingObjectContainer<E>) _theContainers.get(clazz);

      // Return it immediately if one was registered
      if (result != null)
      {
         return result;
      }

      // Negative case: create a new one in the synchronized context
      synchronized (_theContainers)
      {
         // Re-check in the synchronized context
         result = (BlockingObjectContainer<E>) _theContainers.get(clazz);

         if (result == null)
         {
            result = new BlockingObjectContainer<E>();
            _theContainers.put(clazz, result);
         }

         return result;
      }
   }
}