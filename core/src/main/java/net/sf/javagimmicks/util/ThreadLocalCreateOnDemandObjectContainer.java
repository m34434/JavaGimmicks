package net.sf.javagimmicks.util;

/**
 * An implementation of {@link ObjectContainer} that creates an own instance per
 * thread when it is requested for the first time using an underlying
 * {@link Factory} which is commonly known as "lazy loading" pattern.
 * 
 * @param <E>
 *           the type of object the container can carries
 */
public class ThreadLocalCreateOnDemandObjectContainer<E> extends AbstractCreateOnDemandObjectContainer<E>
{
   private final ThreadLocal<E> _instances = new ThreadLocal<E>();

   /**
    * Creates a new instance for the given {@link Factory}
    * 
    * @param factory
    *           the {@link Factory} to use for creating the instances
    */
   public ThreadLocalCreateOnDemandObjectContainer(final Factory<E> factory)
   {
      super(factory);
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
