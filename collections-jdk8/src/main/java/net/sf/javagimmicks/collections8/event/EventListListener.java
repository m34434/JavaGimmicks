package net.sf.javagimmicks.collections8.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link ListEvent}s.
 */
@FunctionalInterface
public interface EventListListener<E> extends EventListener<ListEvent<E>>
{}
