package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Extension;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CDIContext
{
   private static BeanManager _currentBeanManager;

   public static BeanManager getBeanManager()
   {
      if (_currentBeanManager != null)
      {
         synchronized (CDIContext.class)
         {
            if (_currentBeanManager != null)
            {
               return _currentBeanManager;
            }
         }
      }

      try
      {
         final BeanManager result = CDI.current().getBeanManager();
         if (result != null)
         {
            return result;
         }
      }
      catch (final IllegalStateException ex)
      {

      }

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

      throw new IllegalStateException("No BeanManager found!");
   }

   public static <E> E lookup(final BeanManager beanManager, final Class<E> beanType, final Annotation... bindings)
   {
      return Injection.<E> build(beanManager).setClass(beanType).addAnnotations(bindings).getInstance();
   }

   public static <E> E lookup(final BeanManager beanManager, final Class<E> beanType)
   {
      return lookup(beanManager, beanType, new Annotation[0]);
   }

   public static <E> E lookup(final BeanManager beanManager, final String name)
   {
      return Injection.<E> build(beanManager).setName(name).getInstance();
   }

   @SuppressWarnings("unchecked")
   public static <E> E lookup(final BeanManager beanManager, final Type beanType, final Annotation... bindings)
   {
      final Bean<?> bean = beanManager.resolve(beanManager.getBeans(beanType, bindings));
      if (bean == null)
      {
         throw new UnsatisfiedResolutionException("Unable to resolve a bean for " + beanType + " with bindings "
               + Arrays.asList(bindings));
      }
      final CreationalContext<?> cc = beanManager.createCreationalContext(bean);
      return (E) beanManager.getReference(bean, beanType, cc);
   }

   public static <E> E lookup(final BeanManager beanManager, final Type beanType)
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

   public static <E> E lookup(final String name)
   {
      return lookup(getBeanManager(), name);
   }

   private static void setCurrentBeanManager(final BeanManager beanManager)
   {
      synchronized (CDIContext.class)
      {
         _currentBeanManager = beanManager;
      }
   }

   public static class CDIContextExtension implements Extension
   {
      // Called upon CDI context startup - additionally injects the BeanManager
      // which is remembered here
      public void startup(@Observes final BeforeBeanDiscovery event, final BeanManager beanManager)
      {
         setCurrentBeanManager(beanManager);
      }

      // Called upon CDI context shutdown - clear the internal BeanManager
      public void shutdown(@Observes final BeforeShutdown event)
      {
         setCurrentBeanManager(null);
      }
   }
}
