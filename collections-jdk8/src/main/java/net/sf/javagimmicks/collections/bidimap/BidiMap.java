package net.sf.javagimmicks.collections.bidimap;

import java.util.Map;

/**
 * Defines a {@link Map} that is bi-directional which means that values are
 * unique and reverse lookup (value to key) is possible.
 */
public interface BidiMap<K, V> extends Map<K, V>
{
   /**
    * Returns an inverted view of this {@link BidiMap} (keys and values are
    * exchanged).
    * 
    * @return an inverted view of this {@link BidiMap}
    */
   public BidiMap<V, K> inverseBidiMap();

   /**
    * Retrieves the key that belongs to the given value.
    * 
    * @param value
    *           the value whose key should be looked up
    * @return the key for the given value or {@code null} if there is no such
    *         value
    */
   public K getKey(V value);
}
