package net.sf.javagimmicks.collections.event;


public interface SortedSetEvent<E>
{

   enum Type {ADDED, READDED, REMOVED}

   Type getType();

   E getElement();

   ObservableEventSortedSet<E> getSource();

}