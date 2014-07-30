package net.sf.javagimmicks.collections8.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;




import net.sf.javagimmicks.collections8.Ring;
import net.sf.javagimmicks.collections8.RingCursor;
import net.sf.javagimmicks.transform8.BidiFunction;

/**
 * This class is the central entry point to the JavaGimmicks transforming
 * collections API by providing decorator- and generator methods for many
 * transforming types.
 * <p>
 * A more detailed description of the API can be found at in the package
 * description {@link net.sf.javagimmicks.collections8.transformer}.
 * 
 * @see net.sf.javagimmicks.collections8.transformer
 * @author Michael Scholz
 */
public class TransformerUtils
{
   private TransformerUtils()
   {}

   /**
    * Wraps a new transforming {@link Comparator} using the given
    * {@link Function} around a given {@link Comparator}.
    * 
    * @param comparator
    *           the {@link Comparator} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the transforming wrapped {@link Comparator}
    */
   public static <F, T> Comparator<? super T> decorate(final Comparator<? super F> comparator,
         final Function<T, F> transformer)
   {
      return new TransformingComparator<F, T>(comparator, transformer);
   }

   /**
    * Wraps a new transforming {@link Iterator} using the given
    * {@link Function} around a given {@link Iterator}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#iterator">package description</a>.
    * 
    * @param iterator
    *           the {@link Iterator} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the transforming wrapped {@link Iterator}
    */
   public static <F, T> Iterator<T> decorate(final Iterator<F> iterator, final Function<F, T> transformer)
   {
      return new TransformingIterator<F, T>(iterator, transformer);
   }

   /**
    * Wraps a new transforming {@link ListIterator} using the given
    * {@link Function} around a given {@link ListIterator}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#listiterator">package description</a>.
    * 
    * @param iterator
    *           the {@link ListIterator} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the transforming wrapped {@link ListIterator}
    */
   public static <F, T> ListIterator<T> decorate(final ListIterator<F> iterator, final Function<F, T> transformer)
   {
      return new TransformingListIterator<F, T>(iterator, transformer);
   }

   /**
    * Wraps a new transforming {@link ListIterator} using the given
    * {@link BidiFunction} around a given {@link ListIterator}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#listiterator">package description</a>.
    * 
    * @param iterator
    *           the {@link ListIterator} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the transforming wrapped {@link ListIterator}
    */
   public static <F, T> ListIterator<T> decorate(final ListIterator<F> iterator, final BidiFunction<F, T> transformer)
   {
      return new BidiTransformingListIterator<F, T>(iterator, transformer);
   }

   /**
    * Wraps a new transforming {@link Collection} using the given
    * {@link Function} around a given {@link Collection}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#collection">package description</a>.
    * 
    * @param collection
    *           the {@link Collection} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the transforming wrapped {@link Collection}
    */
   public static <F, T> Collection<T> decorate(final Collection<F> collection, final Function<F, T> transformer)
   {
      return new TransformingCollection<F, T>(collection, transformer);
   }

   /**
    * Wraps a new transforming {@link Collection} using the given
    * {@link BidiFunction} around a given {@link Collection}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#collection">package description</a>.
    * 
    * @param collection
    *           the {@link Collection} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the transforming wrapped {@link Collection}
    */
   public static <F, T> Collection<T> decorate(final Collection<F> collection, final BidiFunction<F, T> transformer)
   {
      return new BidiTransformingCollection<F, T>(collection, transformer);
   }

   /**
    * Wraps a new transforming {@link Set} using the given {@link Function}
    * around a given {@link Set}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#set">package description</a>.
    * 
    * @param set
    *           the {@link Set} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the transforming wrapped {@link Set}
    */
   public static <F, T> Set<T> decorate(final Set<F> set, final Function<F, T> transformer)
   {
      return new TransformingSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link Set} using the given
    * {@link BidiFunction} around a given {@link Set}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#set">package description</a>.
    * 
    * @param set
    *           the {@link Set} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the transforming wrapped {@link Set}
    */
   public static <F, T> Set<T> decorate(final Set<F> set, final BidiFunction<F, T> transformer)
   {
      return new BidiTransformingSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link SortedSet} using the given
    * {@link Function} around a given {@link SortedSet}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedset">package description</a>.
    * 
    * @param set
    *           the {@link SortedSet} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the transforming wrapped {@link SortedSet}
    */
   public static <F, T> SortedSet<T> decorate(final SortedSet<F> set, final Function<F, T> transformer)
   {
      return new TransformingSortedSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link SortedSet} using the given
    * {@link BidiFunction} around a given {@link SortedSet}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedset">package description</a>.
    * 
    * @param set
    *           the {@link SortedSet} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the transforming wrapped {@link SortedSet}
    */
   public static <F, T> SortedSet<T> decorate(final SortedSet<F> set, final BidiFunction<F, T> transformer)
   {
      return new BidiTransformingSortedSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link NavigableSet} using the given
    * {@link Function} around a given {@link NavigableSet}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableset">package description</a>.
    * 
    * @param set
    *           the {@link NavigableSet} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the transforming wrapped {@link NavigableSet}
    */
   public static <F, T> NavigableSet<T> decorate(final NavigableSet<F> set, final Function<F, T> transformer)
   {
      return new TransformingNavigableSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link NavigableSet} using the given
    * {@link BidiFunction} around a given {@link NavigableSet}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableset">package description</a>.
    * 
    * @param set
    *           the {@link NavigableSet} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the transforming wrapped {@link NavigableSet}
    */
   public static <F, T> NavigableSet<T> decorate(final NavigableSet<F> set, final BidiFunction<F, T> transformer)
   {
      return new BidiTransformingNavigableSet<F, T>(set, transformer);
   }

   /**
    * Wraps a new transforming {@link List} using the given {@link Function}
    * around a given {@link List}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#list">package description</a>.
    * 
    * @param list
    *           the {@link List} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the transforming wrapped {@link List}
    */
   public static <F, T> List<T> decorate(final List<F> list, final Function<F, T> transformer)
   {
      return new TransformingList<F, T>(list, transformer);
   }

   /**
    * Wraps a new transforming {@link List} using the given
    * {@link BidiFunction} around a given {@link List}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#list">package description</a>.
    * 
    * @param list
    *           the {@link List} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the transforming wrapped {@link List}
    */
   public static <F, T> List<T> decorate(final List<F> list, final BidiFunction<F, T> transformer)
   {
      return new BidiTransformingList<F, T>(list, transformer);
   }

   /**
    * Wraps a new key-transforming {@link Map} using the given
    * {@link Function} around a given {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKey">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the key-transforming wrapped {@link Map}
    */
   public static <KF, KT, V> Map<KT, V> decorateKeyBased(final Map<KF, V> map, final Function<KF, KT> transformer)
   {
      return new KeyTransformingMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new key-transforming {@link Map} using the given
    * {@link BidiFunction} around a given {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKey">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the key-transforming wrapped {@link Map}
    */
   public static <KF, KT, V> Map<KT, V> decorateKeyBased(final Map<KF, V> map,
         final BidiFunction<KF, KT> transformer)
   {
      return new KeyBidiTransformingMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new key-transforming {@link SortedMap} using the given
    * {@link Function} around a given {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedMapKey">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the key-transforming wrapped {@link SortedMap}
    */
   public static <KF, KT, V> SortedMap<KT, V> decorateKeyBased(final SortedMap<KF, V> map,
         final Function<KF, KT> transformer)
   {
      return new KeyTransformingSortedMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new key-transforming {@link SortedMap} using the given
    * {@link BidiFunction} around a given {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedMapKey">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the key-transforming wrapped {@link SortedMap}
    */
   public static <KF, KT, V> SortedMap<KT, V> decorateKeyBased(final SortedMap<KF, V> map,
         final BidiFunction<KF, KT> transformer)
   {
      return new KeyBidiTransformingSortedMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new key-transforming {@link NavigableMap} using the given
    * {@link Function} around a given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableMapKey">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the key-transforming wrapped {@link NavigableMap}
    */
   public static <KF, KT, V> NavigableMap<KT, V> decorateKeyBased(final NavigableMap<KF, V> map,
         final Function<KF, KT> transformer)
   {
      return new KeyTransformingNavigableMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new key-transforming {@link NavigableMap} using the given
    * {@link BidiFunction} around a given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableMapKey">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the key-transforming wrapped {@link NavigableMap}
    */
   public static <KF, KT, V> NavigableMap<KT, V> decorateKeyBased(final NavigableMap<KF, V> map,
         final BidiFunction<KF, KT> transformer)
   {
      return new KeyBidiTransformingNavigableMap<KF, KT, V>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link Map} using the given
    * {@link Function} around a given {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapValue">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the value-transforming wrapped {@link Map}
    */
   public static <K, VF, VT> Map<K, VT> decorateValueBased(final Map<K, VF> map,
         final Function<VF, VT> transformer)
   {
      return new ValueTransformingMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link Map} using the given
    * {@link BidiFunction} around a given {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapValue">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the value-transforming wrapped {@link Map}
    */
   public static <K, VF, VT> Map<K, VT> decorateValueBased(final Map<K, VF> map,
         final BidiFunction<VF, VT> transformer)
   {
      return new ValueBidiTransformingMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link SortedMap} using the given
    * {@link Function} around a given {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedMapValue">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the value-transforming wrapped {@link SortedMap}
    */
   public static <K, VF, VT> SortedMap<K, VT> decorateValueBased(final SortedMap<K, VF> map,
         final Function<VF, VT> transformer)
   {
      return new ValueTransformingSortedMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link SortedMap} using the given
    * {@link BidiFunction} around a given {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#sortedMapValue">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the value-transforming wrapped {@link SortedMap}
    */
   public static <K, VF, VT> SortedMap<K, VT> decorateValueBased(final SortedMap<K, VF> map,
         final BidiFunction<VF, VT> transformer)
   {
      return new ValueBidiTransformingSortedMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link NavigableMap} using the given
    * {@link Function} around a given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableMapValue">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the value-transforming wrapped {@link NavigableMap}
    */
   public static <K, VF, VT> NavigableMap<K, VT> decorateValueBased(final NavigableMap<K, VF> map,
         final Function<VF, VT> transformer)
   {
      return new ValueTransformingNavigableMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new value-transforming {@link NavigableMap} using the given
    * {@link BidiFunction} around a given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#navigableMapValue">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the value-transforming wrapped {@link NavigableMap}
    */
   public static <K, VF, VT> NavigableMap<K, VT> decorateValueBased(final NavigableMap<K, VF> map,
         final BidiFunction<VF, VT> transformer)
   {
      return new ValueBidiTransformingNavigableMap<K, VF, VT>(map, transformer);
   }

   /**
    * Wraps a new key- and value-transforming {@link Map} using the given key-
    * {@link Function} and value-{@link Function} around a given
    * {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param keyFunction
    *           the key-{@link Function} to used for wrapping
    * @param valueFunction
    *           the key-{@link Function} to used for wrapping
    * @return the key-and value-transforming wrapped {@link Map}
    */
   public static <KF, KT, VF, VT> Map<KT, VT> decorate(final Map<KF, VF> map, final Function<KF, KT> keyFunction,
         final Function<VF, VT> valueFunction)
   {
      final Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link Map} using the given key-
    * {@link BidiFunction} and value-{@link Function} around a given
    * {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param keyFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @param valueFunction
    *           the key-{@link Function} to used for wrapping
    * @return the key-and value-transforming wrapped {@link Map}
    */
   public static <KF, KT, VF, VT> Map<KT, VT> decorate(final Map<KF, VF> map,
         final BidiFunction<KF, KT> keyFunction, final Function<VF, VT> valueFunction)
   {
      final Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link Map} using the given key-
    * {@link Function} and value-{@link BidiFunction} around a given
    * {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param keyFunction
    *           the key-{@link Function} to used for wrapping
    * @param valueFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @return the key-and value-transforming wrapped {@link Map}
    */
   public static <KF, KT, VF, VT> Map<KT, VT> decorate(final Map<KF, VF> map, final Function<KF, KT> keyFunction,
         final BidiFunction<VF, VT> valueFunction)
   {
      final Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link Map} using the given key-
    * {@link BidiFunction} and value-{@link BidiFunction} around a given
    * {@link Map}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link Map} to wrap around
    * @param keyFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @param valueFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @return the key-and value-transforming wrapped {@link Map}
    */
   public static <KF, KT, VF, VT> Map<KT, VT> decorate(final Map<KF, VF> map,
         final BidiFunction<KF, KT> keyFunction, final BidiFunction<VF, VT> valueFunction)
   {
      final Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link SortedMap} using the given
    * key-{@link Function} and value-{@link Function} around a given
    * {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param keyFunction
    *           the key-{@link Function} to used for wrapping
    * @param valueFunction
    *           the key-{@link Function} to used for wrapping
    * @return the key-and value-transforming wrapped {@link SortedMap}
    */
   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(final SortedMap<KF, VF> map,
         final Function<KF, KT> keyFunction, final Function<VF, VT> valueFunction)
   {
      final SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link SortedMap} using the given
    * key-{@link BidiFunction} and value-{@link Function} around a given
    * {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param keyFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @param valueFunction
    *           the key-{@link Function} to used for wrapping
    * @return the key-and value-transforming wrapped {@link SortedMap}
    */
   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(final SortedMap<KF, VF> map,
         final BidiFunction<KF, KT> keyFunction, final Function<VF, VT> valueFunction)
   {
      final SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link SortedMap} using the given
    * key-{@link Function} and value-{@link BidiFunction} around a given
    * {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param keyFunction
    *           the key-{@link Function} to used for wrapping
    * @param valueFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @return the key-and value-transforming wrapped {@link SortedMap}
    */
   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(final SortedMap<KF, VF> map,
         final Function<KF, KT> keyFunction, final BidiFunction<VF, VT> valueFunction)
   {
      final SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link SortedMap} using the given
    * key-{@link BidiFunction} and value-{@link BidiFunction} around a
    * given {@link SortedMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link SortedMap} to wrap around
    * @param keyFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @param valueFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @return the key-and value-transforming wrapped {@link SortedMap}
    */
   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(final SortedMap<KF, VF> map,
         final BidiFunction<KF, KT> keyFunction, final BidiFunction<VF, VT> valueFunction)
   {
      final SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link NavigableMap} using the
    * given key-{@link Function} and value-{@link Function} around a given
    * {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param keyFunction
    *           the key-{@link Function} to used for wrapping
    * @param valueFunction
    *           the key-{@link Function} to used for wrapping
    * @return the key-and value-transforming wrapped {@link NavigableMap}
    */
   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(final NavigableMap<KF, VF> map,
         final Function<KF, KT> keyFunction, final Function<VF, VT> valueFunction)
   {
      final NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link NavigableMap} using the
    * given key-{@link BidiFunction} and value-{@link Function} around a
    * given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param keyFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @param valueFunction
    *           the key-{@link Function} to used for wrapping
    * @return the key-and value-transforming wrapped {@link NavigableMap}
    */
   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(final NavigableMap<KF, VF> map,
         final BidiFunction<KF, KT> keyFunction, final Function<VF, VT> valueFunction)
   {
      final NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link NavigableMap} using the
    * given key-{@link Function} and value-{@link BidiFunction} around a
    * given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param keyFunction
    *           the key-{@link Function} to used for wrapping
    * @param valueFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @return the key-and value-transforming wrapped {@link NavigableMap}
    */
   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(final NavigableMap<KF, VF> map,
         final Function<KF, KT> keyFunction, final BidiFunction<VF, VT> valueFunction)
   {
      final NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new key- and value-transforming {@link NavigableMap} using the
    * given key-{@link BidiFunction} and value-{@link BidiFunction} around
    * a given {@link NavigableMap}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#mapKeyValue">package description</a>.
    * 
    * @param map
    *           the {@link NavigableMap} to wrap around
    * @param keyFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @param valueFunction
    *           the key-{@link BidiFunction} to used for wrapping
    * @return the key-and value-transforming wrapped {@link NavigableMap}
    */
   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(final NavigableMap<KF, VF> map,
         final BidiFunction<KF, KT> keyFunction, final BidiFunction<VF, VT> valueFunction)
   {
      final NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueFunction);
      return decorateKeyBased(valueTransformingMap, keyFunction);
   }

   /**
    * Wraps a new transforming {@link RingCursor} using the given
    * {@link Function} around a given {@link RingCursor}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#ringCursor">package description</a>.
    * 
    * @param ringCursor
    *           the {@link RingCursor} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the transforming wrapped {@link RingCursor}
    */
   public static <F, T> RingCursor<T> decorate(final RingCursor<F> ringCursor, final Function<F, T> transformer)
   {
      return new TransformingRingCursor<F, T>(ringCursor, transformer);
   }

   /**
    * Wraps a new transforming {@link RingCursor} using the given
    * {@link BidiFunction} around a given {@link RingCursor}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#ringCursor">package description</a>.
    * 
    * @param ringCursor
    *           the {@link RingCursor} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the transforming wrapped {@link RingCursor}
    */
   public static <F, T> RingCursor<T> decorate(final RingCursor<F> ringCursor, final BidiFunction<F, T> transformer)
   {
      return new BidiTransformingRingCursor<F, T>(ringCursor, transformer);
   }

   /**
    * Wraps a new transforming {@link Ring} using the given {@link Function}
    * around a given {@link Ring}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#ring">package description</a>.
    * 
    * @param ring
    *           the {@link Ring} to wrap around
    * @param transformer
    *           the {@link Function} to use for wrapping
    * @return the transforming wrapped {@link Ring}
    */
   public static <F, T> Ring<T> decorate(final Ring<F> ring, final Function<F, T> transformer)
   {
      return new TransformingRing<F, T>(ring, transformer);
   }

   /**
    * Wraps a new transforming {@link Ring} using the given
    * {@link BidiFunction} around a given {@link Ring}.
    * <p>
    * For a list of available operations see <a
    * href="package-summary.html#ring">package description</a>.
    * 
    * @param ring
    *           the {@link Ring} to wrap around
    * @param transformer
    *           the {@link BidiFunction} to use for wrapping
    * @return the transforming wrapped {@link Ring}
    */
   public static <F, T> Ring<T> decorate(final Ring<F> ring, final BidiFunction<F, T> transformer)
   {
      return new BidiTransformingRing<F, T>(ring, transformer);
   }

   /**
    * Bulk-transforms a given {@link Collection} using the given
    * {@link Function} and stores all transformed elements in a target
    * {@link Collection}.
    * 
    * @param fromCollection
    *           the source {@link Collection} whose elements should be
    *           bulk-transformed
    * @param toCollection
    *           the target {@link Collection} where the transformed elements
    *           should be stored
    * @param transformer
    *           the {@link Function} used for transforming
    */
   public static <F, T> void transform(final Collection<F> fromCollection, final Collection<T> toCollection,
         final Function<F, T> transformer)
   {
      toCollection.addAll(decorate(fromCollection, transformer));
   }

   /**
    * Bulk-transforms a given {@link Collection} using the given
    * {@link Function} into a new {@link ArrayList} and returns it.
    * 
    * @param fromCollection
    *           the source {@link Collection} whose elements should be
    *           bulk-transformed
    * @param transformer
    *           the {@link Function} used for transforming
    * @return the {@link ArrayList} containing the transformed elements
    */
   public static <F, T> ArrayList<T> tronsformToArrayList(final Collection<F> fromCollection,
         final Function<F, T> transformer)
   {
      return transformInternal(fromCollection, new ArrayList<T>(fromCollection.size()), transformer);
   }

   /**
    * Bulk-transforms a given {@link Collection} using the given
    * {@link Function} into a new {@link LinkedList} and returns it.
    * 
    * @param fromCollection
    *           the source {@link Collection} whose elements should be
    *           bulk-transformed
    * @param transformer
    *           the {@link Function} used for transforming
    * @return the {@link LinkedList} containing the transformed elements
    */
   public static <F, T> LinkedList<T> transformToLinkedList(final Collection<F> fromCollection,
         final Function<F, T> transformer)
   {
      return transformInternal(fromCollection, new LinkedList<T>(), transformer);
   }

   /**
    * Bulk-transforms a given {@link Collection} using the given
    * {@link Function} into a new {@link HashSet} and returns it.
    * 
    * @param fromCollection
    *           the source {@link Collection} whose elements should be
    *           bulk-transformed
    * @param transformer
    *           the {@link Function} used for transforming
    * @return the {@link HashSet} containing the transformed elements
    */
   public static <F, T> HashSet<T> transformToHashSet(final Collection<F> fromCollection,
         final Function<F, T> transformer)
   {
      return transformInternal(fromCollection, new HashSet<T>(), transformer);
   }

   /**
    * Bulk-transforms a given {@link Collection} using the given
    * {@link Function} into a new {@link TreeSet} and returns it.
    * 
    * @param fromCollection
    *           the source {@link Collection} whose elements should be
    *           bulk-transformed
    * @param transformer
    *           the {@link Function} used for transforming
    * @return the {@link TreeSet} containing the transformed elements
    */
   public static <F, T> TreeSet<T> transformToTreeSet(final Collection<F> fromCollection,
         final Function<F, T> transformer)
   {
      return transformInternal(fromCollection, new TreeSet<T>(), transformer);
   }

   /**
    * Bulk-key-transforms a given {@link Map} using the given
    * {@link Function} and stores all transformed elements in a target
    * {@link Map}.
    * 
    * @param fromMap
    *           the source {@link Map} whose elements should be bulk-transformed
    * @param toMap
    *           the target {@link Map} where the transformed elements should be
    *           stored
    * @param transformer
    *           the {@link Function} used for transforming
    */
   public static <KF, KT, V> void transformKeys(final Map<KF, V> fromMap, final Map<KT, V> toMap,
         final Function<KF, KT> transformer)
   {
      toMap.putAll(decorateKeyBased(fromMap, transformer));
   }

   /**
    * Bulk-key-transforms a given {@link Map} using the given
    * {@link Function} into a new {@link HashMap} and returns it.
    * 
    * @param fromMap
    *           the source {@link Map} whose elements should be bulk-transformed
    * @param transformer
    *           the {@link Function} used for transforming
    * @return the {@link HashMap} containing the transformed elements
    */
   public static <KF, KT, V> HashMap<KT, V> transformKeysToHashMap(final Map<KF, V> fromMap,
         final Function<KF, KT> transformer)
   {
      return transformKeysInternal(fromMap, new HashMap<KT, V>(), transformer);
   }

   /**
    * Bulk-key-transforms a given {@link Map} using the given
    * {@link Function} into a new {@link TreeMap} and returns it.
    * 
    * @param fromMap
    *           the source {@link Map} whose elements should be bulk-transformed
    * @param transformer
    *           the {@link Function} used for transforming
    * @return the {@link TreeMap} containing the transformed elements
    */
   public static <KF, KT, V> TreeMap<KT, V> transformKeysToTreeMap(final Map<KF, V> fromMap,
         final Function<KF, KT> transformer)
   {
      return transformKeysInternal(fromMap, new TreeMap<KT, V>(), transformer);
   }

   /**
    * Bulk-key-transforms a given {@link Map} using the given
    * {@link Function} and stores all transformed elements in a target
    * {@link Map}.
    * 
    * @param fromMap
    *           the source {@link Map} whose elements should be bulk-transformed
    * @param toMap
    *           the target {@link Map} where the transformed elements should be
    *           stored
    * @param transformer
    *           the {@link Function} used for transforming
    */
   public static <K, VF, VT> void transformValues(final Map<K, VF> fromMap, final Map<K, VT> toMap,
         final Function<VF, VT> transformer)
   {
      toMap.putAll(decorateValueBased(fromMap, transformer));
   }

   /**
    * Bulk-key-transforms a given {@link Map} using the given
    * {@link Function} into a new {@link HashMap} and returns it.
    * 
    * @param fromMap
    *           the source {@link Map} whose elements should be bulk-transformed
    * @param transformer
    *           the {@link Function} used for transforming
    * @return the {@link HashMap} containing the transformed elements
    */
   public static <K, VF, VT> HashMap<K, VT> transformValuesToHashMap(final Map<K, VF> fromMap,
         final Function<VF, VT> transformer)
   {
      return transformValuesInternal(fromMap, new HashMap<K, VT>(), transformer);
   }

   /**
    * Bulk-key-transforms a given {@link Map} using the given
    * {@link Function} into a new {@link TreeMap} and returns it.
    * 
    * @param fromMap
    *           the source {@link Map} whose elements should be bulk-transformed
    * @param transformer
    *           the {@link Function} used for transforming
    * @return the {@link TreeMap} containing the transformed elements
    */
   public static <K, VF, VT> TreeMap<K, VT> transformValuesToTreeMap(final Map<K, VF> fromMap,
         final Function<VF, VT> transformer)
   {
      return transformValuesInternal(fromMap, new TreeMap<K, VT>(), transformer);
   }

   private static <F, T, C extends Collection<T>> C transformInternal(final Collection<F> fromCollection,
         final C toCollection,
         final Function<F, T> transformer)
   {
      transform(fromCollection, toCollection, transformer);

      return toCollection;
   }

   private static <KF, KT, V, C extends Map<KT, V>> C transformKeysInternal(final Map<KF, V> fromMap,
         final C toMap,
         final Function<KF, KT> transformer)
   {
      transformKeys(fromMap, toMap, transformer);

      return toMap;
   }

   private static <K, VF, VT, C extends Map<K, VT>> C transformValuesInternal(final Map<K, VF> fromMap,
         final C toMap,
         final Function<VF, VT> transformer)
   {
      transformValues(fromMap, toMap, transformer);

      return toMap;
   }
}
