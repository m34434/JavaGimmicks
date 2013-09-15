package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sf.javagimmicks.collections.event.ListEvent.Type;

public class ObservableEventList<E> extends AbstractEventList<E>
{
   private static final long serialVersionUID = -6317396247733734848L;

   protected transient List<EventListListener<E>> _listeners;
   
   public ObservableEventList(List<E> decorated)
   {
      super(decorated);
   }

   public void addEventListListener(EventListListener<E> listener)
   {
      if(_listeners == null)
      {
         _listeners = new ArrayList<EventListListener<E>>();
      }
      
      _listeners.add(listener);
   }
   
   public void removeEventListListener(EventListListener<E> listener)
   {
      if(_listeners != null)
      {
         _listeners.remove(listener);
      }
   }
   
   @Override
   public ObservableEventList<E> subList(int fromIndex, int toIndex)
   {
      return new ObservableEventSubList<E>(this, fromIndex, toIndex);
   }

   @Override
   protected void fireElementsAdded(int index, Collection<? extends E> elements)
   {
      ListEvent<E> event = new ListEvent<E>(
         this,
         Type.ADDED,
         index,
         index + elements.size(),
         Collections.unmodifiableList(new ArrayList<E>(elements)));
      
      fireEvent(event);
   }

   @Override
   protected void fireElementUpdated(int index, E element, E newElement)
   {
      ListEvent<E> event = new ListEvent<E>(
         this,
         Type.UPDATED,
         index,
         index,
         Collections.singletonList(element),
         Collections.singletonList(newElement));
      
      fireEvent(event);
   }

   @Override
   protected void fireElementRemoved(int index, E element)
   {
      ListEvent<E> event = new ListEvent<E>(
         this,
         Type.REMOVED,
         index,
         index,
         Collections.singletonList(element));

      fireEvent(event);
   }
   
   private void fireEvent(ListEvent<E> event)
   {
      if(_listeners == null)
      {
         return;
      }
      
      for(EventListListener<E> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }

   protected static class ObservableEventSubList<E> extends ObservableEventList<E>
   {
      private static final long serialVersionUID = 1996968483016862598L;

      protected final ObservableEventList<E> _parent;
      protected final int _offset;
      
      protected ObservableEventSubList(ObservableEventList<E> parent, int fromIndex, int toIndex)
      {
         super(parent._decorated.subList(fromIndex, toIndex));
         
         _parent = parent;
         _offset = fromIndex;
      }

      @Override
      protected void fireElementsAdded(int index, Collection<? extends E> elements)
      {
         super.fireElementsAdded(index, elements);
         _parent.fireElementsAdded(index + _offset, elements);
      }

      @Override
      protected void fireElementRemoved(int index, E element)
      {
         super.fireElementRemoved(index, element);
         _parent.fireElementRemoved(index + _offset, element);
      }

      @Override
      protected void fireElementUpdated(int index, E element, E newElement)
      {
         super.fireElementUpdated(index, element, newElement);
         _parent.fireElementUpdated(index + _offset, element, newElement);
      }
   }
}
