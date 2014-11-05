package net.sf.javagimmicks.collections.transformer;

import java.util.NavigableMap;
import java.util.NavigableSet;

import net.sf.javagimmicks.util.Function;

class ValueTransformingNavigableMap<K, VF, VT>
   extends ValueTransformingSortedMap<K, VF, VT>
   implements NavigableMap<K, VT>
{
   /**
    * @deprecated Use TranformerUtils.decorateValueBased() instead
    */
   @Deprecated
   public ValueTransformingNavigableMap(NavigableMap<K, VF> map, Function<VF, VT> valueTransformer)
   {
      super(map, valueTransformer);
   }
   
   public Entry<K, VT> ceilingEntry(K key)
   {
      Entry<K, VF> ceilingEntry = getNavigableMap().ceilingEntry(key);
      
      return ceilingEntry == null ? null : new ValueTransformingEntry<K, VF, VT>(
         ceilingEntry,
         getTransformerFunction());
   }

   public K ceilingKey(K key)
   {
      return getNavigableMap().ceilingKey(key);
   }

   public NavigableSet<K> descendingKeySet()
   {
      return getNavigableMap().descendingKeySet();
   }

   public NavigableMap<K, VT> descendingMap()
   {
      return TransformerUtils.decorateValueBased(
         getNavigableMap().descendingMap(),
         getTransformerFunction());
   }

   public Entry<K, VT> firstEntry()
   {
      Entry<K, VF> firstEntry = getNavigableMap().firstEntry();
      
      return firstEntry == null ? null : new ValueTransformingEntry<K, VF, VT>(
         firstEntry,
         getTransformerFunction());
   }

   public Entry<K, VT> floorEntry(K key)
   {
      Entry<K, VF> floorEntry = getNavigableMap().floorEntry(key);
      
      return floorEntry == null ? null : new ValueTransformingEntry<K, VF, VT>(
         floorEntry,
         getTransformerFunction());
   }

   public K floorKey(K key)
   {
      return getNavigableMap().floorKey(key);
   }

   public NavigableMap<K, VT> headMap(K toKey, boolean inclusive)
   {
      return TransformerUtils.decorateValueBased( 
         getNavigableMap().headMap(toKey, inclusive),
         getTransformerFunction());
   }

   public Entry<K, VT> higherEntry(K key)
   {
      return new ValueTransformingEntry<K, VF, VT>(
         getNavigableMap().higherEntry(key),
         getTransformerFunction());
   }

   public K higherKey(K key)
   {
      return getNavigableMap().higherKey(key);
   }

   public Entry<K, VT> lastEntry()
   {
      Entry<K, VF> lastEntry = getNavigableMap().lastEntry();
      
      return lastEntry == null ? null : new ValueTransformingEntry<K, VF, VT>(
         lastEntry,
         getTransformerFunction());
   }

   public Entry<K, VT> lowerEntry(K key)
   {
      return new ValueTransformingEntry<K, VF, VT>(
         getNavigableMap().lowerEntry(key),
         getTransformerFunction());
   }

   public K lowerKey(K key)
   {
      return getNavigableMap().lowerKey(key);
   }

   public NavigableSet<K> navigableKeySet()
   {
      return getNavigableMap().navigableKeySet();
   }

   public Entry<K, VT> pollFirstEntry()
   {
      Entry<K, VF> firstEntry = getNavigableMap().pollFirstEntry();
      
      return firstEntry == null ? null : new ValueTransformingEntry<K, VF, VT>(
         firstEntry,
         getTransformerFunction());
   }

   public Entry<K, VT> pollLastEntry()
   {
      Entry<K, VF> lastEntry = getNavigableMap().pollLastEntry();
      
      return lastEntry == null ? null : new ValueTransformingEntry<K, VF, VT>(
         lastEntry,
         getTransformerFunction());
   }

   public NavigableMap<K, VT> subMap(K fromKey, boolean fromInclusive, K toKey,
         boolean toInclusive)
   {
      return TransformerUtils.decorateValueBased( 
         getNavigableMap().subMap(fromKey, fromInclusive, toKey, toInclusive),
         getTransformerFunction());
   }

   public NavigableMap<K, VT> tailMap(K fromKey, boolean inclusive)
   {
      return TransformerUtils.decorateValueBased( 
         getNavigableMap().tailMap(fromKey, inclusive),
         getTransformerFunction());
   }

   protected NavigableMap<K, VF> getNavigableMap()
   {
      return (NavigableMap<K, VF>)_internalMap;
   }
}
