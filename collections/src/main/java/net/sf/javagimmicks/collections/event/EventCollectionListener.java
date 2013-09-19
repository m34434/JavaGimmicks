package net.sf.javagimmicks.collections.event;

import net.sf.javagimmicks.event.EventListener;

public interface EventCollectionListener<E> extends
      EventListener<CollectionEvent<E>, EventCollectionListener<E>>
{}
