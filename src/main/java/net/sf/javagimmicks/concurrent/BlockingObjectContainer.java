package net.sf.javagimmicks.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class serves as a container that allows (de-)registration of objects of
 * the specified type and provides blocking getter methods for them. This means
 * that (depending on the concretely called getter) calls will not return until
 * the desired singleton instance has been registered in this container (or a
 * timeout or interruption has occurred).
 * <p>
 * This might be useful in server environments where the container (e.g. a CDI
 * container) takes care about instantiation (at some time) but clients want or
 * need to access the the instances from without the containers context (e.g.
 * via plain old static getter calls).
 * 
 * @param <E>
 *           the type of object the container can carry
 */
public class BlockingObjectContainer<E>
{
   protected final Lock _lock = new ReentrantLock();
   protected Condition _condition;

   protected E _instance;

   protected boolean _allowOverwrite;

   /**
    * Creates a new instance with given setting for overwriting an existing
    * instance
    * 
    * @param allowOverwrite
    *           the setting for overwriting an existing instance
    * @see {@link #set(Object)}, {@link #isAllowOverwrite()}
    */
   public BlockingObjectContainer(boolean allowOverwrite)
   {
      _allowOverwrite = allowOverwrite;
   }

   /**
    * Creates a new instance disallowing overwriting of an existing instance
    */
   public BlockingObjectContainer()
   {
      this(false);
   }

   /**
    * Returns if an already set object instance may be replaced by another one
    * 
    * @return if an already set object instance my be replaced by another one
    * @see #set(Object)
    */
   public boolean isAllowOverwrite()
   {
      return _allowOverwrite;
   }

   /**
    * Registers an instance in the container
    * 
    * @param instance
    *           the singleton instance to register
    * @throws IllegalStateException
    *            if the given instances is not <code>null</code> and there is
    *            already another instance registered and
    *            {@link #isAllowOverwrite()} is <code>true</code>
    * @see #isAllowOverwrite()
    */
   public void set(E instance) throws IllegalStateException
   {
      // Unset case
      if (instance == null)
      {
         remove();
         return;
      }

      _lock.lock();
      try
      {
         // Check the singleton property
         if (_instance != null)
         {
            // The same object is okay (the call was obsolete)
            if (_instance == instance)
            {
               return;
            }
            else if (!isAllowOverwrite())
            {
               // Another instance was detected! No singleton!
               throw new IllegalStateException("There is already an instance of " + instance.getClass().getName()
                     + " registered! This may not be overwritten!");
            }
         }

         // Register the instance
         _instance = instance;

         // Check, if there are clients waiting for the instance and inform them
         // that it's there now
         if (_condition != null)
         {
            _condition.signalAll();
            _condition = null;
         }
      }
      finally
      {
         _lock.unlock();
      }
   }

   /**
    * Removes the singleton from the container
    * 
    * @return the previously registered singleton instance or <code>null</code>
    *         if none was registered
    */
   public E remove()
   {
      _lock.lock();

      try
      {
         final E result = _instance;
         _instance = null;

         return result;
      }
      finally
      {
         _lock.unlock();
      }
   }

   /**
    * Retrieves the singleton instance waiting uninterruptibly until it is
    * registered (i.e. the waiting Thread will NOT react to
    * {@link Thread#interrupt()} calls)
    * <p>
    * Attention: this call blocks FOREVER - so you REALLY should take care that
    * the requested instance is finally registered
    * 
    * @return the resulting singleton instance
    */
   public E get()
   {
      try
      {
         return get(new WaitStrategy() {

            @Override
            public void await(Condition condition)
            {
               // We will wait uninterruptibly and forever
               condition.awaitUninterruptibly();
            }
         });
      }

      // As the WaitCommand used above does not throw this Exception, it cannot
      // occur outside
      catch (InterruptedException cannotOccur)
      {
         return null;
      }
   }

   /**
    * Retrieves the singleton instance waiting interruptibly until it is
    * registered (i.e. the waiting Thread will react to
    * {@link Thread#interrupt()} calls)
    * <p>
    * Attention: this call block FOREVER (if not interrupted) - so you REALLY
    * should take care that the requested instance is finally registered
    * 
    * @return the resulting singleton instance
    * @throws InterruptedException
    *            if the waiting Thread is interrupted while waiting
    */
   public E getInterruptibly() throws InterruptedException
   {
      return get(new WaitStrategy() {

         @Override
         public void await(Condition condition) throws InterruptedException
         {
            condition.await();
         }
      });
   }

   /**
    * Retrieves the singleton instance waiting interruptibly (i.e. the waiting
    * Thread will react to {@link Thread#interrupt()} calls) until it is
    * registered or the given timeout has elapsed
    * 
    * @param time
    *           the number of {@link TimeUnit}s to wait at until the call
    *           returns
    * @param timeUnit
    *           the {@link TimeUnit} of the given time amount to wait
    * @return the resulting singleton instance
    * @throws InterruptedException
    *            if the waiting Thread is interrupted while waiting
    */
   public E get(final long time, final TimeUnit timeUnit) throws InterruptedException
   {
      return get(new WaitStrategy() {

         @Override
         public void await(Condition condition) throws InterruptedException
         {
            condition.await(time, timeUnit);
         }
      });
   }

   /**
    * Retrieves the singleton instance if currently registered without
    * potentially waiting for it (i.e. the call will always return immediately)
    * 
    * @return the registered singleton instance or <code>null</code> if non is
    *         registered
    */
   public E getNoWait()
   {
      return _instance;
   }

   private E get(WaitStrategy waitStrategy) throws InterruptedException
   {
      _lock.lock();
      try
      {
         // No instance is registered yet so wait until there is one.
         if (_instance == null)
         {
            // If there is none (i.e. we are the first client), create one and
            // register it
            if (_condition == null)
            {
               _condition = _lock.newCondition();
            }

            // Finally, we wait using the given WaitStrategy
            waitStrategy.await(_condition);
         }

         // Finally, we return the (potentially) registered instance
         return _instance;
      }
      finally
      {
         _lock.unlock();
      }
   }

   private interface WaitStrategy
   {
      void await(Condition condition) throws InterruptedException;
   }
}
