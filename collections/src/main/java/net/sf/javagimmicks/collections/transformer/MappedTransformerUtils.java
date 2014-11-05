package net.sf.javagimmicks.collections.transformer;

import java.util.Map;

import net.sf.javagimmicks.collections.bidimap.BidiMap;
import net.sf.javagimmicks.transform.BidiFunction;
import net.sf.javagimmicks.transform.Functions;
import net.sf.javagimmicks.util.Function;

/**
 * Provides features to build {@link Function}s based on {@link Map}s and
 * {@link BidiFunction}s based on {@link BidiMap}s.
 */
public class MappedTransformerUtils
{
   private MappedTransformerUtils()
   {}

   /**
    * Creates a new {@link Function} based on a given {@link Map} which
    * transforms values by looking them up in the given {@link Map}.
    * <p>
    * <b>Attention:</b> the resulting {@link Function} will throw an
    * {@link IllegalArgumentException} if it should transform a value the has no
    * corresponding key within the given {@link Map}.
    * 
    * @param map
    *           the {@link Map} to wrap into a {@link Function}
    * @param <F>
    *           the "from" or source type
    * @param <T>
    *           the "to" or target type
    * 
    * @return the resulting {@link Function}
    */
   public static <F, T> Function<F, T> asTransformer(final Map<F, T> map)
   {
      return new MapTransformer<F, T>(map);
   }

   /**
    * Creates a new {@link BidiFunction} based on a given {@link BidiMap}
    * which transforms values by looking them up in the given {@link BidiMap}.
    * <p>
    * <b>Attention:</b> the resulting {@link BidiFunction} will throw an
    * {@link IllegalArgumentException} if it should transform a value the has no
    * corresponding key (or value) within the given {@link BidiMap}.
    * 
    * @param bidiMap
    *           the {@link BidiMap} to wrap into a {@link BidiFunction}
    * @param <F>
    *           the "from" or source type
    * @param <T>
    *           the "to" or target type
    * @return the resulting {@link BidiFunction}
    */
   public static <F, T> BidiFunction<F, T> asBidiTransformer(final BidiMap<F, T> bidiMap)
   {
      return new BidiMapBidiTransformer<F, T>(bidiMap);
   }

   protected static class MapTransformer<F, T> implements Function<F, T>
   {
      protected final Map<F, T> _baseMap;

      protected MapTransformer(final Map<F, T> map)
      {
         _baseMap = map;
      }

      @Override
      public T apply(final F source)
      {
         if (!_baseMap.containsKey(source))
         {
            throw new IllegalArgumentException("This MapTransformer doesn't contain the given source element '"
                  + source + "'!");
         }

         return _baseMap.get(source);
      }
   }

   protected static class BidiMapBidiTransformer<F, T> extends MapTransformer<F, T> implements BidiFunction<F, T>
   {
      protected BidiMapBidiTransformer(final BidiMap<F, T> map)
      {
         super(map);
      }

      @Override
      public BidiFunction<T, F> invert()
      {
         return Functions.invert(this);
      }

      @Override
      public F applyReverse(final T target)
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
