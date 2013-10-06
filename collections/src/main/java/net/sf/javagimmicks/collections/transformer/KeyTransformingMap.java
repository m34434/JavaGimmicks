package net.sf.javagimmicks.collections.transformer;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.lang.Transformer;
import net.sf.javagimmicks.lang.Transforming;

class KeyTransformingMap<KF, KT, V>
   extends AbstractMap<KT, V>
   implements Transforming<KF, KT>
{
   protected final Map<KF, V> _internalMap;
   protected final Transformer<KF, KT> _transformer;
   
   /**
    * @deprecated Use TranformerUtils.decorateKeyBased() instead
    */
   @Deprecated
   public KeyTransformingMap(Map<KF, V> map, Transformer<KF, KT> transformer)
   {
      _internalMap = map;
      _transformer = transformer;
   }
   
   public Transformer<KF, KT> getTransformer()
   {
      return _transformer;
   }

   @Override
   public void clear()
   {
      _internalMap.clear();
   }

   @Override
   public boolean containsValue(Object value)
   {
      return _internalMap.containsValue(value);
   }

   @Override
   public Set<Map.Entry<KT, V>> entrySet()
   {
      return TransformerUtils.decorate(
         _internalMap.entrySet(),
         new KeyTransformingEntryTransformer<KF, KT, V>(getTransformer()));
   }
   
   @Override
   public boolean isEmpty()
   {
      return _internalMap.isEmpty();
   }

   @Override
   public Set<KT> keySet()
   {
      return TransformerUtils.decorate(
         _internalMap.keySet(),
         getTransformer());
   }

   @Override
   public int size()
   {
      return _internalMap.size();
   }
   
   @Override
   public Collection<V> values()
   {
      return _internalMap.values();
   }
   
   protected KT transform(KF element)
   {
      return getTransformer().transform(element);
   }
   
   protected static class KeyTransformingEntry<KF, KT, V>
      implements Entry<KT, V>, Transforming<KF, KT>
   {
      protected final Entry<KF, V> _internalEntry;
      protected final Transformer<KF, KT> _transformer;
      
      public KeyTransformingEntry(Entry<KF, V> entry, Transformer<KF, KT> transformer)
      {
         _internalEntry = entry;
         _transformer = transformer;
      }

      public Transformer<KF, KT> getTransformer()
      {
         return _transformer;
      }

      public KT getKey()
      {
         return _transformer.transform(_internalEntry.getKey());
      }

      public V getValue()
      {
         return _internalEntry.getValue();
      }

      public V setValue(V value)
      {
         return _internalEntry.setValue(value);
      }
   }
   
   protected static class KeyTransformingEntryTransformer<KF, KT, V>
      implements Transformer<Entry<KF, V>, Entry<KT, V>>
   {
      protected final Transformer<KF, KT> _transformer;
      
      public KeyTransformingEntryTransformer(Transformer<KF, KT> transformer)
      {
         _transformer = transformer;
      }

      public Entry<KT, V> transform(Entry<KF, V> source)
      {
         return new KeyTransformingEntry<KF, KT, V>(source, _transformer);
      }
   }
}
