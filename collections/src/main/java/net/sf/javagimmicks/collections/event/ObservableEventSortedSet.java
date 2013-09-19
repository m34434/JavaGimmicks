package net.sf.javagimmicks.collections.event;

import java.util.SortedSet;

import net.sf.javagimmicks.collections.event.SortedSetEvent.Type;

public class ObservableEventSortedSet<E> extends AbstractEventSortedSet<E> implements
      Observable<SortedSetEvent<E>, EventSortedSetListener<E>>
{
   private static final long serialVersionUID = 7595639007080114146L;

   protected final ObservableBase<SortedSetEvent<E>, EventSortedSetListener<E>> _helper = new ObservableBase<SortedSetEvent<E>, EventSortedSetListener<E>>();

   public ObservableEventSortedSet(final SortedSet<E> decorated)
   {
      super(decorated);
   }

   @Override
   public void addEventListener(final EventSortedSetListener<E> listener)
   {
      _helper.addEventListener(listener);
   }

   @Override
   public void removeEventListener(final EventSortedSetListener<E> listener)
   {
      _helper.removeEventListener(listener);
   }

   @Override
   public ObservableEventSortedSet<E> headSet(final E toElement)
   {
      return new ObservableEventSubSortedSet<E>(this, getDecorated().headSet(toElement));
   }

   @Override
   public ObservableEventSortedSet<E> subSet(final E fromElement, final E toElement)
   {
      return new ObservableEventSubSortedSet<E>(this, getDecorated().subSet(fromElement, toElement));
   }

   @Override
   public ObservableEventSortedSet<E> tailSet(final E fromElement)
   {
      return new ObservableEventSubSortedSet<E>(this, getDecorated().tailSet(fromElement));
   }

   @Override
   protected void fireElementAdded(final E element)
   {
      _helper.fireEvent(new SortedSetEventImpl(Type.ADDED, element));
   }

   @Override
   protected void fireElementReadded(final E element)
   {
      _helper.fireEvent(new SortedSetEventImpl(Type.READDED, element));
   }

   @Override
   protected void fireElementRemoved(final E element)
   {
      _helper.fireEvent(new SortedSetEventImpl(Type.REMOVED, element));
   }

   private class SortedSetEventImpl implements SortedSetEvent<E>
   {
      protected final Type _type;
      protected final E _element;

      public SortedSetEventImpl(final Type type, final E element)
      {
         _type = type;
         _element = element;
      }

      @Override
      public Type getType()
      {
         return _type;
      }

      @Override
      public E getElement()
      {
         return _element;
      }

      @Override
      public ObservableEventSortedSet<E> getSource()
      {
         return ObservableEventSortedSet.this;
      }
   }

   protected static class ObservableEventSubSortedSet<E> extends ObservableEventSortedSet<E>
   {
      private static final long serialVersionUID = 6739254388693769351L;

      protected final ObservableEventSortedSet<E> _parent;

      protected ObservableEventSubSortedSet(final ObservableEventSortedSet<E> parent, final SortedSet<E> decorated)
      {
         super(decorated);

         _parent = parent;
      }

      @Override
      protected void fireElementAdded(final E element)
      {
         super.fireElementAdded(element);
         _parent.fireElementAdded(element);
      }

      @Override
      protected void fireElementReadded(final E element)
      {
         super.fireElementReadded(element);
         _parent.fireElementReadded(element);
      }

      @Override
      protected void fireElementRemoved(final E element)
      {
         super.fireElementRemoved(element);
         _parent.fireElementRemoved(element);
      }
   }
}
