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

   /**
    * Creates a new pseudo-{@link Transformer} that always returns the original
    * value.
    * 
    * @return a new pseudo-{@link Transformer} that always returns the original
    *         value
    */
   public static <E> Transformer<E, E> identityTransformer()
   {
      return identityBidiTransformer();
   }

   /**
    * Creates a new pseudo-{@link BidiTransformer} that always returns the
    * original value.
    * 
    * @return a new pseudo-{@link BidiTransformer} that always returns the
    *         original value
    */
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

   /**
    * Creates a new inverted version of a given {@link BidiTransformer}
    * (exchanges {@link BidiTransformer#transform(Object)} and
    * {@link BidiTransformer#transformBack(Object)}).
    * 
    * @param transformer
    *           the {@link BidiTransformer} to invert
    * @return a inverted version of the given {@link BidiTransformer}
    */
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

   /**
    * Checks if a given object is transforming (if it is an instance of
    * {@link Transforming}).
    * 
    * @param o
    *           the object to check
    * @return if the object is {@link Transforming}
    */
   public static boolean isTransforming(final Object o)
   {
      return o instanceof Transforming<?, ?>;
   }

   /**
    * Checks if a given object is bidi-transforming (if it is an instance of
    * {@link BidiTransforming}).
    * 
    * @param o
    *           the object to check
    * @return if the object is {@link BidiTransforming}
    */
   public static boolean isBidiTransforming(final Object o)
   {
      return o instanceof BidiTransforming<?, ?>;
   }

   /**
    * Returns the {@link Transformer} of a given object if it is
    * {@link Transforming}.
    * 
    * @param transforming
    *           the object to drag the {@link Transformer} out from
    * @return the {@link Transformer} contained in the given object
    * @throws IllegalArgumentException
    *            if the given object is not a {@link Transforming} instance
    * @see #isTransforming(Object)
    */
   public static Transformer<?, ?> getTransformer(final Object transforming)
   {
      if (!isTransforming(transforming))
      {
         throw new IllegalArgumentException("Object is not transforming!");
      }

      return ((Transforming<?, ?>) transforming).getTransformer();
   }

   /**
    * Returns the {@link BidiTransformer} of a given object if it is
    * {@link BidiTransforming}.
    * 
    * @param transforming
    *           the object to drag the {@link BidiTransformer} out from
    * @return the {@link BidiTransformer} contained in the given object
    * @throws IllegalArgumentException
    *            if the given object is not a {@link BidiTransforming} instance
    * @see #isBidiTransforming(Object)
    */
   public static BidiTransformer<?, ?> getBidiTransformer(final Object transforming)
   {
      if (!isBidiTransforming(transforming))
      {
         throw new IllegalArgumentException("Object is not bidi-transforming!");
      }

      return ((BidiTransforming<?, ?>) transforming).getBidiTransformer();
   }

   /**
    * Wraps a new transforming {@link Comparator} using the given
    * {@link Transformer} around a given {@link Comparator}.
    * 
    * @param comparator
    *           the {@link Comparator} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the transforming wrapped {@link Comparator}
    */
   public static <F, T> Comparator<? super T> decorate(final Comparator<? super F> comparator,
         final Transformer<T, F> transformer)
   {
      return new TransformingComparator<F, T>(comparator, transformer);
   }

   /**
    * Wraps a new transforming {@link Iterator} using the given
    * {@link Transformer} around a given {@link Iterator}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#iterator">package description</a>.
    * 
    * @param iterator
    *           the {@link Iterator} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the transforming wrapped {@link Iterator}
    */
   public static <F, T> Iterator<T> decorate(final Iterator<F> iterator, final Transformer<F, T> transformer)
   {
      return new TransformingIterator<F, T>(iterator, transformer);
   }

   /**
    * Wraps a new transforming {@link ListIterator} using the given
    * {@link Transformer} around a given {@link ListIterator}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#listiterator">package description</a>.
    * 
    * @param iterator
    *           the {@link ListIterator} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the transforming wrapped {@link ListIterator}
    */
   public static <F, T> ListIterator<T> decorate(final ListIterator<F> iterator, final Transformer<F, T> transformer)
   {
      return new TransformingListIterator<F, T>(iterator, transformer);
   }

   /**
    * Wraps a new transforming {@link ListIterator} using the given
    * {@link BidiTransformer} around a given {@link ListIterator}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#listiterator">package description</a>.
    * 
    * @param iterator
    *           the {@link ListIterator} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the transforming wrapped {@link ListIterator}
    */
   public static <F, T> ListIterator<T> decorate(final ListIterator<F> iterator, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingListIterator<F, T>(iterator, transformer);
   }

   /**
    * Wraps a new transforming {@link Collection} using the given
    * {@link Transformer} around a given {@link Collection}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#collection">package description</a>.
    * 
    * @param collection
    *           the {@link Collection} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the transforming wrapped {@link Collection}
    */
   public static <F, T> Collection<T> decorate(final Collection<F> collection, final Transformer<F, T> transformer)
   {
      return new TransformingCollection<F, T>(collection, transformer);
   }

   /**
    * Wraps a new transforming {@link Collection} using the given
    * {@link BidiTransformer} around a given {@link Collection}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#collection">package description</a>.
    * 
    * @param collection
    *           the {@link Collection} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the transforming wrapped {@link Collection}
    */
   public static <F, T> Collection<T> decorate(final Collection<F> collection, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingCollection<F, T>(collection, transformer);
   }

   /**
    * Wraps a new transforming {@link Set} using the given {@link Transformer}
    * around a given {@link Set}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#set">package description</a>.
    * 
    * @param set
    *           the {@link Set} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the transforming wrapped {@link Set}
    */
   public static <F, T> Set<T> decorate(final Set<F> set, final Transformer<F, T> transformer)
   {
      return new TransformingSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link Set} using the given
    * {@link BidiTransformer} around a given {@link Set}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#set">package description</a>.
    * 
    * @param set
    *           the {@link Set} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the transforming wrapped {@link Set}
    */
   public static <F, T> Set<T> decorate(final Set<F> set, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link SortedSet} using the given
    * {@link Transformer} around a given {@link SortedSet}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedset">package description</a>.
    * 
    * @param set
    *           the {@link SortedSet} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the transforming wrapped {@link SortedSet}
    */
   public static <F, T> SortedSet<T> decorate(final SortedSet<F> set, final Transformer<F, T> transformer)
   {
      return new TransformingSortedSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link SortedSet} using the given
    * {@link BidiTransformer} around a given {@link SortedSet}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedset">package description</a>.
    * 
    * @param set
    *           the {@link SortedSet} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the transforming wrapped {@link SortedSet}
    */
   public static <F, T> SortedSet<T> decorate(final SortedSet<F> set, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingSortedSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link NavigableSet} using the given
    * {@link Transformer} around a given {@link NavigableSet}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableset">package description</a>.
    * 
    * @param set
    *           the {@link NavigableSet} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the transforming wrapped {@link NavigableSet}
    */
   public static <F, T> NavigableSet<T> decorate(final NavigableSet<F> set, final Transformer<F, T> transformer)
   {
      return new TransformingNavigableSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link NavigableSet} using the given
    * {@link BidiTransformer} around a given {@link NavigableSet}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableset">package description</a>.
    * 
    * @param set
    *           the {@link NavigableSet} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the transforming wrapped {@link NavigableSet}
    */
   public static <F, T> NavigableSet<T> decorate(final NavigableSet<F> set, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingNavigableSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link List} using the given {@link Transformer}
    * around a given {@link List}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#list">package description</a>.
    * 
    * @param list
    *           the {@link List} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the transforming wrapped {@link List}
    */
   public static <F, T> List<T> decorate(final List<F> list, final Transformer<F, T> transformer)
   {
      return new TransformingList<F, T>(list, transformer);
   }

   /**
    * Wraps a new transforming {@link List} using the given
    * {@link BidiTransformer} around a given {@link List}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#list">package description</a>.
    * 
    * @param list
    *           the {@link List} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the transforming wrapped {@link List}
    */
   public static <F, T> List<T> decorate(final List<F> list, final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingList<F, T>(list, transformer);
   }

   /**
    * Wraps a new key-transforming {@link Map} using the given
    * {@link Transformer} around a given {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKey">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the key-transforming wrapped {@link Map}
    */
   public static <KF, KT, V> Map<KT, V> decorateKeyBased(final Map<KF, V> map, final Transformer<KF, KT> transformer)
   {
      return new KeyTransformingMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new key-transforming {@link Map} using the given
    * {@link BidiTransformer} around a given {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKey">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the key-transforming wrapped {@link Map}
    */
   public static <KF, KT, V> Map<KT, V> decorateKeyBased(final Map<KF, V> map,
         final BidiTransformer<KF, KT> transformer)
   {
      return new KeyBidiTransformingMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new key-transforming {@link SortedMap} using the given
    * {@link Transformer} around a given {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedMapKey">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the key-transforming wrapped {@link SortedMap}
    */
   public static <KF, KT, V> SortedMap<KT, V> decorateKeyBased(final SortedMap<KF, V> map,
         final Transformer<KF, KT> transformer)
   {
      return new KeyTransformingSortedMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new key-transforming {@link SortedMap} using the given
    * {@link BidiTransformer} around a given {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedMapKey">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the key-transforming wrapped {@link SortedMap}
    */
   public static <KF, KT, V> SortedMap<KT, V> decorateKeyBased(final SortedMap<KF, V> map,
         final BidiTransformer<KF, KT> transformer)
   {
      return new KeyBidiTransformingSortedMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new key-transforming {@link NavigableMap} using the given
    * {@link Transformer} around a given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableMapKey">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the key-transforming wrapped {@link NavigableMap}
    */
   public static <KF, KT, V> NavigableMap<KT, V> decorateKeyBased(final NavigableMap<KF, V> map,
         final Transformer<KF, KT> transformer)
   {
      return new KeyTransformingNavigableMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new key-transforming {@link NavigableMap} using the given
    * {@link BidiTransformer} around a given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableMapKey">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the key-transforming wrapped {@link NavigableMap}
    */
   public static <KF, KT, V> NavigableMap<KT, V> decorateKeyBased(final NavigableMap<KF, V> map,
         final BidiTransformer<KF, KT> transformer)
   {
      return new KeyBidiTransformingNavigableMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link Map} using the given
    * {@link Transformer} around a given {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapValue">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the value-transforming wrapped {@link Map}
    */
   public static <K, VF, VT> Map<K, VT> decorateValueBased(final Map<K, VF> map,
         final Transformer<VF, VT> transformer)
   {
      return new ValueTransformingMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link Map} using the given
    * {@link BidiTransformer} around a given {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapValue">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the value-transforming wrapped {@link Map}
    */
   public static <K, VF, VT> Map<K, VT> decorateValueBased(final Map<K, VF> map,
         final BidiTransformer<VF, VT> transformer)
   {
      return new ValueBidiTransformingMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link SortedMap} using the given
    * {@link Transformer} around a given {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedMapValue">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the value-transforming wrapped {@link SortedMap}
    */
   public static <K, VF, VT> SortedMap<K, VT> decorateValueBased(final SortedMap<K, VF> map,
         final Transformer<VF, VT> transformer)
   {
      return new ValueTransformingSortedMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link SortedMap} using the given
    * {@link BidiTransformer} around a given {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedMapValue">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the value-transforming wrapped {@link SortedMap}
    */
   public static <K, VF, VT> SortedMap<K, VT> decorateValueBased(final SortedMap<K, VF> map,
         final BidiTransformer<VF, VT> transformer)
   {
      return new ValueBidiTransformingSortedMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link NavigableMap} using the given
    * {@link Transformer} around a given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableMapValue">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param transformer
    *           the {@link Transformer} to use for wrapping
    * @return the value-transforming wrapped {@link NavigableMap}
    */
   public static <K, VF, VT> NavigableMap<K, VT> decorateValueBased(final NavigableMap<K, VF> map,
         final Transformer<VF, VT> transformer)
   {
      return new ValueTransformingNavigableMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link NavigableMap} using the given
    * {@link BidiTransformer} around a given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableMapValue">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param transformer
    *           the {@link BidiTransformer} to use for wrapping
    * @return the value-transforming wrapped {@link NavigableMap}
    */
   public static <K, VF, VT> NavigableMap<K, VT> decorateValueBased(final NavigableMap<K, VF> map,
         final BidiTransformer<VF, VT> transformer)
   {
      return new ValueBidiTransformingNavigableMap<K, VF, VT>(map, transformer);
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
