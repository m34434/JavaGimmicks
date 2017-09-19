package net.sf.javagimmicks.util;

import java.util.concurrent.Callable;

/**
 * An adapter that wraps a given {@link Callable} into a {@link Runnable}
 * implementation (with the need of an additional exception handler) with
 * additional accessors to check and get the result of the last call.
 * 
 * @param <R>
 *           the return type of the wrapped {@link Callable}
 */
public class RunnableCallableAdapter<R> implements Runnable
{
   private final Callable<R> callable;
   private final Consumer<Exception> exceptionHandler;

   private boolean hasResult = false;
   private R lastResult;

   /**
    * Creates a new instance wrapping the given {@link Callable} and calling the
    * given {@link Consumer exception handler} in case of thrown
    * {@link Exception}s.
    * 
    * @param callable
    *           the {@link Callable} to wrap
    * @param exceptionHandler
    *           the exception handler called when the invoked
    *           {@link Callable#call()} throw an {@link Exception}
    */
   public RunnableCallableAdapter(final Callable<R> callable, final Consumer<Exception> exceptionHandler)
   {
      this.callable = callable;
      this.exceptionHandler = exceptionHandler;
   }

   /**
    * Returns if the last invocation of the wrapped {@link Callable#call()} was
    * successful and returned a (possibly <code>null</code>) result.
    * 
    * @return if the last invocation of the wrapped {@link Callable#call()} was
    *         successful
    */
   public boolean hasResult()
   {
      return hasResult;
   }

   /**
    * Returns - if present - the result of the last invocation of the wrapped
    * {@link Callable#call()} (which possibly could be <code>null</code>).
    * 
    * If the last call was not successful (i.e. {@link #hasResult()} returns
    * <code>false</code>) always <code>null</code> is returned.
    * 
    * @return the result of the last call to {@link Callable#call()} or
    *         <code>null</code> if the call was unsuccessful
    */
   public R getLastResult()
   {
      return lastResult;
   }

   @Override
   public void run()
   {
      hasResult = false;
      lastResult = null;
      try
      {
         lastResult = callable.call();
         hasResult = true;
      }
      catch (final Exception e)
      {
         exceptionHandler.accept(e);
      }
   }

   /**
    * Creates a {@link RunnableCallableAdapter} instance around the given
    * {@link Callable} that will ignore any Exceptions thrown by invoking the
    * inner {@link Callable#call()} method.
    * 
    * @param callable
    *           the {@link Callable} to wrap
    * @return the resulting {@link RunnableCallableAdapter}
    * @param <R>
    *           the return type of the wrapped {@link Callable}
    */
   public static <R> RunnableCallableAdapter<R> ignoringExceptions(final Callable<R> callable)
   {
      return new RunnableCallableAdapter<R>(callable, new IgnoringExceptionHandler());
   }

   private static class IgnoringExceptionHandler implements Consumer<Exception>
   {
      @Override
      public void accept(final Exception instance)
      {}
   }
}
