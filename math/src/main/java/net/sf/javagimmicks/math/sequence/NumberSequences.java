package net.sf.javagimmicks.math.sequence;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

   @SuppressWarnings("unchecked")
   public static <S extends NumberSequence<?>> S get(final Class<S> clazz)
   {
      return (S) _cache.get(clazz);
   }

   public static <S extends NumberSequence<?>> void register(final S sequence)
   {
      _cache.put(sequence.getClass(), sequence);
   }
}
