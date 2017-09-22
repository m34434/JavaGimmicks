package net.sf.javagimmicks.testing;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class BlockingTestWorkerTest
{
   private ExecutorService e;

   @After
   public void cleanup()
   {
      if (e != null && !e.isShutdown())
      {
         e.shutdown();
      }
   }

   @Test
   public void testSignalBasic() throws InterruptedException, ExecutionException, TimeoutException
   {
      e = newSingleThreadExecutor();

      final TestThreeStepWorker w = new TestThreeStepWorker();
      final Future<String> f1 = w.submit(e);

      Assert.assertFalse(f1.isDone());

      w.awaitPausing();
      Assert.assertFalse(f1.isDone());
      Assert.assertEquals("1", w.getState());

      w.signal().awaitPausing();
      Assert.assertFalse(f1.isDone());
      Assert.assertEquals("2", w.getState());

      w.signal();
      Assert.assertEquals("3", f1.get(5, SECONDS));

      try
      {
         w.signal();
         Assert.fail("IllegalStateException expected!");
      }
      catch (final IllegalStateException ex)
      {
         Assert.assertEquals(BlockingTestWorker.MSG_CHECK_ADMIN, ex.getMessage());
      }
   }

   @Test
   public void testFinishBasic() throws InterruptedException, ExecutionException, TimeoutException
   {
      e = newSingleThreadExecutor();

      final TestThreeStepWorker w = new TestThreeStepWorker();
      final Future<String> f1 = w.submit(e);

      Assert.assertFalse(f1.isDone());

      w.finish();
      Assert.assertEquals("3", f1.get(5, SECONDS));

      try
      {
         w.signal();
         Assert.fail("IllegalStateException expected!");
      }
      catch (final IllegalStateException ex)
      {
         Assert.assertEquals(BlockingTestWorker.MSG_CHECK_ADMIN, ex.getMessage());
      }
   }

   @Test
   public void testIllegalPauseCall() throws InterruptedException, ExecutionException, TimeoutException
   {
      e = newSingleThreadExecutor();

      final TestThreeStepWorker w = new TestThreeStepWorker();
      w.submit(e);

      try
      {
         w.pauseInterruptibly();
         Assert.fail("IllegalStateException expected!");
      }
      catch (final IllegalStateException ex)
      {
         Assert.assertEquals(BlockingTestWorker.MSG_CHECK_PAUSE, ex.getMessage());
      }
      finally
      {
         w.finish();
      }
   }

   @Test
   public void testIllegalWorkerCalls() throws InterruptedException, ExecutionException, TimeoutException
   {
      e = newSingleThreadExecutor();

      testIllegalWorkerCall(new IllegalWorkerSignal());
      testIllegalWorkerCall(new IllegalWorkerFinish());
      testIllegalWorkerCall(new IllegalWorkerAwaitNextPausing());
      testIllegalWorkerCall(new IllegalWorkerAwaitNextBlocking());
      testIllegalWorkerCall(new IllegalWorkerAwaitExecution());
   }

   private void testIllegalWorkerCall(final BlockingTestWorker<Void> w) throws InterruptedException, TimeoutException
   {
      final Future<Void> f = w.submit(e);

      try
      {
         f.get();
         Assert.fail("ExecutionException expected!");
      }
      catch (final ExecutionException e)
      {
         Assert.assertTrue(e.getCause() instanceof IllegalStateException);
         Assert.assertEquals(BlockingTestWorker.MSG_CHECK_ADMIN, e.getCause().getMessage());
      }
   }

   private static class TestThreeStepWorker extends BlockingTestWorker<String>
   {
      private String state;

      public String getState()
      {
         return state;
      }

      @Override
      protected String doWork() throws Exception
      {
         state = "1";
         pauseInterruptibly();

         state = "2";
         pauseInterruptibly();

         state = "3";

         return state;
      }
   }

   private static class IllegalWorkerSignal extends BlockingTestWorker<Void>
   {
      @Override
      protected Void doWork() throws Exception
      {
         signal();

         return null;
      }
   }

   private static class IllegalWorkerFinish extends BlockingTestWorker<Void>
   {
      @Override
      protected Void doWork() throws Exception
      {
         finish();

         return null;
      }
   }

   private static class IllegalWorkerAwaitNextPausing extends BlockingTestWorker<Void>
   {
      @Override
      protected Void doWork() throws Exception
      {
         awaitPausing();

         return null;
      }
   }

   private static class IllegalWorkerAwaitNextBlocking extends BlockingTestWorker<Void>
   {
      @Override
      protected Void doWork() throws Exception
      {
         awaitBlocking();

         return null;
      }
   }

   private static class IllegalWorkerAwaitExecution extends BlockingTestWorker<Void>
   {
      @Override
      protected Void doWork() throws Exception
      {
         awaitExecution();

         return null;
      }
   }
}