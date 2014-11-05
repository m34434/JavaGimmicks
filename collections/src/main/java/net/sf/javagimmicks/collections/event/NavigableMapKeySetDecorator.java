package net.sf.javagimmicks.collections.event;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedSet;

import net.sf.javagimmicks.collections.transformer.TransformerUtils;
import net.sf.javagimmicks.util.Function;

class NavigableMapKeySetDecorator<K, V> extends AbstractSet<K> implements NavigableSet<K>
{
   protected final NavigableMap<K, V> _parent;

   public NavigableMapKeySetDecorator(final NavigableMap<K, V> parent)
   {
      _parent = parent;
   }

   @Override
   public Comparator<? super K> comparator()
   {
      return _parent.comparator();
   }

   @Override
   public K first()
   {
      return _parent.firstKey();
   }

   @Override
   public K last()
   {
      return _parent.lastKey();
   }

   @Override
   public Iterator<K> iterator()
   {
      final Iterator<Entry<K, V>> entryIterator = _parent.entrySet().iterator();
      return TransformerUtils.decorate(entryIterator, new KeyTransformer<K, V>());
   }

   @Override
   public int size()
   {
      return _parent.size();
   }

   @Override
   public K ceiling(final K e)
   {
      return _parent.ceilingKey(e);
   }

   @Override
   public Iterator<K> descendingIterator()
   {
      return descendingSet().iterator();
   }

   @Override
   public NavigableSet<K> descendingSet()
   {
      return _parent.descendingKeySet();
   }

   @Override
   public K floor(final K e)
   {
      return _parent.floorKey(e);
   }

   @Override
   public K higher(final K e)
   {
      return _parent.higherKey(e);
   }

   @Override
   public K lower(final K e)
   {
      return _parent.lowerKey(e);
   }

   @Override
   public K pollFirst()
   {
      final Entry<K, V> firstEntry = _parent.pollFirstEntry();
      return firstEntry != null ? firstEntry.getKey() : null;
   }

   @Override
   public K pollLast()
   {
      final Entry<K, V> lastEntry = _parent.pollLastEntry();
      return lastEntry != null ? lastEntry.getKey() : null;
   }

   @Override
   public NavigableSet<K> headSet(final K toElement, final boolean inclusive)
   {
      return new NavigableMapKeySetDecorator<K, V>(_parent.headMap(toElement, inclusive));
   }

   @Override
   public SortedSet<K> headSet(final K toElement)
   {
      return headSet(toElement, false);
   }

   @Override
   public NavigableSet<K> subSet(final K fromElement, final boolean fromInclusive, final K toElement,
         final boolean toInclusive)
   {
      return new NavigableMapKeySetDecorator<K, V>(_parent.subMap(fromElement, fromInclusive, toElement, toInclusive));
   }

   @Override
   public SortedSet<K> subSet(final K fromElement, final K toElement)
   {
      return subSet(fromElement, true, toElement, false);
   }

   @Override
   public NavigableSet<K> tailSet(final K fromElement, final boolean inclusive)
   {
      return new NavigableMapKeySetDecorator<K, V>(_parent.tailMap(fromElement, inclusive));
   }

   @Override
   public SortedSet<K> tailSet(final K fromElement)
   {
      return tailSet(fromElement, true);
   }

   protected static class KeyTransformer<K, V> implements Function<Entry<K, V>, K>
   {
      @Override
      public K apply(final Entry<K, V> source)
      {
         return source.getKey();
      }
   }
}
