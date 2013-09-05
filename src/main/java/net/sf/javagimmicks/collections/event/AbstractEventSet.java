package net.sf.javagimmicks.collections.event;

import java.util.Iterator;
import java.util.Set;

import net.sf.javagimmicks.collections.decorators.AbstractUnmodifiableSetDecorator;

public abstract class AbstractEventSet<E> extends AbstractUnmodifiableSetDecorator<E>
{
   private static final long serialVersionUID = -7712383972215104949L;

   public AbstractEventSet(Set<E> decorated)
   {
      super(decorated);
   }
   
   @Override
   public boolean add(E e)
   {
      boolean result = getDecorated().add(e);
      
      if(result)
      {
         fireElementAdded(e);
      }
      else
      {
         fireElementReadded(e);
      }
      
      return result;
   }

   @Override
   public Iterator<E> iterator()
   {
      return new EventSetIterator(getDecorated().iterator());
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean remove(Object o)
   {
      boolean result = getDecorated().remove(o);
      
      if(result)
      {
         fireElementRemoved((E)o);
      }
      
      return result;
   }

   protected class EventSetIterator implements Iterator<E>
   {
      protected final Iterator<E> _decorated;
      protected E _lastElement = null;
      
      public EventSetIterator(Iterator<E> decorated)
      {
         _decorated = decorated;
      }

      public boolean hasNext()
      {
         return _decorated.hasNext();
      }

      public E next()
      {
         _lastElement = _decorated.next();
         
         return _lastElement;
      }

      public void remove()
      {
         _decorated.remove();
         
         fireElementRemoved(_lastElement);
      }
   }

   abstract protected void fireElementAdded(E element);
   abstract protected void fireElementReadded(E element);
   abstract protected void fireElementRemoved(E element);
}