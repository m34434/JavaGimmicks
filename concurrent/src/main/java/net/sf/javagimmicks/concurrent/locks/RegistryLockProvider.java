package net.sf.javagimmicks.concurrent.locks;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class RegistryLockProvider<K> implements MultiLockProvider<K>, Serializable
{
   private static final long serialVersionUID = 6807627151240655773L;

   protected final LockRegistry<K> _registry;
   protected LockStatisticsImpl _stats;

   protected final Lock _exLock = new ReentrantLock();
   protected final Condition _exCondition = _exLock.newCondition();

   protected final Lock _shLock = new ReentrantLock();
   protected final Condition _shCondition = _shLock.newCondition();

   public RegistryLockProvider(final LockRegistry<K> registry)
   {
      _registry = registry;
   }

   @Override
   public final MultiReadWriteLock<K> newLock(final Iterable<K> resources)
   {
      return new MultiReadWriteLockImpl(resources);
   }

   @Override
   public MultiReadWriteLock<K> newLock(final K... resources)
   {
      return newLock(Arrays.asList(resources));
   }

   @Override
   public LockStatistics<K> getStatistics()
   {
      // Lazy getter
      if (_stats == null)
      {
         synchronized (this)
         {
            if (_stats == null)
            {
               _stats = new LockStatisticsImpl();
            }
         }
      }

      return _stats;
   }

   protected class LockStatisticsImpl implements LockStatistics<K>
   {
      @Override
      public void dump(final Writer out) throws IOException
      {
         try
         {
            _exLock.lockInterruptibly();
         }
         catch (final InterruptedException e)
         {
            // If this thread is interrupted from outside, we just skip dumping
            return;
         }

         try
         {
            _registry.dump(out);
         }
         finally
         {
            _exLock.unlock();
         }
      }
   }

   protected class MultiReadWriteLockImpl implements MultiReadWriteLock<K>
   {
      protected final List<K> _resources;
      protected final MultiReadLockImpl _readLock;
      protected final MultiWriteLockImpl _writeLock;

      protected MultiReadWriteLockImpl(final Iterable<K> resources)
      {
         final List<K> internalResourceList = new ArrayList<K>();

         // Predicate out null resources
         for (final K resource : resources)
         {
            if (resource != null)
            {
               internalResourceList.add(resource);
            }
         }

         _resources = Collections.unmodifiableList(internalResourceList);

         _readLock = new MultiReadLockImpl();
         _writeLock = new MultiWriteLockImpl();
      }

      @Override
      public Collection<K> getResourceIds()
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
         public final Collection<K> getResourceIds()
         {
            return MultiReadWriteLockImpl.this.getResourceIds();
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
               while (!_registry.isExclusiveFree(getResourceIds()))
               {
                  _exCondition.awaitUninterruptibly();
               }

               _shLock.lock();
               try
               {
                  _registry.registerShared(getResourceIds());
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
               while (!_registry.isExclusiveFree(getResourceIds()))
               {
                  _exCondition.await();
               }

               _shLock.lockInterruptibly();
               try
               {
                  _registry.registerShared(getResourceIds());
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
               if (!_registry.isExclusiveFree(getResourceIds()))
               {
                  return false;
               }

               if (!_shLock.tryLock())
               {
                  return false;
               }

               try
               {
                  _registry.registerShared(getResourceIds());
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
               while (!_registry.isExclusiveFree(getResourceIds()))
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
                  _registry.registerShared(getResourceIds());
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
               _registry.unregisterShared(getResourceIds());

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
               while (!_registry.isExclusiveFree(getResourceIds()))
               {
                  _exCondition.awaitUninterruptibly();
               }

               // Register the requested resources
               _registry.registerExclusive(getResourceIds());
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
               while (!_registry.isSharedFree(getResourceIds()))
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
               while (!_registry.isExclusiveFree(getResourceIds()))
               {
                  _exCondition.await();
               }

               // Register the requested resources
               _registry.registerExclusive(getResourceIds());
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
               while (!_registry.isSharedFree(getResourceIds()))
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
               if (!_registry.isExclusiveFree(getResourceIds()))
               {
                  return false;
               }

               // Register the requested resources
               _registry.registerExclusive(getResourceIds());
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
               if (!_registry.isSharedFree(getResourceIds()))
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
               while (!_registry.isExclusiveFree(getResourceIds()))
               {
                  nanosLeft = _exCondition.awaitNanos(nanosLeft);

                  if (nanosLeft <= 0L)
                  {
                     return false;
                  }
               }

               // Register the requested resources
               _registry.registerExclusive(getResourceIds());
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
               while (!_registry.isSharedFree(getResourceIds()))
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
               _registry.unregisterExclusive(getResourceIds());

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
