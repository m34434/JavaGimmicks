package net.sf.javagimmicks.swing;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import net.sf.javagimmicks.collections.AutoDroppingQueueDecorator;

/**
 * A wrapper around the Swing event queue with a limited size that drops events
 * if there are too much - useful for example for continuous status updates of
 * some long-running action where it does not matter, if single status updates
 * are lost or not.
 * <p>
 * This class uses an {@link AutoDroppingQueueDecorator} for controlling drop
 * behavior.
 */
public class BoundedEventQueue
{
   private final AutoDroppingQueueDecorator<Runnable> _internalQueue;
   private WorkerThread _workerThread;

   /**
    * Creates a new instance with the given maximum queue size and drop count.
    * 
    * @param maxSize
    *           the maximum size of the internal queue before events are dropped
    * @param dropCount
    *           the number of events to drop if the queue size is exceeded
    */
   public BoundedEventQueue(final int maxSize, final int dropCount)
   {
      _internalQueue = new AutoDroppingQueueDecorator<Runnable>(maxSize, dropCount);
   }

   /**
    * Creates a new instance with the given maximum queue that completely
    * flushes upon reaching this size.
    * 
    * @param maxSize
    *           the maximum size of the internal queue before events are dropped
    */
   public BoundedEventQueue(final int maxSize)
   {
      _internalQueue = new AutoDroppingQueueDecorator<Runnable>(maxSize);
   }

   /**
    * Starts to process the internal queue be forwarding all events to the Swing
    * event queue.
    * 
    * @see SwingUtilities#invokeAndWait(Runnable)
    */
   public void startWorking()
   {
      if (_workerThread == null || !_workerThread.isAlive())
      {
         _workerThread = new WorkerThread();
         _workerThread.start();
      }
   }

   /**
    * Stops forwarding the event within the internal queue to the Swing event
    * queue.
    * 
    * @see SwingUtilities#invokeAndWait(Runnable)
    */
   public void stopWorking()
   {
      if (_workerThread != null && _workerThread.isAlive())
      {
         _workerThread.interrupt();
         _workerThread = null;
      }

      synchronized (_internalQueue)
      {
         _internalQueue.clear();
      }
   }

   /**
    * Submits a new event (as {@link Runnable}) to this instance.
    * 
    * @param action
    *           the event to process within the Swing event queue
    */
   public void invoke(final Runnable action)
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

      @Override
      public void run()
      {
         while (!Thread.interrupted())
         {
            Runnable action;

            synchronized (_internalQueue)
            {
               while (_internalQueue.isEmpty())
               {
                  try
                  {
                     _internalQueue.wait();
                  }
                  catch (final InterruptedException e)
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
            catch (final InterruptedException e)
            {
               return;
            }
            catch (final InvocationTargetException e)
            {
            }
         }
      }
   }
}
