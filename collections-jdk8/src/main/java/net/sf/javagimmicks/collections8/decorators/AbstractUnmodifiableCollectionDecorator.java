package net.sf.javagimmicks.collections8.decorators;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A basic class for unmodifiable {@link Collection} decorators that simply
 * forwards all read-calls to an internal delegate instance.
 */
public abstract class AbstractUnmodifiableCollectionDecorator<E> extends AbstractCollection<E> implements Serializable
{
   private static final long serialVersionUID = -5485318485656641240L;

   protected final Collection<E> _decorated;

   protected AbstractUnmodifiableCollectionDecorator(final Collection<E> decorated)
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

   protected Collection<E> getDecorated()
   {
      return _decorated;
   }
}
