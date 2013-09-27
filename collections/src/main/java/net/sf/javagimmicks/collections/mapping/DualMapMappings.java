package net.sf.javagimmicks.collections.mapping;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.sf.javagimmicks.collections.event.AbstractEventSet;

/**
 * An implementation of {@link Mappings} that internally uses two synchronously
 * updated {@link Map}s (one for the left view and one for the right view).
 */
public class DualMapMappings<L, R> extends AbstractMappings<L, R>
{
   private static final long serialVersionUID = 6670241289938071773L;

   /**
    * Creates a new instance based on a {@link HashMap} for the left view and a
    * {@link HashMap} for the right view - so left keys and right keys should
    * implement {@link Object#hashCode()}.
    * 
    * @return the new instance
    */
   public static <L, R> DualMapMappings<L, R> createHashHashInstance()
   {
      return new DualMapMappings<L, R>(StoreType.HASH.getFactory(), StoreType.HASH.getFactory());
   }

   /**
    * Creates a new instance based on a {@link HashMap} for the left view and a
    * {@link TreeMap} for the right view - so left keys should implement
    * {@link Object#hashCode()} and right keys should be {@link Comparable}.
    * 
    * @return the new instance
    */
   public static <L, R> DualMapMappings<L, R> createHashTreeInstance()
   {
      return new DualMapMappings<L, R>(StoreType.HASH.getFactory(), StoreType.TREE.getFactory());
   }

   /**
    * Creates a new instance based on a {@link TreeMap} for the left view and a
    * {@link HashMap} for the right view - so left keys should be
    * {@link Comparable} and right keys should implement
    * {@link Object#hashCode()}.
    * 
    * @return the new instance
    */
   public static <L, R> DualMapMappings<L, R> createTreeHashInstance()
   {
      return new DualMapMappings<L, R>(StoreType.TREE.getFactory(), StoreType.HASH.getFactory());
   }

   /**
    * Creates a new instance based on a {@link TreeMap} for the left view and a
    * {@link TreeMap} for the right view - so left keys and right keys should be
    * {@link Comparable}.
    * 
    * @return the new instance
    */
   public static <L, R> DualMapMappings<L, R> createTreeTreeInstance()
   {
      return new DualMapMappings<L, R>(StoreType.TREE.getFactory(), StoreType.TREE.getFactory());
   }

   protected final ValueMap<L, R> _left;

   protected DualMapMappings(final StoreFactory leftFactory, final StoreFactory rightFactory)
   {
      _left = new ValueMap<L, R>(leftFactory, rightFactory);
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean put(final L left, final R right)
   {
      if (left == null || right == null)
      {
         throw new IllegalArgumentException("Neither left nor right value may be null!");
      }

      final ValueSet<L, R> valueSet = (ValueSet<L, R>) _left.get(left);
      if (valueSet == null)
      {
         _left.put(left, Collections.singleton(right));

         return true;
      }
      else
      {
         return valueSet.add(right);
      }
   }

   @Override
   public Map<L, Set<R>> getLeftView()
   {
      return _left;
   }

   @Override
   public Map<R, Set<L>> getRightView()
   {
      return _left._partnerMap;
   }

   protected static class ValueMap<L, R> extends AbstractMap<L, Set<R>> implements Serializable
   {
      private static final long serialVersionUID = 3088051603731444631L;

      protected final StoreFactory _factory;
      protected final Map<L, ValueSet<L, R>> _internal;
      protected final ValueMap<R, L> _partnerMap;

      protected ValueMap(final StoreFactory factory, final ValueMap<R, L> partnerMap)
      {
         _factory = factory;
         _internal = factory.createMap();

         _partnerMap = partnerMap;
      }

      protected ValueMap(final StoreFactory leftFactory, final StoreFactory rightFactory)
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
      public Set<R> put(final L key, final Set<R> value)
      {
         if (key == null)
         {
            throw new IllegalArgumentException("Key mustn't be null!");
         }

         if (value == null || value.isEmpty())
         {
            final ValueSet<L, R> valueSet = _internal.remove(key);

            if (valueSet == null)
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
            if (value.contains(null))
            {
               throw new IllegalArgumentException("The value-set may not contain null!");
            }

            ValueSet<L, R> valueSet = _internal.get(key);

            final Set<R> result;
            if (valueSet == null)
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
      public Set<R> remove(final Object key)
      {
         final ValueSet<L, R> valueSet = _internal.remove(key);
         if (valueSet == null)
         {
            return null;
         }

         final Set<R> result = _partnerMap._factory.createSet();
         result.addAll(valueSet);

         valueSet.clear();

         return result;
      }

      @Override
      public Set<R> get(final Object key)
      {
         return _internal.get(key);
      }
   }

   protected static class ValueMapEntrySet<L, R> extends AbstractSet<Entry<L, Set<R>>>
   {
      private final ValueMap<L, R> _parentMap;

      protected ValueMapEntrySet(final ValueMap<L, R> parentMap)
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

      protected ValueMapEntrySetIterator(final ValueMap<L, R> parentMap,
            final Iterator<Entry<L, ValueSet<L, R>>> internalIterator)
      {
         _parentMap = parentMap;
         _internalIterator = internalIterator;
      }

      @Override
      public boolean hasNext()
      {
         return _internalIterator.hasNext();
      }

      @Override
      public Entry<L, Set<R>> next()
      {
         final Entry<L, ValueSet<L, R>> nextEntry = _internalIterator.next();
         _last = nextEntry;

         return new ValueMapEntry<L, R>(_parentMap, nextEntry);
      }

      @Override
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

      protected ValueMapEntry(final ValueMap<L, R> parentMap, final Entry<L, ValueSet<L, R>> nextEntry)
      {
         _parentMap = parentMap;
         this._internalEntry = nextEntry;
      }

      @Override
      public L getKey()
      {
         return _internalEntry.getKey();
      }

      @Override
      public Set<R> getValue()
      {
         return _internalEntry.getValue();
      }

      @Override
      public Set<R> setValue(final Set<R> value)
      {
         if (value == null || value.isEmpty())
         {
            throw new IllegalArgumentException("May not explicitly set null or an empty set as entry value!");
         }

         final ValueSet<L, R> valueSet = _internalEntry.getValue();

         final Set<R> result = _parentMap._partnerMap._factory.createSet();
         result.addAll(valueSet.getDecorated());

         valueSet.clearForReuse();
         valueSet.addAll(value);

         return result;
      }

      @Override
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

      protected ValueSet(final Set<R> decorated, final StoreFactory factory, final L left, final ValueMap<R, L> otherMap)
      {
         super(decorated);
         _factory = factory;
         _left = left;
         _otherMap = otherMap;
      }

      @Override
      public boolean add(final R e)
      {
         if (_detached)
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

      @Override
      public Set<R> getDecorated()
      {
         return super.getDecorated();
      }

      protected void clearForReuse()
      {
         _internalFlag = true;
         clear();
         _internalFlag = false;
      }

      @Override
      protected void fireElementAdded(final R element)
      {
         final Map<R, ValueSet<R, L>> otherInternalMap = _otherMap._internal;
         ValueSet<R, L> otherSet = otherInternalMap.get(element);

         if (otherSet == null)
         {
            final Set<L> otherDecoratedSet = _factory.createSet();
            otherSet = new ValueSet<R, L>(otherDecoratedSet, _otherMap._factory, element, _otherMap._partnerMap);

            otherInternalMap.put(element, otherSet);
         }

         otherSet.getDecorated().add(_left);
      }

      @Override
      protected void fireElementRemoved(final R element)
      {
         final Map<R, ValueSet<R, L>> otherInternalMap = _otherMap._internal;
         final ValueSet<R, L> otherSet = otherInternalMap.get(element);

         otherSet.getDecorated().remove(_left);

         if (otherSet.isEmpty())
         {
            otherSet._detached = true;
            otherInternalMap.remove(element);
         }

         if (isEmpty() && !_internalFlag)
         {
            _detached = true;
            _otherMap._partnerMap._internal.remove(_left);
         }
      }

      @Override
      protected void fireElementReadded(final R element)
      {}
   }

   protected static interface StoreFactory extends Serializable
   {
      public <T> Set<T> createSet();

      public <K, V> Map<K, V> createMap();
   }

   protected static enum StoreType
   {
      HASH(new StoreFactory()
      {
         private static final long serialVersionUID = 5873569465040591757L;

         @Override
         public <K, V> Map<K, V> createMap()
         {
            return new HashMap<K, V>();
         }

         @Override
         public <T> Set<T> createSet()
         {
            return new HashSet<T>();
         }
      }),

      TREE(new StoreFactory()
      {
         private static final long serialVersionUID = 4635243875231393315L;

         @Override
         public <K, V> Map<K, V> createMap()
         {
            return new TreeMap<K, V>();
         }

         @Override
         public <T> Set<T> createSet()
         {
            return new TreeSet<T>();
         }
      });

      private final StoreFactory _factory;

      StoreType(final StoreFactory factory)
      {
         _factory = factory;
      }

      public StoreFactory getFactory()
      {
         return _factory;
      }
   };
}
