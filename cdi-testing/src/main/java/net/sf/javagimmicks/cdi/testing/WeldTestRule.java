package net.sf.javagimmicks.cdi.testing;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.inject.Qualifier;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * A JUnit {@link TestRule} implementation which sets up a local CDI context
 * (using Weld-SE internally) for each test run. CDI beans can be retrieved via
 * the additional methods provided by this class.
 * <p>
 * <b>Note:</b> if you don't need a special {@link Runner} for your test (other
 * than {@link BlockJUnit4ClassRunner}), you can use
 * {@link WeldJUnit4TestRunner} instead of this class which enables CDI
 * injection directly into the test class using @{@link Inject}.
 * 
 * @see WeldJUnit4TestRunner
 */
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

   /**
    * Provides a CDI {@link Instance} object which can be used to lookup any
    * other CDI bean.
    * 
    * @return an {@link Instance} providing access to the whole CDI context
    */
   public Instance<Object> getCDIInstances()
   {
      return _weldContainer.instance();
   }

   /**
    * Looks up a CDI bean in the CDI context with the given type and
    * {@link Qualifier} annotations.
    * 
    * @param clazz
    *           the type of the bean to lookup
    * @param annotations
    *           the {@link Qualifier} annotations the bean should have
    * @return the resulting bean
    * @throws RuntimeException
    *            if the lookup fails or is ambiguous or an annotation is
    *            specified more than once
    * @see Instance#select(Class, Annotation...)
    */
   public <E> E lookup(final Class<E> clazz, final Annotation... annotations)
   {
      return _weldContainer.instance().select(clazz, annotations).get();
   }

   /**
    * Looks up a CDI bean in the CDI context with the given type. and
    * 
    * @param clazz
    *           the type of the bean to lookup
    * @return the resulting bean
    * @throws RuntimeException
    *            if the lookup fails or is ambiguous
    * @see Instance#select(Class, Annotation...)
    */
   public <E> E lookup(final Class<E> clazz)
   {
      return _weldContainer.instance().select(clazz).get();
   }

   /**
    * Looks up a CDI bean in the CDI context for the given {@link Qualifier}
    * annotations.
    * 
    * @param annotations
    *           the {@link Qualifier} annotations the bean should have
    * @return the resulting bean
    * @throws RuntimeException
    *            if the lookup fails or is ambiguous or an annotation is
    *            specified more than once
    * @see Instance#select(Annotation...)
    */
   @SuppressWarnings("unchecked")
   public <E> E lookup(final Annotation... annotations)
   {
      return (E) _weldContainer.instance().select(annotations).get();
   }

   /**
    * Looks up a CDI bean in the CDI context with the given type and
    * {@link Qualifier} annotations.
    * 
    * @param typeLiteral
    *           the type of the bean to lookup specified as {@link TypeLiteral}
    *           instance
    * @param annotations
    *           the {@link Qualifier} annotations the bean should have
    * @return the resulting bean
    * @throws RuntimeException
    *            if the lookup fails or is ambiguous or an annotation is
    *            specified more than once
    * @see Instance#select(TypeLiteral, Annotation...)
    */
   public <E> Instance<E> lookup(final TypeLiteral<E> typeLiteral, final Annotation... annotations)
   {
      return _weldContainer.instance().select(typeLiteral, annotations);
   }
}
