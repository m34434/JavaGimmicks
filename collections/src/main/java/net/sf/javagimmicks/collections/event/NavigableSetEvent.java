package net.sf.javagimmicks.collections.event;


public class NavigableSetEvent<E>
{
   public static enum Type {ADDED, READDED, REMOVED};
   
   protected final ObservableEventNavigableSet<E> _source;
   
   protected final Type _type;
   protected final E _element;
   
   public NavigableSetEvent(ObservableEventNavigableSet<E> source, Type type, E element)
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

   public ObservableEventNavigableSet<E> getSource()
   {
      return _source;
   }
}
