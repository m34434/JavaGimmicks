package net.sf.javagimmicks.collections.transformer;

import java.util.Comparator;
import java.util.SortedSet;

import net.sf.javagimmicks.transform.BidiFunction;
import net.sf.javagimmicks.util.ComparableComparator;

class BidiTransformingSortedSet<F, T> extends BidiTransformingSet<F, T> implements SortedSet<T>
{
   /**
    * @deprecated Use TranformerUtils.decorate() instead
    */
   @Deprecated
   public BidiTransformingSortedSet(SortedSet<F> set, BidiFunction<F, T> transformer)
   {
      super(set, transformer);
   }
   
   @SuppressWarnings("unchecked")
   public Comparator<? super T> comparator()
   {
      Comparator<? super F> baseComparator = getSortedSet().comparator();
      if(baseComparator == null)
      {
         baseComparator = (Comparator<? super F>)ComparableComparator.INSTANCE;
      }
      
      return TransformerUtils.decorate(
         baseComparator,
         getTransformerBidiFunction().invert());
   }
   
   public T first()
   {
      return transform(getSortedSet().first());
   }

   public SortedSet<T> headSet(T toElement)
   {
      return TransformerUtils.decorate(
         getSortedSet().headSet(transformBack(toElement)),
         getTransformerBidiFunction());
   }
   
   public T last()
   {
      return transform(getSortedSet().last());
   }

   public SortedSet<T> subSet(T fromElement, T toElement)
   {
      return TransformerUtils.decorate(
         getSortedSet().subSet(
            transformBack(fromElement),
            transformBack(toElement)),
         getTransformerBidiFunction());
   }

   public SortedSet<T> tailSet(T fromElement)
   {
      return TransformerUtils.decorate(
         getSortedSet().tailSet(transformBack(fromElement)),
         getTransformerBidiFunction());
   }

   protected SortedSet<F> getSortedSet()
   {
      return (SortedSet<F>)_internalSet;
   }
   
   protected T transform(F element)
   {
      return getTransformerFunction().apply(element);
   }
}
