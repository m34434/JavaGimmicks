package net.sf.javagimmicks.util;

/**
 * An implementation of {@link Supplier} that creates the instance using when it
 * is requested for the first time using an underlying {@link Supplier} which is
 * commonly known as "lazy loading" pattern.
 * 
 * @param <E>
 *           the type of object the container can carry
 */
public class LazySupplier<E> extends AbstractLazySupplier<E>
{
   private E _instance;

   /**
    * Creates a new instance for the given {@link Supplier}
    * 
    * @param supplier
    *           the {@link Supplier} to use for creating the instance
    */
   public LazySupplier(final Supplier<E> supplier)
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
