package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;

import net.sf.javagimmicks.collections.event.NavigableSetEvent.Type;

public class ObservableEventNavigableSet<E> extends AbstractEventNavigableSet<E>
{
   private static final long serialVersionUID = -6812183248508925850L;

   protected transient List<EventNavigableSetListener<E>> _listeners;

   public ObservableEventNavigableSet(NavigableSet<E> decorated)
   {
      super(decorated);
   }

   public void addEventNavigableSetListener(EventNavigableSetListener<E> listener)
   {
      if(_listeners == null)
      {
         _listeners = new ArrayList<EventNavigableSetListener<E>>();
      }
      
      _listeners.add(listener);
   }
   
   public void removeEventNavigableSetListener(EventNavigableSetListener<E> listener)
   {
      if(_listeners != null)
      {
         _listeners.remove(listener);
      }
   }
   
   @Override
   public ObservableEventNavigableSet<E> descendingSet()
   {
      return new ObservableEventSubNavigableSet<E>(this, getDecorated().descendingSet());
   }

   @Override
   public ObservableEventNavigableSet<E> headSet(E toElement, boolean inclusive)
   {
      return new ObservableEventSubNavigableSet<E>(this, getDecorated().headSet(toElement, inclusive));
   }

   @Override
   public ObservableEventNavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
   {
      return new ObservableEventSubNavigableSet<E>(this, getDecorated().subSet(fromElement, fromInclusive, toElement, toInclusive));
   }

   @Override
   public ObservableEventNavigableSet<E> tailSet(E fromElement, boolean inclusive)
   {
      return new ObservableEventSubNavigableSet<E>(this, getDecorated().tailSet(fromElement, inclusive));
   }

   @Override
   public ObservableEventSortedSet<E> headSet(E toElement)
   {
      return new ObservableEventSubSortedSet<E>(this, getDecorated().headSet(toElement));
   }

   @Override
   public ObservableEventSortedSet<E> subSet(E fromElement, E toElement)
   {
      return new ObservableEventSubSortedSet<E>(this, getDecorated().subSet(fromElement, toElement));
   }

   @Override
   public ObservableEventSortedSet<E> tailSet(E fromElement)
   {
      return new ObservableEventSubSortedSet<E>(this, getDecorated().tailSet(fromElement));
   }

   @Override
   protected void fireElementAdded(E element)
   {
      fireEvent(new NavigableSetEvent<E>(this, Type.ADDED, element));
   }
   
   @Override
   protected void fireElementReadded(E element)
   {
      fireEvent(new NavigableSetEvent<E>(this, Type.READDED, element));
   }
   
   @Override
   protected void fireElementRemoved(E element)
   {
      fireEvent(new NavigableSetEvent<E>(this, Type.REMOVED, element));
   }
   
   private void fireEvent(NavigableSetEvent<E> event)
   {
      if(_listeners == null)
      {
         return;
      }
      
      for(EventNavigableSetListener<E> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }

   protected static class ObservableEventSubNavigableSet<E> extends ObservableEventNavigableSet<E>
   {
      private static final long serialVersionUID = -4787480179100005627L;

      protected final ObservableEventNavigableSet<E> _parent;

      protected ObservableEventSubNavigableSet(ObservableEventNavigableSet<E> parent, NavigableSet<E> decorated)
      {
         super(decorated);
         
         _parent = parent;
      }

      @Override
      protected void fireElementAdded(E element)
      {
         super.fireElementAdded(element);
         _parent.fireElementAdded(element);
      }

      @Override
      protected void fireElementReadded(E element)
      {
         super.fireElementReadded(element);
         _parent.fireElementReadded(element);
      }

      @Override
      protected void fireElementRemoved(E element)
      {
         super.fireElementRemoved(element);
         _parent.fireElementRemoved(element);
      }
   }

   protected static class ObservableEventSubSortedSet<E> extends ObservableEventSortedSet<E>
   {
      private static final long serialVersionUID = -4787480179100005627L;

      protected final ObservableEventNavigableSet<E> _parent;

      protected ObservableEventSubSortedSet(ObservableEventNavigableSet<E> parent, SortedSet<E> decorated)
      {
         super(decorated);
         
         _parent = parent;
      }

      @Override
      protected void fireElementAdded(E element)
      {
         super.fireElementAdded(element);
         _parent.fireElementAdded(element);
      }

      @Override
      protected void fireElementReadded(E element)
      {
         super.fireElementReadded(element);
         _parent.fireElementReadded(element);
      }

      @Override
      protected void fireElementRemoved(E element)
      {
         super.fireElementRemoved(element);
         _parent.fireElementRemoved(element);
      }
   }
}
