package net.sf.javagimmicks.collections8.transformer;

import java.util.Map;
import java.util.function.Function;

import javax.xml.transform.Transformer;

import net.sf.javagimmicks.collections.bidimap.BidiMap;
import net.sf.javagimmicks.transform.BidiFunction;

/**
 * Provides features to build {@link Transformer}s based on {@link Map}s and
 * {@link BidiFunction}s based on {@link BidiMap}s.
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
   public static <F, T> Function<F, T> asTransformer(final Map<F, T> map)
   {
      return new MapFunction<F, T>(map);
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
    * @return the resulting {@link BidiFunction}
    */
   public static <F, T> BidiFunction<F, T> asBidiFunction(final BidiMap<F, T> bidiMap)
   {
      return new BidiMapBidiFunction<F, T>(bidiMap);
   }

   protected static class MapFunction<F, T> implements Function<F, T>
   {
      protected final Map<F, T> _baseMap;

      protected MapFunction(final Map<F, T> map)
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

   protected static class BidiMapBidiFunction<F, T> extends MapFunction<F, T> implements BidiFunction<F, T>
   {
      protected BidiMapBidiFunction(final BidiMap<F, T> map)
      {
         super(map);
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
