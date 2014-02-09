package net.sf.javagimmicks.cdi;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;

/**
 * Not for developer's use - will be internally called by the CDI container.
 */
public class CDIContextExtension implements Extension
{
   /**
    * Called upon CDI context startup - additionally injects the
    * {@link BeanManager}
    */
   public static void startup(@Observes final BeforeBeanDiscovery event, final BeanManager beanManager)
   {
      CDIContext.setCurrentBeanManager(beanManager);
   }

   /**
    * Called upon CDI context shutdown - clears the internal {@link BeanManager}
    */
   public static void shutdown(@Observes final BeforeShutdown event)
   {
      CDIContext.setCurrentBeanManager(null);
   }
}