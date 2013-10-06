package net.sf.javagimmicks.collections.transformer;

import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.lang.BidiTransformer;
import net.sf.javagimmicks.lang.BidiTransforming;
import net.sf.javagimmicks.lang.Transformers;

class KeyBidiTransformingMap<KF, KT, V>
   extends KeyTransformingMap<KF, KT, V>
   implements BidiTransforming<KF, KT>
{
   /**
    * @deprecated Use TranformerUtils.decorateKeyBased() instead
    */
   @Deprecated
   public KeyBidiTransformingMap(Map<KF, V> map, BidiTransformer<KF, KT> transformer)
   {
      super(map, transformer);
   }
   
   public BidiTransformer<KF, KT> getBidiTransformer()
   {
      return (BidiTransformer<KF, KT>)_transformer;
   }

   @Override
   public Set<Entry<KT, V>> entrySet()
   {
      return TransformerUtils.decorate(
         _internalMap.entrySet(),
         new KeyBidiTransformingEntryBidiTransformer<KF, KT, V>(
            getBidiTransformer()));
   }

   @Override
   public Set<KT> keySet()
   {
      return TransformerUtils.decorate(
         _internalMap.keySet(),
         getBidiTransformer());
   }
   
   @Override
   public V put(KT key, V value)
   {
      return _internalMap.put(transformBack(key), value);
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public V get(Object key)
   {
      try
      {
         return _internalMap.get(transformBack((KT)key));
      }
      catch(ClassCastException ex)
      {
         return super.get(key);
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public boolean containsKey(Object key)
   {
      try
      {
         return _internalMap.containsKey(transformBack((KT)key));
      }
      catch(ClassCastException ex)
      {
         return super.containsKey(key);
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public V remove(Object key)
   {
      try
      {
         return _internalMap.remove(transformBack((KT)key));
      }
      catch(ClassCastException ex)
      {
         return super.remove(key);
      }
   }
   
   protected KF transformBack(KT element)
   {
      return getBidiTransformer().transformBack(element);
   }
   
   protected static class KeyBidiTransformingEntry<KF, KT, V>
      extends KeyTransformingEntry<KF, KT, V>
      implements BidiTransforming<KF, KT>
   {

      public KeyBidiTransformingEntry(Entry<KF, V> entry, BidiTransformer<KF, KT> transformer)
      {
         super(entry, transformer);
      }

      public BidiTransformer<KF, KT> getBidiTransformer()
      {
         return (BidiTransformer<KF, KT>)getTransformer();
      }
   }

   protected static class KeyBidiTransformingEntryBidiTransformer<KF, KT, V>
      extends KeyTransformingEntryTransformer<KF, KT, V>
      implements BidiTransformer<Entry<KF, V>, Entry<KT, V>>
   {
      public KeyBidiTransformingEntryBidiTransformer(BidiTransformer<KF, KT> transformer)
      {
         super(transformer);
      }
   
      public BidiTransformer<Entry<KT, V>, Entry<KF, V>> invert()
      {
         return Transformers.invert(this);
      }

      public Entry<KF, V> transformBack(Entry<KT, V> source)
      {
         return new KeyBidiTransformingEntry<KT, KF, V>(source, getTransformer().invert());
      }

      protected BidiTransformer<KF, KT> getTransformer()
      {
         return (BidiTransformer<KF, KT>)_transformer;
      }
   }

}
