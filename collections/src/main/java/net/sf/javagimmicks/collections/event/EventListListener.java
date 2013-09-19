package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link ListEvent}s.
 */
public interface EventListListener<E> extends EventListener<ListEvent<E>, EventListListener<E>>
{}
