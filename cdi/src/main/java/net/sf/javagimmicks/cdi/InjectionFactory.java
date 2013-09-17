package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;

import net.sf.javagimmicks.lang.ConstantFactory;
import net.sf.javagimmicks.lang.Factory;

public class InjectionFactory<E> extends CDIAware implements Factory<E>
{
   private final Factory<Injection<E>> _injectionFactory;

   public InjectionFactory(final Factory<Injection<E>> injectionFactory)
   {
      _injectionFactory = injectionFactory;
   }

   public InjectionFactory(final Injection<E> injection)
   {
      this(new ConstantFactory<Injection<E>>(injection));
   }

   public InjectionFactory(final Class<E> clazz, final Annotation... annotations)
   {
      this(new Injection<E>(clazz, annotations));
   }

   public InjectionFactory(final Class<E> clazz)
   {
      this(new Injection<E>(clazz));
   }

   @Override
   public final E create()
   {
      return _injectionFactory.create().getInstance(getBeanManager());
   }
}
