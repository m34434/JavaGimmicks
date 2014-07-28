package net.sf.javagimmicks.collections.decorators;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A basic class for {@link Collection} decorators that simply forwards all
 * calls to an internal delegate instance.
 */
public abstract class AbstractCollectionDecorator<E> implements Collection<E>, Serializable
{
   private static final long serialVersionUID = -5203666410345126066L;

   protected final Collection<E> _decorated;

   protected AbstractCollectionDecorator(final Collection<E> decorated)
   {
      _decorated = decorated;
   }

   @Override
   public boolean add(final E e)
   {
      return getDecorated().add(e);
   }

   @Override
   public boolean addAll(final Collection<? extends E> c)
   {
      return getDecorated().addAll(c);
   }

   @Override
   public void clear()
   {
      getDecorated().clear();
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
   public Iterator<E> iterator()
   {
      return getDecorated().iterator();
   }

   @Override
   public boolean remove(final Object o)
   {
      return getDecorated().remove(o);
   }

   @Override
   public boolean removeAll(final Collection<?> c)
   {
      return getDecorated().removeAll(c);
   }

   @Override
   public boolean retainAll(final Collection<?> c)
   {
      return getDecorated().retainAll(c);
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
   public void forEach(Consumer<? super E> action)
   {
      getDecorated().forEach(action);
   }

   @Override
   public Spliterator<E> spliterator()
   {
      return getDecorated().spliterator();
   }

   @Override
   public boolean removeIf(Predicate<? super E> filter)
   {
      return getDecorated().removeIf(filter);
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
