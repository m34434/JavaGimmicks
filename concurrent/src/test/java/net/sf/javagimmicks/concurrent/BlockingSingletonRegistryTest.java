package net.sf.javagimmicks.concurrent;

import static org.junit.Assert.assertSame;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import net.sf.javagimmicks.lang.Factory;
import net.sf.javagimmicks.lang.PrototypeFactory;
import net.sf.javagimmicks.testing.MultiThreadedTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BlockingSingletonRegistryTest
{
   private static final int THREAD_COUNT = 1000;

   // The instance to test
   private BlockingSingletonRegistry _testee;

   // Two (singleton) test objects
   private TestSingletonClass1 _testInstance1;
   private TestSingletonClass2 _testInstance2;

   @Before
   public void setUp() throws Exception
   {
      // Prepare the instance to test ...
      _testee = new BlockingSingletonRegistry();

      // ... and the singletons used for testing
      _testInstance1 = new TestSingletonClass1();
      _testInstance2 = new TestSingletonClass2();
   }

   @After
   public void tearDown() throws Exception
   {
      // Unregister and clean up the test singleton instances and our testee
      if (_testInstance1 != null)
      {
         _testee.remove(_testInstance1);
         _testInstance1 = null;
      }

      if (_testInstance2 != null)
      {
         _testee.remove(_testInstance2);
         _testInstance2 = null;
      }

      _testee = null;
   }

   @SuppressWarnings("unchecked")
   @Test
   public void testMultiThreaded() throws Exception
   {
      // Prepare two Factories for the two test worker types
      final Factory<TestWorker<TestSingletonClass1>> factory1 = createFactory(_testInstance1);
      final Factory<TestWorker<TestSingletonClass2>> factory2 = createFactory(_testInstance2);
      
      // Create the main worker - it waits a second and then propagates the singleton instances
      final Callable<Void> mainWorker = new Callable<Void>()
      {
         @Override
         public Void call() throws Exception
         {
            // We simulate some business logic (to setup our singleton instances)
            Thread.sleep(1000);

            // Finally, we propagate the singleton instances
            _testee.set(_testInstance1);
            _testee.set(_testInstance2);
            
            return null;
         }
      };
      
      // Create a MultiThreadedTestHelper to run the workers
      final MultiThreadedTestHelper<Void> testHelper = new MultiThreadedTestHelper<Void>();
      testHelper.addWorkers(THREAD_COUNT, factory1, factory2);
      
      testHelper.executeWorkers(mainWorker, 10, TimeUnit.SECONDS);
   }

   public static class TestSingletonClass1
   {}

   public static class TestSingletonClass2
   {}

   private class TestWorker<T> implements Callable<Void>, Cloneable
   {
      private final T _refInstance;

      public TestWorker(T refInstance)
      {
         _refInstance = refInstance;
      }
      
      @Override
      public Object clone() throws CloneNotSupportedException
      {
         return super.clone();
      }

      public Void call() throws Exception
      {
         // Get the singleton class (the one of the given reference instance)
         @SuppressWarnings("unchecked")
         final Class<T> clazz = (Class<T>) _refInstance.getClass();

         // Now the critical call: get the singleton instance for the given
         // class from the container
         final T instance = _testee.get(clazz);

         // Check if it's the same object as the reference object
         assertSame("Fetched singleton instance is different to the reference one", _refInstance, instance);
         
         return null;
      }
   }
   
   private <T> Factory<TestWorker<T>> createFactory(T testInstance)
   {
      return new PrototypeFactory<TestWorker<T>>(new TestWorker<T>(testInstance));
   }
}
