package net.sf.javagimmicks.collections8.transformer;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.sf.javagimmicks.transform8.Transforming;

class ValueTransformingMap<K, VF, VT>
   extends AbstractMap<K, VT>
   implements Transforming<VF, VT>
{
   protected final Map<K, VF> _internalMap;
   private final Function<VF, VT> _transformer;
   
   ValueTransformingMap(Map<K, VF> map, Function<VF, VT> valueTransformer)
   {
      _internalMap = map;
      _transformer = valueTransformer;
   }
   
   public Function<VF, VT> getTransformerFunction()
   {
      return _transformer;
   }

   public void clear()
   {
      _internalMap.clear();
   }

   public boolean containsKey(Object key)
   {
      return _internalMap.containsKey(key);
   }

   public Set<Entry<K, VT>> entrySet()
   {
      return TransformerUtils.decorate(
         _internalMap.entrySet(),
         new ValueTransformingEntryTransformer<K, VF, VT>(getTransformerFunction()));
   }
   
   public VT get(Object key)
   {
      VF result = _internalMap.get(key);
      
      if(result == null && !containsKey(key))
      {
         return null;
      }
      
      return transform(result);
   }

   public boolean isEmpty()
   {
      return _internalMap.isEmpty();
   }

   public Set<K> keySet()
   {
      return _internalMap.keySet();
   }

   public VT remove(Object key)
   {
      if(!containsKey(key))
      {
         return null;
      }
      
      VF result = _internalMap.remove(key);
      return transform(result);
   }

   public int size()
   {
      return _internalMap.size();
   }

   public Collection<VT> values()
   {
      return TransformerUtils.decorate(
         _internalMap.values(),
         getTransformerFunction());
   }
   
   protected VT transform(VF element)
   {
      return getTransformerFunction().apply(element);
   }
   
   protected static class ValueTransformingEntry<K, VF, VT>
      implements Entry<K, VT>, Transforming<VF, VT>
   {
      protected final Function<VF, VT> _transformer;
      protected final Entry<K, VF> _internalEntry;
      
      public ValueTransformingEntry(Entry<K, VF> entry, Function<VF, VT> transformer)
      {
         _transformer = transformer;
         _internalEntry = entry;
      }
      
      public Function<VF, VT> getTransformerFunction()
      {
         return _transformer;
      }

      public K getKey()
      {
         return _internalEntry.getKey();
      }

      public VT getValue()
      {
         return getTransformerFunction().apply(_internalEntry.getValue());
      }

      public VT setValue(VT value)
      {
         throw new UnsupportedOperationException();
      }
   }
   
   protected static class ValueTransformingEntryTransformer<K, VF, VT>
      implements Function<Entry<K, VF>, Entry<K, VT>>
   {
      protected final Function<VF, VT> _transformer;
      
      public ValueTransformingEntryTransformer(Function<VF, VT> transformer)
      {
         _transformer = transformer;
      }

      public Entry<K, VT> apply(Entry<K, VF> source)
      {
         return new ValueTransformingEntry<K, VF, VT>(source, _transformer);
      }
   }
}
