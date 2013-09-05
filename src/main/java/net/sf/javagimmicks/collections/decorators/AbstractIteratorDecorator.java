package net.sf.javagimmicks.collections.decorators;

import java.util.Iterator;

/**
 * A basic class for {@link Iterator} decorators
 * that simply forwards all calls to an internal
 * delegate instance.
 */
public abstract class AbstractIteratorDecorator<E> implements Iterator<E>
{
   protected final Iterator<E> _decorated;
   
   protected AbstractIteratorDecorator(Iterator<E> decorated)
   {
      _decorated = decorated;
   }

   /**
    * Returns the decorated instance (the delegate)
    */
   public Iterator<E> getDecorated()
   {
      return _decorated;
   }
   
   public boolean hasNext()
   {
      return getDecorated().hasNext();
   }

   public E next()
   {
      return getDecorated().next();
   }

   public void remove()
   {
      getDecorated().remove();
   }
}
