package net.sf.javagimmicks.collections8;

import java.util.Map;
import java.util.function.Supplier;

import net.sf.javagimmicks.collections8.decorators.AbstractMapDecorator;

/**
 * A special {@link Map} decorator that wraps a given {@link Map} and there
 * automatically fills in (default) values if a call to {@link #get(Object)}
 * tries to receive the value for a not yet contained key. This is useful in
 * some special cases e.g. if {@link NullPointerException}s should be avoided
 * and there is some logic for creating default values.
 * <p>
 * Default values for auto-filling have to be provided/generated via a given
 * {@link Supplier}.
 */
public class AutoFillingMapDecorator<K, V> extends AbstractMapDecorator<K, V>
{
   private static final long serialVersionUID = -955460173448590006L;

   /**
    * Decorates a given {@link Map} using the given {@link Supplier} for
    * generating default values.
    * 
    * @param decorated
    *           the {@link Map} to wrap
    * @param valueSupplier
    *           the {@link Supplier} to use for generating default values
    * @return the resulting wrapped {@link Map}
    */
   public static <K, V> Map<K, V> decorate(final Map<K, V> decorated, final Supplier<V> valueSupplier)
   {
      return new AutoFillingMapDecorator<K, V>(decorated, valueSupplier);
   }

   protected final Supplier<V> _valueSupplier;

   protected AutoFillingMapDecorator(final Map<K, V> decorated, final Supplier<V> valueSupplier)
   {
      super(decorated);
      _valueSupplier = valueSupplier;
   }

   /**
    * Re-implements the default behavior in the following way:
    * <ul>
    * <li>If the given key does exist in the underlying {@link Map}, return the
    * associated value (may be <code>null</code>)
    * <li>If the given key does not yet exist in the underlying {@link Map}
    * <ul>
    * <li>Call the underlying {@link Supplier} the receive a default value</li>
    * <li>Perform a {@link #put(Object, Object)} with the given key and new
    * default value on the underlying {@link Map}
    * <li>Return the default value</li>
    * </ul>
    * </ul>
    */
   @Override
   @SuppressWarnings("unchecked")
   public V get(final Object key)
   {
      if (!containsKey(key))
      {
         put((K) key, _valueSupplier.get());
      }

      return super.get(key);
   }
}
