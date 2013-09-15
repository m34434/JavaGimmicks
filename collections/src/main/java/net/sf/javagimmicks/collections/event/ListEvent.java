package net.sf.javagimmicks.collections.event;

import java.util.List;

public class ListEvent<E>
{
   public static enum Type {ADDED, UPDATED, REMOVED};
   
   protected final ObservableEventList<E> _source;
   
   protected final Type _type;
   protected final int _fromIndex;
   protected final int _toIndex;
   protected final List<E> _elements;
   protected final List<E> _newElements;
   
   public ListEvent(ObservableEventList<E> source, Type type, int fromIndex, int toIndex, List<E> element, List<E> newElement)
   {
      _source = source;
      
      _type = type;
      _fromIndex = fromIndex;
      _toIndex = toIndex;
      
      _elements = element;
      _newElements = newElement;
   }
   
   public ListEvent(ObservableEventList<E> source, Type type, int fromIndex, int toIndex, List<E> element)
   {
      this(source, type, fromIndex, toIndex, element, null);
   }

   public Type getType()
   {
      return _type;
   }
   
   public int getFromIndex()
   {
      return _fromIndex;
   }
   
   public int getToIndex()
   {
      return _toIndex;
   }
   
   public List<E> getElements()
   {
      return _elements;
   }
   
   public List<E> getNewElements()
   {
      return _newElements;
   }

   public ObservableEventList<E> getSource()
   {
      return _source;
   }
}
