package net.sf.javagimmicks.collections8.transformer;

import java.util.NavigableMap;
import java.util.NavigableSet;

import net.sf.javagimmicks.transform8.BidiFunction;

class KeyBidiTransformingNavigableMap<KF, KT, V>
   extends KeyBidiTransformingSortedMap<KF, KT, V>
   implements NavigableMap<KT, V>
{
   KeyBidiTransformingNavigableMap(NavigableMap<KF, V> map, BidiFunction<KF, KT> transformer)
   {
      super(map, transformer);
   }

   public Entry<KT, V> ceilingEntry(KT key)
   {
      Entry<KF, V> ceilingEntry = getNavigableMap().ceilingEntry(transformBack(key));
      
      return ceilingEntry == null ? null : new KeyBidiTransformingEntry<KF, KT, V>(
         ceilingEntry,
         getBidiTransformer());
   }

   public KT ceilingKey(KT key)
   {
      KF ceilingKey = getNavigableMap().ceilingKey(transformBack(key));
      
      return ceilingKey != null ? transform(ceilingKey) : null;
   }

   public NavigableSet<KT> descendingKeySet()
   {
      return TransformerUtils.decorate(
         getNavigableMap().descendingKeySet(),
         getBidiTransformer());
   }

   public NavigableMap<KT, V> descendingMap()
   {
      return TransformerUtils.decorateKeyBased(
         getNavigableMap().descendingMap(),
         getBidiTransformer());
   }

   public Entry<KT, V> firstEntry()
   {
      Entry<KF, V> firstEntry = getNavigableMap().firstEntry();
      
      return firstEntry == null ? null : new KeyBidiTransformingEntry<KF, KT, V>(
         firstEntry,
         getBidiTransformer());
   }

   public Entry<KT, V> floorEntry(KT key)
   {
      Entry<KF, V> floorEntry = getNavigableMap().floorEntry(transformBack(key));
      
      return floorEntry == null ? null : new KeyBidiTransformingEntry<KF, KT, V>(
         floorEntry,
         getBidiTransformer());
   }

   public KT floorKey(KT key)
   {
      KF floorKey = getNavigableMap().floorKey(transformBack(key));
      
      return floorKey != null ? transform(floorKey) : null;
   }

   public NavigableMap<KT, V> headMap(KT toKey, boolean inclusive)
   {
      return TransformerUtils.decorateKeyBased(
         getNavigableMap().headMap(transformBack(toKey), inclusive),
         getBidiTransformer());
   }

   public Entry<KT, V> higherEntry(KT key)
   {
      Entry<KF, V> higherEntry = getNavigableMap().higherEntry(transformBack(key));
      
      return higherEntry == null ? null : new KeyBidiTransformingEntry<KF, KT, V>(
         higherEntry,
         getBidiTransformer());
   }

   public KT higherKey(KT key)
   {
      KF higherKey = getNavigableMap().higherKey(transformBack(key));
      
      return higherKey != null ? transform(higherKey) : null;
   }

   public Entry<KT, V> lastEntry()
   {
      Entry<KF, V> lastEntry = getNavigableMap().lastEntry();
      
      return lastEntry == null ? null : new KeyBidiTransformingEntry<KF, KT, V>(
         lastEntry,
         getBidiTransformer());
   }

   public Entry<KT, V> lowerEntry(KT key)
   {
      Entry<KF, V> lowerEntry = getNavigableMap().lowerEntry(transformBack(key));
      
      return lowerEntry == null ? null : new KeyBidiTransformingEntry<KF, KT, V>(
         lowerEntry,
         getBidiTransformer());
   }

   public KT lowerKey(KT key)
   {
      KF lowerKey = getNavigableMap().lowerKey(transformBack(key));
      
      return lowerKey != null ? transform(lowerKey) : null;
   }

   public NavigableSet<KT> navigableKeySet()
   {
      return TransformerUtils.decorate(
         getNavigableMap().navigableKeySet(),
         getBidiTransformer());
   }

   public Entry<KT, V> pollFirstEntry()
   {
      Entry<KF, V> firstEntry = getNavigableMap().pollFirstEntry();
      
      return firstEntry == null ? null : new KeyBidiTransformingEntry<KF, KT, V>(
         firstEntry,
         getBidiTransformer());
   }

   public Entry<KT, V> pollLastEntry()
   {
      Entry<KF, V> lastEntry = getNavigableMap().pollLastEntry();
      
      return lastEntry == null ? null : new KeyBidiTransformingEntry<KF, KT, V>(
         lastEntry,
         getBidiTransformer());
   }

   public NavigableMap<KT, V> subMap(KT fromKey, boolean fromInclusive,
         KT toKey, boolean toInclusive)
   {
      return TransformerUtils.decorateKeyBased(
         getNavigableMap().subMap(
            transformBack(fromKey),
            fromInclusive,
            transformBack(toKey),
            toInclusive),
         getBidiTransformer());
   }

   public NavigableMap<KT, V> tailMap(KT fromKey, boolean inclusive)
   {
      return TransformerUtils.decorateKeyBased(
         getNavigableMap().tailMap(transformBack(fromKey), inclusive),
         getBidiTransformer());
   }
   
   protected NavigableMap<KF, V> getNavigableMap()
   {
      return (NavigableMap<KF, V>)_internalMap;
   }
}
