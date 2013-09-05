package net.sf.javagimmicks.collections.mapping;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public abstract class AbstractMappings<L, R> implements Mappings<L, R>
{
   private static final long serialVersionUID = -8390090502631423671L;

   public Set<Mapping<L, R>> getMappingSet()
   {
      return new MappingSet<L, R>(getLeftMap().entrySet());
   }
   
   public Iterator<Mapping<L, R>> iterator()
   {
      return getMappingSet().iterator();
   }

   public Mappings<R, L> getInverseMappings()
   {
      return new InverseMappings<L, R>(this);
   }

   public Set<R> getRight(L left)
   {
      return getLeftMap().get(left);
   }

   public Set<L> getLeft(R right)
   {
      return getRightMap().get(right);
   }
   
   public boolean put(L left, R right)
   {
      throw new UnsupportedOperationException();
   }
   
   public boolean putLeft(R right, Collection<? extends L> c)
   {
      boolean result = false;

      for(L left : c)
      {
         result |= put(left, right);
      }
      
      return result;
   }

   public boolean putRight(L left, Collection<? extends R> c)
   {
      boolean result = false;
      
      for(R right : c)
      {
         result |= put(left, right);
      }
      
      return result;
   }

   public boolean remove(L left, R right)
   {
      final Set<R> mappedValuesLeft = getRight(left);
      
      return mappedValuesLeft != null ? mappedValuesLeft.remove(right) : false;
   }
   
   public boolean containsLeft(L left)
   {
      return getLeftMap().containsKey(left);
   }

   public boolean containsRight(R right)
   {
      return getRightMap().containsKey(right);
   }

   public boolean contains(L left, R right)
   {
      final Set<R> rightSet = getRight(left);
      return rightSet != null && rightSet.contains(right);
   }

   public Set<R> removeRight(L left)
   {
      return getLeftMap().remove(left);
   }

   public Set<L> removeLeft(R right)
   {
      return getRightMap().remove(right);
   }
   
   public void clear()
   {
      getLeftMap().clear();
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
      
      if(!(obj instanceof Mappings<?, ?>))
      {
         return false;
      }
      
      Mappings<?, ?> other = (Mappings<?, ?>)obj;
      
      return getMappingSet().equals(other.getMappingSet());
   }

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

      public MappingSet(Set<Entry<L, Set<R>>> entries)
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
         for(Entry<L, Set<R>> entry : _entries)
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
      
      protected MappingIterator(Iterator<Entry<L, Set<R>>> entryIterator)
      {
         _entryIterator = entryIterator;
      }

      public boolean hasNext()
      {
         return (_valueItertor != null && _valueItertor.hasNext()) || _entryIterator.hasNext();
      }
      
      public Mapping<L, R> next()
      {
         moveNext();

         return new DefaultMapping<L, R>(_currentEntry.getKey(), _currentValue);
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
            _valueItertor = _currentEntry.getValue().iterator();
         }

         _currentValue = _valueItertor.next();
      }
   }
   
   public static class InverseMappings<L, R> extends AbstractMappings<R, L>
   {
      private static final long serialVersionUID = -4525522714547396946L;
      
      protected final Mappings<L, R> _partner;
      
      public InverseMappings(Mappings<L, R> partner)
      {
         _partner = partner;
      }

      @Override
      public Mappings<L, R> getInverseMappings()
      {
         return _partner;
      }

      @Override
      public boolean put(R left, L right)
      {
         return _partner.put(right, left);
      }

      @Override
      public void clear()
      {
         _partner.clear();
      }

      public Map<R, Set<L>> getLeftMap()
      {
         return _partner.getRightMap();
      }

      @Override
      public Set<L> getRight(R left)
      {
         return _partner.getLeft(left);
      }

      @Override
      public Set<R> getLeft(L right)
      {
         return _partner.getRight(right);
      }

      public Map<L, Set<R>> getRightMap()
      {
         return _partner.getLeftMap();
      }

      @Override
      public boolean remove(R left, L right)
      {
         return _partner.remove(right, left);
      }

      @Override
      public Set<L> removeRight(R left)
      {
         return _partner.removeLeft(left);
      }

      @Override
      public Set<R> removeLeft(L right)
      {
         return _partner.removeRight(right);
      }
   }
}
