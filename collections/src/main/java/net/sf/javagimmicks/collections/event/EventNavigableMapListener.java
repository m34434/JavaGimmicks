package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link NavigableMapEvent}s.
 */
public interface EventNavigableMapListener<K, V> extends EventListener<NavigableMapEvent<K, V>>
{}
