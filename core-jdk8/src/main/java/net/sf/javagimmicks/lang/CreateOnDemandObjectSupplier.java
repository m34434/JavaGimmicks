package net.sf.javagimmicks.lang;

import java.util.function.Supplier;

/**
 * An implementation of {@link Supplier} that creates the instance using when it
 * is requested for the first time using another underlying {@link Supplier}
 * which is commonly known as "lazy loading" pattern.
 * 
 * @param <E>
 *           the type of object the supplier can carry
 */
public class CreateOnDemandObjectSupplier<E> extends AbstractCreateOnDemandSupplier<E>
{
   private E _instance;

   /**
    * Creates a new instance for the given {@link Supplier}
    * 
    * @param supplier
    *           the {@link Supplier} to use for creating the instance
    */
   public CreateOnDemandObjectSupplier(final Supplier<E> supplier)
   {
      super(supplier);
   }

   @Override
   protected E getInstance()
   {
      return _instance;
   }

   @Override
   protected void setInstance(final E instance)
   {
      _instance = instance;
   }
}
