package net.sf.javagimmicks.collections.decorators;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMapDecorator<K, V> implements Map<K, V>, Serializable
{
   private static final long serialVersionUID = 7755485389940064486L;

   protected final Map<K, V> _decorated;

   protected AbstractMapDecorator(Map<K, V> decorated)
   {
      _decorated = decorated;
   }
   
   public Map<K, V> getDecorated()
   {
      return _decorated;
   }

   public void clear()
   {
      getDecorated().clear();
   }

   public boolean containsKey(Object key)
   {
      return getDecorated().containsKey(key);
   }

   public boolean containsValue(Object value)
   {
      return getDecorated().containsValue(value);
   }

   public Set<Map.Entry<K, V>> entrySet()
   {
      return getDecorated().entrySet();
   }

   public V get(Object key)
   {
      return getDecorated().get(key);
   }

   public boolean isEmpty()
   {
      return getDecorated().isEmpty();
   }

   public Set<K> keySet()
   {
      return getDecorated().keySet();
   }

   public V put(K key, V value)
   {
      return getDecorated().put(key, value);
   }

   public void putAll(Map<? extends K, ? extends V> m)
   {
      getDecorated().putAll(m);
   }

   public V remove(Object key)
   {
      return getDecorated().remove(key);
   }

   public int size()
   {
      return getDecorated().size();
   }

   public Collection<V> values()
   {
      return getDecorated().values();
   }
   
   public String toString()
   {
      return getDecorated().toString();
   }
}
