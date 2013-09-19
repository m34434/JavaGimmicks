package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link SortedSetEvent}s.
 */
public interface EventSortedSetListener<E> extends EventListener<SortedSetEvent<E>, EventSortedSetListener<E>>
{}
