package net.sf.javagimmicks.collections.event;


public class SortedSetEvent<E>
{
   public static enum Type {ADDED, READDED, REMOVED};
   
   protected final ObservableEventSortedSet<E> _source;
   
   protected final Type _type;
   protected final E _element;
   
   public SortedSetEvent(ObservableEventSortedSet<E> source, Type type, E element)
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

   public ObservableEventSortedSet<E> getSource()
   {
      return _source;
   }
}
