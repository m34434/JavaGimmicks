package net.sf.javagimmicks.collections8.decorators;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

/**
 * A basic class for unmodifiable {@link Map} decorators that simply forwards
 * all read-calls to an internal delegate instance.
 */
public abstract class AbstractUnmodifiableMapDecorator<K, V> extends AbstractMap<K, V> implements Serializable
{
   private static final long serialVersionUID = -760336385294119474L;

   protected final Map<K, V> _decorated;

   protected AbstractUnmodifiableMapDecorator(final Map<K, V> decorated)
   {
      _decorated = decorated;
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
   public int size()
   {
      return getDecorated().size();
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
