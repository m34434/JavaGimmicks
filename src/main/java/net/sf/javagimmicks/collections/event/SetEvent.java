package net.sf.javagimmicks.collections.event;


public class SetEvent<E>
{
   public static enum Type {ADDED, READDED, REMOVED};
   
   protected final ObservableEventSet<E> _source;
   
   protected final Type _type;
   protected final E _element;
   
   public SetEvent(ObservableEventSet<E> source, Type type, E element)
   {
      _source = source;
      
      _type = type;
      _element = element;
   }

   public Type getType()
   {
      return _type;
   }
   
   public E getElement()
   {
      return _element;
   }

   public ObservableEventSet<E> getSource()
   {
      return _source;
   }
}
