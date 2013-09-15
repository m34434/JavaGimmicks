package net.sf.javagimmicks.collections.composite;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class CompositeCollection<E> extends AbstractCollection<E>
{
   protected final List<? extends Collection<E>> _collections;

   CompositeCollection(List<? extends Collection<E>> collections)
   {
      _collections = collections;
   }

   @Override
   public Iterator<E> iterator()
   {
      return CompositeIterator.fromCollectionList(_collections);
   }

   @Override
   public int size()
   {
      int result = 0;
      for(Collection<E> c : _collections)
      {
         result += c.size();
      }
      
      return result;
   }
}
