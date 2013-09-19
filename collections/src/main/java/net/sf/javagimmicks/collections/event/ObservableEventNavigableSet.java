package net.sf.javagimmicks.collections.event;

import java.util.NavigableSet;
import java.util.SortedSet;

import net.sf.javagimmicks.collections.event.NavigableSetEvent.Type;
import net.sf.javagimmicks.event.Observable;
import net.sf.javagimmicks.event.ObservableBase;

public class ObservableEventNavigableSet<E> extends AbstractEventNavigableSet<E> implements
      Observable<NavigableSetEvent<E>, EventNavigableSetListener<E>>
{
   private static final long serialVersionUID = -6812183248508925850L;

   protected final ObservableBase<NavigableSetEvent<E>, EventNavigableSetListener<E>> _helper = new ObservableBase<NavigableSetEvent<E>, EventNavigableSetListener<E>>();

   public ObservableEventNavigableSet(final NavigableSet<E> decorated)
   {
      super(decorated);
   }

   @Override
   public void addEventListener(final EventNavigableSetListener<E> listener)
   {
      _helper.addEventListener(listener);
   }

   @Override
   public void removeEventListener(final EventNavigableSetListener<E> listener)
   {
      _helper.removeEventListener(listener);
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
      _helper.fireEvent(new NavigableSetEventImpl(Type.ADDED, element));
   }

   @Override
   protected void fireElementReadded(final E element)
   {
      _helper.fireEvent(new NavigableSetEventImpl(Type.READDED, element));
   }

   @Override
   protected void fireElementRemoved(final E element)
   {
      _helper.fireEvent(new NavigableSetEventImpl(Type.REMOVED, element));
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
