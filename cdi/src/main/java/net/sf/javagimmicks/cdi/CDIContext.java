package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CDIContext
{
   private static BeanManager _fallbackBeanManager;

   public static void setFallbackBeanManager(final BeanManager beanManager)
   {
      synchronized (CDIContext.class)
      {
         _fallbackBeanManager = beanManager;
      }
   }

   public static BeanManager getBeanManager()
   {
      try
      {
         return (BeanManager) InitialContext.doLookup("java:comp/BeanManager");
      }
      catch (final NamingException e)
      {
      }

      try
      {
         return (BeanManager) InitialContext.doLookup("java:comp/env/BeanManager");
      }
      catch (final NamingException e)
      {
      }

      if (_fallbackBeanManager != null)
      {
         synchronized (CDIContext.class)
         {
            if (_fallbackBeanManager != null)
            {
               return _fallbackBeanManager;
            }
         }
      }

      throw new IllegalStateException("No BeanManager found!");
   }

   public static <E> E lookup(final BeanManager beanManager, final Class<E> beanType, final Annotation... bindings)
   {
      final Bean<?> bean = beanManager.resolve(beanManager.getBeans(beanType, bindings));
      if (bean == null)
      {
         throw new UnsatisfiedResolutionException("Unable to resolve a bean for " + beanType + " with bindings "
               + Arrays.asList(bindings));
      }
      final CreationalContext<?> cc = beanManager.createCreationalContext(bean);
      return beanType.cast(beanManager.getReference(bean, beanType, cc));
   }

   public static <E> E lookup(final BeanManager beanManager, final Class<E> beanType)
   {
      return lookup(beanManager, beanType, new Annotation[0]);
   }

   public static <E> E lookup(final Class<E> beanType, final Annotation... bindings)
   {
      return lookup(getBeanManager(), beanType, bindings);
   }

   public static <E> E lookup(final Class<E> beanType)
   {
      return lookup(getBeanManager(), beanType);
   }

   @SuppressWarnings("unchecked")
   public static <E> E lookup(final BeanManager beanManager, final String name)
   {
      final Bean<?> bean = beanManager.resolve(beanManager.getBeans(name));
      if (bean == null)
      {
         throw new UnsatisfiedResolutionException("Unable to resolve a bean name " + name);
      }
      final CreationalContext<?> cc = beanManager.createCreationalContext(bean);
      return (E) beanManager.getReference(bean, bean.getBeanClass(), cc);
   }

   public static <E> E lookup(final String name)
   {
      return lookup(getBeanManager(), name);
   }
}
