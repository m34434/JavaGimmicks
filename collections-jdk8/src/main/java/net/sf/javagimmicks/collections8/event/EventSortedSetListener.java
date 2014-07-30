package net.sf.javagimmicks.collections8.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link SortedSetEvent}s.
 */
@FunctionalInterface
public interface EventSortedSetListener<E> extends EventListener<SortedSetEvent<E>>
{}
