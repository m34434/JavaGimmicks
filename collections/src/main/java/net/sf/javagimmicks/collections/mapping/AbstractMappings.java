package net.sf.javagimmicks.collections.mapping;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class AbstractMappings<L, R> implements Mappings<L, R>
{
   private static final long serialVersionUID = -8390090502631423671L;

   @Override
   public Set<Mapping<L, R>> getMappingSet()
   {
      return new MappingSet<L, R>(getLeftMap().entrySet());
   }

   @Override
   public Iterator<Mapping<L, R>> iterator()
   {
      return getMappingSet().iterator();
   }

   @Override
   public Mappings<R, L> getInverseMappings()
   {
      return new InverseMappings<L, R>(this);
   }

   @Override
   public Set<R> getRight(final L left)
   {
      return getLeftMap().get(left);
   }

   @Override
   public Set<L> getLeft(final R right)
   {
      return getRightMap().get(right);
   }

   @Override
   public boolean put(final L left, final R right)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean putLeft(final R right, final Collection<? extends L> c)
   {
      boolean result = false;

      for (final L left : c)
      {
         result |= put(left, right);
      }

      return result;
   }

   @Override
   public boolean putRight(final L left, final Collection<? extends R> c)
   {
      boolean result = false;

      for (final R right : c)
      {
         result |= put(left, right);
      }

      return result;
   }

   @Override
   public boolean remove(final L left, final R right)
   {
      final Set<R> mappedValuesLeft = getRight(left);

      return mappedValuesLeft != null ? mappedValuesLeft.remove(right) : false;
   }

   @Override
   public boolean containsLeft(final L left)
   {
      return getLeftMap().containsKey(left);
   }

   @Override
   public boolean containsRight(final R right)
   {
      return getRightMap().containsKey(right);
   }

   @Override
   public boolean contains(final L left, final R right)
   {
      final Set<R> rightSet = getRight(left);
      return rightSet != null && rightSet.contains(right);
   }

   @Override
   public Set<R> removeRight(final L left)
   {
      return getLeftMap().remove(left);
   }

   @Override
   public Set<L> removeLeft(final R right)
   {
      return getRightMap().remove(right);
   }

   @Override
   public void clear()
   {
      getLeftMap().clear();
   }

   @Override
   public int size()
   {
      return getMappingSet().size();
   }

   @Override
   public boolean isEmpty()
   {
      return getMappingSet().isEmpty();
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }

      if (!(obj instanceof Mappings<?, ?>))
      {
         return false;
      }

      final Mappings<?, ?> other = (Mappings<?, ?>) obj;

      return getMappingSet().equals(other.getMappingSet());
   }

   @Override
   public String toString()
   {
      return new StringBuilder()
            .append(getLeftMap())
            .append(" | ")
            .append(getRightMap())
            .toString();
   }

   protected static class MappingSet<L, R> extends AbstractSet<Mapping<L, R>>
   {
      protected final Set<Entry<L, Set<R>>> _entries;

      public MappingSet(final Set<Entry<L, Set<R>>> entries)
      {
         _entries = entries;
      }

      @Override
      public Iterator<Mapping<L, R>> iterator()
      {
         return new MappingIterator<L, R>(_entries.iterator());
      }

      @Override
      public int size()
      {
         int size = 0;
         for (final Entry<L, Set<R>> entry : _entries)
         {
            size += entry.getValue().size();
         }

         return size;
      }
   }

   protected static class MappingIterator<L, R> implements Iterator<Mapping<L, R>>
   {
      protected final Iterator<Entry<L, Set<R>>> _entryIterator;

      protected Iterator<R> _valueItertor;
      protected Entry<L, Set<R>> _currentEntry;
      protected R _currentValue;

      protected MappingIterator(final Iterator<Entry<L, Set<R>>> entryIterator)
      {
         _entryIterator = entryIterator;
      }

      @Override
      public boolean hasNext()
      {
         return (_valueItertor != null && _valueItertor.hasNext()) || _entryIterator.hasNext();
      }

      @Override
      public Mapping<L, R> next()
      {
         moveNext();

         return new DefaultMapping<L, R>(_currentEntry.getKey(), _currentValue);
      }

      @Override
      public void remove()
      {
         _valueItertor.remove();
         if (_currentEntry.getValue().isEmpty())
         {
            _entryIterator.remove();
         }
      }

      private void moveNext()
      {
         if (_valueItertor == null || !_valueItertor.hasNext())
         {
            _currentEntry = _entryIterator.next();
            _valueItertor = _currentEntry.getValue().iterator();
         }

         _currentValue = _valueItertor.next();
      }
   }

   protected static class InverseMappings<L, R> extends AbstractMappings<R, L>
   {
      private static final long serialVersionUID = -4525522714547396946L;

      protected final Mappings<L, R> _partner;

      public InverseMappings(final Mappings<L, R> partner)
      {
         _partner = partner;
      }

      @Override
      public Mappings<L, R> getInverseMappings()
      {
         return _partner;
      }

      @Override
      public boolean put(final R left, final L right)
      {
         return _partner.put(right, left);
      }

      @Override
      public void clear()
      {
         _partner.clear();
      }

      @Override
      public Map<R, Set<L>> getLeftMap()
      {
         return _partner.getRightMap();
      }

      @Override
      public Set<L> getRight(final R left)
      {
         return _partner.getLeft(left);
      }

      @Override
      public Set<R> getLeft(final L right)
      {
         return _partner.getRight(right);
      }

      @Override
      public Map<L, Set<R>> getRightMap()
      {
         return _partner.getLeftMap();
      }

      @Override
      public boolean remove(final R left, final L right)
      {
         return _partner.remove(right, left);
      }

      @Override
      public Set<L> removeRight(final R left)
      {
         return _partner.removeLeft(left);
      }

      @Override
      public Set<R> removeLeft(final L right)
      {
         return _partner.removeRight(right);
      }
   }
}
