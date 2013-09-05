package net.sf.javagimmicks.collections.decorators;

import java.util.Set;

/**
 * A basic class for {@link Set} decorators
 * that simply forwards all calls to an internal
 * delegate instance.
 */
public abstract class AbstractSetDecorator<E> extends AbstractCollectionDecorator<E> implements Set<E>
{
   private static final long serialVersionUID = 1997599261091959009L;

   protected AbstractSetDecorator(Set<E> decorated)
   {
      super(decorated);
   }

   /**
    * Returns the decorated instance (the delegate)
    */
   public Set<E> getDecorated()
   {
      return (Set<E>)super.getDecorated();
   }
}
