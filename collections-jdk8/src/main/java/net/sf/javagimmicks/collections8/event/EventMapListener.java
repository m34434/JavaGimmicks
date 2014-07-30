package net.sf.javagimmicks.collections8.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link MapEvent}s.
 */
@FunctionalInterface
public interface EventMapListener<K, V> extends EventListener<MapEvent<K, V>>
{}
