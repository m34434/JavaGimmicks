package net.sf.javagimmicks.collections.event;

import java.util.Collection;

public class CollectionEvent<E>
{
   public static enum Type {ADDED, REMOVED};
   
   protected final ObservableEventCollection<E> _source;
   
   protected final Type _type;
   protected final Collection<E> _elements;
   
   public CollectionEvent(ObservableEventCollection<E> source, Type type, Collection<E> elements)
   {
      _source = source;
      _type = type;
      _elements = elements;
   }

   public Type getType()
   {
      return _type;
   }
   
   public Collection<E> getElements()
   {
      return _elements;
   }

   public ObservableEventCollection<E> getSource()
   {
      return _source;
   }
}
