package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.lang.LangUtils;

public class MapEvent<K, V>
{  
   public static enum Type {ADDED, UPDATED, REMOVED};
   
   protected final ObservableEventMap<K, V> _source;
   
   protected final Type _type;
   protected final K _key;
   protected final V _value;
   protected final V _newValue;
   
   public MapEvent(ObservableEventMap<K, V> source, Type type, K key, V value, V newValue)
   {
      _source = source;
      _type = type;
      _key = key;
      _value = value;
      _newValue = newValue;
   }

   public MapEvent(ObservableEventMap<K, V> source, Type type, K key, V value)
   {
      this(source, type, key, value, null);
   }
   
   public ObservableEventMap<K, V> getSource()
   {
      return _source;
   }

   public Type getType()
   {
      return _type;
   }

   public K getKey()
   {
      return _key;
   }

   public V getValue()
   {
      return _value;
   }

   public V getNewValue()
   {
      return _newValue;
   }
   
   public boolean equals(Object o)
   {
      if(!(o instanceof MapEvent<?, ?>))
      {
         return false;
      }
      
      MapEvent<?, ?> other = (MapEvent<?, ?>)o;
      return
         _source == other._source &&
         _type == other._type &&
         LangUtils.equalsNullSafe(_key, other._key) &&
         LangUtils.equalsNullSafe(_value, other._value) &&
         LangUtils.equalsNullSafe(_newValue, other._newValue);
   }
}
