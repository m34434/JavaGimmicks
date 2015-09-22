package net.sf.javagimmicks.collections8.transformer;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

import net.sf.javagimmicks.transform8.Transforming;

class TransformingSpliterator<F, T> implements Transforming<F, T>, Spliterator<T>
{
   protected final Spliterator<F> _internalSpliterator;
   private final Function<F, T> _transformer;

   TransformingSpliterator(final Spliterator<F> internalSpliterator, final Function<F, T> transformer)
   {
      _internalSpliterator = internalSpliterator;
      _transformer = transformer;
   }

   @Override
   public boolean tryAdvance(final Consumer<? super T> action)
   {
      return _internalSpliterator.tryAdvance(TransformerUtils.decorate(action, getTransformerFunction()));
   }

   @Override
   public Spliterator<T> trySplit()
   {
      final Spliterator<F> split = _internalSpliterator.trySplit();

      return split != null ? TransformerUtils.decorate(split, getTransformerFunction()) : null;
   }

   @Override
   public long estimateSize()
   {
      return _internalSpliterator.estimateSize();
   }

   @Override
   public int characteristics()
   {
      return _internalSpliterator.characteristics();
   }

   @Override
   public Function<F, T> getTransformerFunction()
   {
      return _transformer;
   }
}