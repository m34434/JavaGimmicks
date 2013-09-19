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

   public ObservableEventNavigableSet(final NavigableSet<E> decorated)
   {
      super(decorated);
   }

   public void addEventNavigableSetListener(final EventNavigableSetListener<E> listener)
   {
      if (_listeners == null)
      {
         _listeners = new ArrayList<EventNavigableSetListener<E>>();
      }

      _listeners.add(listener);
   }

   public void removeEventNavigableSetListener(final EventNavigableSetListener<E> listener)
   {
      if (_listeners != null)
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
   public ObservableEventNavigableSet<E> headSet(final E toElement, final boolean inclusive)
   {
      return new ObservableEventSubNavigableSet<E>(this, getDecorated().headSet(toElement, inclusive));
   }

   @Override
   public ObservableEventNavigableSet<E> subSet(final E fromElement, final boolean fromInclusive, final E toElement,
         final boolean toInclusive)
   {
      return new ObservableEventSubNavigableSet<E>(this, getDecorated().subSet(fromElement, fromInclusive, toElement,
            toInclusive));
   }

   @Override
   public ObservableEventNavigableSet<E> tailSet(final E fromElement, final boolean inclusive)
   {
      return new ObservableEventSubNavigableSet<E>(this, getDecorated().tailSet(fromElement, inclusive));
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
      fireEvent(new NavigableSetEventImpl(Type.ADDED, element));
   }

   @Override
   protected void fireElementReadded(final E element)
   {
      fireEvent(new NavigableSetEventImpl(Type.READDED, element));
   }

   @Override
   protected void fireElementRemoved(final E element)
   {
      fireEvent(new NavigableSetEventImpl(Type.REMOVED, element));
   }

   private void fireEvent(final NavigableSetEvent<E> event)
   {
      if (_listeners == null)
      {
         return;
      }

      for (final EventNavigableSetListener<E> listener : _listeners)
      {
         listener.eventOccured(event);
      }
   }

   private class NavigableSetEventImpl implements NavigableSetEvent<E>
   {
      protected final Type _type;
      protected final E _element;

      public NavigableSetEventImpl(final Type type, final E element)
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
      public ObservableEventNavigableSet<E> getSource()
      {
         return ObservableEventNavigableSet.this;
      }
   }

   protected static class ObservableEventSubNavigableSet<E> extends ObservableEventNavigableSet<E>
   {
      private static final long serialVersionUID = -4787480179100005627L;

      protected final ObservableEventNavigableSet<E> _parent;

      protected ObservableEventSubNavigableSet(final ObservableEventNavigableSet<E> parent,
            final NavigableSet<E> decorated)
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

   protected static class ObservableEventSubSortedSet<E> extends ObservableEventSortedSet<E>
   {
      private static final long serialVersionUID = -4787480179100005627L;

      protected final ObservableEventNavigableSet<E> _parent;

      protected ObservableEventSubSortedSet(final ObservableEventNavigableSet<E> parent, final SortedSet<E> decorated)
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
