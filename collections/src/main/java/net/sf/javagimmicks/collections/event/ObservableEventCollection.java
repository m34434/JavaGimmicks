package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.sf.javagimmicks.collections.event.CollectionEvent.Type;

public class ObservableEventCollection<E> extends AbstractEventCollection<E> implements
      Observable<CollectionEvent<E>, EventCollectionListener<E>>
{
   private static final long serialVersionUID = -4055919694275882002L;

   protected final ObservableBase<CollectionEvent<E>, EventCollectionListener<E>> _helper = new ObservableBase<CollectionEvent<E>, EventCollectionListener<E>>();

   public ObservableEventCollection(final Collection<E> decorated)
   {
      super(decorated);
   }

   @Override
   public void addEventListener(final EventCollectionListener<E> listener)
   {
      _helper.addEventListener(listener);
   }

   @Override
   public void removeEventListener(final EventCollectionListener<E> listener)
   {
      _helper.removeEventListener(listener);
   }

   @Override
   protected void fireElementsAdded(final Collection<? extends E> elements)
   {
      _helper.fireEvent(new CollectionEventImpl(Type.ADDED, Collections.unmodifiableCollection(new ArrayList<E>(
            elements))));
   }

   @Override
   protected void fireElementRemoved(final E element)
   {
      _helper.fireEvent(new CollectionEventImpl(Type.REMOVED, Collections.singleton(element)));
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
