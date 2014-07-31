package net.sf.javagimmicks.collections8.decorators;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A basic class for {@link Map} decorators that simply forwards all calls to an
 * internal delegate instance.
 */
public abstract class AbstractMapDecorator<K, V> implements Map<K, V>, Serializable
{
   private static final long serialVersionUID = 7755485389940064486L;

   protected final Map<K, V> _decorated;

   protected AbstractMapDecorator(final Map<K, V> decorated)
   {
      _decorated = decorated;
   }

   @Override
   public void clear()
   {
      getDecorated().clear();
   }

   @Override
   public boolean containsKey(final Object key)
   {
      return getDecorated().containsKey(key);
   }

   @Override
   public boolean containsValue(final Object value)
   {
      return getDecorated().containsValue(value);
   }

   @Override
   public Set<Map.Entry<K, V>> entrySet()
   {
      return getDecorated().entrySet();
   }

   @Override
   public V get(final Object key)
   {
      return getDecorated().get(key);
   }

   @Override
   public boolean isEmpty()
   {
      return getDecorated().isEmpty();
   }

   @Override
   public Set<K> keySet()
   {
      return getDecorated().keySet();
   }

   @Override
   public V put(final K key, final V value)
   {
      return getDecorated().put(key, value);
   }

   @Override
   public void putAll(final Map<? extends K, ? extends V> m)
   {
      getDecorated().putAll(m);
   }

   @Override
   public V remove(final Object key)
   {
      return getDecorated().remove(key);
   }

   @Override
   public int size()
   {
      return getDecorated().size();
   }

   @Override
   public Collection<V> values()
   {
      return getDecorated().values();
   }

   @Override
   public String toString()
   {
      return getDecorated().toString();
   }

   @Override
   public V getOrDefault(final Object key, final V defaultValue)
   {
      return getDecorated().getOrDefault(key, defaultValue);
   }

   @Override
   public void forEach(final BiConsumer<? super K, ? super V> action)
   {
      getDecorated().forEach(action);
   }

   @Override
   public void replaceAll(final BiFunction<? super K, ? super V, ? extends V> function)
   {
      getDecorated().replaceAll(function);
   }

   @Override
   public V putIfAbsent(final K key, final V value)
   {
      return getDecorated().putIfAbsent(key, value);
   }

   @Override
   public boolean remove(final Object key, final Object value)
   {
      return getDecorated().remove(key, value);
   }

   @Override
   public boolean replace(final K key, final V oldValue, final V newValue)
   {
      return getDecorated().replace(key, oldValue, newValue);
   }

   @Override
   public V replace(final K key, final V value)
   {
      return getDecorated().replace(key, value);
   }

   @Override
   public V computeIfAbsent(final K key, final Function<? super K, ? extends V> mappingFunction)
   {
      return getDecorated().computeIfAbsent(key, mappingFunction);
   }

   @Override
   public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction)
   {
      return getDecorated().computeIfPresent(key, remappingFunction);
   }

   @Override
   public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> remappingFunction)
   {
      return getDecorated().compute(key, remappingFunction);
   }

   @Override
   public V merge(final K key, final V value, final BiFunction<? super V, ? super V, ? extends V> remappingFunction)
   {
      return getDecorated().merge(key, value, remappingFunction);
   }

   protected Map<K, V> getDecorated()
   {
      return _decorated;
   }
}
