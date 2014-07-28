package net.sf.javagimmicks.collections.decorators;

import java.io.Serializable;
import java.util.Map.Entry;

/**
 * A basic class for {@link Entry} decorators that simply forwards all calls to
 * an internal delegate instance.
 */
public abstract class AbstractEntryDecorator<K, V> implements Entry<K, V>, Serializable
{
   private static final long serialVersionUID = 8776856876384827340L;

   protected final Entry<K, V> _decorated;

   protected AbstractEntryDecorator(final Entry<K, V> decorated)
   {
      _decorated = decorated;
   }

   @Override
   public K getKey()
   {
      return getDecorated().getKey();
   }

   @Override
   public V getValue()
   {
      return getDecorated().getValue();
   }

   @Override
   public V setValue(final V value)
   {
      return getDecorated().setValue(value);
   }

   protected Entry<K, V> getDecorated()
   {
      return _decorated;
   }
}
