package net.sf.javagimmicks.util;

abstract class AbstractCreateOnDemandObjectContainer<E> implements ObjectContainer<E>
{
   private final Factory<E> _factory;

   protected AbstractCreateOnDemandObjectContainer(final Factory<E> factory)
   {
      _factory = factory;
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
            instance = _factory.create();
            setInstance(instance);
         }
      }

      return instance;
   }

   abstract protected E getInstance();

   abstract protected void setInstance(E instance);
}
