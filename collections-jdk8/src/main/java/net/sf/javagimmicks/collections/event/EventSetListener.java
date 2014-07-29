package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link SetEvent}s.
 */
@FunctionalInterface
public interface EventSetListener<E> extends EventListener<SetEvent<E>>
{}
