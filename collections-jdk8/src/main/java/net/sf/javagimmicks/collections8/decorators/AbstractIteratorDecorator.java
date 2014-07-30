package net.sf.javagimmicks.collections8.decorators;

import java.util.Iterator;

/**
 * A basic class for {@link Iterator} decorators that simply forwards all calls
 * to an internal delegate instance.
 */
public abstract class AbstractIteratorDecorator<E> implements Iterator<E>
{
   protected final Iterator<E> _decorated;

   protected AbstractIteratorDecorator(final Iterator<E> decorated)
   {
      _decorated = decorated;
   }

   @Override
   public boolean hasNext()
   {
      return getDecorated().hasNext();
   }

   @Override
   public E next()
   {
      return getDecorated().next();
   }

   @Override
   public void remove()
   {
      getDecorated().remove();
   }

   protected Iterator<E> getDecorated()
   {
      return _decorated;
   }
}
