package net.sf.javagimmicks.concurrent.locks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class RegistryLockProvider<K> implements MultiLockProvider<K>, Serializable
{
   private static final long serialVersionUID = 6807627151240655773L;

   protected final LockRegistry<K> _registry;

   protected final Lock _exLock = new ReentrantLock();
   protected final Condition _exCondition = _exLock.newCondition();

   protected final Lock _shLock = new ReentrantLock();
   protected final Condition _shCondition = _shLock.newCondition();

   public RegistryLockProvider(final LockRegistry<K> registry)
   {
      _registry = registry;
   }

   @Override
   public final MultiReadWriteLock<K> newLock(final Collection<K> resources)
   {
      return new MultiReadWriteLockImpl(resources);
   }

   protected class MultiReadWriteLockImpl implements MultiReadWriteLock<K>
   {
      protected final Collection<K> _resources;
      protected final MultiReadLockImpl _readLock;
      protected final MultiWriteLockImpl _writeLock;

      protected MultiReadWriteLockImpl(final Collection<K> resources)
      {
         final Collection<K> internalResourceList = new ArrayList<K>();

         // Filter out null resources
         for (final K resource : resources)
         {
            if (resource != null)
            {
               internalResourceList.add(resource);
            }
         }

         _resources = Collections.unmodifiableCollection(internalResourceList);

         _readLock = new MultiReadLockImpl();
         _writeLock = new MultiWriteLockImpl();
      }

      @Override
      public Collection<K> getResources()
      {
         return _resources;
      }

      @Override
      public MultiLock<K> readLock()
      {
         return _readLock;
      }

      @Override
      public MultiLock<K> writeLock()
      {
         return _writeLock;
      }

      protected abstract class MultiLockImpl implements MultiLock<K>
      {
         protected final ThreadLocal<Boolean> _lockedFlag = new ThreadLocal<Boolean>();

         @Override
         public final Collection<K> getResources()
         {
            return MultiReadWriteLockImpl.this.getResources();
         }

         @Override
         public boolean isLockedByThisThread()
         {
            final Boolean b = _lockedFlag.get();

            return b != null && b.booleanValue();
         }

         @Override
         public void lock()
         {
            testUnlocked();

            lockInternal();

            setLockedByThisThread(true);
         }

         @Override
         public void lockInterruptibly() throws InterruptedException
         {
            testUnlocked();

            lockInterruptiblyInternal();

            setLockedByThisThread(true);
         }

         @Override
         public Condition newCondition()
         {
            throw new UnsupportedOperationException();
         }

         @Override
         public boolean tryLock()
         {
            testUnlocked();

            final boolean result = tryLockInternal();

            if (result)
            {
               setLockedByThisThread(true);
            }

            return result;
         }

         @Override
         public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException
         {
            testUnlocked();

            final boolean result = tryLockInternal(time, unit);

            if (result)
            {
               setLockedByThisThread(true);
            }

            return result;
         }

         @Override
         public void unlock()
         {
            testLocked();

            unlockInternal();

            setLockedByThisThread(false);
         }

         protected final void setLockedByThisThread(final boolean locked)
         {
            _lockedFlag.set(locked ? Boolean.TRUE : null);
         }

         protected void testLocked()
         {
            if (!isLockedByThisThread())
            {
               throw new IllegalStateException("Lock is not locked by this thread!");
            }
         }

         protected void testUnlocked()
         {
            if (isLockedByThisThread())
            {
               throw new IllegalStateException("Lock is already locked by this thread!");
            }
         }

         abstract protected void lockInternal();

         abstract protected void lockInterruptiblyInternal() throws InterruptedException;

         abstract protected boolean tryLockInternal();

         abstract protected boolean tryLockInternal(long time, TimeUnit unit) throws InterruptedException;

         abstract protected void unlockInternal();
      }

      protected class MultiReadLockImpl extends MultiLockImpl
      {
         @Override
         protected void lockInternal()
         {
            _exLock.lock();
            try
            {
               while (!_registry.isExclusiveFree(getResources()))
               {
                  _exCondition.awaitUninterruptibly();
               }

               _shLock.lock();
               try
               {
                  _registry.registerShared(getResources());
               }
               finally
               {
                  _shLock.unlock();
               }
            }
            finally
            {
               _exLock.unlock();
            }
         }

         @Override
         protected void lockInterruptiblyInternal() throws InterruptedException
         {
            _exLock.lockInterruptibly();
            try
            {
               while (!_registry.isExclusiveFree(getResources()))
               {
                  _exCondition.await();
               }

               _shLock.lockInterruptibly();
               try
               {
                  _registry.registerShared(getResources());
               }
               finally
               {
                  _shLock.unlock();
               }
            }
            finally
            {
               _exLock.unlock();
            }
         }

         @Override
         protected boolean tryLockInternal()
         {
            if (!_exLock.tryLock())
            {
               return false;
            }

            try
            {
               if (!_registry.isExclusiveFree(getResources()))
               {
                  return false;
               }

               if (!_shLock.tryLock())
               {
                  return false;
               }

               try
               {
                  _registry.registerShared(getResources());
               }
               finally
               {
                  _shLock.unlock();
               }
            }
            finally
            {
               _exLock.unlock();
            }

            return true;
         }

         @Override
         protected boolean tryLockInternal(final long time, final TimeUnit unit)
               throws InterruptedException
         {
            final long nanosTimestamp = System.nanoTime();

            if (!_exLock.tryLock(time, unit))
            {
               return false;
            }

            long nanosLeft = unit.toNanos(time) - (System.nanoTime() - nanosTimestamp);
            try
            {
               while (!_registry.isExclusiveFree(getResources()))
               {
                  nanosLeft = _exCondition.awaitNanos(nanosLeft);

                  if (nanosLeft <= 0L)
                  {
                     return false;
                  }
               }

               if (!_shLock.tryLock(nanosLeft, TimeUnit.NANOSECONDS))
               {
                  return false;
               }

               try
               {
                  _registry.registerShared(getResources());
               }
               finally
               {
                  _shLock.unlock();
               }
            }
            finally
            {
               _exLock.unlock();
            }

            return true;
         }

         @Override
         protected void unlockInternal()
         {
            final Thread currentThread = Thread.currentThread();
            final int priority = currentThread.getPriority();

            try
            {
               currentThread.setPriority(Thread.MAX_PRIORITY);
            }
            catch (final SecurityException ignore)
            {
               // We may not change the thread priority! Bad luck!
            }

            _shLock.lock();
            try
            {
               _registry.unregisterShared(getResources());

               _shCondition.signalAll();
            }
            finally
            {
               _shLock.unlock();

               try
               {
                  currentThread.setPriority(priority);
               }
               catch (final SecurityException ignore)
               {
                  // We may not change the thread priority! Bad luck!
               }
            }
         }
      }

      protected class MultiWriteLockImpl extends MultiLockImpl
      {
         @Override
         protected void lockInternal()
         {
            // Get the exclusive lock
            _exLock.lock();
            try
            {
               // Wait until all requested resource are free
               while (!_registry.isExclusiveFree(getResources()))
               {
                  _exCondition.awaitUninterruptibly();
               }

               // Register the requested resources
               _registry.registerExclusive(getResources());
            }
            finally
            {
               // Don't forget to unlock
               _exLock.unlock();
            }

            // Get the shared lock
            _shLock.lock();
            try
            {
               // Wait until the requested resources are also freed in the
               // shared registry
               while (!_registry.isSharedFree(getResources()))
               {
                  _shCondition.awaitUninterruptibly();
               }

               // Don't forget to unlock
               _shLock.unlock();
            }
            // Error handling: When an unexpected problem occurs (i.e. Error or
            // RuntimeException)
            // the resources must be freed in the exclusive registry.
            // To avoid deadlocks, unlock the shared lock first.
            catch (final Error e)
            {
               _shLock.unlock();
               unlockInternal();
               throw e;
            }
            catch (final RuntimeException e)
            {
               _shLock.unlock();
               unlockInternal();
               throw e;
            }
         }

         @Override
         protected void lockInterruptiblyInternal() throws InterruptedException
         {
            // Get the exclusive lock
            _exLock.lockInterruptibly();
            try
            {
               // Wait until all requested resource are free
               while (!_registry.isExclusiveFree(getResources()))
               {
                  _exCondition.await();
               }

               // Register the requested resources
               _registry.registerExclusive(getResources());
            }
            finally
            {
               // Don't forget to unlock
               _exLock.unlock();
            }

            // Get the shared lock; if interrupted, don't forget to call
            // unlock()
            try
            {
               _shLock.lockInterruptibly();
            }
            catch (final InterruptedException e)
            {
               unlockInternal();
               throw e;
            }

            try
            {
               // Wait until the requested resources are also freed in the
               // shared registry
               while (!_registry.isSharedFree(getResources()))
               {
                  _shCondition.await();
               }

               // Don't forget to unlock
               _shLock.unlock();
            }
            // Error handling: When an unexpected problem occurs (i.e. Error or
            // RuntimeException)
            // the resources must be freed in the exclusive registry.
            // To avoid deadlocks, unlock the shared lock first.
            catch (final Error e)
            {
               _shLock.unlock();
               unlockInternal();
               throw e;
            }
            catch (final RuntimeException e)
            {
               _shLock.unlock();
               unlockInternal();
               throw e;
            }
            catch (final InterruptedException e)
            {
               // Safely try the unlock (lock state is unclear at this point)
               try
               {
                  _shLock.unlock();
               }
               catch (final IllegalMonitorStateException ex)
               {
               }

               unlockInternal();
               throw e;
            }
         }

         @Override
         protected boolean tryLockInternal()
         {
            // Get the exclusive lock
            if (!_exLock.tryLock())
            {
               return false;
            }

            try
            {
               // Wait until all requested resource are free
               if (!_registry.isExclusiveFree(getResources()))
               {
                  return false;
               }

               // Register the requested resources
               _registry.registerExclusive(getResources());
            }
            finally
            {
               // Don't forget to unlock
               _exLock.unlock();
            }

            // Get the shared lock
            if (!_shLock.tryLock())
            {
               unlockInternal();

               return false;
            }

            try
            {
               // Wait until the requested resources are also freed in the
               // shared registry
               if (!_registry.isSharedFree(getResources()))
               {
                  _shLock.unlock();
                  unlockInternal();

                  return false;
               }

               // Don't forget to unlock
               _shLock.unlock();
            }
            // Error handling: When an unexpected problem occurs (i.e. Error or
            // RuntimeException)
            // the resources must be freed in the exclusive registry.
            // To avoid deadlocks, unlock the shared lock first.
            catch (final Error e)
            {
               _shLock.unlock();
               unlockInternal();
               throw e;
            }
            catch (final RuntimeException e)
            {
               _shLock.unlock();
               unlockInternal();
               throw e;
            }

            return true;
         }

         @Override
         protected boolean tryLockInternal(final long time, final TimeUnit unit) throws InterruptedException
         {
            long nanoTimestamp = System.nanoTime();

            // Try to get the exclusive lock; abort, if not successful
            if (!_exLock.tryLock(time, unit))
            {
               return false;
            }

            // Calculate how much time is left; abort, if none
            long nanosLeft = unit.toNanos(time) - (System.nanoTime() - nanoTimestamp);
            if (nanosLeft <= 0L)
            {
               _exLock.unlock();

               return false;
            }

            try
            {
               // Wait until all requested resource are free
               while (!_registry.isExclusiveFree(getResources()))
               {
                  nanosLeft = _exCondition.awaitNanos(nanosLeft);

                  if (nanosLeft <= 0L)
                  {
                     return false;
                  }
               }

               // Register the requested resources
               _registry.registerExclusive(getResources());
            }
            finally
            {
               // Don't forget to unlock
               _exLock.unlock();
            }

            nanoTimestamp = System.nanoTime();

            // Get the shared lock; if interrupted or time is over, don't forget
            // to call unlock()
            try
            {
               if (nanosLeft <= 0L || !_shLock.tryLock(nanosLeft, TimeUnit.NANOSECONDS))
               {
                  unlockInternal();

                  return false;
               }
            }
            catch (final InterruptedException e)
            {
               unlockInternal();
               throw e;
            }

            // Check again, if we have time left; if not, unlock the shared lock
            // and call unlock()
            nanosLeft -= (System.nanoTime() - nanoTimestamp);
            if (nanosLeft <= 0L)
            {
               _shLock.unlock();
               unlockInternal();

               return false;
            }

            try
            {
               // Wait until the requested resources are also freed in the
               // shared registry
               while (!_registry.isSharedFree(getResources()))
               {
                  nanosLeft = _shCondition.awaitNanos(nanosLeft);

                  if (nanosLeft <= 0L)
                  {
                     _shLock.unlock();
                     unlockInternal();

                     return false;
                  }
               }

               // Don't forget to unlock
               _shLock.unlock();
            }
            // Error handling: When an unexpected problem occurs (i.e. Error or
            // RuntimeException)
            // the resources must be freed in the exclusive registry.
            // To avoid deadlocks, unlock the shared lock first.
            catch (final Error e)
            {
               _shLock.unlock();
               unlockInternal();
               throw e;
            }
            catch (final RuntimeException e)
            {
               _shLock.unlock();
               unlockInternal();
               throw e;
            }
            catch (final InterruptedException e)
            {
               // Safely try the unlock (lock state is unclear at this point)
               try
               {
                  _shLock.unlock();
               }
               catch (final IllegalMonitorStateException ex)
               {
               }

               unlockInternal();
               throw e;
            }

            return true;
         }

         @Override
         protected void unlockInternal()
         {
            final Thread currentThread = Thread.currentThread();
            final int priority = currentThread.getPriority();

            try
            {
               currentThread.setPriority(Thread.MAX_PRIORITY);
            }
            catch (final SecurityException ignore)
            {
               // We may not change the thread priority! Bad luck!
            }

            _exLock.lock();
            try
            {
               _registry.unregisterExclusive(getResources());

               _exCondition.signalAll();
            }
            finally
            {
               _exLock.unlock();

               try
               {
                  currentThread.setPriority(priority);
               }
               catch (final SecurityException ignore)
               {
                  // We may not change the thread priority! Bad luck!
               }
            }
         }
      }
   }
}
