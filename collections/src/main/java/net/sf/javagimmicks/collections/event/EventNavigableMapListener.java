package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.EventListener;

public interface EventNavigableMapListener<K, V> extends
      EventListener<NavigableMapEvent<K, V>, EventNavigableMapListener<K, V>>
{}
