package net.sf.javagimmicks.collections.bidimap;

import java.util.Map;

public interface BidiMap<K, V> extends Map<K, V>
{
   public BidiMap<V, K> inverseBidiMap();
   
   public K getKey(V value);
}
