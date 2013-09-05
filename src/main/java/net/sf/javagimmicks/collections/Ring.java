package net.sf.javagimmicks.collections;

import java.util.Collection;

/**
 * Defines a {@link Collection}-like data structure that organizes elements
 * within a ring structure, so there is a well-defined order but no index,
 * beginning or end.
 * <p>
 * The interface fully extends the {@link Collection} interface but additionally
 * extends {@link Traversable} that allows to create a {@link Traverser} for it
 * which is a special kind of iterator that defines not beginning or end but
 * operations to modify or traverse the underlying data structure.
 * 
 * @param <E>
 *           the type of elements this {@link Ring} can contain
 */
public interface Ring<E> extends Collection<E>, Traversable<E>
{}
