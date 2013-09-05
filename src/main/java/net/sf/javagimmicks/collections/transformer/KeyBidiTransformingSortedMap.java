package net.sf.javagimmicks.collections.transformer;

import java.util.Comparator;
import java.util.SortedMap;

import net.sf.javagimmicks.util.ComparableComparator;

class KeyBidiTransformingSortedMap<KF, KT, V> extends KeyBidiTransformingMap<KF, KT, V> implements SortedMap<KT, V>
{
   /**
    * @deprecated Use TranformerUtils.decorateKeyBased() instead
    */
   @Deprecated
   public KeyBidiTransformingSortedMap(SortedMap<KF, V> map, BidiTransformer<KF, KT> transformer)
   {
      super(map, transformer);
   }
   
   @SuppressWarnings("unchecked")
   public Comparator<? super KT> comparator()
   {
      Comparator<? super KF> baseComparator = getSortedMap().comparator();
      if(baseComparator == null)
      {
         baseComparator = (Comparator<? super KF>)ComparableComparator.INSTANCE;
      }
      
      return TransformerUtils.decorate(
         baseComparator,
         getBidiTransformer().invert());
   }

   public KT firstKey()
   {
      return transform(getSortedMap().firstKey());
   }

   public SortedMap<KT, V> headMap(KT toKey)
   {
      return TransformerUtils.decorateKeyBased(
         getSortedMap().headMap(transformBack(toKey)),
         getBidiTransformer());
   }

   public KT lastKey()
   {
      return transform(getSortedMap().lastKey());
   }

   public SortedMap<KT, V> subMap(KT fromKey, KT toKey)
   {
      return TransformerUtils.decorateKeyBased(
         getSortedMap().subMap(
            transformBack(fromKey),
            transformBack(toKey)),
         getBidiTransformer());
   }

   public SortedMap<KT, V> tailMap(KT fromKey)
   {
      return TransformerUtils.decorateKeyBased(
         getSortedMap().tailMap(transformBack(fromKey)),
         getBidiTransformer());
   }

   protected SortedMap<KF, V> getSortedMap()
   {
      return (SortedMap<KF, V>)_internalMap;
   }

}
