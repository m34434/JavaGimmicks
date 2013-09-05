package net.sf.javagimmicks.collections.transformer;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

class ValueBidiTransformingSortedMap<K, VF, VT> extends ValueBidiTransformingMap<K, VF, VT> implements SortedMap<K, VT>
{
   /**
    * @deprecated Use TranformerUtils.decorateValueBased() instead
    */
   @Deprecated
   public ValueBidiTransformingSortedMap(Map<K, VF> map, BidiTransformer<VF, VT> valueTransformer)
   {
      super(map, valueTransformer);
      // TODO Auto-generated constructor stub
   }

   public Comparator<? super K> comparator()
   {
      return getSortedMap().comparator();
   }

   public K firstKey()
   {
      return getSortedMap().firstKey();
   }

   public SortedMap<K, VT> headMap(K toKey)
   {
      return TransformerUtils.decorateValueBased(
         getSortedMap().headMap(toKey),
         getBidiTransformer());
   }

   public K lastKey()
   {
      return getSortedMap().lastKey();
   }

   public SortedMap<K, VT> subMap(K fromKey, K toKey)
   {
      return TransformerUtils.decorateValueBased(
         getSortedMap().subMap(fromKey, toKey),
         getBidiTransformer());
   }

   public SortedMap<K, VT> tailMap(K fromKey)
   {
      return TransformerUtils.decorateValueBased(
         getSortedMap().tailMap(fromKey),
         getBidiTransformer());
   }

   
   protected SortedMap<K, VF> getSortedMap()
   {
      return (SortedMap<K, VF>)_internalMap;
   }
}
