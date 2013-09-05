package net.sf.javagimmicks.collections.transformer;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import net.sf.javagimmicks.collections.bidimap.BidiMap;

public class MappedTransformerUtils
{
   private MappedTransformerUtils() {}
   
   
   
   public static <F, T> Transformer<F, T> asTransformer(final Map<F, T> map)
   {
      return new MapTransformer<F, T>(map);
   }
   
   public static <F, T> BidiTransformer<F, T> asBidiTransformer(BidiMap<F, T> bidiMap)
   {
      return new BidiMapBidiTransformer<F, T>(bidiMap);
   }
   
   public static <F, T> Comparator<? super T> map(Comparator<? super F> comparator, Map<T, F> map)
   {
      return TransformerUtils.decorate(comparator, asTransformer(map));
   }
   
   public static <F, T> Iterator<T> map(Iterator<F> base, Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }
   
   public static <F, T> Collection<T> map(Collection<F> base, Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> Collection<T> map(Collection<F> base, BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }
   
   public static <F, T> Set<T> map(Set<F> base, Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> Set<T> map(Set<F> base, BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }
   
   public static <F, T> SortedSet<T> map(SortedSet<F> base, Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> SortedSet<T> map(SortedSet<F> base, BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }
   
   public static <F, T> NavigableSet<T> map(NavigableSet<F> base, Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> NavigableSet<T> map(NavigableSet<F> base, BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }
   
   public static <F, T> ListIterator<T> map(ListIterator<F> base, Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> ListIterator<T> map(ListIterator<F> base, BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }
   
   public static <F, T> List<T> map(List<F> base, Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> List<T> map(List<F> base, BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }
   
   public static <KF, KT, V> Map<KT, V> mapKeyBased(Map<KF, V> base, Map<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asTransformer(map));
   }
   
   public static <KF, KT, V> Map<KT, V> mapKeyBased(Map<KF, V> base, BidiMap<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asBidiTransformer(map));
   }
   
   public static <K, VF, VT> Map<K, VT> mapValueBased(Map<K, VF> base, Map<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asTransformer(map));
   }
   
   public static <K, VF, VT> Map<K, VT> mapValueBased(Map<K, VF> base, BidiMap<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asBidiTransformer(map));
   }
   
   public static <KF, KT, VF, VT> Map<KT, VT> map(Map<KF, VF> base, Map<KF, KT> keyMap, Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asTransformer(valueMap));
   }
   
   public static <KF, KT, VF, VT> Map<KT, VT> map(Map<KF, VF> base, BidiMap<KF, KT> keyMap, Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asTransformer(valueMap));
   }
   
   public static <KF, KT, VF, VT> Map<KT, VT> map(Map<KF, VF> base, Map<KF, KT> keyMap, BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asBidiTransformer(valueMap));
   }
   
   public static <KF, KT, VF, VT> Map<KT, VT> map(Map<KF, VF> base, BidiMap<KF, KT> keyMap, BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asBidiTransformer(valueMap));
   }
   
   public static <KF, KT, V> SortedMap<KT, V> mapKeyBased(SortedMap<KF, V> base, Map<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asTransformer(map));
   }
   
   public static <KF, KT, V> SortedMap<KT, V> mapKeyBased(SortedMap<KF, V> base, BidiMap<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asBidiTransformer(map));
   }
   
   public static <K, VF, VT> SortedMap<K, VT> mapValueBased(SortedMap<K, VF> base, Map<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asTransformer(map));
   }
   
   public static <K, VF, VT> SortedMap<K, VT> mapValueBased(SortedMap<K, VF> base, BidiMap<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asBidiTransformer(map));
   }
   
   public static <KF, KT, VF, VT> SortedMap<KT, VT> map(SortedMap<KF, VF> base, Map<KF, KT> keyMap, Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asTransformer(valueMap));
   }
   
   public static <KF, KT, VF, VT> SortedMap<KT, VT> map(SortedMap<KF, VF> base, BidiMap<KF, KT> keyMap, Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asTransformer(valueMap));
   }
   
   public static <KF, KT, VF, VT> SortedMap<KT, VT> map(SortedMap<KF, VF> base, Map<KF, KT> keyMap, BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asBidiTransformer(valueMap));
   }
   
   public static <KF, KT, VF, VT> SortedMap<KT, VT> map(SortedMap<KF, VF> base, BidiMap<KF, KT> keyMap, BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asBidiTransformer(valueMap));
   }
   
   public static <KF, KT, V> NavigableMap<KT, V> mapKeyBased(NavigableMap<KF, V> base, Map<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asTransformer(map));
   }
   
   public static <KF, KT, V> NavigableMap<KT, V> mapKeyBased(NavigableMap<KF, V> base, BidiMap<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asBidiTransformer(map));
   }
   
   public static <K, VF, VT> NavigableMap<K, VT> mapValueBased(NavigableMap<K, VF> base, Map<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asTransformer(map));
   }
   
   public static <K, VF, VT> NavigableMap<K, VT> mapValueBased(NavigableMap<K, VF> base, BidiMap<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asBidiTransformer(map));
   }
   
   public static <KF, KT, VF, VT> NavigableMap<KT, VT> map(NavigableMap<KF, VF> base, Map<KF, KT> keyMap, Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asTransformer(valueMap));
   }
   
   public static <KF, KT, VF, VT> NavigableMap<KT, VT> map(NavigableMap<KF, VF> base, BidiMap<KF, KT> keyMap, Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asTransformer(valueMap));
   }
   
   public static <KF, KT, VF, VT> NavigableMap<KT, VT> map(NavigableMap<KF, VF> base, Map<KF, KT> keyMap, BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asBidiTransformer(valueMap));
   }
   
   public static <KF, KT, VF, VT> NavigableMap<KT, VT> map(NavigableMap<KF, VF> base, BidiMap<KF, KT> keyMap, BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asBidiTransformer(valueMap));
   }
   
   protected static class MapTransformer<F, T> implements Transformer<F, T>
   {
      protected final Map<F, T> _baseMap;

      protected MapTransformer(Map<F, T> map)
      {
         _baseMap = map;
      }

      public T transform(F source)
      {
         return _baseMap.get(source);
      }
   }
   
   protected static class BidiMapBidiTransformer<F, T> extends MapTransformer<F, T> implements BidiTransformer<F, T>
   {
      protected BidiMapBidiTransformer(BidiMap<F, T> map)
      {
         super(map);
      }

      public BidiTransformer<T, F> invert()
      {
         return TransformerUtils.invert(this);
      }

      public F transformBack(T source)
      {
         return ((BidiMap<F, T>)_baseMap).inverseBidiMap().get(source);
      }
   }
}
