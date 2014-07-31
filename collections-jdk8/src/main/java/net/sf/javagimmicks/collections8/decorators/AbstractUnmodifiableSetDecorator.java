package net.sf.javagimmicks.collections8.decorators;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A basic class for unmodifiable {@link Set} decorators that simply forwards
 * all read-calls to an internal delegate instance.
 */
public abstract class AbstractUnmodifiableSetDecorator<E> extends AbstractSet<E> implements Serializable
{
   private static final long serialVersionUID = -2933050231778253100L;

   protected final Set<E> _decorated;

   protected AbstractUnmodifiableSetDecorator(final Set<E> decorated)
   {
      _decorated = decorated;
   }

   @Override
   public boolean contains(final Object o)
   {
      return getDecorated().contains(o);
   }

   @Override
   public boolean containsAll(final Collection<?> c)
   {
      return getDecorated().containsAll(c);
   }

   @Override
   public boolean isEmpty()
   {
      return getDecorated().isEmpty();
   }

   @Override
   public int size()
   {
      return getDecorated().size();
   }

   @Override
   public Object[] toArray()
   {
      return getDecorated().toArray();
   }

   @Override
   public <T> T[] toArray(final T[] a)
   {
      return getDecorated().toArray(a);
   }

   @Override
   public String toString()
   {
      return getDecorated().toString();
   }

   @Override
   public void forEach(final Consumer<? super E> action)
   {
      getDecorated().forEach(action);
   }

   @Override
   public Spliterator<E> spliterator()
   {
      return getDecorated().spliterator();
   }

   @Override
   public Stream<E> stream()
   {
      return getDecorated().stream();
   }

   @Override
   public Stream<E> parallelStream()
   {
      return getDecorated().parallelStream();
   }

   protected Set<E> getDecorated()
   {
      return _decorated;
   }
}
