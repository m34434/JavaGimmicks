package net.sf.javagimmicks.testing;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
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
public abstract class BlockingTestWorker<R> implements Callable<R>
{
   static final String MSG_CHECK_PAUSE = "Pause operations may only be called by the Thread which is executing 'call()'!";
   static final String MSG_CHECK_ADMIN = "Admin operations ('signal', 'finish', 'awaitNextBreak', 'awaitExecution') may only be called while 'call()' is executing and from outside of the Thread which is executing 'call()'!";

   private final ReentrantLock lock = new ReentrantLock(true);
   private final Condition cond = lock.newCondition();

   private boolean pausing = false;
   private boolean finishMode;
   private Thread executingThread;

   abstract protected R doWork() throws Exception;

   @Override
   public final R call() throws Exception
   {
      lock.lock();
      if (executingThread != null)
      {
         throw new IllegalStateException("Only one thread can call this worker at the same time!");
      }

      executingThread = Thread.currentThread();
      finishMode = false;

      try
      {
         return doWork();
      }
      finally
      {
         executingThread = null;
         lock.unlock();
      }
   }

   /**
    * Returns if the worker is currently pausing.
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
    * Returns if the worker is currently executing.
    * 
    * @return if the worker is currently executing
    */
   public boolean isExecuting()
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
    * Returns if the worker is currently working (it is {@link #isExecuting()
    * executing} but not {@link #isPausing() pausing}).
    * 
    * @return if the worker is currently working
    */
   public boolean isWorking()
   {
      lock.lock();
      try
      {
         return isExecuting() && !isPausing();
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
      checkAdminOperation(true);

      if (!finishMode)
      {
         doSignal();
      }

      return this;
   }

   /**
    * Signals the worker to finish by continuing if currently paused and
    * ignoring any further pausing points.
    * 
    * @throws IllegalStateException
    *            if no thread is currently executing the worker or the worker
    *            thread itself calls this operation
    */
   public BlockingTestWorker<R> finish()
   {
      checkAdminOperation(true);

      finishMode = true;

      if (isPausing())
      {
         doSignal();
      }

      return this;
   }

   public BlockingTestWorker<R> awaitNextBreak()
   {
      checkAdminOperation(true);

      doAwaitNextBreak();

      return this;
   }

   public BlockingTestWorker<R> awaitExecution()
   {
      checkAdminOperation(false);

      while (!isExecuting())
      {
         Thread.yield();
      }

      return this;
   }

   public Runnable asRunnable(final Consumer<Exception> exceptionHandler)
   {
      return new RunnableCallableAdapter<R>(this, exceptionHandler);
   }

   public Runnable asRunnableIgnoringExceptions()
   {
      return RunnableCallableAdapter.ignoringExceptions(this);
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

   protected boolean pause(final long amount, final TimeUnit timeUnit) throws InterruptedException
   {
      checkPause();
      if (finishMode)
      {
         return true;
      }

      pausing = true;
      try
      {
         return cond.await(amount, timeUnit);
      }
      finally
      {
         pausing = false;
      }
   }

   protected void pause()
   {
      checkPause();
      if (finishMode)
      {
         return;
      }

      pausing = true;
      try
      {
         // System.out.println("Before await");
         cond.awaitUninterruptibly();
         // System.out.println("After await");
      }
      finally
      {
         pausing = false;
      }
   }

   protected void pauseInterruptibly() throws InterruptedException
   {
      checkPause();
      if (finishMode)
      {
         return;
      }

      pausing = true;
      try
      {
         cond.await();
      }
      finally
      {
         pausing = false;
      }
   }

   private void doAwaitNextBreak()
   {
      while (isWorking())
      {
         Thread.yield();
      }
   }

   private void checkPause()
   {
      if (!lock.isHeldByCurrentThread() || executingThread != Thread.currentThread())
      {
         throw new IllegalStateException(
               MSG_CHECK_PAUSE);
      }
   }

   private void checkAdminOperation(final boolean enforceExecuting)
   {
      if (lock.isHeldByCurrentThread() || (enforceExecuting && executingThread == null)
            || executingThread == Thread.currentThread())
      {
         throw new IllegalStateException(MSG_CHECK_ADMIN);
      }
   }

   private void doSignal()
   {
      if (!isPausing())
      {
         return;
      }

      lock.lock();
      try
      {
         cond.signalAll();
      }
      finally
      {
         lock.unlock();
      }

      doAwaitNextBreak();
   }
}