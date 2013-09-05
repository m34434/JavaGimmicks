package net.sf.javagimmicks.collections;

public interface Traversable<E> extends Iterable<E>
{
    public Traverser<E> traverser();
}
