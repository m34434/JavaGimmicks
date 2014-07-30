package net.sf.javagimmicks.collections8.transformer;

import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.function.Function;

class KeyTransformingNavigableMap<KF, KT, V>
      extends KeyTransformingSortedMap<KF, KT, V>
      implements NavigableMap<KT, V>
{
   /**
    * @deprecated Use TranformerUtils.decorateKeyBased() instead
    */
   @Deprecated
   public KeyTransformingNavigableMap(final NavigableMap<KF, V> map, final Function<KF, KT> transformer)
   {
      super(map, transformer);
   }

   @Override
   public Entry<KT, V> ceilingEntry(final KT key)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public KT ceilingKey(final KT key)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public NavigableSet<KT> descendingKeySet()
   {
      return TransformerUtils.decorate(
            getNavigableMap().descendingKeySet(),
            getTransformer());
   }

   @Override
   public NavigableMap<KT, V> descendingMap()
   {
      return TransformerUtils.decorateKeyBased(
            getNavigableMap().descendingMap(),
            getTransformer());
   }

   @Override
   public Entry<KT, V> firstEntry()
   {
      final Entry<KF, V> firstEntry = getNavigableMap().firstEntry();

      return firstEntry == null ? null : new KeyTransformingEntry<KF, KT, V>(
            firstEntry,
            getTransformer());
   }

   @Override
   public Entry<KT, V> floorEntry(final KT key)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public KT floorKey(final KT key)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public NavigableMap<KT, V> headMap(final KT toKey, final boolean inclusive)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public Entry<KT, V> higherEntry(final KT key)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public KT higherKey(final KT key)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public Entry<KT, V> lastEntry()
   {
      final Entry<KF, V> lastEntry = getNavigableMap().lastEntry();

      return lastEntry == null ? null : new KeyTransformingEntry<KF, KT, V>(
            lastEntry,
            getTransformer());
   }

   @Override
   public Entry<KT, V> lowerEntry(final KT key)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public KT lowerKey(final KT key)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public NavigableSet<KT> navigableKeySet()
   {
      return TransformerUtils.decorate(
            getNavigableMap().navigableKeySet(),
            getTransformer());
   }

   @Override
   public Entry<KT, V> pollFirstEntry()
   {
      final Entry<KF, V> first = getNavigableMap().pollFirstEntry();

      return first == null ? null : new KeyTransformingEntry<KF, KT, V>(
            first,
            getTransformer());
   }

   @Override
   public Entry<KT, V> pollLastEntry()
   {
      final Entry<KF, V> lastEntry = getNavigableMap().pollLastEntry();

      return lastEntry == null ? null : new KeyTransformingEntry<KF, KT, V>(
            lastEntry,
            getTransformer());
   }

   @Override
   public NavigableMap<KT, V> subMap(final KT fromKey, final boolean fromInclusive,
         final KT toKey, final boolean toInclusive)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public NavigableMap<KT, V> tailMap(final KT fromKey, final boolean inclusive)
   {
      throw new UnsupportedOperationException();
   }

   protected NavigableMap<KF, V> getNavigableMap()
   {
      return (NavigableMap<KF, V>) _internalMap;
   }
}
