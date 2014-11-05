package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;

import javax.inject.Qualifier;

import net.sf.javagimmicks.util.ConstantFactory;
import net.sf.javagimmicks.util.Supplier;

/**
 * A {@link Supplier} implementation that creates new instances via CDI lookup
 * mechanisms. Lookups can be configured using the {@link InjectionSpec} API.
 */
public class InjectionFactory<E> extends CDIAware implements Supplier<E>
{
   private final Supplier<InjectionSpec<E>> _injectionFactory;

   /**
    * Creates a new instance based on a given {@link Supplier} for
    * {@link InjectionSpec}s.
    * <p>
    * Every call to {@link #get()} will result in a respective call to
    * {@link InjectionSpec#getInstance(javax.enterprise.inject.spi.BeanManager)}.
    * 
    * @param injectionFactory
    *           the {@link Supplier} to use for creating injection information
    *           via {@link InjectionSpec} instances.
    */
   public InjectionFactory(final Supplier<InjectionSpec<E>> injectionFactory)
   {
      _injectionFactory = injectionFactory;
   }

   /**
    * Convenience constructor to {@link #InjectionFactory(Supplier)} that always
    * uses the specified {@link InjectionSpec} for looking up objects instead of
    * creating a new {@link InjectionSpec} all the time for each call to
    * {@link #get()}.
    * 
    * @param injection
    *           the {@link InjectionSpec} to use for bean creation
    */
   public InjectionFactory(final InjectionSpec<E> injection)
   {
      this(new ConstantFactory<InjectionSpec<E>>(injection));
   }

   /**
    * Convenience constructor to {@link #InjectionFactory(InjectionSpec)} that
    * uses the given {@link Class} and {@link Annotation}s as injection
    * information.
    * 
    * @param clazz
    *           the {@link Class} of objects to lookup
    * @param annotations
    *           the {@link Qualifier} annotations of the bean to lookup
    */
   public InjectionFactory(final Class<E> clazz, final Annotation... annotations)
   {
      this(new InjectionSpec<E>(clazz, annotations));
   }

   /**
    * Convenience constructor to {@link #InjectionFactory(Class, Annotation...)}
    * that builds injection information only upon a given {@link Class}.
    * 
    * @param clazz
    *           the {@link Class} of objects to lookup
    */
   public InjectionFactory(final Class<E> clazz)
   {
      this(new InjectionSpec<E>(clazz));
   }

   @Override
   public final E get()
   {
      return _injectionFactory.get().getInstance(getBeanManager());
   }
}
