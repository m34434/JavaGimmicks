package net.sf.javagimmicks.collections.decorators;

import java.util.Iterator;

public abstract class AbstractIteratorDecorator<E> implements Iterator<E>
{
   protected final Iterator<E> _decorated;
   
   protected AbstractIteratorDecorator(Iterator<E> decorated)
   {
      _decorated = decorated;
   }

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
