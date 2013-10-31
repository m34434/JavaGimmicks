package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.InjectionTargetFactory;
import javax.inject.Named;
import javax.inject.Qualifier;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * This is the central access point to the CDI {@link BeanManager} from non-CDI
 * classes - also provides some simple bean lookup methods - use
 * {@link InjectionSpec} for advanced lookups.
 */
public class CDIContext
{
   /**
    * The JNDI name that a {@link BeanManager} should have in a full-stack EE
    * application server
    */
   public static final String JNDI_FULL_EE = "java:comp/BeanManager";

   /**
    * The JNDI name that a {@link BeanManager} could have in some servlet
    * containers (e.g. Tomcat)
    */
   public static final String JNDI_SERVLET = "java:comp/env/BeanManager";

   private static BeanManager _currentBeanManager;

   private CDIContext()
   {}

   /**
    * This method represents the core functionality of this class - it follows
    * different strategies to get access to the CDI {@link BeanManager} instance
    * and return it.
    * <p>
    * These are the following:
    * <ol>
    * <li>Register a CDI {@link Extension} which intercepts CDI container
    * startup and shutdown events and stores the so provided {@link BeanManager}
    * internally for later use - this is sufficient in most cases and makes the
    * other strategies nearly unnecessary</li>
    * <li>Try to get the {@link BeanManager} via the CDI API itself (call
    * {@link CDI#current()} and then {@link CDI#getBeanManager()})</li>
    * <li>Try to lookup the {@link BeanManager} in JNDI with name
    * {@link #JNDI_FULL_EE}</li>
    * <li>Try to lookup the {@link BeanManager} in JNDI with name
    * {@link #JNDI_SERVLET}</li>
    * <ol>
    * 
    * @return the current CDI {@link BeanManager} instance
    * @throws IllegalStateException
    *            if no {@link BeanManager} could be found with any of the name
    *            strategies
    */
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
         return (BeanManager) InitialContext.doLookup(JNDI_FULL_EE);
      }
      catch (final NamingException e)
      {
      }

      try
      {
         return (BeanManager) InitialContext.doLookup(JNDI_SERVLET);
      }
      catch (final NamingException e)
      {
      }

      throw new IllegalStateException("No BeanManager found!");
   }

   /**
    * Looks up a bean of the given type and with the given {@link Qualifier}s
    * within the CDI context.
    * 
    * @param beanType
    *           the type of the bean to lookup
    * @param bindings
    *           the {@link Qualifier} annotations of the bean
    * @return the resulting CDI bean
    */
   public static <E> E lookup(final Class<E> beanType, final Annotation... bindings)
   {
      return InjectionSpec.<E> build(getBeanManager()).setClass(beanType).addAnnotations(bindings).getInstance();
   }

   /**
    * Looks up a bean of the given type within the CDI context.
    * 
    * @param beanType
    *           the type of the bean to lookup
    * @return the resulting CDI bean
    */
   public static <E> E lookup(final Class<E> beanType)
   {
      return lookup(beanType, new Annotation[0]);
   }

   /**
    * Looks up a {@link Named} bean within the CDI context.
    * 
    * @param name
    *           the name of the {@link Named} bean
    * @return the resulting CDI bean
    */
   public static <E> E lookup(final String name)
   {
      return InjectionSpec.<E> build(getBeanManager()).setName(name).getInstance();
   }

   /**
    * Processes non-constructor injections and calls to post-construct methods
    * on a given non-CDI bean.
    * 
    * @param nonCdiObject
    *           a given bean that was not instantiated via CDI
    * @return the bean itself after injections and post-constructs were
    *         performed
    */
   @SuppressWarnings("unchecked")
   public static <T> T initBean(final T nonCdiObject)
   {
      final BeanManager beanManager = getBeanManager();

      // Get all the object we need
      final Bean<T> bean = (Bean<T>) beanManager.resolve(beanManager.getBeans(nonCdiObject.getClass()));
      final AnnotatedType<T> annotatedType = (AnnotatedType<T>) beanManager
            .createAnnotatedType(nonCdiObject.getClass());

      InjectionTarget<T> injectionTarget;
      try
      {
         // CDI 1.1
         final InjectionTargetFactory<T> injectionTargetFactory = beanManager.getInjectionTargetFactory(annotatedType);
         injectionTarget = injectionTargetFactory.createInjectionTarget(bean);
      }
      catch (final IncompatibleClassChangeError e)
      {
         // CDI 1.0
         injectionTarget = beanManager.createInjectionTarget(annotatedType);
      }

      // Perform injections and run @PostConstruct
      injectionTarget.inject(nonCdiObject, beanManager.createCreationalContext(bean));
      injectionTarget.postConstruct(nonCdiObject);

      return nonCdiObject;
   }

   private static void setCurrentBeanManager(final BeanManager beanManager)
   {
      synchronized (CDIContext.class)
      {
         _currentBeanManager = beanManager;
      }
   }

   /**
    * Not for developer's use - will be internally called by the CDI container.
    */
   public static class CDIContextExtension implements Extension
   {
      /**
       * Called upon CDI context startup - additionally injects the
       * {@link BeanManager}
       */
      public void startup(@Observes final BeforeBeanDiscovery event, final BeanManager beanManager)
      {
         setCurrentBeanManager(beanManager);
      }

      /**
       * Called upon CDI context shutdown - clears the internal
       * {@link BeanManager}
       */
      public void shutdown(@Observes final BeforeShutdown event)
      {
         setCurrentBeanManager(null);
      }
   }
}
