package net.sf.javagimmicks.collections.mapping;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.sf.javagimmicks.collections.event.AbstractEventMap;

public class DualMapValueMappings<L, R, E> extends AbstractValueMappings<L, R, E>
{
    private static final long serialVersionUID = 7852860994833056710L;

   public static <L, R, E> DualMapValueMappings<L, R, E> createHashHashInstance()
   {
      return new DualMapValueMappings<L, R, E>(StoreType.HASH.getFactory(), StoreType.HASH.getFactory());
   }
   
   public static <L, R, E> DualMapValueMappings<L, R, E> createHashTreeInstance()
   {
      return new DualMapValueMappings<L, R, E>(StoreType.HASH.getFactory(), StoreType.TREE.getFactory());
   }
   
   public static <L, R, E> DualMapValueMappings<L, R, E> createTreeHashInstance()
   {
      return new DualMapValueMappings<L, R, E>(StoreType.TREE.getFactory(), StoreType.HASH.getFactory());
   }
   
   public static <L, R, E> DualMapValueMappings<L, R, E> createTreeTreeInstance()
   {
      return new DualMapValueMappings<L, R, E>(StoreType.TREE.getFactory(), StoreType.TREE.getFactory());
   }

   protected final OuterMap<L, R, E> _left;
   
   protected DualMapValueMappings(StoreFactory leftFactory, StoreFactory rightFactory)
   {
      _left = new OuterMap<L, R, E>(leftFactory, rightFactory);
   }

   @SuppressWarnings("unchecked")
   @Override
   public E put(L left, R right, E value)
   {
      if(left == null || right == null)
      {
         throw new IllegalArgumentException("Neither left nor right value may be null!");
      }
      
      InnerMap<L, R, E> innerMap = (InnerMap<L, R, E>) _left.get(left);
      if(innerMap == null)
      {
         _left.put(left, Collections.singletonMap(right, value));
         
         return null;
      }
      else
      {
         return innerMap.put(right, value);
      }
   }

   public Map<L, Map<R, E>> getLeftOuterMap()
   {
      return _left;
   }

   public Map<R, Map<L, E>> getRightOuterMap()
   {
      return _left._partnerMap;
   }

   protected static class OuterMap<L, R, E> extends AbstractMap<L, Map<R, E>> implements Map<L, Map<R, E>>, Serializable
   {
      private static final long serialVersionUID = 3088051603731444631L;

      protected final StoreFactory _factory;
      protected final Map<L, InnerMap<L, R, E>> _internal;
      protected final OuterMap<R, L, E> _partnerMap;

      protected OuterMap(StoreFactory factory, OuterMap<R, L, E> partnerMap)
      {
         _factory = factory;
         _internal = factory.createMap();
         
         _partnerMap = partnerMap;
      }

      protected OuterMap(StoreFactory leftFactory, StoreFactory rightFactory)
      {
         _factory = leftFactory;
         _internal = leftFactory.createMap();
         
         _partnerMap = new OuterMap<R, L, E>(rightFactory, this);
      }

      @Override
      public Set<Entry<L, Map<R, E>>> entrySet()
      {
         return new OuterMapEntrySet<L, R, E>(this);
      }

      @Override
      public Map<R, E> put(L key, Map<R, E> value)
      {
         if(key == null)
         {
            throw new IllegalArgumentException("Key mustn't be null!");
         }
         
         if(value == null || value.isEmpty())
         {
            InnerMap<L, R, E> innerMap = _internal.remove(key);

            if(innerMap == null)
            {
               return null;
            }
            
            final Map<R, E> result = _partnerMap._factory.createMap();
            result.putAll(innerMap.getDecorated());
            
            innerMap.clear();
            
            return result;
         }
         else
         {
            if(value.containsKey(null))
            {
               throw new IllegalArgumentException("The value-set may not contain null!");
            }

            InnerMap<L, R, E> innerMap = _internal.get(key);
            
            final Map<R, E> result;
            if(innerMap == null)
            {
               result = null;

               final Map<R, E> decorated = _partnerMap._factory.createMap();
               innerMap = new InnerMap<L, R, E>(decorated, _factory, key, _partnerMap);
               
               _internal.put(key, innerMap);
            }
            else
            {
               result = _partnerMap._factory.createMap();
               result.putAll(innerMap.getDecorated());

               innerMap.clearForReuse();
            }
            
            innerMap.putAll(value);
            
            return result;
         }
      }
      
      @Override
      public Map<R, E> remove(Object key)
      {
         InnerMap<L, R, E> innerMap = _internal.remove(key);
         if(innerMap == null)
         {
            return null;
         }
         
         final Map<R, E> result = _partnerMap._factory.createMap();
         result.putAll(innerMap);
         
         innerMap.clear();
         
         return result;
      }
      
      @Override
      public Map<R, E> get(Object key)
      {
         return _internal.get(key);
      }
   }
   
   protected static class OuterMapEntrySet<L, R, E> extends AbstractSet<Entry<L, Map<R, E>>>
   {
      private final OuterMap<L, R, E> _parentMap;
      
      protected OuterMapEntrySet(OuterMap<L, R, E> parentMap)
      {
         _parentMap = parentMap;
      }

      @Override
      public Iterator<Entry<L, Map<R, E>>> iterator()
      {
         return new OuterMapEntrySetIterator<L, R, E>(_parentMap, _parentMap._internal.entrySet().iterator());
      }
      
      @Override
      public int size()
      {
         return _parentMap._internal.size();
      }
   }

   protected static class OuterMapEntrySetIterator<L, R, E> implements Iterator<Entry<L, Map<R, E>>>
   {
      private final OuterMap<L, R, E> _parentMap;
      private final Iterator<Entry<L, InnerMap<L, R, E>>> _internalIterator;
      private Entry<L, InnerMap<L, R, E>> _last;

      protected OuterMapEntrySetIterator(OuterMap<L, R, E> parentMap, Iterator<Entry<L, InnerMap<L, R, E>>> internalIterator)
      {
         _parentMap = parentMap;
         _internalIterator = internalIterator;
      }

      public boolean hasNext()
      {
         return _internalIterator.hasNext();
      }

      public Entry<L, Map<R, E>> next()
      {
         final Entry<L, InnerMap<L, R, E>> nextEntry = _internalIterator.next();
         _last = nextEntry;
         
         return new OuterMapEntry<L, R, E>(_parentMap, nextEntry);
      }

      public void remove()
      {
         _internalIterator.remove();
         final InnerMap<L, R, E> innerMap = _last.getValue();
         innerMap.clear();
      }
   }

   protected static class OuterMapEntry<L, R, E> implements Entry<L, Map<R, E>>
   {
      private final OuterMap<L, R, E> _parentMap;
      private final Entry<L, InnerMap<L, R, E>> _internalEntry;

      protected OuterMapEntry(OuterMap<L, R, E> parentMap, Entry<L, InnerMap<L, R, E>> internalEntry)
      {
         _parentMap = parentMap;
         _internalEntry = internalEntry;
      }

      public L getKey()
      {
         return _internalEntry.getKey();
      }

      public Map<R, E> getValue()
      {
         return _internalEntry.getValue();
      }

      public Map<R, E> setValue(Map<R, E> value)
      {
         if(value == null || value.isEmpty())
         {
            throw new IllegalArgumentException("May not explicitly set null or an empty set as entry value!");
         }
         
         final InnerMap<L, R, E> innerMap = _internalEntry.getValue();
         
         final Map<R, E> result = _parentMap._partnerMap._factory.createMap();
         result.putAll(innerMap.getDecorated());
         
         innerMap.clearForReuse();
         innerMap.putAll(value);
         
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

   protected static class InnerMap<L, R, E> extends AbstractEventMap<R, E>
   {
      private static final long serialVersionUID = -8381132398717092121L;

      protected final StoreFactory _factory;

      protected final L _left;
      protected final OuterMap<R, L, E> _otherMap;
      
      protected boolean _detached = false;
      
      private boolean _internalFlag = false;
      
      protected InnerMap(Map<R, E> decorated, StoreFactory factory, L left, OuterMap<R, L, E> otherMap)
      {
         super(decorated);
         _factory = factory;
         _left = left;
         _otherMap = otherMap;
      }
      
      @Override
      public E put(R key, E value)
      {
         if(_detached)
         {
            throw new IllegalStateException("Value set is detached! No further adding possible!");
         }

         return super.put(key, value);
      }

      protected void clearForReuse()
      {
         _internalFlag = true;
         clear();
         _internalFlag = false;
      }

      @Override
      protected void fireEntryAdded(R key, E value)
      {
         Map<R, InnerMap<R, L, E>> otherInternalMap = _otherMap._internal;
         InnerMap<R, L, E> otherInnerMap = otherInternalMap.get(key);
         
         if(otherInnerMap == null)
         {
            Map<L, E> otherDecoratedMap = _factory.createMap();
            otherInnerMap = new InnerMap<R, L, E>(otherDecoratedMap, _otherMap._factory, key, _otherMap._partnerMap);
            
            otherInternalMap.put(key, otherInnerMap);
         }
         
         otherInnerMap.getDecorated().put(_left, value);
      }

      @Override
      protected void fireEntryRemoved(R key, E value)
      {
         Map<R, InnerMap<R, L, E>> otherInternalMap = _otherMap._internal;
         InnerMap<R, L, E> otherInnerMap = otherInternalMap.get(key);

         otherInnerMap.getDecorated().remove(_left);
         
         if(otherInnerMap.isEmpty())
         {
            otherInnerMap._detached = true;
            otherInternalMap.remove(key);
         }
         
         if(isEmpty() && !_internalFlag)
         {
            _detached = true;
            _otherMap._partnerMap._internal.remove(_left);
         }
      }

      @Override
      protected void fireEntryUpdated(R key, E oldValue, E newValue)
      {
         Map<R, InnerMap<R, L, E>> otherInternalMap = _otherMap._internal;
         InnerMap<R, L, E> otherInnerMap = otherInternalMap.get(key);
         
         otherInnerMap.getDecorated().put(_left, newValue);
      }
   }

   protected static interface StoreFactory extends Serializable
   {
      public <K, V> Map<K, V> createMap();
   }

   public static enum StoreType
   {
      HASH(new StoreFactory()
      {
         private static final long serialVersionUID = 2570752436161022580L;

         public <K, V> Map<K, V> createMap() { return new HashMap<K, V>(); }
      }),
      
      TREE(new StoreFactory()
      {
         private static final long serialVersionUID = -2301586648259664423L;

         public <K, V> Map<K, V> createMap() { return new TreeMap<K, V>(); }
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
