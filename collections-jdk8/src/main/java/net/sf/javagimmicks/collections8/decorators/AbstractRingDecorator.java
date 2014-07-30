package net.sf.javagimmicks.collections8.decorators;

import net.sf.javagimmicks.collections8.Ring;
import net.sf.javagimmicks.collections8.RingCursor;

/**
 * A basic class for {@link Ring} decorators that simply forwards all calls to
 * an internal delegate instance.
 */
public abstract class AbstractRingDecorator<E> extends AbstractCollectionDecorator<E> implements Ring<E>
{
   private static final long serialVersionUID = -8852652567693291175L;

   protected AbstractRingDecorator(final Ring<E> decorated)
   {
      super(decorated);
   }

   @Override
   public RingCursor<E> cursor()
   {
      return getDecorated().cursor();
   }

   @Override
   protected Ring<E> getDecorated()
   {
      return (Ring<E>) super.getDecorated();
   }
}
