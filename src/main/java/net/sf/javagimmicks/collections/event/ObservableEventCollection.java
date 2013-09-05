package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sf.javagimmicks.collections.event.CollectionEvent.Type;

public class ObservableEventCollection<E> extends AbstractEventCollection<E>
{
   private static final long serialVersionUID = -4055919694275882002L;

   protected transient List<EventCollectionListener<E>> _listeners;

   public ObservableEventCollection(Collection<E> decorated)
   {
      super(decorated);
   }

   public void addEventCollectionListener(EventCollectionListener<E> listener)
   {
      if(_listeners == null)
      {
         _listeners = new ArrayList<EventCollectionListener<E>>();
      }
      
      _listeners.add(listener);
   }
   
   public void removeEventCollectionListener(EventCollectionListener<E> listener)
   {
      if(_listeners != null)
      {
         _listeners.remove(listener);
      }
   }

   @Override
   protected void fireElementsAdded(Collection<? extends E> elements)
   {
      fireEvent(new CollectionEvent<E>(this, Type.ADDED, Collections.unmodifiableCollection(new ArrayList<E>(elements))));
   }
   
   @Override
   protected void fireElementRemoved(E element)
   {
      fireEvent(new CollectionEvent<E>(this, Type.REMOVED, Collections.singleton(element)));
   }
   
   private void fireEvent(CollectionEvent<E> event)
   {
      if(_listeners == null)
      {
         return;
      }
      
      for(EventCollectionListener<E> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }
}
