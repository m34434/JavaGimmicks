package net.sf.javagimmicks.collections.transformer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.lang.BidiTransformer;
import net.sf.javagimmicks.lang.BidiTransforming;
import net.sf.javagimmicks.lang.Transformers;

class ValueBidiTransformingMap<K, VF, VT>
   extends ValueTransformingMap<K, VF, VT>
   implements BidiTransforming<VF, VT>
{
   /**
    * @deprecated Use TranformerUtils.decorateValueBased() instead
    */
   @Deprecated
   public ValueBidiTransformingMap(Map<K, VF> map, BidiTransformer<VF, VT> valueTransformer)
   {
      super(map, valueTransformer);
   }
   
   public BidiTransformer<VF, VT> getBidiTransformer()
   {
      return (BidiTransformer<VF, VT>)getTransformer();
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public boolean containsValue(Object value)
   {
      try
      {
         return _internalMap.containsValue(getBidiTransformer().transformBack((VT)value));
      }
      catch (ClassCastException e)
      {
         return super.containsValue(value);
      }
   }

   @Override
   public Set<Entry<K, VT>> entrySet()
   {
      return TransformerUtils.decorate(
         _internalMap.entrySet(),
         new ValueBidiTransformingEntryBidiTransformer<K, VF, VT>(getBidiTransformer()));
   }

   @Override
   public VT put(K key, VT value)
   {
      boolean keyContained = containsKey(key);

      VF result = _internalMap.put(key, getBidiTransformer().transformBack(value));
      return !keyContained ? null : getTransformer().transform(result);
   }
   
   @Override
   public Collection<VT> values()
   {
      return TransformerUtils.decorate(_internalMap.values(), getBidiTransformer());
   }

   protected static class ValueBidiTransformingEntry<K, VF, VT>
      extends ValueTransformingEntry<K, VF, VT>
      implements BidiTransforming<VF, VT>
   {
      public ValueBidiTransformingEntry(Entry<K, VF> entry, BidiTransformer<VF, VT> transformer)
      {
         super(entry, transformer);
      }
      
      public BidiTransformer<VF, VT> getBidiTransformer()
      {
         return (BidiTransformer<VF, VT>)_transformer;
      }

      @Override
      public VT setValue(VT value)
      {
         VF result = _internalEntry.setValue(getBidiTransformer().transformBack(value));
         return getTransformer().transform(result);
      }
   }

   protected static class ValueBidiTransformingEntryBidiTransformer<K, VF, VT>
      extends ValueTransformingEntryTransformer<K, VF, VT>
      implements BidiTransformer<Map.Entry<K, VF>, Map.Entry<K, VT>>
   {
      public ValueBidiTransformingEntryBidiTransformer(BidiTransformer<VF, VT> transformer)
      {
         super(transformer);
      }

      public Entry<K, VF> transformBack(Entry<K, VT> source)
      {
         return new ValueBidiTransformingEntry<K, VT, VF>(source, getTransformer().invert());
      }

      public BidiTransformer<Entry<K, VT>, Entry<K, VF>> invert()
      {
         return Transformers.invert(this);
      }

      protected BidiTransformer<VF, VT> getTransformer()
      {
         return (BidiTransformer<VF, VT>)_transformer;
      }
   }
}
