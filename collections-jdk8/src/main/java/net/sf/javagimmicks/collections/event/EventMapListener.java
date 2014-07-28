package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link MapEvent}s.
 */
public interface EventMapListener<K, V> extends EventListener<MapEvent<K, V>>
{}
