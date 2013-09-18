package net.sf.javagimmicks.cdi.testing;

import javax.inject.Inject;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * A special JUnit4 test runner (extending {@link BlockJUnit4ClassRunner}) which
 * enables CDI injection (using @{@link Inject}) within the test class.
 * <p>
 * Internally it uses Weld-SE which sets up a CDI container in the local
 * environment without the need for an application server.
 * <p>
 * <b>Note:</b> a separate Container lifecycle (startup/shutdown) is created
 * <b>for each test method</b>.
 * <p>
 * <b>Note:</b> if you cannot use this {@link Runner} (e.g. because you need to
 * use a different one from {@link BlockJUnit4ClassRunner}) you can use
 * {@link WeldTestRule} instead which enables CDI as well for each test method
 * but forces CDI lookups to be done via lookup methods (not via @{@link Inject}
 * ).
 * 
 * @see WeldTestRule
 */
public class WeldJUnit4TestRunner extends BlockJUnit4ClassRunner
{
   private final ThreadLocal<WeldContainer> _weldContainer = new ThreadLocal<WeldContainer>();

   /**
    * Creates a WeldJUnit4TestRunner to run {@code klass}
    * 
    * @throws InitializationError
    *            if the test class is malformed.
    */
   public WeldJUnit4TestRunner(final Class<?> klass) throws InitializationError
   {
      super(klass);
   }

   @Override
   protected Statement methodBlock(final FrameworkMethod method)
   {
      // Setup Weld - store the WeldContainer within the Thread
      final Weld weld = new Weld();
      _weldContainer.set(weld.initialize());

      // Build the original statement (this is a command object that will
      // execute the test method)
      // This will finally call createTest() which looks up the test class via
      // CDI instead of instantiating it via reflection
      final Statement innerStatement = super.methodBlock(method);

      // The reference to the WeldContainer is no longer needed - cleanup
      _weldContainer.set(null);

      // Wrap a new statement (== command) around the original one that takes
      // care about shutting down Weld after test execution
      return new Statement() {

         @Override
         public void evaluate() throws Throwable
         {
            try
            {
               innerStatement.evaluate();
            }
            finally
            {
               weld.shutdown();
            }
         }
      };
   }

   @Override
   protected Object createTest() throws Exception
   {
      // Do not instantiate the test class via reflection - instead look it up
      // via CDI
      return _weldContainer.get().instance().select(getTestClass().getJavaClass()).get();
   }
}
