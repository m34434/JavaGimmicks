package net.sf.javagimmicks.collections.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.sf.javagimmicks.collections.event.CollectionEvent.Type;
import net.sf.javagimmicks.event.Observable;
import net.sf.javagimmicks.event.ObservableBase;

/**
 * A {@link Collection} decorator that serves as an {@link Observable} for
 * {@link CollectionEvent}s.
 */
public class ObservableEventCollection<E> extends AbstractEventCollection<E> implements
      Observable<CollectionEvent<E>, EventCollectionListener<E>>
{
   private static final long serialVersionUID = -4055919694275882002L;

   protected final ObservableBase<CollectionEvent<E>, EventCollectionListener<E>> _helper = new ObservableBase<CollectionEvent<E>, EventCollectionListener<E>>();

   /**
    * Wraps a new {@link ObservableEventCollection} around a given
    * {@link Collection}.
    * 
    * @param decorated
    *           the {@link Collection} to wrap around
    */
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
