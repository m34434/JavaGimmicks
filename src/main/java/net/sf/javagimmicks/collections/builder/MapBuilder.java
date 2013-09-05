package net.sf.javagimmicks.collections.builder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapBuilder<K, V, T extends Map<K, V>>
{
   protected final T _internalMap;
   
   public static <K, V, T extends Map<K, V>> MapBuilder<K, V, T> create(T internalMap)
   {
      return new MapBuilder<K, V, T>(internalMap);
   }
   
   public static <K, V> MapBuilder<K, V, HashMap<K, V>> createHashMap()
   {
      return create(new HashMap<K, V>());
   }
   
   public static <K, V> MapBuilder<K, V, TreeMap<K, V>> createTreeMap()
   {
      return create(new TreeMap<K, V>());
   }
   
   public static <K, V> MapBuilder<K, V, TreeMap<K, V>> createTreeMap(Comparator<? super K> comparator)
   {
      return create(new TreeMap<K, V>(comparator));
   }
   
   public MapBuilder(T internalMap)
   {
      _internalMap = internalMap;
   }
   
   public MapBuilder<K, V, T> put(K key, V value)
   {
      _internalMap.put(key, value);
      return this;
   }
   
   public MapBuilder<K, V, T> putAll(Map<? extends K, ? extends V> map)
   {
      _internalMap.putAll(map);
      return this;
   }
   
   public MapBuilder<K, V, T> remove(Object key)
   {
      _internalMap.remove(key);
      return this;
   }
   
   public MapBuilder<K, V, T> clear()
   {
      _internalMap.clear();
      return this;
   }
   
   public T toMap()
   {
      return _internalMap;
   }
   
   public String toString()
   {
      return _internalMap.toString();
   }
}
