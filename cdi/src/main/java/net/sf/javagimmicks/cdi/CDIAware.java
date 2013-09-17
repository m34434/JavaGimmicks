package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.BeanManager;

public abstract class CDIAware
{
   protected BeanManager getBeanManager()
   {
      return CDIContext.getBeanManager();
   }

   protected <E> E lookup(final String name)
   {
      return CDIContext.lookup(name);
   }

   protected <E> E lookup(final Class<E> clazz, final Annotation... annotations)
   {
      return CDIContext.lookup(clazz, annotations);
   }

   protected <E> E lookup(final Class<E> clazz)
   {
      return CDIContext.lookup(clazz);
   }
}
