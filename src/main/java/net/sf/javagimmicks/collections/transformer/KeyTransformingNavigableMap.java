package net.sf.javagimmicks.collections.transformer;

import java.util.NavigableMap;
import java.util.NavigableSet;

class KeyTransformingNavigableMap<KF, KT, V>
   extends KeyTransformingSortedMap<KF, KT, V>
   implements NavigableMap<KT, V>
{
   /**
    * @deprecated Use TranformerUtils.decorateKeyBased() instead
    */
   @Deprecated
   public KeyTransformingNavigableMap(NavigableMap<KF, V> map, Transformer<KF, KT> transformer)
   {
      super(map, transformer);
   }

   public Entry<KT, V> ceilingEntry(KT key)
   {
      throw new UnsupportedOperationException();
   }

   public KT ceilingKey(KT key)
   {
      throw new UnsupportedOperationException();
   }

   public NavigableSet<KT> descendingKeySet()
   {
      return TransformerUtils.decorate(
         getNavigableMap().descendingKeySet(),
         getTransformer());
   }

   public NavigableMap<KT, V> descendingMap()
   {
      return TransformerUtils.decorateKeyBased(
            getNavigableMap().descendingMap(),
            getTransformer());
   }

   public Entry<KT, V> firstEntry()
   {
      Entry<KF, V> firstEntry = getNavigableMap().firstEntry();
      
      return firstEntry == null ? null : new KeyTransformingEntry<KF, KT, V>(
         firstEntry,
         getTransformer());
   }

   public Entry<KT, V> floorEntry(KT key)
   {
      throw new UnsupportedOperationException();
   }

   public KT floorKey(KT key)
   {
      throw new UnsupportedOperationException();
   }

   public NavigableMap<KT, V> headMap(KT toKey, boolean inclusive)
   {
      throw new UnsupportedOperationException();
   }

   public Entry<KT, V> higherEntry(KT key)
   {
      throw new UnsupportedOperationException();
   }

   public KT higherKey(KT key)
   {
      throw new UnsupportedOperationException();
   }

   public Entry<KT, V> lastEntry()
   {
      Entry<KF, V> lastEntry = getNavigableMap().lastEntry();
      
      return lastEntry == null ? null : new KeyTransformingEntry<KF, KT, V>(
            lastEntry,
            getTransformer());
   }

   public Entry<KT, V> lowerEntry(KT key)
   {
      throw new UnsupportedOperationException();
   }

   public KT lowerKey(KT key)
   {
      throw new UnsupportedOperationException();
   }

   public NavigableSet<KT> navigableKeySet()
   {
      return TransformerUtils.decorate(
         getNavigableMap().navigableKeySet(),
         getTransformer());
   }

   public Entry<KT, V> pollFirstEntry()
   {
      Entry<KF, V> first = getNavigableMap().pollFirstEntry();
      
      return first == null ? null : new KeyTransformingEntry<KF, KT, V>(
            first,
            getTransformer());
   }

   public Entry<KT, V> pollLastEntry()
   {
      Entry<KF, V> lastEntry = getNavigableMap().pollLastEntry();
      
      return lastEntry == null ? null : new KeyTransformingEntry<KF, KT, V>(
            lastEntry,
            getTransformer());
   }

   public NavigableMap<KT, V> subMap(KT fromKey, boolean fromInclusive,
         KT toKey, boolean toInclusive)
   {
      throw new UnsupportedOperationException();
   }

   public NavigableMap<KT, V> tailMap(KT fromKey, boolean inclusive)
   {
      throw new UnsupportedOperationException();
   }
   
   protected NavigableMap<KF, V> getNavigableMap()
   {
      return (NavigableMap<KF, V>)_internalMap;
   }
}
