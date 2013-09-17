package net.sf.javagimmicks.cdi.testing;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.rules.ExternalResource;

public class WeldTestRule extends ExternalResource
{
   private Weld _weld;
   private WeldContainer _weldContainer;

   @Override
   protected void before() throws Throwable
   {
      _weld = new Weld();
      _weldContainer = _weld.initialize();
   }

   @Override
   protected void after()
   {
      _weldContainer = null;
      _weld.shutdown();
      _weld = null;
   }

   public <E> E lookup(final Class<E> clazz, final Annotation... annotations)
   {
      return _weldContainer.instance().select(clazz, annotations).get();
   }

   public <E> E lookup(final Class<E> clazz)
   {
      return _weldContainer.instance().select(clazz).get();
   }

   @SuppressWarnings("unchecked")
   public <E> E lookup(final Annotation... annotations)
   {
      return (E) _weldContainer.instance().select(annotations).get();
   }

   public <E> Instance<E> lookup(final TypeLiteral<E> typeLiteral, final Annotation... annotations)
   {
      return _weldContainer.instance().select(typeLiteral, annotations);
   }
}
