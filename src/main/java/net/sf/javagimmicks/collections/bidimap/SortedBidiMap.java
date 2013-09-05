package net.sf.javagimmicks.collections.bidimap;

import java.util.SortedMap;

public interface SortedBidiMap<K, V> extends SortedMap<K, V>, BidiMap<K, V>
{
   public SortedBidiMap<V, K> inverseBidiSortedMap();
}
