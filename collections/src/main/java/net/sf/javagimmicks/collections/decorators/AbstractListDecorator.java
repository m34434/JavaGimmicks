package net.sf.javagimmicks.collections.decorators;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * A basic class for {@link List} decorators that simply forwards all calls to
 * an internal delegate instance.
 */
public abstract class AbstractListDecorator<E> extends AbstractCollectionDecorator<E> implements List<E>
{
   private static final long serialVersionUID = -1438615027180189129L;

   protected AbstractListDecorator(final List<E> decorated)
   {
      super(decorated);
   }

   @Override
   public void add(final int index, final E element)
   {
      getDecorated().add(index, element);
   }

   @Override
   public boolean addAll(final int index, final Collection<? extends E> c)
   {
      return getDecorated().addAll(index, c);
   }

   @Override
   public E get(final int index)
   {
      return getDecorated().get(index);
   }

   @Override
   public int indexOf(final Object o)
   {
      return getDecorated().indexOf(o);
   }

   @Override
   public int lastIndexOf(final Object o)
   {
      return getDecorated().lastIndexOf(o);
   }

   @Override
   public ListIterator<E> listIterator()
   {
      return getDecorated().listIterator();
   }

   @Override
   public ListIterator<E> listIterator(final int index)
   {
      return getDecorated().listIterator(index);
   }

   @Override
   public E remove(final int index)
   {
      return getDecorated().remove(index);
   }

   @Override
   public E set(final int index, final E element)
   {
      return getDecorated().set(index, element);
   }

   @Override
   public List<E> subList(final int fromIndex, final int toIndex)
   {
      return getDecorated().subList(fromIndex, toIndex);
   }

   @Override
   protected List<E> getDecorated()
   {
      return (List<E>) super.getDecorated();
   }
}
