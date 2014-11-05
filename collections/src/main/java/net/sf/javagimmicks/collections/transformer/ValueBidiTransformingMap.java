package net.sf.javagimmicks.collections.transformer;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.sf.javagimmicks.transform.BidiFunction;
import net.sf.javagimmicks.transform.BidiTransforming;
import net.sf.javagimmicks.transform.Functions;

class ValueBidiTransformingMap<K, VF, VT>
   extends ValueTransformingMap<K, VF, VT>
   implements BidiTransforming<VF, VT>
{
   /**
    * @deprecated Use TranformerUtils.decorateValueBased() instead
    */
   @Deprecated
   public ValueBidiTransformingMap(Map<K, VF> map, BidiFunction<VF, VT> valueTransformer)
   {
      super(map, valueTransformer);
   }
   
   public BidiFunction<VF, VT> getTransformerBidiFunction()
   {
      return (BidiFunction<VF, VT>)getTransformerFunction();
   }
   
   @SuppressWarnings("unchecked")
   @Override
   public boolean containsValue(Object value)
   {
      try
      {
         return _internalMap.containsValue(getTransformerBidiFunction().applyReverse((VT)value));
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
         new ValueBidiTransformingEntryBidiTransformer<K, VF, VT>(getTransformerBidiFunction()));
   }

   @Override
   public VT put(K key, VT value)
   {
      boolean keyContained = containsKey(key);

      VF result = _internalMap.put(key, getTransformerBidiFunction().applyReverse(value));
      return !keyContained ? null : getTransformerFunction().apply(result);
   }
   
   @Override
   public Collection<VT> values()
   {
      return TransformerUtils.decorate(_internalMap.values(), getTransformerBidiFunction());
   }

   protected static class ValueBidiTransformingEntry<K, VF, VT>
      extends ValueTransformingEntry<K, VF, VT>
      implements BidiTransforming<VF, VT>
   {
      public ValueBidiTransformingEntry(Entry<K, VF> entry, BidiFunction<VF, VT> transformer)
      {
         super(entry, transformer);
      }
      
      public BidiFunction<VF, VT> getTransformerBidiFunction()
      {
         return (BidiFunction<VF, VT>)_transformer;
      }

      @Override
      public VT setValue(VT value)
      {
         VF result = _internalEntry.setValue(getTransformerBidiFunction().applyReverse(value));
         return getTransformerFunction().apply(result);
      }
   }

   protected static class ValueBidiTransformingEntryBidiTransformer<K, VF, VT>
      extends ValueTransformingEntryTransformer<K, VF, VT>
      implements BidiFunction<Map.Entry<K, VF>, Map.Entry<K, VT>>
   {
      public ValueBidiTransformingEntryBidiTransformer(BidiFunction<VF, VT> transformer)
      {
         super(transformer);
      }

      public Entry<K, VF> applyReverse(Entry<K, VT> source)
      {
         return new ValueBidiTransformingEntry<K, VT, VF>(source, getTransformer().invert());
      }

      public BidiFunction<Entry<K, VT>, Entry<K, VF>> invert()
      {
         return Functions.invert(this);
      }

      protected BidiFunction<VF, VT> getTransformer()
      {
         return (BidiFunction<VF, VT>)_transformer;
      }
   }
}
