package net.sf.javagimmicks.collections8.transformer;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

import net.sf.javagimmicks.transform8.BidiFunction;

class ValueBidiTransformingSortedMap<K, VF, VT> extends ValueBidiTransformingMap<K, VF, VT> implements SortedMap<K, VT>
{
   ValueBidiTransformingSortedMap(final Map<K, VF> map, final BidiFunction<VF, VT> valueTransformer)
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
            getTransformerBidiFunction());
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
            getTransformerBidiFunction());
   }

   @Override
   public SortedMap<K, VT> tailMap(final K fromKey)
   {
      return TransformerUtils.decorateValueBased(
            getSortedMap().tailMap(fromKey),
            getTransformerBidiFunction());
   }

   protected SortedMap<K, VF> getSortedMap()
   {
      return (SortedMap<K, VF>) _internalMap;
   }
}
