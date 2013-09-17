package net.sf.javagimmicks.cdi.testing;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class WeldJUnit4TestRunner extends BlockJUnit4ClassRunner
{
   private Weld _weld;
   private WeldContainer _weldContainer;

   public WeldJUnit4TestRunner(final Class<?> klass) throws InitializationError
   {
      super(klass);
   }

   @Override
   protected Statement methodBlock(final FrameworkMethod method)
   {
      _weld = new Weld();
      _weldContainer = _weld.initialize();

      try
      {
         return super.methodBlock(method);
      }
      finally
      {
         _weldContainer = null;
         _weld.shutdown();
         _weld = null;
      }
   }

   @Override
   protected Object createTest() throws Exception
   {
      return _weldContainer.instance().select(getTestClass().getJavaClass()).get();
   }
}
