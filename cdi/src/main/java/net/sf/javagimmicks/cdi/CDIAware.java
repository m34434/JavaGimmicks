package net.sf.javagimmicks.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Named;
import javax.inject.Qualifier;

import net.sf.javagimmicks.cdi.InjectionSpec.Builder;

/**
 * A base class for Java beans which can not be instantiated via CDI (e.g.
 * because they are instantiated via reflection by some given framework) but
 * need access to the CDI context.
 * <p>
 * Provides helper methods for looking up beans from CDI or access the CDI
 * {@link BeanManager}.
 * <p>
 * Alternatively non-constructor injections and post-construct methods can be
 * performed by either calling {@link #illuminate()} or calling
 * {@link #CDIAware(boolean)} with a {@code true} parameter.
 * <p>
 * <b>Attention:</b> Illumination requires CDI 1.1 or higher
 * 
 * @see CDIContext#illuminate(Object)
 */
public abstract class CDIAware
{
   /**
    * Creates a new instance with optionally performing automatic
    * non-constructor injections and post-construct callbacks.
    * <p>
    * <b>Attention:</b> Auto-illumination requires CDI 1.1 or higher
    * 
    * @param illuminate
    *           controls if automatic 'illumination' should be performed
    * @see #illuminate()
    */
   public CDIAware(final boolean illuminate)
   {
      if (illuminate)
      {
         illuminate();
      }
   }

   /**
    * Creates a new instance without performing 'illumination'. You can do this
    * it any time by calling {@link #illuminate()}. Alternatively, lookups can
    * also be performed by the other methods.
    */
   public CDIAware()
   {
      this(false);
   }

   /**
    * Provides access to the CDI {@link BeanManager}.
    * 
    * @return the CDI {@link BeanManager}
    * @see CDIContext#getBeanManager()
    */
   public BeanManager getBeanManager()
   {
      return CDIContext.getBeanManager();
   }

   /**
    * Looks up a {@link Named} bean within the CDI context.
    * 
    * @param name
    *           the name of the {@link Named} bean
    * @return the resulting CDI bean
    * @see CDIContext#lookup(String)
    */
   public <E> E lookup(final String name)
   {
      return CDIContext.lookup(name);
   }

   /**
    * Looks up a bean of the given type and with the given {@link Qualifier}s
    * within the CDI context.
    * 
    * @param clazz
    *           the type of the bean to lookup
    * @param annotations
    *           the {@link Qualifier} annotations of the bean
    * @return the resulting CDI bean
    * @see CDIContext#lookup(Class, Annotation...)
    */
   public <E> E lookup(final Class<E> clazz, final Annotation... annotations)
   {
      return CDIContext.lookup(clazz, annotations);
   }

   /**
    * Looks up a bean of the given type within the CDI context.
    * 
    * @param clazz
    *           the type of the bean to lookup
    * @return the resulting CDI bean
    * @see CDIContext#lookup(Class)
    */
   public <E> E lookup(final Class<E> clazz)
   {
      return CDIContext.lookup(clazz);
   }

   /**
    * Creates a new {@link Builder} for building a CDI lookup via fluent API.
    * 
    * @return the resulting {@link Builder}
    * @see InjectionSpec#build()
    */
   public <E> InjectionSpec.Builder<E> buildLookup()
   {
      return InjectionSpec.build(getBeanManager());
   }

   /**
    * Performs non-constructor injections and post-construct callbacks on this
    * bean.
    * <p>
    * <b>Attention:</b> Requires CDI 1.1 or higher
    * 
    * @see CDIContext#illuminate(Object)
    */
   public void illuminate()
   {
      CDIContext.illuminate(this);
   }
}
