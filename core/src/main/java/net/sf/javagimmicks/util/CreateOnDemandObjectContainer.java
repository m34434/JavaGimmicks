package net.sf.javagimmicks.util;

/**
 * An implementation of {@link ObjectContainer} that creates the instance using
 * when it is requested for the first time using an underlying {@link Factory}
 * which is commonly known as "lazy loading" pattern.
 * 
 * @param <E>
 *           the type of object the container can carry
 */
public class CreateOnDemandObjectContainer<E> extends AbstractCreateOnDemandObjectContainer<E>
{
   private E _instance;

   /**
    * Creates a new instance for the given {@link Factory}
    * 
    * @param factory
    *           the {@link Factory} to use for creating the instance
    */
   public CreateOnDemandObjectContainer(final Factory<E> factory)
   {
      super(factory);
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
