package net.sf.javagimmicks.util;

abstract class AbstractCreateOnDemandObjectContainer<E> implements Supplier<E>
{
   private final Supplier<E> _baseSupplier;

   protected AbstractCreateOnDemandObjectContainer(final Supplier<E> factory)
   {
      _baseSupplier = factory;
   }

   @Override
   public final E get()
   {
      E instance = getInstance();
      if (instance != null)
      {
         return instance;
      }

      synchronized (this)
      {
         instance = getInstance();
         if (instance == null)
         {
            instance = _baseSupplier.get();
            setInstance(instance);
         }
      }

      return instance;
   }

   abstract protected E getInstance();

   abstract protected void setInstance(E instance);
}
