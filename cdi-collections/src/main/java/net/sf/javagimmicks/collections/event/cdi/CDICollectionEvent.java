package net.sf.javagimmicks.collections.event.cdi;

import java.util.Collection;

public class CDICollectionEvent
{
   public static enum Type
   {
      ADDED, REMOVED
   };

   protected final CDIEventCollection<?> _source;

   protected final Type _type;
   protected final Collection<?> _elements;

   public <E> CDICollectionEvent(final CDIEventCollection<E> source, final Type type, final Collection<E> elements)
   {
      _source = source;
      _type = type;
      _elements = elements;
   }

   public Type getType()
   {
      return _type;
   }

   public Collection<?> getElements()
   {
      return _elements;
   }

   public CDIEventCollection<?> getSource()
   {
      return _source;
   }
}
