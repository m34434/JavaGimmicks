package net.sf.javagimmicks.collections8.decorators;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.javagimmicks.collections8.RingCursor;

/**
 * A basic class for {@link RingCursor} decorators that simply forwards all
 * calls to an internal delegate instance.
 */
public abstract class AbstractRingCursorDecorator<E> implements RingCursor<E>
{
   protected final RingCursor<E> _decorated;

   protected AbstractRingCursorDecorator(final RingCursor<E> decorated)
   {
      _decorated = decorated;
   }

   @Override
   public int size()
   {
      return getDecorated().size();
   }

   @Override
   public boolean isEmpty()
   {
      return getDecorated().isEmpty();
   }

   @Override
   public E get()
   {
      return getDecorated().get();
   }

   public void insertAfter(final Collection<? extends E> collection)
   {
      getDecorated().insertAfter(collection);
   }

   @Override
   public void insertAfter(final E value)
   {
      getDecorated().insertAfter(value);
   }

   public void insertBefore(final Collection<? extends E> collection)
   {
      getDecorated().insertBefore(collection);
   }

   @Override
   public void insertBefore(final E value)
   {
      getDecorated().insertBefore(value);
   }

   @Override
   public E next()
   {
      return getDecorated().next();
   }

   @Override
   public E next(final int count)
   {
      return getDecorated().next(count);
   }

   @Override
   public E previous()
   {
      return getDecorated().previous();
   }

   @Override
   public E previous(final int count)
   {
      return getDecorated().previous(count);
   }

   @Override
   public E remove()
   {
      return getDecorated().remove();
   }

   @Override
   public E set(final E value)
   {
      return getDecorated().set(value);
   }

   @Override
   public List<E> toList()
   {
      return getDecorated().toList();
   }

   @Override
   public RingCursor<E> cursor()
   {
      return getDecorated().cursor();
   }

   @Override
   public Iterator<E> iterator()
   {
      return getDecorated().iterator();
   }

   protected RingCursor<E> getDecorated()
   {
      return _decorated;
   }
}
