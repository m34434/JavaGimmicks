package net.sf.javagimmicks.collections8.decorators;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

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

   protected Map<K, V> getDecorated()
   {
      return _decorated;
   }
}
