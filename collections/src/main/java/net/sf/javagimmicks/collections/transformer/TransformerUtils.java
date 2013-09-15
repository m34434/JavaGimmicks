package net.sf.javagimmicks.collections.transformer;

import java.util.NavigableMap;
import java.util.NavigableSet;

/**
 * This class is the central entry point to the Javagimmicks
 * transformer API by providing decorator- and generator
 * methods for many transforming types.
 * <p>
 * A more detailed description of the API can be found
 * at in the package description
 * {@link net.sf.javagimmicks.collections.transformer}.
 * @see net.sf.javagimmicks.collections.transformer
 * @author Michael Scholz
 */
@SuppressWarnings("deprecation")
public class TransformerUtils extends TransformerUtils15
{
   protected TransformerUtils() {}
   
   public static <F, T> NavigableSet<T> decorate(NavigableSet<F> set, Transformer<F, T> transformer)
   {
      return new TransformingNavigableSet<F, T>(set, transformer);
   }

   public static <F, T> NavigableSet<T> decorate(NavigableSet<F> set, BidiTransformer<F, T> transformer)
   {
      return new BidiTransformingNavigableSet<F, T>(set, transformer);
   }

   public static <KF, KT, V> NavigableMap<KT, V> decorateKeyBased(NavigableMap<KF, V> map, Transformer<KF, KT> keyTransformer)
   {
      return new KeyTransformingNavigableMap<KF, KT, V>(map, keyTransformer);
   }

   public static <KF, KT, V> NavigableMap<KT, V> decorateKeyBased(NavigableMap<KF, V> map, BidiTransformer<KF, KT> keyTransformer)
   {
      return new KeyBidiTransformingNavigableMap<KF, KT, V>(map, keyTransformer);
   }
   
   public static <K, VF, VT> NavigableMap<K, VT> decorateValueBased(NavigableMap<K, VF> map, Transformer<VF, VT> valueTransformer)
   {
      return new ValueTransformingNavigableMap<K, VF, VT>(map, valueTransformer);
   }

   public static <K, VF, VT> NavigableMap<K, VT> decorateValueBased(NavigableMap<K, VF> map, BidiTransformer<VF, VT> valueTransformer)
   {
      return new ValueBidiTransformingNavigableMap<K, VF, VT>(map, valueTransformer);
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(NavigableMap<KF, VF> map, Transformer<KF, KT> keyTransformer, Transformer<VF, VT> valueTransformer)
   {
      NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(NavigableMap<KF, VF> map, BidiTransformer<KF, KT> keyTransformer, Transformer<VF, VT> valueTransformer)
   {
      NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(NavigableMap<KF, VF> map, Transformer<KF, KT> keyTransformer, BidiTransformer<VF, VT> valueTransformer)
   {
      NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }

   public static <KF, KT, VF, VT> NavigableMap<KT, VT> decorate(NavigableMap<KF, VF> map, BidiTransformer<KF, KT> keyTransformer, BidiTransformer<VF, VT> valueTransformer)
   {
      NavigableMap<KF, VT> valueTransformingMap = decorateValueBased(map, valueTransformer);
      return decorateKeyBased(valueTransformingMap, keyTransformer);
   }
}
