package net.sf.javagimmicks.collections8.composite;

import static net.sf.javagimmicks.collections8.transformer.TransformerUtils.decorate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

class CompositeIterator<E> implements Iterator<E>
{
   protected final Iterator<? extends Iterator<E>> _iterators;

   protected Iterator<E> _currentIterator;
   protected Iterator<E> _nextIterator;

   CompositeIterator(final List<? extends Iterator<E>> iterators)
   {
      _iterators = iterators.iterator();

      findNextIterator();
   }

   static <E, C extends Collection<E>> CompositeIterator<E> fromCollectionList(final List<C> collections)
   {
      final List<Iterator<E>> iteratorList = new ArrayList<Iterator<E>>(decorate(collections,
            Collection::iterator));

      return new CompositeIterator<E>(iteratorList);
   }

   @Override
   public boolean hasNext()
   {
      if (_currentIterator != null && _currentIterator.hasNext())
      {
         return true;
      }

      findNextIterator();
      return _nextIterator != null && _nextIterator.hasNext();
   }

   @Override
   public E next()
   {
      if (!hasNext())
      {
         throw new NoSuchElementException();
      }

      return moveNext();
   }

   @Override
   public void remove()
   {
      if (_currentIterator == null)
      {
         throw new IllegalStateException();
      }

      _currentIterator.remove();
   }

   protected void findNextIterator()
   {
      if (_nextIterator != null)
      {
         return;
      }

      while (_iterators.hasNext())
      {
         _nextIterator = _iterators.next();

         if (_nextIterator.hasNext())
         {
            break;
         }
      }
   }

   protected E moveNext()
   {
      if (_nextIterator != null)
      {
         _currentIterator = _nextIterator;
         _nextIterator = null;
      }

      return _currentIterator.next();
   }
}