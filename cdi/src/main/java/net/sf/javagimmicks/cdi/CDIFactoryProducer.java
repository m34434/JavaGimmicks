package net.sf.javagimmicks.cdi;

import javax.enterprise.inject.spi.InjectionPoint;

import net.sf.javagimmicks.lang.Factory;

/**
 * A base class for writing CDI producers based on a given {@link Factory} and
 * allowing advances instance configuration via {@link InjectionPoint}.
 * <p>
 * Subclasses should call {@link #produceInternal(InjectionPoint)} in their
 * producer methods and can provide custom configuration logic by overwriting
 * {@link #configure(Object, InjectionPoint)}.
 * 
 * @param <E>
 *           The type of beans to create
 */
public abstract class CDIFactoryProducer<E>
{
   private final Factory<? extends E> _factory;

   /**
    * Creates a new instance around the given {@link Factory}
    * 
    * @param factory
    *           the {@link Factory} responsible for creating new bean instances
    */
   public CDIFactoryProducer(final Factory<? extends E> factory)
   {
      _factory = factory;
   }

   /**
    * Subclasses should call this method from the provided producer methods in
    * order to create a new bean instance optionally passing the CDI injected
    * {@link InjectionPoint}.
    * 
    * @param injectionPoint
    *           the CDI provided {@link InjectionPoint} the carries information
    *           about the injection point of the bean - if non-null then
    *           {@link #configure(Object, InjectionPoint)} will be called to
    *           configure the created bean instances based on the
    *           {@link InjectionPoint}
    * @return the produces (and optionally configured) bean
    */
   protected final E produceInternal(final InjectionPoint injectionPoint)
   {
      final E result = _factory.create();

      if (injectionPoint != null)
      {
         configure(result, injectionPoint);
      }

      return result;
   }

   protected void configure(final E newInstance, final InjectionPoint injectionPoint)
   {}
}
