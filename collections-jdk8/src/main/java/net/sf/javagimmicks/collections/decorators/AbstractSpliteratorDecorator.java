package net.sf.javagimmicks.collections.decorators;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class AbstractSpliteratorDecorator<E> implements Spliterator<E>
{
   protected final Spliterator<E> _decorated;

   protected AbstractSpliteratorDecorator(Spliterator<E> decorated)
   {
      this._decorated = decorated;
   }

   public boolean tryAdvance(Consumer<? super E> action)
   {
      return getDecorated().tryAdvance(action);
   }

   public void forEachRemaining(Consumer<? super E> action)
   {
      getDecorated().forEachRemaining(action);
   }

   public Spliterator<E> trySplit()
   {
      return getDecorated().trySplit();
   }

   public long estimateSize()
   {
      return getDecorated().estimateSize();
   }

   public long getExactSizeIfKnown()
   {
      return getDecorated().getExactSizeIfKnown();
   }

   public int characteristics()
   {
      return getDecorated().characteristics();
   }

   public boolean hasCharacteristics(int characteristics)
   {
      return getDecorated().hasCharacteristics(characteristics);
   }

   public Comparator<? super E> getComparator()
   {
      return getDecorated().getComparator();
   }
   
   protected Spliterator<E> getDecorated()
   {
      return _decorated;
   }
   
}
