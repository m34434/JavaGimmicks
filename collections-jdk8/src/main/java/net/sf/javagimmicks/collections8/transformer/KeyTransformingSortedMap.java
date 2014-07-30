package net.sf.javagimmicks.collections8.transformer;

import java.util.Comparator;
import java.util.SortedMap;
import java.util.function.Function;

class KeyTransformingSortedMap<KF, KT, V> extends KeyTransformingMap<KF, KT, V> implements SortedMap<KT, V>
{
   KeyTransformingSortedMap(SortedMap<KF, V> map, Function<KF, KT> transformer)
   {
      super(map, transformer);
   }

   public Comparator<? super KT> comparator()
   {
      throw new UnsupportedOperationException();
   }

   public KT firstKey()
   {
      return transform(getSortedMap().firstKey());
   }

   public SortedMap<KT, V> headMap(KT toKey)
   {
      throw new UnsupportedOperationException();
   }

   public KT lastKey()
   {
      return transform(getSortedMap().lastKey());
   }

   public SortedMap<KT, V> subMap(KT fromKey, KT toKey)
   {
      throw new UnsupportedOperationException();
   }

   public SortedMap<KT, V> tailMap(KT fromKey)
   {
      throw new UnsupportedOperationException();
   }  

   protected SortedMap<KF, V> getSortedMap()
   {
      return (SortedMap<KF, V>)_internalMap;
   }
}
