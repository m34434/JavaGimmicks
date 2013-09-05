package net.sf.javagimmicks.collections.decorators;

import net.sf.javagimmicks.collections.Ring;
import net.sf.javagimmicks.collections.Traverser;

/**
 * A basic class for {@link Ring} decorators
 * that simply forwards all calls to an internal
 * delegate instance.
 */
public abstract class AbstractRingDecorator<E> extends AbstractCollectionDecorator<E> implements Ring<E>
{
   private static final long serialVersionUID = -8852652567693291175L;

   protected AbstractRingDecorator(Ring<E> decorated)
   {
      super(decorated);
   }

   /**
    * Returns the decorated instance (the delegate)
    */
   @Override
   public Ring<E> getDecorated()
   {
      return (Ring<E>)super.getDecorated();
   }
   
   public Traverser<E> traverser()
   {
      return getDecorated().traverser();
   }
}
