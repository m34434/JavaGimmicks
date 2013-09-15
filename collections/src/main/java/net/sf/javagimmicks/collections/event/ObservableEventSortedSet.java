package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import net.sf.javagimmicks.collections.event.SortedSetEvent.Type;

public class ObservableEventSortedSet<E> extends AbstractEventSortedSet<E>
{
   private static final long serialVersionUID = 7595639007080114146L;

   protected transient List<EventSortedSetListener<E>> _listeners;

   public ObservableEventSortedSet(SortedSet<E> decorated)
   {
      super(decorated);
   }
   
   public void addEventSortedSetListener(EventSortedSetListener<E> listener)
   {
      if(_listeners == null)
      {
         _listeners = new ArrayList<EventSortedSetListener<E>>();
      }
      
      _listeners.add(listener);
   }
   
   public void removeEventSortedSetListener(EventSortedSetListener<E> listener)
   {
      if(_listeners != null)
      {
         _listeners.remove(listener);
      }
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
      fireEvent(new SortedSetEvent<E>(this, Type.ADDED, element));
   }
   
   @Override
   protected void fireElementReadded(E element)
   {
      fireEvent(new SortedSetEvent<E>(this, Type.READDED, element));
   }
   
   @Override
   protected void fireElementRemoved(E element)
   {
      fireEvent(new SortedSetEvent<E>(this, Type.REMOVED, element));
   }
   
   private void fireEvent(SortedSetEvent<E> event)
   {
      if(_listeners == null)
      {
         return;
      }
      
      for(EventSortedSetListener<E> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }

   protected static class ObservableEventSubSortedSet<E> extends ObservableEventSortedSet<E>
   {
      private static final long serialVersionUID = 6739254388693769351L;

      protected final ObservableEventSortedSet<E> _parent;

      protected ObservableEventSubSortedSet(ObservableEventSortedSet<E> parent, SortedSet<E> decorated)
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
