package net.sf.javagimmicks.collections.transformer;

import java.util.Map;

import net.sf.javagimmicks.collections.bidimap.BidiMap;
import net.sf.javagimmicks.transform.BidiTransformer;
import net.sf.javagimmicks.transform.Transformer;
import net.sf.javagimmicks.transform.Transformers;

/**
 * Provides features to build {@link Transformer}s based on {@link Map}s and
 * {@link BidiTransformer}s based on {@link BidiMap}s.
 */
public class MappedTransformerUtils
{
   private MappedTransformerUtils()
   {}

   /**
    * Creates a new {@link Transformer} based on a given {@link Map} which
    * transforms values by looking them up in the given {@link Map}.
    * <p>
    * <b>Attention:</b> the resulting {@link Transformer} will throw an
    * {@link IllegalArgumentException} if it should transform a value the has no
    * corresponding key within the given {@link Map}.
    * 
    * @param map
    *           the {@link Map} to wrap into a {@link Transformer}
    * @return the resulting {@link Transformer}
    */
   public static <F, T> Transformer<F, T> asTransformer(final Map<F, T> map)
   {
      return new MapTransformer<F, T>(map);
   }

   /**
    * Creates a new {@link BidiTransformer} based on a given {@link BidiMap}
    * which transforms values by looking them up in the given {@link BidiMap}.
    * <p>
    * <b>Attention:</b> the resulting {@link BidiTransformer} will throw an
    * {@link IllegalArgumentException} if it should transform a value the has no
    * corresponding key (or value) within the given {@link BidiMap}.
    * 
    * @param bidiMap
    *           the {@link BidiMap} to wrap into a {@link BidiTransformer}
    * @return the resulting {@link BidiTransformer}
    */
   public static <F, T> BidiTransformer<F, T> asBidiTransformer(final BidiMap<F, T> bidiMap)
   {
      return new BidiMapBidiTransformer<F, T>(bidiMap);
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
         if (!_baseMap.containsKey(source))
         {
            throw new IllegalArgumentException("This MapTransformer doesn't contain the given source element '"
                  + source + "'!");
         }

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
         return Transformers.invert(this);
      }

      @Override
      public F transformBack(final T target)
      {
         if (!_baseMap.containsValue(target))
         {
            throw new IllegalArgumentException("This BidiMapTransformer doesn't contain the given target element '"
                  + target + "'!");
         }

         return ((BidiMap<F, T>) _baseMap).getKey(target);
      }
   }
}
