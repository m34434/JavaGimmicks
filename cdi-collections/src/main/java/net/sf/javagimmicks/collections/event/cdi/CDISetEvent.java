package net.sf.javagimmicks.collections.event.cdi;

public class CDISetEvent
{
   public static enum Type
   {
      ADDED, READDED, REMOVED
   };

   protected final CDIEventSet<?> _source;

   protected final Type _type;
   protected final Object _element;

   public <E> CDISetEvent(final CDIEventSet<E> source, final Type type, final E element)
   {
      _source = source;

      _type = type;
      _element = element;
   }

   public Type getType()
   {
      return _type;
   }

   public Object getElement()
   {
      return _element;
   }

   public CDIEventSet<?> getSource()
   {
      return _source;
   }
}
