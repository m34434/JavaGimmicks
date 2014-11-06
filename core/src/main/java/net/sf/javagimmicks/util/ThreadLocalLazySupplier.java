package net.sf.javagimmicks.util;

/**
 * An implementation of {@link Supplier} that creates an own instance per thread
 * when it is requested for the first time using an underlying {@link Supplier}
 * which is commonly known as "lazy loading" pattern.
 * 
 * @param <E>
 *           the type of object the container can carries
 */
public class ThreadLocalLazySupplier<E> extends AbstractLazySupplier<E>
{
   private final ThreadLocal<E> _instances = new ThreadLocal<E>();

   /**
    * Creates a new instance for the given {@link Supplier}
    * 
    * @param supplier
    *           the {@link Supplier} to use for creating the instances
    */
   public ThreadLocalLazySupplier(final Supplier<E> supplier)
   {
      super(supplier);
   }

   @Override
   protected E getInstance()
   {
      return _instances.get();
   }

   @Override
   protected void setInstance(final E instance)
   {
      _instances.set(instance);
   }
}
