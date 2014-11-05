package net.sf.javagimmicks.util8;

import java.util.function.Supplier;

abstract class AbstractLazySupplier<E> implements Supplier<E>
{
   private final Supplier<E> _baseSupplier;

   protected AbstractLazySupplier(final Supplier<E> baseSupplier)
   {
      _baseSupplier = baseSupplier;
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
