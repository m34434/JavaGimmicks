package net.sf.javagimmicks.collections.transformer;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.javagimmicks.transform.Transforming;
import net.sf.javagimmicks.util.Function;

class TransformingSet<F, T>
   extends AbstractSet<T>
   implements Transforming<F, T>
{
   protected final Set<F> _internalSet;
   private final Function<F, T> _transformer;
   
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingSet(Set<F> set, Function<F, T> transformer)
   {
      _internalSet = set;
      _transformer = transformer;
   }

   public Function<F, T> getTransformerFunction()
   {
      return _transformer;
   }

   @Override
   public Iterator<T> iterator()
   {
      return TransformerUtils.decorate(_internalSet.iterator(), getTransformerFunction());
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
