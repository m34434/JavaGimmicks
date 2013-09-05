package net.sf.javagimmicks.swing;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import net.sf.javagimmicks.collections.AutoSkippingQueueDecorator;

public class BoundedEventQueue
{
   private final AutoSkippingQueueDecorator<Runnable> _internalQueue;
   private WorkerThread _workerThread;
   
   public BoundedEventQueue(int maxSize, int skipCount)
   {
      _internalQueue = new AutoSkippingQueueDecorator<Runnable>(maxSize, skipCount);
   }
   
   public BoundedEventQueue(int maxSize)
   {
      _internalQueue = new AutoSkippingQueueDecorator<Runnable>(maxSize);
   }
   
   public void startWorking()
   {
      if(_workerThread == null || !_workerThread.isAlive())
      {
         _workerThread = new WorkerThread();
         _workerThread.start();
      }
   }
   
   public void stopWorking()
   {
      if(_workerThread != null && _workerThread.isAlive())
      {
         _workerThread.interrupt();
         _workerThread = null;
      }
      
      synchronized (_internalQueue)
      {
         _internalQueue.clear();
      }
   }
   
   public void invoke(Runnable action)
   {
      synchronized (_internalQueue)
      {
         _internalQueue.offer(action);
         _internalQueue.notify();
      }
   }
   
   @Override
   protected void finalize() throws Throwable
   {
      stopWorking();
   }

   private class WorkerThread extends Thread
   {
      public WorkerThread()
      {
         super("BoundedEventQueue_Worker");
      }

      public void run()
      {
         while(!Thread.interrupted())
         {
            Runnable action;
            
            synchronized (_internalQueue)
            {
               while(_internalQueue.isEmpty())
               {
                  try
                  {
                     _internalQueue.wait();
                  }
                  catch (InterruptedException e)
                  {
                     return;
                  }
               }
               
               action = _internalQueue.poll();
            }

            try
            {
               SwingUtilities.invokeAndWait(action);
            }
            catch (InterruptedException e)
            {
               return;
            }
            catch (InvocationTargetException e)
            {
            }
         }
      }
   }
}
