package net.sf.javagimmicks.collections.bidimap;

import java.util.SortedMap;

/**
 * A {@link BidiMap} extension of {@link SortedMap}.
 */
public interface SortedBidiMap<K, V> extends SortedMap<K, V>, BidiMap<K, V>
{
   public SortedBidiMap<V, K> inverseBidiSortedMap();
}
