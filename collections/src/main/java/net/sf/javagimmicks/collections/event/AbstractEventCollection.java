package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import net.sf.javagimmicks.collections.decorators.AbstractUnmodifiableCollectionDecorator;

public abstract class AbstractEventCollection<E> extends AbstractUnmodifiableCollectionDecorator<E>
{
   private static final long serialVersionUID = -8335291555421718053L;

   public AbstractEventCollection(Collection<E> decorated)
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
      
      return result;
   }
   
   @Override
   public boolean addAll(Collection<? extends E> c)
   {
      final Collection<E> decorated = getDecorated();
      final ArrayList<E> added = new ArrayList<E>();
      
      for(E element : c)
      {
         if(decorated.add(element))
         {
            added.add(element);
         }
      }
      
      if(!added.isEmpty())
      {
         fireElementsAdded(added);
         return true;
      }
      else
      {
         return false;
      }
   }

   @Override
   public Iterator<E> iterator()
   {
      return new EventCollectionIterator(getDecorated().iterator());
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

   abstract protected void fireElementsAdded(Collection<? extends E> c);
   abstract protected void fireElementRemoved(E element);
   
   protected void fireElementAdded(E element)
   {
      fireElementsAdded(Collections.singleton(element));
   }
   
   protected class EventCollectionIterator implements Iterator<E>
   {
      protected final Iterator<E> _decorated;
      protected E _lastElement = null;
      
      public EventCollectionIterator(Iterator<E> decorated)
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
}
