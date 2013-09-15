package net.sf.javagimmicks.concurrent;

import java.util.concurrent.Callable;

public class CallableRunnableAdapter implements Callable<Void>
{
   private final Runnable _runnable;
      
   public CallableRunnableAdapter(Runnable runnable)
   {
      _runnable = runnable;
   }

   public Void call()
   {
      _runnable.run();
      return null;
   }
}