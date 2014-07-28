package net.sf.javagimmicks.collections.transformer;

import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.transform.BidiFunction;
import net.sf.javagimmicks.transform.BidiTransforming;
import net.sf.javagimmicks.transform.Transformers;

class KeyBidiTransformingMap<KF, KT, V>
   extends KeyTransformingMap<KF, KT, V>
   implements BidiTransforming<KF, KT>
{
   /**
    * @deprecated Use TranformerUtils.decorateKeyBased() instead
    */
   @Deprecated
   public KeyBidiTransformingMap(Map<KF, V> map, BidiFunction<KF, KT> transformer)
   {
      super(map, transformer);
   }
   
   public BidiFunction<KF, KT> getBidiTransformer()
   {
      return (BidiFunction<KF, KT>)_transformer;
   }

   @Override
   public Set<Entry<KT, V>> entrySet()
   {
      return TransformerUtils.decorate(
         _internalMap.entrySet(),
         new KeyBidiTransformingEntryBidiFunction<KF, KT, V>(
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
      return getBidiTransformer().applyReverse(element);
   }
   
   protected static class KeyBidiTransformingEntry<KF, KT, V>
      extends KeyTransformingEntry<KF, KT, V>
      implements BidiTransforming<KF, KT>
   {

      public KeyBidiTransformingEntry(Entry<KF, V> entry, BidiFunction<KF, KT> transformer)
      {
         super(entry, transformer);
      }

      public BidiFunction<KF, KT> getBidiTransformer()
      {
         return (BidiFunction<KF, KT>)getTransformer();
      }
   }

   protected static class KeyBidiTransformingEntryBidiFunction<KF, KT, V>
      extends KeyTransformingEntryTransformer<KF, KT, V>
      implements BidiFunction<Entry<KF, V>, Entry<KT, V>>
   {
      public KeyBidiTransformingEntryBidiFunction(BidiFunction<KF, KT> transformer)
      {
         super(transformer);
      }
   
      public BidiFunction<Entry<KT, V>, Entry<KF, V>> invert()
      {
         return Transformers.invert(this);
      }

      public Entry<KF, V> applyReverse(Entry<KT, V> source)
      {
         return new KeyBidiTransformingEntry<KT, KF, V>(source, getTransformer().invert());
      }

      protected BidiFunction<KF, KT> getTransformer()
      {
         return (BidiFunction<KF, KT>)_transformer;
      }
   }

}
