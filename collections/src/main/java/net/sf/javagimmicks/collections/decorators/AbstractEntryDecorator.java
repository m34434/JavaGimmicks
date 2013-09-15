package net.sf.javagimmicks.collections.decorators;

import java.io.Serializable;
import java.util.Map.Entry;

/**
 * A basic class for {@link Entry} decorators
 * that simply forwards all calls to an internal
 * delegate instance.
 */
public abstract class AbstractEntryDecorator<K, V> implements Entry<K, V>, Serializable
{
   private static final long serialVersionUID = 8776856876384827340L;

   protected final Entry<K, V> _decorated;

   protected AbstractEntryDecorator(Entry<K, V> decorated)
   {
      _decorated = decorated;
   }
   
   /**
    * Returns the decorated instance (the delegate)
    */
   public Entry<K, V> getDecorated()
   {
      return _decorated;
   }

   public K getKey()
   {
      return getDecorated().getKey();
   }

   public V getValue()
   {
      return getDecorated().getValue();
   }

   public V setValue(V value)
   {
      return getDecorated().setValue(value);
   }
}
