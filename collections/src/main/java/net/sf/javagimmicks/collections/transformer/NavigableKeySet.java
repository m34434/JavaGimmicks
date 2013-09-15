package net.sf.javagimmicks.collections.transformer;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.Map.Entry;

public class NavigableKeySet<K, V> extends AbstractSet<K> implements NavigableSet<K>
{
   protected final NavigableMap<K, V> _parent;
   
   public NavigableKeySet(NavigableMap<K, V> parent)
   {
      _parent = parent;
   }

   public Comparator<? super K> comparator()
   {
      return _parent.comparator();
   }

   public K first()
   {
      return _parent.firstKey();
   }

   public K last()
   {
      return _parent.lastKey();
   }

   @Override
   public Iterator<K> iterator()
   {
      Iterator<Entry<K, V>> entryIterator = _parent.entrySet().iterator();
      return TransformerUtils.decorate(entryIterator, new KeyTransformer<K, V>());
   }

   @Override
   public int size()
   {
      return _parent.size();
   }

   public K ceiling(K e)
   {
      return _parent.ceilingKey(e);
   }

   public Iterator<K> descendingIterator()
   {
      return descendingSet().iterator();
   }

   public NavigableSet<K> descendingSet()
   {
      return _parent.descendingKeySet();
   }

   public K floor(K e)
   {
      return _parent.floorKey(e);
   }

   public K higher(K e)
   {
      return _parent.higherKey(e);
   }

   public K lower(K e)
   {
      return _parent.lowerKey(e);
   }

   public K pollFirst()
   {
      Entry<K, V> firstEntry = _parent.pollFirstEntry();
      return firstEntry != null ? firstEntry.getKey() : null;
   }

   public K pollLast()
   {
      Entry<K, V> lastEntry = _parent.pollLastEntry();
      return lastEntry != null ? lastEntry.getKey() : null;
   }

   public NavigableSet<K> headSet(K toElement, boolean inclusive)
   {
      return new NavigableKeySet<K, V>(_parent.headMap(toElement, inclusive));
   }

   public SortedSet<K> headSet(K toElement)
   {
      return headSet(toElement, false);
   }

   public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive)
   {
      return new NavigableKeySet<K, V>(_parent.subMap(fromElement, fromInclusive, toElement, toInclusive));
   }

   public SortedSet<K> subSet(K fromElement, K toElement)
   {
      return subSet(fromElement, true, toElement, false);
   }

   public NavigableSet<K> tailSet(K fromElement, boolean inclusive)
   {
      return new NavigableKeySet<K, V>(_parent.tailMap(fromElement, inclusive));
   }

   public SortedSet<K> tailSet(K fromElement)
   {
      return tailSet(fromElement, true);
   }

   protected static class KeyTransformer<K, V> implements Transformer<Entry<K, V>, K>
   {
      public K transform(Entry<K, V> source)
      {
         return source.getKey();
      }
   }
}
