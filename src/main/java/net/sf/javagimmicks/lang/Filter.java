package net.sf.javagimmicks.lang;

/**
 * This is an interface for objects that can filter other objects.
 * 
 * @param <E>
 *           the type of objects to filter
 */
public interface Filter<E>
{
   public boolean accepts(E element);
}
