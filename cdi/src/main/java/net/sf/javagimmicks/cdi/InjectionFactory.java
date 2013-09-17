package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;

import net.sf.javagimmicks.lang.ConstantFactory;
import net.sf.javagimmicks.lang.Factory;

public class InjectionFactory<E> extends CDIAware implements Factory<E>
{
   private final Factory<InjectionSpec<E>> _injectionFactory;

   public InjectionFactory(final Factory<InjectionSpec<E>> injectionFactory)
   {
      _injectionFactory = injectionFactory;
   }

   public InjectionFactory(final InjectionSpec<E> injection)
   {
      this(new ConstantFactory<InjectionSpec<E>>(injection));
   }

   public InjectionFactory(final Class<E> clazz, final Annotation... annotations)
   {
      this(new InjectionSpec<E>(clazz, annotations));
   }

   public InjectionFactory(final Class<E> clazz)
   {
      this(new InjectionSpec<E>(clazz));
   }

   @Override
   public final E create()
   {
      return _injectionFactory.create().getInstance(getBeanManager());
   }
}
