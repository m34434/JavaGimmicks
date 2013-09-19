package net.sf.javagimmicks.collections.event;

import java.util.Collection;

public class CollectionEventImpl<E> implements CollectionEvent<E>
{
   protected final ObservableEventCollection<E> _source;

   protected final Type _type;
   protected final Collection<E> _elements;

   public CollectionEventImpl(final ObservableEventCollection<E> source, final Type type, final Collection<E> elements)
   {
      _source = source;
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
      return _source;
   }
}
