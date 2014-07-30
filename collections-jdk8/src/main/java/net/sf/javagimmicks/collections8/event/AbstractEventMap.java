package net.sf.javagimmicks.collections8.event;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.collections8.decorators.AbstractEntryDecorator;
import net.sf.javagimmicks.collections8.decorators.AbstractIteratorDecorator;
import net.sf.javagimmicks.collections8.decorators.AbstractUnmodifiableMapDecorator;

/**
 * A base {@link Map} wrapper that reports changes to internal callback methods
 * - these must be overwritten by concrete implementations in order to react in
 * any way to the changes.
 * <p>
 * Methods that <b>must</b> be overwritten:
 * <ul>
 * <li>{@link #fireEntryAdded(Object, Object)}</li>
 * <li>{@link #fireEntryUpdated(Object, Object, Object)}</li>
 * <li>{@link #fireEntryRemoved(Object, Object)}</li>
 * </ul>
 */
public abstract class AbstractEventMap<K, V> extends AbstractUnmodifiableMapDecorator<K, V>
{
   private static final long serialVersionUID = -2690296055006665266L;

   /**
    * Wraps a new instance around a given {@link Map}
    * 
    * @param decorated
    *           the {@link Map} to wrap
    */
   public AbstractEventMap(final Map<K, V> decorated)
   {
      super(decorated);
   }

   @Override
   public Set<Entry<K, V>> entrySet()
   {
      return new EventMapEntrySet(getDecorated().entrySet());
   }

   @Override
   public V put(final K key, final V value)
   {
      final boolean isUpdate = containsKey(key);
      final V oldValue = getDecorated().put(key, value);

      if (isUpdate)
      {
         fireEntryUpdated(key, oldValue, value);
      }
      else
      {
         fireEntryAdded(key, value);
      }

      return oldValue;
   }

   abstract protected void fireEntryAdded(K key, V value);

   abstract protected void fireEntryUpdated(K key, V oldValue, V newValue);

   abstract protected void fireEntryRemoved(K key, V value);

   protected class EventMapEntrySet extends AbstractEventSet<Entry<K, V>>
   {
      private static final long serialVersionUID = 4496963842926801525L;

      protected EventMapEntrySet(final Set<Entry<K, V>> decorated)
      {
         super(decorated);
      }

      @Override
      public Iterator<Entry<K, V>> iterator()
      {
         return new AbstractIteratorDecorator<Entry<K, V>>(super.iterator())
         {
            @Override
            public Entry<K, V> next()
            {
               return new AbstractEntryDecorator<K, V>(super.next())
               {
                  private static final long serialVersionUID = -6377534237333144069L;

                  @Override
                  public V setValue(final V value)
                  {
                     final V oldValue = super.setValue(value);

                     fireEntryUpdated(getKey(), oldValue, value);

                     return oldValue;
                  }
               };
            }
         };
      }

      @Override
      public boolean add(final Entry<K, V> e)
      {
         throw new UnsupportedOperationException("Cannot directly add entries to the EntrySet of an AbstractEventMap!");
      }

      @Override
      protected void fireElementAdded(final Entry<K, V> element)
      {}

      @Override
      protected void fireElementReadded(final Entry<K, V> element)
      {}

      @Override
      protected void fireElementRemoved(final Entry<K, V> element)
      {
         fireEntryRemoved(element.getKey(), element.getValue());
      }
   }
}
