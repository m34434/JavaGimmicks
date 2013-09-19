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

   public ObservableEventCollection(final Collection<E> decorated)
   {
      super(decorated);
   }

   public void addEventCollectionListener(final EventCollectionListener<E> listener)
   {
      if (_listeners == null)
      {
         _listeners = new ArrayList<EventCollectionListener<E>>();
      }

      _listeners.add(listener);
   }

   public void removeEventCollectionListener(final EventCollectionListener<E> listener)
   {
      if (_listeners != null)
      {
         _listeners.remove(listener);
      }
   }

   @Override
   protected void fireElementsAdded(final Collection<? extends E> elements)
   {
      fireEvent(new CollectionEventImpl(Type.ADDED, Collections.unmodifiableCollection(new ArrayList<E>(
            elements))));
   }

   @Override
   protected void fireElementRemoved(final E element)
   {
      fireEvent(new CollectionEventImpl(Type.REMOVED, Collections.singleton(element)));
   }

   private void fireEvent(final CollectionEvent<E> event)
   {
      if (_listeners == null)
      {
         return;
      }

      for (final EventCollectionListener<E> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }

   private class CollectionEventImpl implements CollectionEvent<E>
   {
      protected final Type _type;
      protected final Collection<E> _elements;

      public CollectionEventImpl(final Type type,
            final Collection<E> elements)
      {
         _type = type;
         _elements = elements;
      }

      @Override
      public Type getType()
      {
         return _type;
      }

      @Override
      public Collection<E> getElements()
      {
         return _elements;
      }

      @Override
      public ObservableEventCollection<E> getSource()
      {
         return ObservableEventCollection.this;
      }
   }
}
