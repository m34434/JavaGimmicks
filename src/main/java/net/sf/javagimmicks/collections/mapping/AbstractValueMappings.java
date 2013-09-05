package net.sf.javagimmicks.collections.mapping;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.javagimmicks.collections.transformer.Transformer;
import net.sf.javagimmicks.collections.transformer.TransformerUtils;
import net.sf.javagimmicks.lang.LangUtils;

public abstract class AbstractValueMappings<L, R, E> implements ValueMappings<L, R, E>
{
   private static final long serialVersionUID = 3350171311023033933L;

   public Set<Mapping<L, R, E>> getMappingSet()
   {
      return new MappingSet<L, R, E>(getLeftOuterMap().entrySet());
   }
   
   public Iterator<Mapping<L, R, E>> iterator()
   {
      return getMappingSet().iterator();
   }

   public ValueMappings<R, L, E> getInverseMappings()
   {
      return new InverseMappings<L, R, E>(this);
   }

   public Map<R, E> getRightInnerMap(L left)
   {
      return getLeftOuterMap().get(left);
   }

   public Map<L, E> getLeftInnerMap(R right)
   {
      return getRightOuterMap().get(right);
   }
   
   public E put(L left, R right, E value)
   {
      throw new UnsupportedOperationException();
   }
   
   public void putLeft(R right, Map<? extends L, ? extends E> c)
   {
      for(Entry<? extends L, ? extends E> left : c.entrySet())
      {
         put(left.getKey(), right, left.getValue());
      }
   }

   public void putRight(L left, Map<? extends R, ? extends E> c)
   {
      for(Entry<? extends R, ? extends E> right : c.entrySet())
      {
         put(left, right.getKey(), right.getValue());
      }
   }
   
   public E get(L left, R right)
   {
      final Map<R, E> rightInnerMap = getRightInnerMap(left);
      
      return rightInnerMap != null ? rightInnerMap.get(right): null;
   }

   public E remove(L left, R right)
   {
      final Map<R, E> mappedValuesLeft = getRightInnerMap(left);
      
      return mappedValuesLeft != null ? mappedValuesLeft.remove(right) : null;
   }
   
   public Collection<E> getValues()
   {
      return TransformerUtils.decorate(getMappingSet(), VALUE_TRANSFORMER);
   }

   public boolean containsLeft(L left)
   {
      return getLeftOuterMap().containsKey(left);
   }

   public boolean containsRight(R right)
   {
      return getRightOuterMap().containsKey(right);
   }

   public boolean containsMapping(L left, R right)
   {
      final Map<R, E> rightInnerMap = getRightInnerMap(left);
      return rightInnerMap != null && rightInnerMap.containsKey(right);
   }

   public Map<R, E> removeRight(L left)
   {
      return getLeftOuterMap().remove(left);
   }

   public Map<L, E> removeLeft(R right)
   {
      return getRightOuterMap().remove(right);
   }
   
   public void clear()
   {
      getLeftOuterMap().clear();
   }
   
   public int size()
   {
      return getMappingSet().size();
   }
   
   public boolean isEmpty()
   {
      return getMappingSet().isEmpty();
   }

   @Override
   public boolean equals(Object obj)
   {
      if(this == obj)
      {
         return true;
      }
      
      if(!(obj instanceof ValueMappings<?, ?, ?>))
      {
         return false;
      }
      
      ValueMappings<?, ?, ?> other = (ValueMappings<?, ?, ?>)obj;
      
      return getMappingSet().equals(other.getMappingSet());
   }

   public String toString()
   {
      return new StringBuilder()
         .append(getLeftOuterMap())
         .append(" | ")
         .append(getRightOuterMap())
         .toString();
   }
   
   public static abstract class AbstractMapping<L, R, E> implements Mapping<L, R, E>
   {
      private static final long serialVersionUID = -293860609319776316L;

      public Mapping<R, L, E> getInverseMapping()
      {
         return new AbstractMapping<R, L, E>()
         {
            private static final long serialVersionUID = -2436668735196156472L;

            public Mapping<L, R, E> getInverseMapping()
            {
               return AbstractMapping.this;
            }

            public R getLeft()
            {
               return AbstractMapping.this.getRight();
            }

            public L getRight()
            {
               return AbstractMapping.this.getLeft();
            }

            public E getValue()
            {
               return AbstractMapping.this.getValue();
            }
         };
      }

      @Override
      public boolean equals(Object obj)
      {
         if(this == obj)
         {
            return true;
         }
         
         if(!(obj instanceof Mapping<?, ?, ?>))
         {
            return false;
         }
         
         Mapping<?, ?, ?> other = (Mapping<?, ?, ?>)obj;
         
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

      public MappingSet(Set<Entry<L, Map<R, E>>> entries)
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
         for(Entry<L, Map<R, E>> entry : _entries)
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
      
      protected MappingIterator(Iterator<Entry<L, Map<R, E>>> entryIterator)
      {
         _entryIterator = entryIterator;
      }

      public boolean hasNext()
      {
         return (_valueItertor != null && _valueItertor.hasNext()) || _entryIterator.hasNext();
      }
      
      public Mapping<L, R, E> next()
      {
         moveNext();
         
         final L left = _currentEntry.getKey();
         final R right = _currentValue.getKey();
         final E value = _currentValue.getValue();
         
         final AbstractMapping<L, R, E> mapping = new AbstractMapping<L, R, E>()
         {
            private static final long serialVersionUID = -893342873662381319L;

            public L getLeft()
            {
               return left;
            }

            public R getRight()
            {
               return right;
            }

            public E getValue()
            {
               return value;
            }
         };
         
         return mapping;
      }
      
      public void remove()
      {
         _valueItertor.remove();
         if(_currentEntry.getValue().isEmpty())
         {
            _entryIterator.remove();
         }
      }

      private void moveNext()
      {
         if(_valueItertor == null || !_valueItertor.hasNext())
         {
            _currentEntry = _entryIterator.next();
            _valueItertor = _currentEntry.getValue().entrySet().iterator();
         }

         _currentValue = _valueItertor.next();
      }
   }
   
   public static class InverseMappings<L, R, E> extends AbstractValueMappings<R, L, E>
   {
      private static final long serialVersionUID = 6830247408926542348L;
      
      protected final ValueMappings<L, R, E> _partner;
      
      public InverseMappings(ValueMappings<L, R, E> partner)
      {
         _partner = partner;
      }

      @Override
      public ValueMappings<L, R, E> getInverseMappings()
      {
         return _partner;
      }

      @Override
      public E put(R left, L right, E value)
      {
         return _partner.put(right, left, value);
      }

      @Override
      public void clear()
      {
         _partner.clear();
      }

      public Map<R, Map<L, E>> getLeftOuterMap()
      {
         return _partner.getRightOuterMap();
      }

      @Override
      public Map<L, E> getRightInnerMap(R left)
      {
         return _partner.getLeftInnerMap(left);
      }

      @Override
      public Map<R, E> getLeftInnerMap(L right)
      {
         return _partner.getRightInnerMap(right);
      }

      public Map<L, Map<R, E>> getRightOuterMap()
      {
         return _partner.getLeftOuterMap();
      }

      @Override
      public E remove(R left, L right)
      {
         return _partner.remove(right, left);
      }

      @Override
      public Map<L, E> removeRight(R left)
      {
         return _partner.removeLeft(left);
      }

      @Override
      public Map<R, E> removeLeft(L right)
      {
         return _partner.removeRight(right);
      }
   }
   
   protected final Transformer<Mapping<L, R, E>, E> VALUE_TRANSFORMER = new Transformer<Mapping<L,R,E>, E>()
   {
      public E transform(Mapping<L, R, E> source)
      {
         return source.getValue();
      }
   };
}
