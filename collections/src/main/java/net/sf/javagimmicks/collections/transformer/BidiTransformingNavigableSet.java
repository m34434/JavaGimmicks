package net.sf.javagimmicks.collections.transformer;

import java.util.Iterator;
import java.util.NavigableSet;

class BidiTransformingNavigableSet<F, T>
   extends BidiTransformingSortedSet<F, T>
   implements NavigableSet<T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingNavigableSet(NavigableSet<F> set, BidiTransformer<F, T> transformer)
   {
      super(set, transformer);
   }

   public T ceiling(T e)
   {
      F ceiling = getNavigableSet().ceiling(transformBack(e));
      return ceiling != null ? transform(ceiling) : null;
   }

   public Iterator<T> descendingIterator()
   {
      return TransformerUtils.decorate(
         getNavigableSet().descendingIterator(),
         getTransformer());
   }

   public NavigableSet<T> descendingSet()
   {
      return TransformerUtils.decorate(
         getNavigableSet().descendingSet(),
         getBidiTransformer());
   }

   public T floor(T e)
   {
      F floor = getNavigableSet().floor(transformBack(e));
      return floor != null ? transform(floor) : null;
   }

   public NavigableSet<T> headSet(T toElement, boolean inclusive)
   {
      return TransformerUtils.decorate(
         getNavigableSet().headSet(transformBack(toElement), inclusive),
         getBidiTransformer());
   }

   public T higher(T e)
   {
      F higher = getNavigableSet().higher(transformBack(e));
      return higher != null ? transform(higher) : null;
   }

   public T lower(T e)
   {
      F lower = getNavigableSet().lower(transformBack(e));
      return lower != null ? transform(lower) : null;
   }

   public T pollFirst()
   {
      F first = getNavigableSet().pollFirst();
      return first != null ? transform(first) : null;
   }

   public T pollLast()
   {
      F last = getNavigableSet().pollLast();
      return last != null ? transform(last) : null;
   }

   public NavigableSet<T> subSet(T fromElement, boolean fromInclusive,
         T toElement, boolean toInclusive)
   {
      return TransformerUtils.decorate(
         getNavigableSet().subSet(
               transformBack(fromElement), fromInclusive,
               transformBack(toElement), toInclusive),
         getBidiTransformer());
   }

   public NavigableSet<T> tailSet(T fromElement, boolean inclusive)
   {
      return TransformerUtils.decorate(
         getNavigableSet().tailSet(transformBack(fromElement), inclusive),
         getBidiTransformer());
   }

   protected NavigableSet<F> getNavigableSet()
   {
      return (NavigableSet<F>)_internalSet;
   }
}
