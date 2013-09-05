package net.sf.javagimmicks.collections.decorators;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;

/**
 * A basic class for unmodifiable {@link Collection}
 * decorators that simply forwards all read-calls
 * to an internal delegate instance.
 */
public abstract class AbstractUnmodifiableCollectionDecorator<E> extends AbstractCollection<E> implements Serializable
{
   private static final long serialVersionUID = -5485318485656641240L;

   protected final Collection<E> _decorated;
   
   protected AbstractUnmodifiableCollectionDecorator(Collection<E> decorated)
   {
      _decorated = decorated;
   }
   
   public Collection<E> getDecorated()
   {
      return _decorated;
   }

   public boolean contains(Object o)
   {
      return getDecorated().contains(o);
   }

   public boolean containsAll(Collection<?> c)
   {
      return getDecorated().containsAll(c);
   }

   public boolean isEmpty()
   {
      return getDecorated().isEmpty();
   }

   public int size()
   {
      return getDecorated().size();
   }

   public Object[] toArray()
   {
      return getDecorated().toArray();
   }

   public <T> T[] toArray(T[] a)
   {
      return getDecorated().toArray(a);
   }
   
   public String toString()
   {
      return getDecorated().toString();
   }
}
