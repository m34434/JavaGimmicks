package net.sf.javagimmicks.collections8.decorators;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A basic class for {@link Spliterator} decorators that simply forwards all
 * calls to an internal delegate instance.
 */
public class AbstractSpliteratorDecorator<E> implements Spliterator<E>
{
   protected final Spliterator<E> _decorated;

   protected AbstractSpliteratorDecorator(final Spliterator<E> decorated)
   {
      this._decorated = decorated;
   }

   @Override
   public boolean tryAdvance(final Consumer<? super E> action)
   {
      return getDecorated().tryAdvance(action);
   }

   @Override
   public void forEachRemaining(final Consumer<? super E> action)
   {
      getDecorated().forEachRemaining(action);
   }

   @Override
   public Spliterator<E> trySplit()
   {
      return getDecorated().trySplit();
   }

   @Override
   public long estimateSize()
   {
      return getDecorated().estimateSize();
   }

   @Override
   public long getExactSizeIfKnown()
   {
      return getDecorated().getExactSizeIfKnown();
   }

   @Override
   public int characteristics()
   {
      return getDecorated().characteristics();
   }

   @Override
   public boolean hasCharacteristics(final int characteristics)
   {
      return getDecorated().hasCharacteristics(characteristics);
   }

   @Override
   public Comparator<? super E> getComparator()
   {
      return getDecorated().getComparator();
   }

   protected Spliterator<E> getDecorated()
   {
      return _decorated;
   }

}
