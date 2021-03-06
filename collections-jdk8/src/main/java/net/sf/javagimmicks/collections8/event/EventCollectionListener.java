package net.sf.javagimmicks.collections8.event;

import net.sf.javagimmicks.event.EventListener;

/**
 * An {@link EventListener} the observes {@link CollectionEvent}s.
 */
@FunctionalInterface
public interface EventCollectionListener<E> extends EventListener<CollectionEvent<E>>
{}
