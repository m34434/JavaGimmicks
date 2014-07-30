package net.sf.javagimmicks.collections8.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link NavigableSetEvent}s.
 */
@FunctionalInterface
public interface EventNavigableSetListener<E> extends EventListener<NavigableSetEvent<E>>
{}
