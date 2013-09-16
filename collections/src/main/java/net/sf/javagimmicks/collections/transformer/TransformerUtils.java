package net.sf.javagimmicks.collections.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import net.sf.javagimmicks.collections.Ring;
import net.sf.javagimmicks.collections.RingCursor;

/**
 * This class is the central entry point to the Javagimmicks transformer API by
 * providing decorator- and generator methods for many transforming types.
 * <p>
 * A more detailed description of the API can be found at in the package
 * description {@link net.sf.javagimmicks.collections.transformer}.
 * 
 * @see net.sf.javagimmicks.collections.transformer
 * @author Michael Scholz
 */
@SuppressWarnings("deprecation")
public class TransformerUtils
{
   protected TransformerUtils()
   {}

   public static <E> Transformer<E, E> identityTransformer()
   {
      return identityBidiTransformer();
   }

   public static <E> BidiTransformer<E, E> identityBidiTransformer()
   {
      return new BidiTransformer<E, E>()
      {
         @Override
         public E transform(final E source)
         {
            return source;
         }

         @Override
         public BidiTransformer<E, E> invert()
         {
            return this;
         }

         @Override
         public E transformBack(final E source)
         {
            return source;
         }
      };
   }

   public static <F, T> BidiTransformer<T, F> invert(final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformer<T, F>()
      {
         @Override
         public T transformBack(final F source)
         {
            return transformer.transform(source);
         }

         @Override
         public F transform(final T source)
         {
            return transformer.transformBack(source);
         }

         @Override
         public BidiTransformer<F, T> invert()
         {
            return transformer;
         }
      };
   }

   public static boolean isTransforming(final Object o)
   {
      return o instanceof Transforming<?, ?>;
   }

   public static boolean isBidiTransforming(final Object o)
   {
      return o instanceof BidiTransforming<?, ?>;
   }

   public static Transformer<?, ?> getTransformer(final Object transforming)
   {
      if (!isTransforming(transforming))
      {
         throw new IllegalArgumentException("Object is not transforming!");
      }

      return ((Transforming<?, ?>) transforming).getTransformer();
   }

   public static BidiTransformer<?, ?> getBidiTransformer(final Object transforming)
   {
      if (!isBidiTransforming(transforming))
      {
         throw new IllegalArgumentException("Object is not bidi-transforming!");
      }

      return ((BidiTransforming<?, ?>) transforming).getBidiTransformer();
   }

   public static <F, T> Comparator<? super T> decorate(final Comparator<? super F> comparator,
         final Transformer<T, F> transformer)
   {
      return new TransformingComparator<F, T>(comparator, transformer);
   }

   public static <F, T> Iterator<T> decorate(final Iterator<F> iterator, final Transformer<F, T> transformer)
   {
      return new TransformingIterator<F, T>(iterator, transformer);
   }

   public static <F, T> ListIterator<T> decorate(final ListIterator<F> iterator, final Transformer<F, T> transformer)
   {
      return new TransformingListIterator<F, T>(iterator, transformer);
   }

   public static <F, T> ListIterator<T> decorate(final ListIterator<F> iterator, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingListIterator<F, T>(iterator, transformer);
   }

   public static <F, T> Collection<T> decorate(final Collection<F> collection, final Transformer<F, T> transformer)
   {
      return new TransformingCollection<F, T>(collection, transformer);
   }

   public static <F, T> Collection<T> decorate(final Collection<F> collection, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingCollection<F, T>(collection, transformer);
   }

   public static <F, T> Set<T> decorate(final Set<F> set, final Transformer<F, T> transformer)
   {
      return new TransformingSet<F, T>(set, transformer);
   }

   public static <F, T> Set<T> decorate(final Set<F> set, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingSet<F, T>(set, transformer);
   }

   public static <F, T> SortedSet<T> decorate(final SortedSet<F> set, final Transformer<F, T> transformer)
   {
      return new TransformingSortedSet<F, T>(set, transformer);
   }

   public static <F, T> SortedSet<T> decorate(final SortedSet<F> set, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingSortedSet<F, T>(set, transformer);
   }

   public static <F, T> List<T> decorate(final List<F> list, final Transformer<F, T> transformer)
   {
      return new TransformingList<F, T>(list, transformer);
   }

   public static <F, T> List<T> decorate(final List<F> list, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingList<F, T>(list, transformer);
   }

   public static <KF, KT, V> Map<KT, V> decorateKeyBased(final Map<KF, V> map, final Transformer<KF, KT> keyTransformer)
   {
      return new KeyTransformingMap<KF, KT, V>(map, keyTransformer);
   }

   public static <KF, KT, V> Map<KT, V> decorateKeyBased(final Map<KF, V> map,
         final BidiTransformer<KF, KT> keyTransformer)
   {
      return new KeyBidiTransformingMap<KF, KT, V>(map, keyTransformer);
   }

   public static <KF, KT, V> SortedMap<KT, V> decorateKeyBased(final SortedMap<KF, V> map,
         final Transformer<KF, KT> keyTransformer)
   {
      return new KeyTransformingSortedMap<KF, KT, V>(map, keyTransformer);
   }

   public static <KF, KT, V> SortedMap<KT, V> decorateKeyBased(final SortedMap<KF, V> map,
         final BidiTransformer<KF, KT> keyTransformer)
   {
      return new KeyBidiTransformingSortedMap<KF, KT, V>(map, keyTransformer);
   }

   public static <K, VF, VT> Map<K, VT> decorateValueBased(final Map<K, VF> map,
         final Transformer<VF, VT> valueTransformer)
   {
      return new ValueTransformingMap<K, VF, VT>(map, valueTransformer);
   }

   public static <K, VF, VT> Map<K, VT> decorateValueBased(final Map<K, VF> map,
         final BidiTransformer<VF, VT> valueTransformer)
   {
      return new ValueBidiTransformingMap<K, VF, VT>(map, valueTransformer);
   }

   public static <K, VF, VT> SortedMap<K, VT> decorateValueBased(final SortedMap<K, VF> map,
         final Transformer<VF, VT> valueTransformer)
   {
      return new ValueTransformingSortedMap<K, VF, VT>(map, valueTransformer);
   }

   public static <K, VF, VT> SortedMap<K, VT> decorateValueBased(final SortedMap<K, VF> map,
         final BidiTransformer<VF, VT> valueTransformer)
   {
      return new ValueBidiTransformingSortedMap<K, VF, VT>(map, valueTransformer);
   }

   public static <KF, KT, VF, VT> Map<KT, VT> decorate(final Map<KF, VF> map, final Transformer<KF, KT> keyTransformer,
         final Transformer<VF, VT> valueTransformer)
   {
      final Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> Map<KT, VT> decorate(final Map<KF, VF> map,
         final BidiTransformer<KF, KT> keyTransformer, final Transformer<VF, VT> valueTransformer)
   {
      final Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> Map<KT, VT> decorate(final Map<KF, VF> map, final Transformer<KF, KT> keyTransformer,
         final BidiTransformer<VF, VT> valueTransformer)
   {
      final Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> Map<KT, VT> decorate(final Map<KF, VF> map,
         final BidiTransformer<KF, KT> keyTransformer, final BidiTransformer<VF, VT> valueTransformer)
   {
      final Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(final SortedMap<KF, VF> map,
         final Transformer<KF, KT> keyTransformer, final Transformer<VF, VT> valueTransformer)
   {
      final SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(final SortedMap<KF, VF> map,
         final BidiTransformer<KF, KT> keyTransformer, final Transformer<VF, VT> valueTransformer)
   {
      final SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(final SortedMap<KF, VF> map,
         final Transformer<KF, KT> keyTransformer, final BidiTransformer<VF, VT> valueTransformer)
   {
      final SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(final SortedMap<KF, VF> map,
         final BidiTransformer<KF, KT> keyTransformer, final BidiTransformer<VF, VT> valueTransformer)
   {
      final SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <F, T> NavigableSet<T> decorate(final NavigableSet<F> set, final Transformer<F, T> transformer)
   {
      return new TransformingNavigableSet<F, T>(set, transformer);
   }

   public static <F, T> NavigableSet<T> decorate(final NavigableSet<F> set, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingNavigableSet<F, T>(set, transformer);
   }

   public static <KF, KT, V> NavigableMap<KT, V> decorateKeyBased(final NavigableMap<KF, V> map,
         final Transformer<KF, KT> keyTransformer)
   {
      return new KeyTransformingNavigableMap<KF, KT, V>(map, keyTransformer);
   }

   public static <KF, KT, V> NavigableMap<KT, V> decorateKeyBased(final NavigableMap<KF, V> map,
         final BidiTransformer<KF, KT> keyTransformer)
   {
      return new KeyBidiTransformingNavigableMap<KF, KT, V>(map, keyTransformer);
   }

   public static <K, VF, VT> NavigableMap<K, VT> decorateValueBased(final NavigableMap<K, VF> map,
         final Transformer<VF, VT> valueTransformer)
   {
      return new ValueTransformingNavigableMap<K, VF, VT>(map, valueTransformer);
   }

   public static <K, VF, VT> NavigableMap<K, VT> decorateValueBased(final NavigableMap<K, VF> map,
         final BidiTransformer<VF, VT> valueTransformer)
   {
      return new ValueBidiTransformingNavigableMap<K, VF, VT>(map, valueTransformer);
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(final NavigableMap<KF, VF> map,
         final Transformer<KF, KT> keyTransformer, final Transformer<VF, VT> valueTransformer)
   {
      final NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(final NavigableMap<KF, VF> map,
         final BidiTransformer<KF, KT> keyTransformer, final Transformer<VF, VT> valueTransformer)
   {
      final NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(final NavigableMap<KF, VF> map,
         final Transformer<KF, KT> keyTransformer, final BidiTransformer<VF, VT> valueTransformer)
   {
      final NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(final NavigableMap<KF, VF> map,
         final BidiTransformer<KF, KT> keyTransformer, final BidiTransformer<VF, VT> valueTransformer)
   {
      final NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <F, T> RingCursor<T> decorate(final RingCursor<F> ringCursor, final Transformer<F, T> transformer)
   {
      return new TransformingRingCursor<F, T>(ringCursor, transformer);
   }

   public static <F, T> RingCursor<T> decorate(final RingCursor<F> ringCursor, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingRingCursor<F, T>(ringCursor, transformer);
   }

   public static <F, T> Ring<T> decorate(final Ring<F> ring, final Transformer<F, T> transformer)
   {
      return new TransformingRing<F, T>(ring, transformer);
   }

   public static <F, T> Ring<T> decorate(final Ring<F> ring, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingRing<F, T>(ring, transformer);
   }

   public static <F, T> void transform(final Collection<F> fromCollection, final Collection<T> toCollection,
         final Transformer<F, T> transformer)
   {
      toCollection.addAll(decorate(fromCollection, transformer));
   }

   public static <F, T> ArrayList<T> tronsformToArrayList(final Collection<F> fromCollection,
         final Transformer<F, T> transformer)
   {
      return transformInternal(fromCollection, new ArrayList<T>(fromCollection.size()), transformer);
   }

   public static <F, T> LinkedList<T> transformToLinkedList(final Collection<F> fromCollection,
         final Transformer<F, T> transformer)
   {
      return transformInternal(fromCollection, new LinkedList<T>(), transformer);
   }

   private static <F, T, C extends Collection<T>> C transformInternal(final Collection<F> fromCollection,
         final C toCollection,
         final Transformer<F, T> transformer)
   {
      transform(fromCollection, toCollection, transformer);

      return toCollection;
   }
}
