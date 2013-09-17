package net.sf.javagimmicks.cdi;

import javax.enterprise.inject.spi.InjectionPoint;

import net.sf.javagimmicks.lang.Factory;

public abstract class CDIFactoryProducer<E>
{
   private final Factory<? extends E> _factory;

   protected CDIFactoryProducer(final Factory<? extends E> factory)
   {
      _factory = factory;
   }

   protected final E produceInternal(final InjectionPoint injectionPoint)
   {
      final E result = _factory.create();

      configure(result, injectionPoint);

      return result;
   }

   abstract protected void configure(E newInstance, InjectionPoint injectionPoint);
}
