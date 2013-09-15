package net.sf.javagimmicks.collections.mapping;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import net.sf.javagimmicks.collections.event.AbstractEventSet;

public class DualMapMappings<L, R> extends AbstractMappings<L, R>
{
    private static final long serialVersionUID = 6670241289938071773L;

   public static <L, R> DualMapMappings<L, R> createHashHashInstance()
   {
      return new DualMapMappings<L, R>(StoreType.HASH.getFactory(), StoreType.HASH.getFactory());
   }
   
   public static <L, R> DualMapMappings<L, R> createHashTreeInstance()
   {
      return new DualMapMappings<L, R>(StoreType.HASH.getFactory(), StoreType.TREE.getFactory());
   }
   
   public static <L, R> DualMapMappings<L, R> createTreeHashInstance()
   {
      return new DualMapMappings<L, R>(StoreType.TREE.getFactory(), StoreType.HASH.getFactory());
   }
   
   public static <L, R> DualMapMappings<L, R> createTreeTreeInstance()
   {
      return new DualMapMappings<L, R>(StoreType.TREE.getFactory(), StoreType.TREE.getFactory());
   }

   protected final ValueMap<L, R> _left;
   
   protected DualMapMappings(StoreFactory leftFactory, StoreFactory rightFactory)
   {
      _left = new ValueMap<L, R>(leftFactory, rightFactory);
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean put(L left, R right)
   {
      if(left == null || right == null)
      {
         throw new IllegalArgumentException("Neither left nor right value may be null!");
      }
      
      ValueSet<L, R> valueSet = (ValueSet<L, R>) _left.get(left);
      if(valueSet == null)
      {
         _left.put(left, Collections.singleton(right));
         
         return true;
      }
      else
      {
         return valueSet.add(right);
      }
   }
   
   public Map<L, Set<R>> getLeftMap()
   {
      return _left;
   }

   public Map<R, Set<L>> getRightMap()
   {
      return _left._partnerMap;
   }

   protected static class ValueMap<L, R> extends AbstractMap<L, Set<R>> implements Serializable
   {
      private static final long serialVersionUID = 3088051603731444631L;

      protected final StoreFactory _factory;
      protected final Map<L, ValueSet<L, R>> _internal;
      protected final ValueMap<R, L> _partnerMap;

      protected ValueMap(StoreFactory factory, ValueMap<R, L> partnerMap)
      {
         _factory = factory;
         _internal = factory.createMap();
         
         _partnerMap = partnerMap;
      }

      protected ValueMap(StoreFactory leftFactory, StoreFactory rightFactory)
      {
         _factory = leftFactory;
         _internal = leftFactory.createMap();
         
         _partnerMap = new ValueMap<R, L>(rightFactory, this);
      }

      @Override
      public Set<Entry<L, Set<R>>> entrySet()
      {
         return new ValueMapEntrySet<L, R>(this);
      }
      
      public Iterator<Entry<L, Set<R>>> iterator()
      {
         return entrySet().iterator();
      }

      @Override
      public Set<R> put(L key, Set<R> value)
      {
         if(key == null)
         {
            throw new IllegalArgumentException("Key mustn't be null!");
         }
         
         if(value == null || value.isEmpty())
         {
            ValueSet<L, R> valueSet = _internal.remove(key);

            if(valueSet == null)
            {
               return null;
            }
            
            final Set<R> result = _partnerMap._factory.createSet();
            result.addAll(valueSet.getDecorated());
            
            valueSet.clear();
            
            return result;
         }
         else
         {
            if(value.contains(null))
            {
               throw new IllegalArgumentException("The value-set may not contain null!");
            }

            ValueSet<L, R> valueSet = _internal.get(key);
            
            final Set<R> result;
            if(valueSet == null)
            {
               result = null;

               final Set<R> decorated = _partnerMap._factory.createSet();
               valueSet = new ValueSet<L, R>(decorated, _factory, key, _partnerMap);
               
               _internal.put(key, valueSet);
            }
            else
            {
               result = _partnerMap._factory.createSet();
               result.addAll(valueSet.getDecorated());

               valueSet.clearForReuse();
            }
            
            valueSet.addAll(value);
            
            return result;
         }
      }
      
      @Override
      public Set<R> remove(Object key)
      {
         ValueSet<L, R> valueSet = _internal.remove(key);
         if(valueSet == null)
         {
            return null;
         }
         
         final Set<R> result = _partnerMap._factory.createSet();
         result.addAll(valueSet);
         
         valueSet.clear();
         
         return result;
      }
      
      @Override
      public Set<R> get(Object key)
      {
         return _internal.get(key);
      }
   }
   
   protected static class ValueMapEntrySet<L, R> extends AbstractSet<Entry<L, Set<R>>>
   {
      private final ValueMap<L, R> _parentMap;
      
      protected ValueMapEntrySet(ValueMap<L, R> parentMap)
      {
         _parentMap = parentMap;
      }

      @Override
      public Iterator<Entry<L, Set<R>>> iterator()
      {
         return new ValueMapEntrySetIterator<L, R>(_parentMap, _parentMap._internal.entrySet().iterator());
      }
      
      @Override
      public int size()
      {
         return _parentMap._internal.size();
      }
   }

   protected static class ValueMapEntrySetIterator<L, R> implements Iterator<Entry<L, Set<R>>>
   {
      private final ValueMap<L, R> _parentMap;
      private final Iterator<Entry<L, ValueSet<L, R>>> _internalIterator;
      private Entry<L, ValueSet<L, R>> _last;

      protected ValueMapEntrySetIterator(ValueMap<L, R> parentMap, Iterator<Entry<L, ValueSet<L, R>>> internalIterator)
      {
         _parentMap = parentMap;
         _internalIterator = internalIterator;
      }

      public boolean hasNext()
      {
         return _internalIterator.hasNext();
      }

      public Entry<L, Set<R>> next()
      {
         final Entry<L, ValueSet<L, R>> nextEntry = _internalIterator.next();
         _last = nextEntry;
         
         return new ValueMapEntry<L, R>(_parentMap, nextEntry);
      }

      public void remove()
      {
         _internalIterator.remove();
         final ValueSet<L, R> valueSet = _last.getValue();
         valueSet.clear();
      }
   }

   protected static class ValueMapEntry<L, R> implements Entry<L, Set<R>>
   {
      private final ValueMap<L, R> _parentMap;
      private final Entry<L, ValueSet<L, R>> _internalEntry;

      protected ValueMapEntry(ValueMap<L, R> parentMap, Entry<L, ValueSet<L, R>> nextEntry)
      {
         _parentMap = parentMap;
         this._internalEntry = nextEntry;
      }

      public L getKey()
      {
         return _internalEntry.getKey();
      }

      public Set<R> getValue()
      {
         return _internalEntry.getValue();
      }

      public Set<R> setValue(Set<R> value)
      {
         if(value == null || value.isEmpty())
         {
            throw new IllegalArgumentException("May not explicitly set null or an empty set as entry value!");
         }
         
         final ValueSet<L,R> valueSet = _internalEntry.getValue();
         
         final Set<R> result = _parentMap._partnerMap._factory.createSet();
         result.addAll(valueSet.getDecorated());
         
         valueSet.clearForReuse();
         valueSet.addAll(value);
         
         return result;
      }
      
      public String toString()
      {
         return new StringBuilder()
            .append(getKey())
            .append("=[")
            .append(getValue())
            .append("]")
            .toString();
      }
   }

   protected static class ValueSet<L, R> extends AbstractEventSet<R>
   {
      private static final long serialVersionUID = -8381132398717092121L;

      protected final StoreFactory _factory;

      protected final L _left;
      protected final ValueMap<R, L> _otherMap;
      
      protected boolean _detached = false;
      
      private boolean _internalFlag = false;
      
      protected ValueSet(Set<R> decorated, StoreFactory factory, L left, ValueMap<R, L> otherMap)
      {
         super(decorated);
         _factory = factory;
         _left = left;
         _otherMap = otherMap;
      }
      
      @Override
      public boolean add(R e)
      {
         if(_detached)
         {
            throw new IllegalStateException("Value set is detached! No further adding possible!");
         }
         
         return super.add(e);
      }

      @Override
      public void clear()
      {
         // TODO Auto-generated method stub
         super.clear();
      }
      
      protected void clearForReuse()
      {
         _internalFlag = true;
         clear();
         _internalFlag = false;
      }

      @Override
      protected void fireElementAdded(R element)
      {
         Map<R, ValueSet<R, L>> otherInternalMap = _otherMap._internal;
         ValueSet<R, L> otherSet = otherInternalMap.get(element);
         
         if(otherSet == null)
         {
            Set<L> otherDecoratedSet = _factory.createSet();
            otherSet = new ValueSet<R, L>(otherDecoratedSet, _otherMap._factory, element, _otherMap._partnerMap);
            
            otherInternalMap.put(element, otherSet);
         }
         
         otherSet.getDecorated().add(_left);
      }

      @Override
      protected void fireElementRemoved(R element)
      {
         Map<R, ValueSet<R, L>> otherInternalMap = _otherMap._internal;
         ValueSet<R, L> otherSet = otherInternalMap.get(element);

         otherSet.getDecorated().remove(_left);
         
         if(otherSet.isEmpty())
         {
            otherSet._detached = true;
            otherInternalMap.remove(element);
         }
         
         if(isEmpty() && !_internalFlag)
         {
            _detached = true;
            _otherMap._partnerMap._internal.remove(_left);
         }
      }

      @Override
      protected void fireElementReadded(R element)
      {
      }
   }

   protected static interface StoreFactory extends Serializable
   {
      public <T> Set<T> createSet();
      public <K, V> Map<K, V> createMap();
   }

   public static enum StoreType
   {
      HASH(new StoreFactory()
      {
         private static final long serialVersionUID = 5873569465040591757L;
         
         public <K, V> Map<K, V> createMap() { return new HashMap<K, V>(); }
         public <T> Set<T> createSet() { return new HashSet<T>(); }
      }),
      
      TREE(new StoreFactory()
      {
         private static final long serialVersionUID = 4635243875231393315L;
         
         public <K, V> Map<K, V> createMap() { return new TreeMap<K, V>(); }
         public <T> Set<T> createSet() { return new TreeSet<T>(); }
      });
      
      private final StoreFactory _factory;

      StoreType(StoreFactory factory)
      {
         _factory = factory;
      }
      
      public StoreFactory getFactory()
      {
         return _factory;
      }
   };
}
