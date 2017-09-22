package net.sf.javagimmicks.concurrent.locks;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import net.sf.javagimmicks.testing.BlockingTestWorker;

public class MultiLockProviderTest
{
   private static final MultiLockProvider<String> PROVIDER = MultiLockProviderFactory
         .<String> getHashBasedInstance()
         .get();

   private ExecutorService e;

   @After
   public void shutdown()
   {
      if (e != null && !e.isShutdown())
      {
         e.shutdownNow();
      }
   }

   @Test
   public void testBasic() throws Exception
   {
      e = Executors.newFixedThreadPool(3);

      final WriteLockWorker ab = new WriteLockWorker("a", "b");
      final WriteLockWorker a = new WriteLockWorker("a");
      final WriteLockWorker b = new WriteLockWorker("b");

      final Future<Void> fab = ab.submit(e);
      final Future<Void> fa = a.submit(e);
      final Future<Void> fb = b.submit(e);

      ab.awaitPausing();
      a.awaitPausing();
      b.awaitPausing();

      // No worker has passed the first gate, so no lock is acquired
      Assert.assertFalse(ab.locked);
      Assert.assertFalse(a.locked);
      Assert.assertFalse(b.locked);

      // "ab" may pass - it will get the lock and reach the second pause point
      ab.signal().awaitPausing();
      Assert.assertTrue(ab.locked);

      // "a" may now pass - it will get stuck when waiting for it's lock =>
      // because "ab" is currently locked
      a.signal().awaitBlocking();
      Assert.assertFalse(a.locked);

      // "b" may now pass - it will get stuck when waiting for it's lock =>
      // because "ab" is currently locked
      b.signal().awaitBlocking();
      Assert.assertFalse(b.locked);

      // "ab" may now pass the second pause point
      // i.e. release the lock and finish
      ab.signal();
      fab.get();
      Assert.assertFalse(ab.locked);

      // "a" should now be able to continue and get it's lock for "a"
      a.awaitPausing();
      Assert.assertTrue(a.locked);

      // "b" should now be able to continue and get it's lock for "b"
      b.awaitPausing();
      Assert.assertTrue(b.locked);

      System.out.println("=================");
   }

   private static class WriteLockWorker extends BlockingTestWorker<Void>
   {
      private final List<String> resources;
      private final MultiReadWriteLock<String> lock;
      private final MultiLock<String> writeLock;

      private boolean locked;

      public WriteLockWorker(final String... resources)
      {
         this.resources = Arrays.asList(resources);
         lock = PROVIDER.newLock(this.resources);
         writeLock = lock.writeLock();
      }

      @Override
      protected Void doWork() throws Exception
      {
         System.out.printf("%s : (1) About to pause!%n", this.resources);
         pauseInterruptibly();
         System.out.printf("%s : (2) Now continuing!%n", this.resources);

         System.out.printf("%s : (3) About to get write lock!%n", this.resources);
         writeLock.lock();
         locked = true;
         System.out.printf("%s : (4) Got write lock!%n", this.resources);

         try
         {
            System.out.printf("%s : (5) About to pause!%n", this.resources);
            pauseInterruptibly();
            System.out.printf("%s : (6) Now continuing!%n", this.resources);
         }
         finally
         {
            System.out.printf("%s : (7) About to release write lock!%n", this.resources);
            writeLock.unlock();
            locked = false;
            System.out.printf("%s : (8) Released write lock!%n", this.resources);
         }

         return null;
      }
   }
}
