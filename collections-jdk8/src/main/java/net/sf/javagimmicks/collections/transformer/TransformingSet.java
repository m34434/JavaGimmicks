package net.sf.javagimmicks.collections.transformer;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.javagimmicks.transform.Transformer;
import net.sf.javagimmicks.transform.Transforming;

class TransformingSet<F, T>
   extends AbstractSet<T>
   implements Transforming<F, T>
{
   protected final Set<F> _internalSet;
   private final Transformer<F, T> _transformer;
   
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public TransformingSet(Set<F> set, Transformer<F, T> transformer)
   {
      _internalSet = set;
      _transformer = transformer;
   }

   public Transformer<F, T> getTransformer()
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
