package net.sf.javagimmicks.collections.mapping;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.javagimmicks.collections.transformer.Transformer;
import net.sf.javagimmicks.collections.transformer.TransformerUtils;
import net.sf.javagimmicks.lang.LangUtils;

public abstract class AbstractValueMappings<L, R, E> implements ValueMappings<L, R, E>
{
   private static final long serialVersionUID = 3350171311023033933L;

   @Override
   public Set<Mapping<L, R, E>> getMappingSet()
   {
      return new MappingSet<L, R, E>(getLeftOuterMap().entrySet());
   }

   @Override
   public Iterator<Mapping<L, R, E>> iterator()
   {
      return getMappingSet().iterator();
   }

   @Override
   public ValueMappings<R, L, E> getInverseMappings()
   {
      return new InverseMappings<L, R, E>(this);
   }

   @Override
   public Map<R, E> getRightInnerMap(final L left)
   {
      return getLeftOuterMap().get(left);
   }

   @Override
   public Map<L, E> getLeftInnerMap(final R right)
   {
      return getRightOuterMap().get(right);
   }

   @Override
   public E put(final L left, final R right, final E value)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public void putLeft(final R right, final Map<? extends L, ? extends E> c)
   {
      for (final Entry<? extends L, ? extends E> left : c.entrySet())
      {
         put(left.getKey(), right, left.getValue());
      }
   }

   @Override
   public void putRight(final L left, final Map<? extends R, ? extends E> c)
   {
      for (final Entry<? extends R, ? extends E> right : c.entrySet())
      {
         put(left, right.getKey(), right.getValue());
      }
   }

   @Override
   public E get(final L left, final R right)
   {
      final Map<R, E> rightInnerMap = getRightInnerMap(left);

      return rightInnerMap != null ? rightInnerMap.get(right) : null;
   }

   @Override
   public E remove(final L left, final R right)
   {
      final Map<R, E> mappedValuesLeft = getRightInnerMap(left);

      return mappedValuesLeft != null ? mappedValuesLeft.remove(right) : null;
   }

   @Override
   public Collection<E> getValues()
   {
      return TransformerUtils.decorate(getMappingSet(), VALUE_TRANSFORMER);
   }

   @Override
   public boolean containsLeft(final L left)
   {
      return getLeftOuterMap().containsKey(left);
   }

   @Override
   public boolean containsRight(final R right)
   {
      return getRightOuterMap().containsKey(right);
   }

   @Override
   public boolean containsMapping(final L left, final R right)
   {
      final Map<R, E> rightInnerMap = getRightInnerMap(left);
      return rightInnerMap != null && rightInnerMap.containsKey(right);
   }

   @Override
   public Map<R, E> removeRight(final L left)
   {
      return getLeftOuterMap().remove(left);
   }

   @Override
   public Map<L, E> removeLeft(final R right)
   {
      return getRightOuterMap().remove(right);
   }

   @Override
   public void clear()
   {
      getLeftOuterMap().clear();
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
            .append(getLeftOuterMap())
            .append(" | ")
            .append(getRightOuterMap())
            .toString();
   }

   protected static abstract class AbstractMapping<L, R, E> implements Mapping<L, R, E>
   {
      private static final long serialVersionUID = -293860609319776316L;

      @Override
      public Mapping<R, L, E> getInverseMapping()
      {
         return new AbstractMapping<R, L, E>()
         {
            private static final long serialVersionUID = -2436668735196156472L;

            @Override
            public Mapping<L, R, E> getInverseMapping()
            {
               return AbstractMapping.this;
            }

            @Override
            public R getLeft()
            {
               return AbstractMapping.this.getRight();
            }

            @Override
            public L getRight()
            {
               return AbstractMapping.this.getLeft();
            }

            @Override
            public E getValue()
            {
               return AbstractMapping.this.getValue();
            }
         };
      }

      @Override
      public boolean equals(final Object obj)
      {
         if (this == obj)
         {
            return true;
         }

         if (!(obj instanceof Mapping<?, ?, ?>))
         {
            return false;
         }

         final Mapping<?, ?, ?> other = (Mapping<?, ?, ?>) obj;

         return getLeft().equals(other.getLeft()) && getRight().equals(other.getRight())
               && LangUtils.equalsNullSafe(getValue(), other.getValue());
      }

      @Override
      public int hashCode()
      {
         return 5 * getLeft().hashCode() + 7 * getRight().hashCode() + 3872123;
      }

      @Override
      public String toString()
      {
         return new StringBuilder()
               .append("[")
               .append(getLeft())
               .append("/")
               .append(getRight())
               .append(": ")
               .append(getValue())
               .append("]")
               .toString();
      }
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

         final L left = _currentEntry.getKey();
         final R right = _currentValue.getKey();
         final E value = _currentValue.getValue();

         final AbstractMapping<L, R, E> mapping = new AbstractMapping<L, R, E>()
         {
            private static final long serialVersionUID = -893342873662381319L;

            @Override
            public L getLeft()
            {
               return left;
            }

            @Override
            public R getRight()
            {
               return right;
            }

            @Override
            public E getValue()
            {
               return value;
            }
         };

         return mapping;
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
      public ValueMappings<L, R, E> getInverseMappings()
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
      public Map<R, Map<L, E>> getLeftOuterMap()
      {
         return _partner.getRightOuterMap();
      }

      @Override
      public Map<L, E> getRightInnerMap(final R left)
      {
         return _partner.getLeftInnerMap(left);
      }

      @Override
      public Map<R, E> getLeftInnerMap(final L right)
      {
         return _partner.getRightInnerMap(right);
      }

      @Override
      public Map<L, Map<R, E>> getRightOuterMap()
      {
         return _partner.getLeftOuterMap();
      }

      @Override
      public E remove(final R left, final L right)
      {
         return _partner.remove(right, left);
      }

      @Override
      public Map<L, E> removeRight(final R left)
      {
         return _partner.removeLeft(left);
      }

      @Override
      public Map<R, E> removeLeft(final L right)
      {
         return _partner.removeRight(right);
      }
   }

   protected final Transformer<Mapping<L, R, E>, E> VALUE_TRANSFORMER = new Transformer<Mapping<L, R, E>, E>()
   {
      @Override
      public E transform(final Mapping<L, R, E> source)
      {
         return source.getValue();
      }
   };
}
