package net.sf.javagimmicks.testing;

import java.lang.Thread.State;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.javagimmicks.util.Consumer;
import net.sf.javagimmicks.util.RunnableCallableAdapter;

/**
 * A helper for implementing test worker threads that can pause execution at
 * defined points and await signals from outside when to continue.
 * 
 * @param <R>
 *           the return type of the worker operation
 */
public abstract class BlockingTestWorker<R>
{
   static final String MSG_CHECK_PAUSE = "Pause operations may only be called by the Thread which is executing the worker!";
   static final String MSG_CHECK_ADMIN = "Control operations ('signal', 'finish', 'awaitPausing', 'awaitBlocking', 'awaitExecution') may only be called while the worker is executed by another Thread!";

   private final ReentrantLock lock = new ReentrantLock(true);
   private final Condition pauseCondition = lock.newCondition();
   private final Condition endPauseCondition = lock.newCondition();

   private boolean pausing = false;
   private boolean finishMode;
   private Thread executingThread;

   private long waitDuration;
   private TimeUnit waitTimeUnit;

   /**
    * Creates a new instance with the given default wait period for blocking
    * control operations ({@link #awaitBlocking()}, {@link #awaitPausing()},
    * {@link #awaitExecution(Future)}, {@link #awaitExecution()}).
    * 
    * @param waitDuration
    *           the default wait duration
    * @param waitTimeUnit
    *           the {@link TimeUnit} for the default wait duration
    * @see #setDefaultWaitTime(long, TimeUnit)
    * @see #awaitBlocking()
    * @see #awaitPausing()
    * @see #awaitExecution(Future)
    * @see #awaitExecution()
    */
   public BlockingTestWorker(final long waitDuration, final TimeUnit waitTimeUnit)
   {
      setDefaultWaitTime(waitDuration, waitTimeUnit);
   }

   /**
    * Creates a new instance with a default wait period of 500 milliseconds for
    * blocking control operations ({@link #awaitBlocking()},
    * {@link #awaitPausing()}, {@link #awaitExecution(Future)},
    * {@link #awaitExecution()}).
    * 
    * @param waitDuration
    *           the default
    * @param waitTimeUnit
    * @see #awaitBlocking()
    * @see #awaitPausing()
    * @see #awaitExecution(Future)
    * @see #awaitExecution()
    */
   public BlockingTestWorker()
   {
      this(500, TimeUnit.MILLISECONDS);
   }

   abstract protected R doWork() throws Exception;

   /**
    * Returns if the worker is currently pausing.
    * <p>
    * A worker is pausing if it has called one of the internal pause operations
    * ({@link #pause(long, TimeUnit)} or {@link #pauseInterruptibly()}) and
    * since then neither {@link #signal()} nor {@link #finish()} have been
    * called and if the worker was not previously set to {@link #isFinishing()
    * finishing mode} by calling {@link #finish()}.
    * 
    * @return if the worker is currently pausing
    */
   public boolean isPausing()
   {
      lock.lock();
      try
      {
         return pausing;
      }
      finally
      {
         lock.unlock();
      }
   }

   /**
    * Returns if the worker is currently being executed by some thread.
    * 
    * @return if the worker is currently being executed
    */
   public boolean isExecuted()
   {
      lock.lock();
      try
      {
         return executingThread != null;
      }
      finally
      {
         lock.unlock();
      }
   }

   /**
    * Returns if the worker is currently being executed by some thread and this
    * thread is in a {@link Thread#getState() wait state} ({@link State#BLOCKED}
    * or {@link State#WAITING}}.
    * 
    * @return if the worker is currently being executed but blocked or waiting
    */
   public boolean isBlocked()
   {
      lock.lock();
      try
      {
         return executingThread != null
               && (executingThread.getState() == State.BLOCKED || executingThread.getState() == State.WAITING);
      }
      finally
      {
         lock.unlock();
      }
   }

   /**
    * Returns if the worker was set to finish mode by calling {@link #finish()}
    * since it's last execution start.
    * <p>
    * See {@link #finish()} for more details about the finish mode.
    * 
    * @return if the worker was set to finish mode
    * @see #finish()
    */
   public boolean isFinishing()
   {
      lock.lock();
      try
      {
         return finishMode;
      }
      finally
      {
         lock.unlock();
      }
   }

   /**
    * Signals the worker to continue from pausing up to the next potential
    * pausing point.
    * 
    * @throws IllegalStateException
    *            if no thread is currently executing the worker or the worker
    *            thread itself calls this operation
    */
   public BlockingTestWorker<R> signal()
   {
      lock.lock();
      try
      {
         checkAdminOperation(true);

         if (!finishMode && isPausing())
         {
            pauseCondition.signal();
            endPauseCondition.awaitUninterruptibly();
         }
      }
      finally
      {
         lock.unlock();
      }

      return this;
   }

   /**
    * Sets the worker to the finish mode.
    * <p>
    * In finish mode, any call to the internal pause operations
    * ({@link #pause(long, TimeUnit)} or {@link #pauseInterruptibly()}) will no
    * longer block, so that the worker will finish directly without any other
    * pausing.
    * <p>
    * Further, if the worker is {@link #isPausing()} when this operation is
    * being called it will perform a {@link #signal() signaling} to wake it up.
    * <p>
    * The operation {@link #isFinishing()} can be used to check if the worker is
    * in finish mode.
    * <p>
    * Finish mode is automatically disabled when an instance the worker created
    * or is being executed further times.
    * 
    * @throws IllegalStateException
    *            if no thread is currently executing the worker or the worker
    *            thread itself calls this operation
    * @see #isFinishing()
    */
   public BlockingTestWorker<R> finish()
   {
      lock.lock();
      try
      {
         checkAdminOperation(true);

         finishMode = true;

         if (isPausing())
         {
            pauseCondition.signal();
            endPauseCondition.awaitUninterruptibly();
         }
      }
      finally
      {
         lock.unlock();
      }

      return this;
   }

   /**
    * Waits the given amount of time for the worker to enter a
    * {@link #isBlocked() blocking state}.
    * 
    * @param duration
    *           the wait duration
    * @param timeUnit
    *           the {@link TimeUnit} for the wait duration
    * @return the instance itself
    * @throws TimeoutException
    *            if the given wait time is exceeded
    * @throws IllegalArgumentException
    *            if duration < 0 or timeUnit is <code>null</code>
    */
   public BlockingTestWorker<R> awaitBlocking(final long duration, final TimeUnit timeUnit) throws TimeoutException
   {
      checkWaitTime(duration, timeUnit);

      lock.lock();
      try
      {
         checkAdminOperation(true);
      }
      finally
      {
         lock.unlock();
      }

      final long threshold = System.nanoTime() + timeUnit.toNanos(duration);
      while (!isBlocked())
      {
         if (System.nanoTime() > threshold)
         {
            throw new TimeoutException("Waiting for worker to block timed out!");
         }

         Thread.yield();
      }

      return this;
   }

   /**
    * Waits the default amount of time for the worker to enter a
    * {@link #isBlocked() blocking state}.
    * <p>
    * The default wait time can be adjusted during instance creation or
    * afterwards by calling {@link #setDefaultWaitTime(long, TimeUnit)}.
    * 
    * @return the instance itself
    * @throws TimeoutException
    *            if the given wait time is exceeded
    * @see #setDefaultWaitTime(long, TimeUnit)
    */
   public BlockingTestWorker<R> awaitBlocking() throws TimeoutException
   {
      return awaitBlocking(waitDuration, waitTimeUnit);
   }

   /**
    * Waits the given amount of time for the worker to enter a
    * {@link #isPausing() pausing state}.
    * 
    * @param duration
    *           the wait duration
    * @param timeUnit
    *           the {@link TimeUnit} for the wait duration
    * @return the instance itself
    * @throws TimeoutException
    *            if the given wait time is exceeded
    * @throws IllegalArgumentException
    *            if duration < 0 or timeUnit is <code>null</code>
    */
   public BlockingTestWorker<R> awaitPausing(final long duration, final TimeUnit timeUnit) throws TimeoutException
   {
      checkWaitTime(duration, timeUnit);

      lock.lock();
      try
      {
         checkAdminOperation(true);
      }
      finally
      {
         lock.unlock();
      }

      final long threshold = System.nanoTime() + timeUnit.toNanos(duration);
      while (!isPausing())
      {
         if (System.nanoTime() > threshold)
         {
            throw new TimeoutException("Waiting for worker to pause timed out!");
         }

         Thread.yield();
      }

      return this;
   }

   /**
    * Waits the default amount of time for the worker to enter a
    * {@link #isPausing() pausing state}.
    * <p>
    * The default wait time can be adjusted during instance creation or
    * afterwards by calling {@link #setDefaultWaitTime(long, TimeUnit)}.
    * 
    * @return the instance itself
    * @throws TimeoutException
    *            if the given wait time is exceeded
    * @see #setDefaultWaitTime(long, TimeUnit)
    */
   public BlockingTestWorker<R> awaitPausing() throws TimeoutException
   {
      return awaitPausing(waitDuration, waitTimeUnit);
   }

   public BlockingTestWorker<R> awaitExecution(final long duration, final TimeUnit timeUnit, final Future<R> f)
         throws TimeoutException
   {
      checkWaitTime(duration, timeUnit);

      if (f != null && f.isDone())
      {
         return this;
      }

      lock.lock();
      try
      {
         checkAdminOperation(false);
      }
      finally
      {
         lock.unlock();
      }

      final long threshold = System.nanoTime() + timeUnit.toNanos(duration);

      while (!isExecuted() && (f == null || !f.isDone()))
      {
         if (System.nanoTime() > threshold)
         {
            throw new TimeoutException("Waiting for worker to begin execution timed out!");
         }

         Thread.yield();
      }

      return this;
   }

   public BlockingTestWorker<R> awaitExecution(final Future<R> f) throws TimeoutException
   {
      return awaitExecution(waitDuration, waitTimeUnit, f);
   }

   public BlockingTestWorker<R> awaitExecution() throws TimeoutException
   {
      return awaitExecution(null);
   }

   public BlockingTestWorker<R> setDefaultWaitTime(final long duration, final TimeUnit timeUnit)
   {
      checkWaitTime(duration, timeUnit);

      waitDuration = duration;
      waitTimeUnit = timeUnit;

      return this;
   }

   public Callable<R> asCallable()
   {
      return new Callable<R>() {
         @Override
         public R call() throws Exception
         {
            lock.lock();
            try
            {
               if (executingThread != null)
               {
                  throw new IllegalStateException("Only one thread can call this worker at the same time!");
               }

               executingThread = Thread.currentThread();
               finishMode = false;
            }
            finally
            {
               lock.unlock();
            }

            try
            {
               return doWork();
            }
            finally
            {
               lock.lock();
               executingThread = null;
               lock.unlock();
            }
         }
      };
   }

   public Runnable asRunnable(final Consumer<Exception> exceptionHandler)
   {
      return new RunnableCallableAdapter<R>(asCallable(), exceptionHandler);
   }

   public Runnable asRunnableIgnoringExceptions()
   {
      return RunnableCallableAdapter.ignoringExceptions(asCallable());
   }

   public Thread asThread(final String threadName, final Consumer<Exception> exceptionHandler)
   {
      return new Thread(asRunnable(exceptionHandler), threadName);
   }

   public Thread asThread(final Consumer<Exception> exceptionHandler)
   {
      return new Thread(asRunnable(exceptionHandler));
   }

   public Thread asThreadIgnoringExceptions(final String threadName)
   {
      return new Thread(asRunnableIgnoringExceptions(), threadName);
   }

   public Thread asThreadIgnoringExceptions()
   {
      return new Thread(asRunnableIgnoringExceptions());
   }

   public Future<R> submit(final ExecutorService e) throws TimeoutException
   {
      final Future<R> result = e.submit(asCallable());

      awaitExecution(result);

      return result;
   }

   protected boolean pause(final long duration, final TimeUnit timeUnit) throws InterruptedException
   {
      checkPause();

      final long beginNanos = System.nanoTime();

      if (!lock.tryLock(duration, timeUnit))
      {
         return false;
      }

      final long timeRemainingNanos = timeUnit.toNanos(duration) - beginNanos;

      try
      {
         if (finishMode)
         {
            return true;
         }

         pausing = true;
         try
         {
            return pauseCondition.await(timeRemainingNanos, TimeUnit.NANOSECONDS);
         }
         finally
         {
            pausing = false;
            endPauseCondition.signalAll();
         }
      }
      finally
      {
         lock.unlock();
      }
   }

   protected void pauseInterruptibly() throws InterruptedException
   {
      lock.lockInterruptibly();
      try
      {
         checkPause();
         if (finishMode)
         {
            return;
         }

         pausing = true;
         try
         {
            pauseCondition.await();
         }
         finally
         {
            pausing = false;
            endPauseCondition.signalAll();
         }
      }
      finally
      {
         lock.unlock();
      }
   }

   private void checkPause()
   {
      if (executingThread != Thread.currentThread())
      {
         throw new IllegalStateException(MSG_CHECK_PAUSE);
      }
   }

   private void checkAdminOperation(final boolean enforceExecuting)
   {
      if ((enforceExecuting && executingThread == null)
            || executingThread == Thread.currentThread())
      {
         throw new IllegalStateException(MSG_CHECK_ADMIN);
      }
   }

   private void checkWaitTime(final long duration, final TimeUnit timeUnit)
   {
      if (duration < 0)
      {
         throw new IllegalArgumentException("Duration must be greater or equal to '0'!");
      }

      Objects.requireNonNull(timeUnit, "TimeUnit may not be null!");
   }
}