package net.sf.javagimmicks.collections8.builder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * A builder class for building {@link Map}s using a fluent API with one chained
 * call.
 * 
 * @param <K>
 *           the type of keys the resulting {@link Map} will use
 * @param <V>
 *           the type of values the resulting {@link Map} will use
 * @param <T>
 *           the type of the produced {@link Map}
 */
public class MapBuilder<K, V, T extends Map<K, V>> implements Supplier<T>
{
   protected final T _internalMap;

   /**
    * Wraps a new {@link MapBuilder} around the given {@link Map}
    * 
    * @param internalMap
    *           the {@link Map} to wrap a new {@link MapBuilder} around
    * @param <K>
    *           the type of keys the resulting {@link Map} will use
    * @param <V>
    *           the type of values the resulting {@link Map} will use
    * @param <T>
    *           the type of the produced {@link Map}
    * @return the resulting {@link MapBuilder}
    */
   public static <K, V, T extends Map<K, V>> MapBuilder<K, V, T> create(final T internalMap)
   {
      return new MapBuilder<K, V, T>(internalMap);
   }

   /**
    * Creates a new {@link MapBuilder} for building a new {@link HashMap}
    * 
    * @param <K>
    *           the type of keys the resulting {@link HashMap} will use
    * @param <V>
    *           the type of values the resulting {@link HashMap} will use
    * @return the resulting {@link MapBuilder}
    */
   public static <K, V> MapBuilder<K, V, HashMap<K, V>> createHashMap()
   {
      return create(new HashMap<K, V>());
   }

   /**
    * Creates a new {@link MapBuilder} for building a new {@link TreeMap}
    * 
    * @param <K>
    *           the type of keys the resulting {@link TreeMap} will use
    * @param <V>
    *           the type of values the resulting {@link TreeMap} will use
    * @return the resulting {@link MapBuilder}
    */
   public static <K, V> MapBuilder<K, V, TreeMap<K, V>> createTreeMap()
   {
      return create(new TreeMap<K, V>());
   }

   /**
    * Creates a new {@link MapBuilder} for building a new {@link TreeMap} based
    * on the given {@link Comparator}
    * 
    * @param comparator
    *           the {@link Comparator} for the {@link TreeMap} to create
    * @param <K>
    *           the type of keys the resulting {@link TreeMap} will use
    * @param <V>
    *           the type of values the resulting {@link TreeMap} will use
    * @return the resulting {@link MapBuilder}
    */
   public static <K, V> MapBuilder<K, V, TreeMap<K, V>> createTreeMap(final Comparator<? super K> comparator)
   {
      return create(new TreeMap<K, V>(comparator));
   }

   /**
    * Creates a new {@link MapBuilder} around a given internal {@link Map}
    * 
    * @param internalMap
    *           the {@link Map} to wrap around
    */
   public MapBuilder(final T internalMap)
   {
      _internalMap = internalMap;
   }

   /**
    * Calls {@link Map#put(Object, Object)} on the underlying {@link Map} and
    * returns itself
    * 
    * @param key
    *           key with which the specified value is to be associated
    * @param value
    *           value to be associated with the specified key
    * @return the {@link MapBuilder} itself
    * @see Map#put(Object, Object)
    */
   public MapBuilder<K, V, T> put(final K key, final V value)
   {
      _internalMap.put(key, value);
      return this;
   }

   /**
    * Calls {@link Map#putAll(Map)} on the underlying {@link Map} and returns
    * itself
    * 
    * @param map
    *           mappings to be stored in this map
    * @return the {@link MapBuilder} itself
    * @see Map#putAll(Map)
    */
   public MapBuilder<K, V, T> putAll(final Map<? extends K, ? extends V> map)
   {
      _internalMap.putAll(map);
      return this;
   }

   /**
    * Calls {@link Map#remove(Object)} on the underlying {@link Map} and returns
    * itself
    * 
    * @param key
    *           key whose mapping is to be removed from the map
    * @return the {@link MapBuilder} itself
    * @see Map#remove(Object)
    */
   public MapBuilder<K, V, T> remove(final Object key)
   {
      _internalMap.remove(key);
      return this;
   }

   /**
    * Calls {@link Map#clear()} on the underlying {@link Map} and returns itself
    * 
    * @return the {@link MapBuilder} itself
    * @see Map#clear()
    */
   public MapBuilder<K, V, T> clear()
   {
      _internalMap.clear();
      return this;
   }

   /**
    * Returns the underlying {@link Map}
    * 
    * @return the underlying {@link Map}
    */
   @Override
   public T get()
   {
      return _internalMap;
   }

   @Override
   public String toString()
   {
      return _internalMap.toString();
   }
}
