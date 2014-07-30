package net.sf.javagimmicks.collections8.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link SetEvent}s.
 */
@FunctionalInterface
public interface EventSetListener<E> extends EventListener<SetEvent<E>>
{}
