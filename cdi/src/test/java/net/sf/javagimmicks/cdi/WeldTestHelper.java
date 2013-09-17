package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

@Ignore
public class WeldTestHelper
{
   private Weld _weld;
   private WeldContainer _weldContainer;

   @Before
   public void setUpClass() throws Exception
   {
      _weld = new Weld();
      _weldContainer = _weld.initialize();
   }

   @After
   public void tearDown() throws Exception
   {
      _weldContainer = null;
      _weld.shutdown();
      _weld = null;
   }

   protected <E> E lookup(final Class<E> clazz, final Annotation... annotations)
   {
      return _weldContainer.instance().select(clazz, annotations).get();
   }

   protected <E> E lookup(final Class<E> clazz)
   {
      return _weldContainer.instance().select(clazz).get();
   }

   @SuppressWarnings("unchecked")
   protected <E> E lookup(final Annotation... annotations)
   {
      return (E) _weldContainer.instance().select(annotations).get();
   }

   protected <E> Instance<E> lookup(final TypeLiteral<E> typeLiteral, final Annotation... annotations)
   {
      return _weldContainer.instance().select(typeLiteral, annotations);
   }

}