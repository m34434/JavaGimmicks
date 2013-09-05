package net.sf.javagimmicks.collections.decorators;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.javagimmicks.collections.Traverser;

/**
 * A basic class for {@link Traverser} decorators
 * that simply forwards all calls to an internal
 * delegate instance.
 */
public abstract class AbstractTraverserDecorator<E> implements Traverser<E>
{
   protected final Traverser<E> _decorated;

   protected AbstractTraverserDecorator(Traverser<E> decorated)
   {
      _decorated = decorated;
   }
   
   /**
    * Returns the decorated instance (the delegate)
    */
   public Traverser<E> getDecorated()
   {
      return _decorated;
   }
   
   public int size()
   {
      return getDecorated().size();
   }
   
   public boolean isEmpty()
   {
      return getDecorated().isEmpty();
   }

   public E get()
   {
      return getDecorated().get();
   }

   public void insertAfter(Collection<? extends E> collection)
   {
      getDecorated().insertAfter(collection);
   }

   public void insertAfter(E value)
   {
      getDecorated().insertAfter(value);
   }

   public void insertBefore(Collection<? extends E> collection)
   {
      getDecorated().insertBefore(collection);
   }

   public void insertBefore(E value)
   {
      getDecorated().insertBefore(value);
   }

   public E next()
   {
      return getDecorated().next();
   }

   public E next(int count)
   {
      return getDecorated().next(count);
   }

   public E previous()
   {
      return getDecorated().previous();
   }

   public E previous(int count)
   {
      return getDecorated().previous(count);
   }

   public E remove()
   {
      return getDecorated().remove();
   }

   public E set(E value)
   {
      return getDecorated().set(value);
   }

   public List<E> toList()
   {
      return getDecorated().toList();
   }

   public Traverser<E> traverser()
   {
      return getDecorated().traverser();
   }

   public Iterator<E> iterator()
   {
      return getDecorated().iterator();
   }
}
