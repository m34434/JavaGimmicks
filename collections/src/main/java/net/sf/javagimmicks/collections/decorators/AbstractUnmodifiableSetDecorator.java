package net.sf.javagimmicks.collections.decorators;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Set;

/**
 * A basic class for unmodifiable {@link Set}
 * decorators that simply forwards all read-calls
 * to an internal delegate instance.
 */
public abstract class AbstractUnmodifiableSetDecorator<E> extends AbstractSet<E> implements Serializable
{
   private static final long serialVersionUID = -2933050231778253100L;

   protected final Set<E> _decorated;
   
   protected AbstractUnmodifiableSetDecorator(Set<E> decorated)
   {
      _decorated = decorated;
   }
   
   /**
    * Returns the decorated instance (the delegate)
    */
   public Set<E> getDecorated()
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
