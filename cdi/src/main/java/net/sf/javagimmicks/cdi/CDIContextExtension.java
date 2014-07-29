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
    * 
    * @param beanManager
    *           the injected CDI {@link BeanManager} - will be remembered within
    *           the {@link CDIContext}
    * @param event
    *           the observed CDI event
    */
   public static void startup(@Observes final BeforeBeanDiscovery event, final BeanManager beanManager)
   {
      CDIContext.setCurrentBeanManager(beanManager);
   }

   /**
    * Called upon CDI context shutdown - clears the internal {@link BeanManager}
    * 
    * @param event
    *           the observed CDI event
    */
   public static void shutdown(@Observes final BeforeShutdown event)
   {
      CDIContext.setCurrentBeanManager(null);
   }
}