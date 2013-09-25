package net.sf.javagimmicks.concurrent.locks;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.Format;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

public class MultiLockProviderTest
{
   private static final int NUM_RESOURCES = 20;
   private static final int NUM_WORKERS = 50;
   private static final long WAIT_TIME = 1000;
   private static final long WORK_TIME = 250;
   private static final int FRACTION_EX = 10;
   private static final int FRACTION_LOCKS = 4;

   private static final PrintStream LOG;
   private static final Random RANDOM = new Random();
   private static final List<Character> RESOURCES = new ArrayList<Character>(NUM_RESOURCES);
   private static final Format FORMAT = new MessageFormat("{0,number,00}");
   private static final MultiLockProvider<Character> PROVIDER = MultiLockProviderFactory
         .<Character> getHashBasedInstance()
         .create();

   static
   {
      for (int i = 0; i < NUM_RESOURCES; ++i)
      {
         RESOURCES.add((char) ('a' + i));
      }

      PrintStream log;
      try
      {
         log = new PrintStream(new FileOutputStream("status.log"));
      }
      catch (final FileNotFoundException e)
      {
         log = System.out;
      }
      LOG = log;
   }

   public static void main(final String[] args)
   {
      final List<Thread> threads = new ArrayList<Thread>(NUM_WORKERS);
      for (int i = 1; i <= NUM_WORKERS; ++i)
      {
         // Thread thread = new Thread(new StrictLockWorker(i));
         final Thread thread = new Thread(new TimeoutTryWorker(i));
         // Thread thread = new Thread(new TryWorker(i));
         threads.add(thread);
         thread.start();
      }

      JOptionPane.showMessageDialog(null, "Click to exit");

      for (final Thread thread : threads)
      {
         thread.interrupt();
      }
   }

   protected static class TimeoutTryWorker extends Worker
   {
      public TimeoutTryWorker(final int id)
      {
         super(id);
      }

      @Override
      protected boolean doLock(final MultiLock<Character> lock) throws InterruptedException
      {
         return lock.tryLock(WAIT_TIME, TimeUnit.MILLISECONDS);
      }
   }

   protected static class TryWorker extends Worker
   {
      public TryWorker(final int id)
      {
         super(id);
      }

      @Override
      protected boolean doLock(final MultiLock<Character> lock) throws InterruptedException
      {
         return lock.tryLock();
      }
   }

   protected static class StrictLockWorker extends Worker
   {
      public StrictLockWorker(final int id)
      {
         super(id);
      }

      @Override
      protected boolean doLock(final MultiLock<Character> lock) throws InterruptedException
      {
         lock.lock();

         return true;
      }
   }

   protected abstract static class Worker implements Runnable
   {
      protected final int _id;

      public Worker(final int id)
      {
         _id = id;
      }

      protected abstract boolean doLock(MultiLock<Character> lock) throws InterruptedException;

      @Override
      public void run()
      {
         while (!Thread.currentThread().isInterrupted())
         {
            final List<Character> resources = getResources();
            final boolean shared = RANDOM.nextInt(FRACTION_EX) != 0;

            final MultiReadWriteLock<Character> rwLock = PROVIDER.newLock(resources);
            final MultiLock<Character> lock = shared ? rwLock.readLock() : rwLock.writeLock();

            log(shared, resources, "ready");
            try
            {
               long time = System.currentTimeMillis();

               if (doLock(lock))
               {
                  time = System.currentTimeMillis() - time;

                  log(shared, resources, "lock (" + time + ")");

                  try
                  {
                     Thread.sleep(WORK_TIME);
                  }
                  catch (final InterruptedException e)
                  {
                     log(shared, resources, "aborted work");
                     lock.unlock();
                     log(shared, resources, "unlock");
                     break;
                  }

                  lock.unlock();
                  log(shared, resources, "unlock");
               }
               else
               {
                  log(shared, resources, "failed");
               }
            }
            catch (final InterruptedException e)
            {
               log(shared, resources, "interrupted");
               break;
            }
         }
      }

      @Override
      public String toString()
      {
         return "Worker" + FORMAT.format(new Object[] { _id });
      }

      protected List<Character> getResources()
      {
         final List<Character> result = new ArrayList<Character>(NUM_RESOURCES);

         for (final Character c : RESOURCES)
         {
            if (RANDOM.nextInt(FRACTION_LOCKS) == 0)
            {
               result.add(c);
            }
         }

         return result;
      }

      protected void log(final boolean shared, final List<Character> resources, final String message)
      {
         final String output = new StringBuilder()
               .append(toString())
               .append(" ")
               .append(shared ? "SH " : "EX ")
               .append(message)
               .append(" ")
               .append(resources.toString())
               .toString();

         LOG.println(output);
      }
   }
}
