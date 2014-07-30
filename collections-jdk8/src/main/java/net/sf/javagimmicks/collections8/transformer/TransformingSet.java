package net.sf.javagimmicks.collections8.transformer;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

import net.sf.javagimmicks.transform8.Transforming;

class TransformingSet<F, T>
   extends AbstractSet<T>
   implements Transforming<F, T>
{
   protected final Set<F> _internalSet;
   private final Function<F, T> _transformer;
   
   TransformingSet(Set<F> set, Function<F, T> transformer)
   {
      _internalSet = set;
      _transformer = transformer;
   }

   public Function<F, T> getTransformer()
   {
      return _transformer;
   }

   @Override
   public Iterator<T> iterator()
   {
      return TransformerUtils.decorate(_internalSet.iterator(), getTransformer());
   }

   @Override
   public int size()
   {
      return _internalSet.size();
   }

   @Override
   public void clear()
   {
      _internalSet.clear();
   }
   
}
