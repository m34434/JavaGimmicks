package net.sf.javagimmicks.collections8.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link SortedMapEvent}s.
 */
@FunctionalInterface
public interface EventSortedMapListener<K, V> extends EventListener<SortedMapEvent<K, V>>
{}
