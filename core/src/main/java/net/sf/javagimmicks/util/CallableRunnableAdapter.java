package net.sf.javagimmicks.util;

import java.util.concurrent.Callable;

/**
 * An implementation of {@link Callable} that wraps around a given
 * {@link Runnable}.
 */
public class CallableRunnableAdapter implements Callable<Void>
{
   private final Runnable _runnable;

   /**
    * Creates a new instance around the given {@link Runnable}.
    * 
    * @param runnable
    *           the {@link Runnable} to wrap
    */
   public CallableRunnableAdapter(final Runnable runnable)
   {
      _runnable = runnable;
   }

   @Override
   public Void call()
   {
      _runnable.run();
      return null;
   }
}