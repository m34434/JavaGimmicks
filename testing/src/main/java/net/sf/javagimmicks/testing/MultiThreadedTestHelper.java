package net.sf.javagimmicks.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.sf.javagimmicks.util.CallableRunnableAdapter;
import net.sf.javagimmicks.util.Factory;

/**
 * This class helps to execute multi-threaded unit tests in a manner of
 * executing a number of workers in parallel. Results (positive or negative) are
 * collected into a {@link TestResult} object which can be used to examine what
 * happened within the workers. Depending on the value of {@link #isAutoFail()}
 * automatically a respective {@link AssertionError} will be thrown if any of
 * the workers ran into any problem
 * <p>
 * Additionally a "main" worker can be submitted when starting a test run which
 * will then be executed within the main test thread after all other workers
 * have been started.
 * 
 * @param <R>
 *           the type of result objects the workers can produce
 */
public class MultiThreadedTestHelper<R>
{
   private static String LINE_SEP = System.getProperty("line.separator");

   private List<Callable<R>> _workers = new LinkedList<Callable<R>>();

   private boolean _autoFail;

   /**
    * Creates a new instance with the given auto-fail mode
    * 
    * @param autoFail
    *           the active-state of the auto-fail mode
    * @see #setAutoFail(boolean)
    */
   public MultiThreadedTestHelper(final boolean autoFail)
   {
      _autoFail = autoFail;
   }

   /**
    * Create a new instance with auto-fail set to <code>true</code>
    * 
    * @see #setAutoFail(boolean)
    */
   public MultiThreadedTestHelper()
   {
      this(true);
   }

   /**
    * Returns if auto-fail is active
    * 
    * @return if auto-fail is active
    * @see #setAutoFail(boolean)
    */
   public boolean isAutoFail()
   {
      return _autoFail;
   }

   /**
    * Activates or deactivates the auto-fail mode. If active a resepctive
    * {@link AssertionError} will automatically thrown after test runs if any
    * worker failed
    * 
    * @param autoFail
    *           the desired active-state of the auto-fail mode
    */
   public void setAutoFail(final boolean autoFail)
   {
      _autoFail = autoFail;
   }

   /**
    * Adds a bunch of workers to be executed within test runs. Workers will be
    * started in the same order as they were added.
    * 
    * @param workers
    *           an {@link Iterable} of workers represented as {@link Callable}
    *           objects (see {@link CallableRunnableAdapter} for adding
    *           {@link Runnable} objects instead)
    * @see CallableRunnableAdapter
    */
   public void addWorkers(final Iterable<? extends Callable<R>> workers)
   {
      for (final Callable<R> worker : workers)
      {
         if (worker != null)
         {
            _workers.add(worker);
         }
      }
   }

   /**
    * Convenience method for massively adding workers. Worker are not specified
    * directly, instead a number {@link Factory} instances can be provided
    * together with a count that determines, how many instances each
    * {@link Factory} should add here.
    * <p>
    * This means, the number of workers added is the multiplication of the given
    * <code>count</code> parameter and the number of given {@link Factory}
    * instances
    * 
    * @param count
    *           the number of instances each provided {@link Factory} should
    *           create
    * @param factories
    *           any number of {@link Factory} instances that should create
    *           workers
    */
   public void addWorkers(final int count, final Iterable<Factory<? extends Callable<R>>> factories)
   {
      for (int i = 0; i < count; ++i)
      {
         for (final Factory<? extends Callable<R>> factory : factories)
         {
            if (factory != null)
            {
               final Callable<R> worker = factory.create();
               if (worker != null)
               {
                  _workers.add(worker);
               }
            }
         }
      }
   }

   /**
    * Convenience method for {@link #addWorkers(int, Iterable)} that allows
    * specifying {@link Factory} instances as var-args list
    * 
    * @param count
    *           the number of instances each provided {@link Factory} should
    *           create
    * @param factories
    *           any number of {@link Factory} instances that should create
    *           workers
    * @see #addWorkers(int, Factory...)
    */
   public void addWorkers(final int count, final Factory<? extends Callable<R>>... factories)
   {
      addWorkers(count, Arrays.asList(factories));
   }

   /**
    * Starts a test run with all registered workers and a "main" worker.
    * <p>
    * <b>ATTENTION:</b> The calling thread will be blocked until all worker have
    * finished.
    * 
    * @param mainWorker
    *           the "main" worker which will be started within the main test
    *           thread after all "sub" workers have been started
    * @return the {@link TestResult} containing all test result details
    *         (including any thrown errors)
    * @throws AssertionError
    *            if anything in the test run went wrong and
    *            {@link #isAutoFail()} is <code>true</code>
    * @see #isAutoFail()
    */
   public <F> TestResult<F, R> executeWorkers(final Callable<F> mainWorker) throws AssertionError
   {
      return executeWorkers(mainWorker, new LatchWaitStrategy()
      {
         @Override
         public boolean await(final CountDownLatch latch) throws InterruptedException
         {
            latch.await();
            return true;
         }
      });
   }

   /**
    * Starts a test run with all registered workers and a "main" worker aborting
    * the test after a given time.
    * 
    * @param mainWorker
    *           the "main" worker which will be started within the main test
    *           thread after all "sub" workers have been started
    * @param timeout
    *           the amount of time to wait with the given {@link TimeUnit} until
    *           the test run is aborted
    * @param unit
    *           the unit of time to wait until the test run is aborted
    * @return the {@link TestResult} containing all test result details
    *         (including any thrown errors)
    * @throws AssertionError
    *            if anything in the test run went wrong and
    *            {@link #isAutoFail()} is <code>true</code>
    * @see #isAutoFail()
    */
   public <F> TestResult<F, R> executeWorkers(final Callable<F> mainWorker, final long timeout, final TimeUnit unit)
         throws AssertionError
   {
      return executeWorkers(mainWorker, new LatchWaitStrategy()
      {
         @Override
         public boolean await(final CountDownLatch latch) throws InterruptedException, AssertionError
         {
            return latch.await(timeout, unit);
         }
      });
   }

   /**
    * Convenience method for {@link #executeWorkers(Callable)} but taking the
    * main worker as a {@link Runnable}
    * 
    * @see #executeWorkers(Callable)
    */
   public TestResult<Void, R> executeWorkers(final Runnable mainWorker) throws AssertionError
   {
      return executeWorkers(mainWorker != null ? new CallableRunnableAdapter(mainWorker) : (Callable<Void>) null);
   }

   /**
    * Convenience method for {@link #executeWorkers(Callable, long, TimeUnit)}
    * but taking the main worker as a {@link Runnable}
    * 
    * @see #executeWorkers(Callable, long, TimeUnit)
    */
   public TestResult<Void, R> executeWorkers(final Runnable mainWorker, final long timeout, final TimeUnit unit)
         throws AssertionError
   {
      return executeWorkers(mainWorker != null ? new CallableRunnableAdapter(mainWorker) : (Callable<Void>) null,
            timeout, unit);
   }

   /**
    * Convenience method for {@link #executeWorkers(Callable)} that will run the
    * test without a "main" worker
    * 
    * @see #executeWorkers(Callable)
    */
   public TestResult<Void, R> executeWorkers() throws AssertionError
   {
      return executeWorkers((Callable<Void>) null);
   }

   /**
    * Convenience method for {@link #executeWorkers(Callable, long, TimeUnit)}
    * that will run the test without a "main" worker
    * 
    * @see #executeWorkers(Callable, long, TimeUnit)
    */
   public TestResult<Void, R> executeWorkers(final long timeout, final TimeUnit unit) throws AssertionError
   {
      return executeWorkers((Callable<Void>) null, timeout, unit);
   }

   /**
    * Testers can override this method to provide their own implementation of
    * {@link ExecutorService} to run the "sub" workers. By default the
    * {@link ExecutorService} is created by calling
    * {@link Executors#newFixedThreadPool(int)}
    * 
    * @param size
    *           the number of workers that have to be executed
    * @return an instance of {@link ExecutorService} that should execute the
    *         workers
    */
   protected ExecutorService getExecutorService(final int size)
   {
      return Executors.newFixedThreadPool(size);
   }

   private List<Worker<R>> setupWorkers(final CountDownLatch latch)
   {
      final List<Worker<R>> workers = new ArrayList<Worker<R>>(_workers.size());

      // Wrap the worker Callables into a respective internal Worker (which will
      // take care about collecting results)
      for (final Callable<R> workerCallable : _workers)
      {
         workers.add(new Worker<R>(workerCallable, latch));
      }
      return workers;
   }

   private List<Future<WorkerResult<R>>> runAll(final ExecutorService executor, final List<Worker<R>> workers)
   {
      final List<Future<WorkerResult<R>>> result = new ArrayList<Future<WorkerResult<R>>>(workers.size());

      // Submit all the workers into the ExecutorService collecting their
      // Futures
      for (final Worker<R> worker : workers)
      {
         result.add(executor.submit(worker));
      }

      return result;
   }

   private <F> TestResult<F, R> executeWorkers(final Callable<F> mainWorker, final LatchWaitStrategy latchWaitStrategy)
         throws AssertionError
   {
      final int workerCount = _workers.size();

      // Let us get an ExecutorService for the current size of workers
      final ExecutorService executor = getExecutorService(workerCount);

      // Create a CountDownlatch with the same size to be later able to join
      // after their termination
      final CountDownLatch latch = new CountDownLatch(workerCount);

      // Create the workers and hand them over our latch (they will report to it
      // after termination)
      final List<Worker<R>> workers = setupWorkers(latch);

      // Start the workers with the ExecutorService
      final List<Future<WorkerResult<R>>> workerResults = runAll(executor, workers);

      // Prepare the result object
      final TestResult<F, R> result = new TestResult<F, R>();

      try
      {
         // If we have a "Main Worker", we call it now directly
         if (mainWorker != null)
         {
            // Execute it with our normal worker helper object
            final WorkerResult<F> mainWorkerResult = new Worker<F>(mainWorker, null).call();

            // Adapt the "Main Worker's" result to our result
            result.setMainWorkerResult(mainWorkerResult);
         }

         // Now it's time to join - we wait for the Latch with the given
         // strategy
         // But we can skip this if the main worker already failed
         if (result.isSuccess())
         {
            try
            {
               if (!latchWaitStrategy.await(latch))
               {
                  result._mainWorkerResult._assertionError = new AssertionError(
                        "Workers did not terminate within given time");
               }
            }
            catch (final InterruptedException e)
            {
               result._mainWorkerResult._interruptedException = e;
            }
         }
      }

      // Ensure the Executor to be shutdown (it might not have been shutdown
      // e.g. because workers are still running or main worker failed)
      finally
      {
         if (!executor.isShutdown())
         {
            executor.shutdownNow();
         }
      }

      // Collect and register the results of all already finished workers
      addWorkerResults(result, workerResults);

      // If auto-fail is activated and one or more of the workers failed, throw
      // an according AssertionError
      if (_autoFail)
      {
         result.assertSuccessful();
      }

      return result;
   }

   private <F> void addWorkerResults(final TestResult<F, R> result, final List<Future<WorkerResult<R>>> results)
         throws AssertionError
   {
      // Walk through the Futures, collect their results and add them to the
      // main result object
      for (final ListIterator<Future<WorkerResult<R>>> iter = results.listIterator(); iter.hasNext();)
      {
         final Future<WorkerResult<R>> workerFuture = iter.next();

         // There is only a result if the execution ended
         if (workerFuture.isDone())
         {
            try
            {
               result.addWorkerResult(iter.previousIndex(), workerFuture.get());
            }

            // Theoretically we ensured that we won't get here
            catch (final Exception e)
            {
               throw new AssertionError("Unexpected exception while getting worker results: " + e.toString());
            }
         }
      }
   }

   /**
    * Represents the results of one single worker.
    * <p>
    * There can be one of four results
    * <ul>
    * <li>The resulting object produced by the worker if it did not fail - get
    * via {@link #getResult()}</li>
    * <li>An {@link AssertionError} if an assertion within the worker failed -
    * get via {@link #getAssertionError()}</li>
    * <li>An {@link InterruptedException} if the worker was interrupted - get
    * via {@link #getInterruptedException()}</li>
    * <li>Any other {@link Throwable} if it was thrown by the worker - get via
    * {@link #getOtherError()}</li>
    * </ul>
    * 
    * @param <R>
    *           the type of result objects the worker could produce
    */
   public static class WorkerResult<R>
   {
      protected AssertionError _assertionError;
      protected InterruptedException _interruptedException;
      protected Throwable _otherError;

      protected R _result;

      protected WorkerResult()
      {}

      /**
       * Returns an according fail message if the worker did not finish
       * successfully
       * 
       * @return the according fail message or <code>null</code> if the worker
       *         finished successfully
       * @see #isSuccess()
       */
      public String buildFailMessage()
      {
         if (_assertionError != null)
         {
            return _assertionError.toString();
         }
         else if (_interruptedException != null)
         {
            return _interruptedException.toString();
         }
         else if (_otherError != null)
         {
            return _otherError.toString();
         }
         else
         {
            return null;
         }
      }

      /**
       * Return the {@link AssertionError} that might have been thrown by the
       * worker
       * 
       * @return the resulting {@link AssertionError} or <code>null</code> if
       *         none occurred
       * @see #isSuccess()
       */
      public AssertionError getAssertionError()
      {
         return _assertionError;
      }

      /**
       * Return the {@link InterruptedException} that might have been thrown by
       * the worker
       * 
       * @return the resulting {@link InterruptedException} or <code>null</code>
       *         if none occurred
       * @see #isSuccess()
       */
      public InterruptedException getInterruptedException()
      {
         return _interruptedException;
      }

      /**
       * Return the non-{@link AssertionError} {@link Throwable} that might have
       * been thrown by the worker
       * 
       * @return the resulting {@link Throwable} or <code>null</code> if none
       *         occurred
       * @see #isSuccess()
       */
      public Throwable getOtherError()
      {
         return _otherError;
      }

      /**
       * Return the result object that the worker might have been produced
       * 
       * @return the result object or <code>null</code> if the worker failed
       * @see #isSuccess()
       */
      public R getResult()
      {
         return _result;
      }

      /**
       * Checks if the associated worker was successfully finished
       * 
       * @return if the associated worker was successfully finished
       */
      public boolean isSuccess()
      {
         return _assertionError == null && _interruptedException == null && _otherError == null;
      }
   }

   /**
    * Acts as a container for the results of a test run which consists in
    * particular of the results of the "main" worker and the results of all
    * "sub" workers.
    * 
    * @param <F>
    *           the result type of the "main" worker
    * @param <R>
    *           the result type of the "sub" workers
    */
   public static class TestResult<F, R>
   {
      protected WorkerResult<F> _mainWorkerResult = new WorkerResult<F>();

      protected final SortedMap<Integer, WorkerResult<R>> _workerResults = new TreeMap<Integer, WorkerResult<R>>();
      protected final SortedMap<Integer, WorkerResult<R>> _failedWorkerResults = new TreeMap<Integer, WorkerResult<R>>();

      protected TestResult()
      {}

      /**
       * Checks if the whole test run was successful i.e. all workers (including
       * the "main" one) were successful
       * 
       * @return if the whole test run was successful
       */
      public boolean isSuccess()
      {
         return _mainWorkerResult.isSuccess() && _failedWorkerResults.isEmpty();
      }

      /**
       * Builds a composite fail message from all the wokers' results
       * 
       * @return the composed fail message
       */
      public String buildFailMessage()
      {
         final StringBuilder result = new StringBuilder();
         if (!_mainWorkerResult.isSuccess())
         {
            result.append("Main worker failed with reason: ").append(_mainWorkerResult.buildFailMessage())
                  .append(LINE_SEP);
         }

         for (final Entry<Integer, WorkerResult<R>> entry : _failedWorkerResults.entrySet())
         {
            result.append("Worker thread ").append(entry.getKey()).append(" failed with reason: ")
                  .append(entry.getValue().buildFailMessage()).append(LINE_SEP);
         }

         return result.toString();
      }

      /**
       * Throws an {@link AssertionError} if the test was not successful.
       * <p>
       * Calls {@link #buildFailMessage()} for getting the error message
       * 
       * @throws AssertionError
       *            the resulting
       * @see #buildFailMessage()
       * @see #isSuccess()
       */
      public void assertSuccessful() throws AssertionError
      {
         if (!isSuccess())
         {
            throw new AssertionError(buildFailMessage());
         }
      }

      /**
       * Returns the result of the main worker's call as {@link TestResult}
       * object
       * 
       * @return the result of the main worker's call
       * @see WorkerResult
       */
      public WorkerResult<F> getMainWorkerResult()
      {
         return _mainWorkerResult;
      }

      /**
       * Return the results of the sub workers as a {@link SortedMap} which
       * identifies each worker by a unique numbers (matching the order in which
       * the workers were started)
       * 
       * @return a {@link SortedMap} containing all {@link WorkerResult}s of all
       *         workers
       * @see WorkerResult
       */
      public SortedMap<Integer, WorkerResult<R>> getWorkerResults()
      {
         return Collections.unmodifiableSortedMap(_workerResults);
      }

      /**
       * Return the results of the all sub workers that failed as a
       * {@link SortedMap} which identifies each worker by a unique numbers
       * (matching the order in which the workers were started)
       * 
       * @return a {@link SortedMap} containing all {@link WorkerResult}s of all
       *         failed workers
       * @see WorkerResult
       */
      public SortedMap<Integer, WorkerResult<R>> getFailedWorkerResults()
      {
         return Collections.unmodifiableSortedMap(_failedWorkerResults);
      }

      protected void setMainWorkerResult(final WorkerResult<F> mainWorkerResult)
      {
         _mainWorkerResult = mainWorkerResult;
      }

      protected void addWorkerResult(final int id, final WorkerResult<R> result)
      {
         _workerResults.put(id, result);

         if (!result.isSuccess())
         {
            _failedWorkerResults.put(id, result);
         }
      }
   }

   private interface LatchWaitStrategy
   {
      boolean await(CountDownLatch latch) throws InterruptedException;
   }

   private static class Worker<R> implements Callable<WorkerResult<R>>
   {
      private final Callable<R> _delegate;
      private final CountDownLatch _latch;

      public Worker(final Callable<R> delegate, final CountDownLatch latch)
      {
         _delegate = delegate;
         _latch = latch;
      }

      @Override
      public WorkerResult<R> call()
      {
         final WorkerResult<R> result = new WorkerResult<R>();
         try
         {
            result._result = _delegate.call();
         }
         catch (final AssertionError e)
         {
            result._assertionError = e;
         }
         catch (final Throwable t)
         {
            result._otherError = t;
         }
         finally
         {
            if (_latch != null)
            {
               _latch.countDown();
            }
         }

         return result;
      }
   }
}