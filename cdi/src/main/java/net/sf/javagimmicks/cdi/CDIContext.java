package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CDIContext
{
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

      try
      {
         final Class<?> oWeldContainerClass = Class.forName("org.jboss.weld.Container");
         final Method oWeldContainerInstanceMethod = oWeldContainerClass.getDeclaredMethod("instance",
               new Class[0]);
         final Object oWeldContainer = oWeldContainerInstanceMethod.invoke(null);

         if (oWeldContainer != null)
         {
            final BeanManager tempBeanManager = (BeanManager) oWeldContainer.getClass()
                  .getMethod("deploymentManager", new Class[0])
                  .invoke(oWeldContainer);

            return lookup(tempBeanManager, BeanManager.class);
         }
      }
      catch (final Exception ignore)
      {

      }

      throw new IllegalStateException("No BeanManager found!");
   }

   @SuppressWarnings("unchecked")
   public static <E> E lookup(final BeanManager beanManager, final Class<E> beanType, final Annotation... qualifiers)
   {
      final Set<Bean<?>> beans = beanManager.getBeans(beanType, qualifiers);

      if (beans.isEmpty())
      {
         throw new IllegalArgumentException("Could not find a unique bean type for the given parameters!");
      }

      final Bean<E> bean = (Bean<E>) beanManager.resolve(beans);
      return (E) beanManager.getReference(bean, beanType, beanManager.createCreationalContext(bean));
   }

   public static <E> E lookup(final BeanManager beanManager, final Class<E> beanType)
   {
      return lookup(beanManager, beanType, new Annotation[0]);
   }

   public static <E> E lookup(final Class<E> beanType, final Annotation... qualifiers)
   {
      return lookup(getBeanManager(), beanType, qualifiers);
   }

   public static <E> E lookup(final Class<E> beanType)
   {
      return lookup(getBeanManager(), beanType);
   }

   @SuppressWarnings("unchecked")
   public static <E> E lookup(final BeanManager beanManager, final String name)
   {
      final Set<Bean<?>> beans = beanManager.getBeans(name);

      if (beans.isEmpty())
      {
         throw new IllegalArgumentException("Could not find a unique bean type for the given parameters!");
      }

      final Bean<E> bean = (Bean<E>) beanManager.resolve(beans);
      return (E) beanManager.getReference(bean, bean.getTypes().iterator().next(),
            beanManager.createCreationalContext(bean));
   }

   public static <E> E lookup(final String name)
   {
      return lookup(getBeanManager(), name);
   }

}
