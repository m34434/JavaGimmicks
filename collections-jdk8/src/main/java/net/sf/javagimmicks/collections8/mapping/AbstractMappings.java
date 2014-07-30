package net.sf.javagimmicks.collections8.mapping;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * An abstract implementation of {@link Mappings} that provides default
 * implementations for almost all methods.
 * <p>
 * Methods to be implemented by subclasses are:
 * <ul>
 * <li>{@link #getLeftView()}</li>
 * <li>{@link #getRightView()}</li>
 * <li>{@link #put(Object, Object)}</li>
 * </ul>
 */
public abstract class AbstractMappings<L, R> implements Mappings<L, R>, Serializable
{
   private static final long serialVersionUID = -8390090502631423671L;

   protected AbstractMappings()
   {}

   @Override
   public Set<Mapping<L, R>> getMappingSet()
   {
      return new MappingSet<L, R>(getLeftView().entrySet());
   }

   @Override
   public Mappings<R, L> invert()
   {
      return new InverseMappings<L, R>(this);
   }

   @Override
   public boolean put(final L left, final R right)
   {
      throw new UnsupportedOperationException();
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
            .append(getLeftView())
            .append(" | ")
            .append(getRightView())
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
      public Mappings<L, R> invert()
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
      public Map<R, Set<L>> getLeftView()
      {
         return _partner.getRightView();
      }

      @Override
      public Set<L> getAllForLeftKey(final R left)
      {
         return _partner.getAllForRightKey(left);
      }

      @Override
      public Set<R> getAllForRightKey(final L right)
      {
         return _partner.getAllForLeftKey(right);
      }

      @Override
      public Map<L, Set<R>> getRightView()
      {
         return _partner.getLeftView();
      }

      @Override
      public boolean remove(final R left, final L right)
      {
         return _partner.remove(right, left);
      }

      @Override
      public Set<L> removeLeftKey(final R left)
      {
         return _partner.removeRightKey(left);
      }

      @Override
      public Set<R> removeRightKey(final L right)
      {
         return _partner.removeLeftKey(right);
      }
   }
}
