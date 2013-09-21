package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link SortedMapEvent}s.
 */
public interface EventSortedMapListener<K, V> extends EventListener<SortedMapEvent<K, V>>
{}
