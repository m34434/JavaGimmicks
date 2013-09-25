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

/**
 * Provides features to build {@link Transformer}s based on {@link Map}s and
 * {@link BidiTransformer}s based on {@link BidiMap}s.
 */
public class MappedTransformerUtils
{
   private MappedTransformerUtils()
   {}

   public static <F, T> Transformer<F, T> asTransformer(final Map<F, T> map)
   {
      return new MapTransformer<F, T>(map);
   }

   public static <F, T> BidiTransformer<F, T> asBidiTransformer(final BidiMap<F, T> bidiMap)
   {
      return new BidiMapBidiTransformer<F, T>(bidiMap);
   }

   public static <F, T> Comparator<? super T> map(final Comparator<? super F> comparator, final Map<T, F> map)
   {
      return TransformerUtils.decorate(comparator, asTransformer(map));
   }

   public static <F, T> Iterator<T> map(final Iterator<F> base, final Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> Collection<T> map(final Collection<F> base, final Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> Collection<T> map(final Collection<F> base, final BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }

   public static <F, T> Set<T> map(final Set<F> base, final Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> Set<T> map(final Set<F> base, final BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }

   public static <F, T> SortedSet<T> map(final SortedSet<F> base, final Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> SortedSet<T> map(final SortedSet<F> base, final BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }

   public static <F, T> NavigableSet<T> map(final NavigableSet<F> base, final Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> NavigableSet<T> map(final NavigableSet<F> base, final BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }

   public static <F, T> ListIterator<T> map(final ListIterator<F> base, final Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> ListIterator<T> map(final ListIterator<F> base, final BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }

   public static <F, T> List<T> map(final List<F> base, final Map<F, T> map)
   {
      return TransformerUtils.decorate(base, asTransformer(map));
   }

   public static <F, T> List<T> map(final List<F> base, final BidiMap<F, T> map)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(map));
   }

   public static <KF, KT, V> Map<KT, V> mapKeyBased(final Map<KF, V> base, final Map<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asTransformer(map));
   }

   public static <KF, KT, V> Map<KT, V> mapKeyBased(final Map<KF, V> base, final BidiMap<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asBidiTransformer(map));
   }

   public static <K, VF, VT> Map<K, VT> mapValueBased(final Map<K, VF> base, final Map<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asTransformer(map));
   }

   public static <K, VF, VT> Map<K, VT> mapValueBased(final Map<K, VF> base, final BidiMap<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asBidiTransformer(map));
   }

   public static <KF, KT, VF, VT> Map<KT, VT> map(final Map<KF, VF> base, final Map<KF, KT> keyMap,
         final Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asTransformer(valueMap));
   }

   public static <KF, KT, VF, VT> Map<KT, VT> map(final Map<KF, VF> base, final BidiMap<KF, KT> keyMap,
         final Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asTransformer(valueMap));
   }

   public static <KF, KT, VF, VT> Map<KT, VT> map(final Map<KF, VF> base, final Map<KF, KT> keyMap,
         final BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asBidiTransformer(valueMap));
   }

   public static <KF, KT, VF, VT> Map<KT, VT> map(final Map<KF, VF> base, final BidiMap<KF, KT> keyMap,
         final BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asBidiTransformer(valueMap));
   }

   public static <KF, KT, V> SortedMap<KT, V> mapKeyBased(final SortedMap<KF, V> base, final Map<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asTransformer(map));
   }

   public static <KF, KT, V> SortedMap<KT, V> mapKeyBased(final SortedMap<KF, V> base, final BidiMap<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asBidiTransformer(map));
   }

   public static <K, VF, VT> SortedMap<K, VT> mapValueBased(final SortedMap<K, VF> base, final Map<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asTransformer(map));
   }

   public static <K, VF, VT> SortedMap<K, VT> mapValueBased(final SortedMap<K, VF> base, final BidiMap<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asBidiTransformer(map));
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> map(final SortedMap<KF, VF> base, final Map<KF, KT> keyMap,
         final Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asTransformer(valueMap));
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> map(final SortedMap<KF, VF> base, final BidiMap<KF, KT> keyMap,
         final Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asTransformer(valueMap));
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> map(final SortedMap<KF, VF> base, final Map<KF, KT> keyMap,
         final BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asBidiTransformer(valueMap));
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> map(final SortedMap<KF, VF> base, final BidiMap<KF, KT> keyMap,
         final BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asBidiTransformer(valueMap));
   }

   public static <KF, KT, V> NavigableMap<KT, V> mapKeyBased(final NavigableMap<KF, V> base, final Map<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asTransformer(map));
   }

   public static <KF, KT, V> NavigableMap<KT, V> mapKeyBased(final NavigableMap<KF, V> base, final BidiMap<KF, KT> map)
   {
      return TransformerUtils.decorateKeyBased(base, asBidiTransformer(map));
   }

   public static <K, VF, VT> NavigableMap<K, VT> mapValueBased(final NavigableMap<K, VF> base, final Map<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asTransformer(map));
   }

   public static <K, VF, VT> NavigableMap<K, VT> mapValueBased(final NavigableMap<K, VF> base, final BidiMap<VF, VT> map)
   {
      return TransformerUtils.decorateValueBased(base, asBidiTransformer(map));
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> map(final NavigableMap<KF, VF> base, final Map<KF, KT> keyMap,
         final Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asTransformer(valueMap));
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> map(final NavigableMap<KF, VF> base,
         final BidiMap<KF, KT> keyMap, final Map<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asTransformer(valueMap));
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> map(final NavigableMap<KF, VF> base, final Map<KF, KT> keyMap,
         final BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asTransformer(keyMap), asBidiTransformer(valueMap));
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> map(final NavigableMap<KF, VF> base,
         final BidiMap<KF, KT> keyMap, final BidiMap<VF, VT> valueMap)
   {
      return TransformerUtils.decorate(base, asBidiTransformer(keyMap), asBidiTransformer(valueMap));
   }

   protected static class MapTransformer<F, T> implements Transformer<F, T>
   {
      protected final Map<F, T> _baseMap;

      protected MapTransformer(final Map<F, T> map)
      {
         _baseMap = map;
      }

      @Override
      public T transform(final F source)
      {
         return _baseMap.get(source);
      }
   }

   protected static class BidiMapBidiTransformer<F, T> extends MapTransformer<F, T> implements BidiTransformer<F, T>
   {
      protected BidiMapBidiTransformer(final BidiMap<F, T> map)
      {
         super(map);
      }

      @Override
      public BidiTransformer<T, F> invert()
      {
         return TransformerUtils.invert(this);
      }

      @Override
      public F transformBack(final T source)
      {
         return ((BidiMap<F, T>) _baseMap).inverseBidiMap().get(source);
      }
   }
}
