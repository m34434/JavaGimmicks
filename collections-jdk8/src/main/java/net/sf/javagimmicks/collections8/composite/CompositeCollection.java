package net.sf.javagimmicks.collections8.composite;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;

class CompositeCollection<E> extends AbstractCollection<E>
{
   protected final List<? extends Collection<E>> _collections;

   CompositeCollection(final List<? extends Collection<E>> collections)
   {
      _collections = collections;
   }

   @Override
   public Iterator<E> iterator()
   {
      return CompositeIterator.fromCollectionList(_collections);
   }

   @Override
   public Spliterator<E> spliterator()
   {
      return CompositeSpliterator.fromCollectionList(_collections);
   }

   @Override
   public int size()
   {
      int result = 0;
      for (final Collection<E> c : _collections)
      {
         result += c.size();
      }

      return result;
   }
}
