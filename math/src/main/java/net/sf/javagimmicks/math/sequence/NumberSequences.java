package net.sf.javagimmicks.math.sequence;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A singleton registry for {@link NumberSequence} instances.
 */
public class NumberSequences
{
   private static final Map<Class<?>, NumberSequence<?>> _cache = Collections
         .synchronizedMap(new HashMap<Class<?>, NumberSequence<?>>());

   static
   {
      register(new FactorialSequence());
   }

   private NumberSequences()
   {}

   /**
    * Retrieves the {@link NumberSequence} of the given {@link Class}.
    * 
    * @param clazz
    *           the {@link Class} of the {@link NumberSequence} to retrieve
    * @return the resulting {@link NumberSequence} or {@code null} of there is
    *         no such instance cached
    */
   @SuppressWarnings("unchecked")
   public static <S extends NumberSequence<?>> S get(final Class<S> clazz)
   {
      return (S) _cache.get(clazz);
   }

   /**
    * Registers a new {@link NumberSequence} within the cache.
    * 
    * @param sequence
    *           the new {@link NumberSequence} to cache
    */
   public static <S extends NumberSequence<?>> void register(final S sequence)
   {
      _cache.put(sequence.getClass(), sequence);
   }
}
