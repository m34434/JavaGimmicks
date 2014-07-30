package net.sf.javagimmicks.collections8.mapping;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * An abstract implementation of {@link ValueMappings} that provides default
 * implementations for almost all methods.
 * <p>
 * Methods to be implemented by subclasses are:
 * <ul>
 * <li>{@link #getLeftView()}</li>
 * <li>{@link #getRightView()}</li>
 * <li>{@link #put(Object, Object, Object)}</li>
 * </ul>
 */
public abstract class AbstractValueMappings<L, R, E> implements ValueMappings<L, R, E>, Serializable
{
   private static final long serialVersionUID = 3350171311023033933L;

   protected AbstractValueMappings()
   {}

   @Override
   public Set<Mapping<L, R, E>> getMappingSet()
   {
      return new MappingSet<L, R, E>(getLeftView().entrySet());
   }

   @Override
   public ValueMappings<R, L, E> invert()
   {
      return new InverseMappings<L, R, E>(this);
   }

   @Override
   public E put(final L left, final R right, final E value)
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

      if (!(obj instanceof ValueMappings<?, ?, ?>))
      {
         return false;
      }

      final ValueMappings<?, ?, ?> other = (ValueMappings<?, ?, ?>) obj;

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

   protected static class MappingSet<L, R, E> extends AbstractSet<Mapping<L, R, E>>
   {
      protected final Set<Entry<L, Map<R, E>>> _entries;

      public MappingSet(final Set<Entry<L, Map<R, E>>> entries)
      {
         _entries = entries;
      }

      @Override
      public Iterator<Mapping<L, R, E>> iterator()
      {
         return new MappingIterator<L, R, E>(_entries.iterator());
      }

      @Override
      public int size()
      {
         int size = 0;
         for (final Entry<L, Map<R, E>> entry : _entries)
         {
            size += entry.getValue().size();
         }

         return size;
      }
   }

   protected static class MappingIterator<L, R, E> implements Iterator<Mapping<L, R, E>>
   {
      protected final Iterator<Entry<L, Map<R, E>>> _entryIterator;

      protected Iterator<Entry<R, E>> _valueItertor;
      protected Entry<L, Map<R, E>> _currentEntry;
      protected Entry<R, E> _currentValue;

      protected MappingIterator(final Iterator<Entry<L, Map<R, E>>> entryIterator)
      {
         _entryIterator = entryIterator;
      }

      @Override
      public boolean hasNext()
      {
         return (_valueItertor != null && _valueItertor.hasNext()) || _entryIterator.hasNext();
      }

      @Override
      public Mapping<L, R, E> next()
      {
         moveNext();

         return new DefaultValueMapping<L, R, E>(_currentEntry.getKey(), _currentValue.getKey(),
               _currentValue.getValue());
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
            _valueItertor = _currentEntry.getValue().entrySet().iterator();
         }

         _currentValue = _valueItertor.next();
      }
   }

   protected static class InverseMappings<L, R, E> extends AbstractValueMappings<R, L, E>
   {
      private static final long serialVersionUID = 6830247408926542348L;

      protected final ValueMappings<L, R, E> _partner;

      public InverseMappings(final ValueMappings<L, R, E> partner)
      {
         _partner = partner;
      }

      @Override
      public ValueMappings<L, R, E> invert()
      {
         return _partner;
      }

      @Override
      public E put(final R left, final L right, final E value)
      {
         return _partner.put(right, left, value);
      }

      @Override
      public void clear()
      {
         _partner.clear();
      }

      @Override
      public Map<R, Map<L, E>> getLeftView()
      {
         return _partner.getRightView();
      }

      @Override
      public Map<L, E> getAllForLeftKey(final R left)
      {
         return _partner.getAllForRightKey(left);
      }

      @Override
      public Map<R, E> getAllForRightKey(final L right)
      {
         return _partner.getAllForLeftKey(right);
      }

      @Override
      public Map<L, Map<R, E>> getRightView()
      {
         return _partner.getLeftView();
      }

      @Override
      public E remove(final R left, final L right)
      {
         return _partner.remove(right, left);
      }

      @Override
      public Map<L, E> removeLeftKey(final R left)
      {
         return _partner.removeRightKey(left);
      }

      @Override
      public Map<R, E> removeRightKey(final L right)
      {
         return _partner.removeLeftKey(right);
      }
   }
}
