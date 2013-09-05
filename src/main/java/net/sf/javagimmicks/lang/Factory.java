package net.sf.javagimmicks.lang;

/**
 * A simple interface for factory classes - classes the generate instances of (other) classes
 * @param <E> the type of the instances to create
 */
public interface Factory<E>
{
   public E create();
}
