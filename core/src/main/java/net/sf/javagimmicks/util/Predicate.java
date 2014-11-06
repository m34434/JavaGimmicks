package net.sf.javagimmicks.util;

/**
 * An interface for objects that can filter other objects.
 * 
 * @param <E>
 *           the type of objects to filter
 */
public interface Predicate<E>
{
   /**
    * Determines of a given element is accepted by this {@link Predicate}.
    * 
    * @param element
    *           the element to filter out or not
    * @return if the given element is accepted by this {@link Predicate}
    */
   public boolean test(E element);
}
