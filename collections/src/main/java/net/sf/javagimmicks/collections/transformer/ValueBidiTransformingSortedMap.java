package net.sf.javagimmicks.collections.transformer;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

import net.sf.javagimmicks.transform.BidiTransformer;

class ValueBidiTransformingSortedMap<K, VF, VT> extends ValueBidiTransformingMap<K, VF, VT> implements SortedMap<K, VT>
{
   /**
    * @deprecated Use TranformerUtils.decorateValueBased() instead
    */
   @Deprecated
   public ValueBidiTransformingSortedMap(final Map<K, VF> map, final BidiTransformer<VF, VT> valueTransformer)
   {
      super(map, valueTransformer);
   }

   @Override
   public Comparator<? super K> comparator()
   {
      return getSortedMap().comparator();
   }

   @Override
   public K firstKey()
   {
      return getSortedMap().firstKey();
   }

   @Override
   public SortedMap<K, VT> headMap(final K toKey)
   {
      return TransformerUtils.decorateValueBased(
            getSortedMap().headMap(toKey),
            getBidiTransformer());
   }

   @Override
   public K lastKey()
   {
      return getSortedMap().lastKey();
   }

   @Override
   public SortedMap<K, VT> subMap(final K fromKey, final K toKey)
   {
      return TransformerUtils.decorateValueBased(
            getSortedMap().subMap(fromKey, toKey),
            getBidiTransformer());
   }

   @Override
   public SortedMap<K, VT> tailMap(final K fromKey)
   {
      return TransformerUtils.decorateValueBased(
            getSortedMap().tailMap(fromKey),
            getBidiTransformer());
   }

   protected SortedMap<K, VF> getSortedMap()
   {
      return (SortedMap<K, VF>) _internalMap;
   }
}
