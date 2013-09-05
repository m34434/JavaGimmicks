package net.sf.javagimmicks.collections.transformer;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import net.sf.javagimmicks.collections.Ring;
import net.sf.javagimmicks.collections.RingCursor;

/**
 * This class is the central entry point to the Javagimmicks
 * transformer API (Java 1.5 version) by providing decorator-
 * and generator methods for many transforming types.
 * <p>
 * A more detailed description of the API can be found
 * at in the package description
 * {@link net.sf.javagimmicks.collections.transformer}.
 * @see net.sf.javagimmicks.collections.transformer
 * @author Michael Scholz
 */
@SuppressWarnings("deprecation")
public class TransformerUtils15
{
   protected TransformerUtils15() {}
   
   public static <E> Transformer<E, E> identityTransformer()
   {
      return identityBidiTransformer();
   }
   
   public static <E> BidiTransformer<E, E> identityBidiTransformer()
   {
      return new BidiTransformer<E, E>()
      {
         public E transform(E source)
         {
            return source;
         }

         public BidiTransformer<E, E> invert()
         {
            return this;
         }

         public E transformBack(E source)
         {
            return source;
         }
      };
   }
   
   public static <F,T> BidiTransformer<T, F> invert(final BidiTransformer<F, T> transformer)
   {
      return new BidiTransformer<T, F>()
      {
         public T transformBack(F source)
         {
            return transformer.transform(source);
         }
   
         public F transform(T source)
         {
            return transformer.transformBack(source);
         }
   
         public BidiTransformer<F, T> invert()
         {
            return transformer;
         }
      };
   }
   
   public static boolean isTransforming(Object o)
   {
      return o instanceof Transforming<?, ?>;
   }
   
   public static boolean isBidiTransforming(Object o)
   {
      return o instanceof BidiTransforming<?, ?>;
   }
   
   public static Transformer<?, ?> getTransformer(Object transforming)
   {
      if(!isTransforming(transforming))
      {
         throw new IllegalArgumentException("Object is not transforming!");
      }
      
      return ((Transforming<?, ?>)transforming).getTransformer();
   }
   
   public static BidiTransformer<?, ?> getBidiTransformer(Object transforming)
   {
      if(!isBidiTransforming(transforming))
      {
         throw new IllegalArgumentException("Object is not bidi-transforming!");
      }
      
      return ((BidiTransforming<?, ?>)transforming).getBidiTransformer();
   }
   
   public static <F, T> Comparator<? super T> decorate(Comparator<? super F> comparator, Transformer<T, F> transformer)
   {
      return new TransformingComparator<F, T>(comparator, transformer);
   }
   
   public static <F, T> Iterator<T> decorate(Iterator<F> iterator, Transformer<F, T> transformer)
   {
      return new TransformingIterator<F, T>(iterator, transformer);
   }

   public static <F, T> ListIterator<T> decorate(ListIterator<F> iterator, Transformer<F, T> transformer)
   {
      return new TransformingListIterator<F, T>(iterator, transformer);
   }

   public static <F, T> ListIterator<T> decorate(ListIterator<F> iterator, BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingListIterator<F, T>(iterator, transformer);
   }

   public static <F, T> Collection<T> decorate(Collection<F> collection, Transformer<F, T> transformer)
   {
      return new TransformingCollection<F, T>(collection, transformer);
   }

   public static <F, T> Collection<T> decorate(Collection<F> collection, BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingCollection<F, T>(collection, transformer);
   }

   public static <F, T> Set<T> decorate(Set<F> set, Transformer<F, T> transformer)
   {
      return new TransformingSet<F, T>(set, transformer);
   }

   public static <F, T> Set<T> decorate(Set<F> set, BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingSet<F, T>(set, transformer);
   }

   public static <F, T> SortedSet<T> decorate(SortedSet<F> set, Transformer<F, T> transformer)
   {
      return new TransformingSortedSet<F, T>(set, transformer);
   }

   public static <F, T> SortedSet<T> decorate(SortedSet<F> set, BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingSortedSet<F, T>(set, transformer);
   }

   public static <F, T> List<T> decorate(List<F> list, Transformer<F, T> transformer)
   {
      return new TransformingList<F, T>(list, transformer);
   }

   public static <F, T> List<T> decorate(List<F> list, BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingList<F, T>(list, transformer);
   }
   
   public static <KF, KT, V> Map<KT, V> decorateKeyBased(Map<KF, V> map, Transformer<KF, KT> keyTransformer)
   {
      return new KeyTransformingMap<KF, KT, V>(map, keyTransformer);
   }

   public static <KF, KT, V> Map<KT, V> decorateKeyBased(Map<KF, V> map, BidiTransformer<KF, KT> keyTransformer)
   {
      return new KeyBidiTransformingMap<KF, KT, V>(map, keyTransformer);
   }
   
   public static <KF, KT, V> SortedMap<KT, V> decorateKeyBased(SortedMap<KF, V> map, Transformer<KF, KT> keyTransformer)
   {
      return new KeyTransformingSortedMap<KF, KT, V>(map, keyTransformer);
   }

   public static <KF, KT, V> SortedMap<KT, V> decorateKeyBased(SortedMap<KF, V> map, BidiTransformer<KF, KT> keyTransformer)
   {
      return new KeyBidiTransformingSortedMap<KF, KT, V>(map, keyTransformer);
   }
   
   public static <K, VF, VT> Map<K, VT> decorateValueBased(Map<K, VF> map, Transformer<VF, VT> valueTransformer)
   {
      return new ValueTransformingMap<K, VF, VT>(map, valueTransformer);
   }

   public static <K, VF, VT> Map<K, VT> decorateValueBased(Map<K, VF> map, BidiTransformer<VF, VT> valueTransformer)
   {
      return new ValueBidiTransformingMap<K, VF, VT>(map, valueTransformer);
   }

   public static <K, VF, VT> SortedMap<K, VT> decorateValueBased(SortedMap<K, VF> map, Transformer<VF, VT> valueTransformer)
   {
      return new ValueTransformingSortedMap<K, VF, VT>(map, valueTransformer);
   }

   public static <K, VF, VT> SortedMap<K, VT> decorateValueBased(SortedMap<K, VF> map, BidiTransformer<VF, VT> valueTransformer)
   {
      return new ValueBidiTransformingSortedMap<K, VF, VT>(map, valueTransformer);
   }

   public static <KF, KT, VF, VT> Map<KT, VT> decorate(Map<KF, VF> map, Transformer<KF, KT> keyTransformer, Transformer<VF, VT> valueTransformer)
   {
      Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> Map<KT, VT> decorate(Map<KF, VF> map, BidiTransformer<KF, KT> keyTransformer, Transformer<VF, VT> valueTransformer)
   {
      Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> Map<KT, VT> decorate(Map<KF, VF> map, Transformer<KF, KT> keyTransformer, BidiTransformer<VF, VT> valueTransformer)
   {
      Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> Map<KT, VT> decorate(Map<KF, VF> map, BidiTransformer<KF, KT> keyTransformer, BidiTransformer<VF, VT> valueTransformer)
   {
      Map<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(SortedMap<KF, VF> map, Transformer<KF, KT> keyTransformer, Transformer<VF, VT> valueTransformer)
   {
      SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(SortedMap<KF, VF> map, BidiTransformer<KF, KT> keyTransformer, Transformer<VF, VT> valueTransformer)
   {
      SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(SortedMap<KF, VF> map, Transformer<KF, KT> keyTransformer, BidiTransformer<VF, VT> valueTransformer)
   {
      SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> SortedMap<KT, VT> decorate(SortedMap<KF, VF> map, BidiTransformer<KF, KT> keyTransformer, BidiTransformer<VF, VT> valueTransformer)
   {
      SortedMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <F, T> RingCursor<T> decorate(RingCursor<F> ringCursor, Transformer<F, T> transformer)
   {
      return new TransformingRingCursor<F, T>(ringCursor, transformer);
   }

   public static <F, T> RingCursor<T> decorate(RingCursor<F> ringCursor, BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingRingCursor<F, T>(ringCursor, transformer);
   }

   public static <F, T> Ring<T> decorate(Ring<F> ring, Transformer<F, T> transformer)
   {
      return new TransformingRing<F, T>(ring, transformer);
   }

   public static <F, T> Ring<T> decorate(Ring<F> ring, BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingRing<F, T>(ring, transformer);
   }

}
