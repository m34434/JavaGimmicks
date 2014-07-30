package net.sf.javagimmicks.collections8.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link NavigableMapEvent}s.
 */
@FunctionalInterface
public interface EventNavigableMapListener<K, V> extends EventListener<NavigableMapEvent<K, V>>
{}
