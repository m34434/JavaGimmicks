package net.sf.javagimmicks.collections8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import net.sf.javagimmicks.util.Pair;

class CrossProductSpliterator<A, B> implements Spliterator<Pair<A, B>>
{
   private final Collection<? extends A> _aCollection;
   private final Collection<? extends B> _bCollection;

   CrossProductSpliterator(final Collection<? extends A> aCollection, final Collection<? extends B> bCollection)
   {
      this._aCollection = aCollection;
      this._bCollection = bCollection;
   }

   @Override
   public boolean tryAdvance(final Consumer<? super Pair<A, B>> action)
   {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public Spliterator<Pair<A, B>> trySplit()
   {
      final int aSize = _aCollection.size();
      if (aSize == 0)
      {
         return null;
      }

      if (aSize == 1)
      {
         final A aElement = _aCollection.iterator().next();
         return new AElementSpliterator<A, B>(aElement, _bCollection.spliterator());
      }

      final List<A> aFirstHalf = new ArrayList<>(aSize / 2);
      final Iterator<? extends A> aIter = _aCollection.iterator();
      for (int i = 0; i < aSize / 2; ++i)
      {
         final A aElement = aIter.next();
         aFirstHalf.add(aElement);
         aIter.remove();
      }

      return new CrossProductSpliterator<A, B>(aFirstHalf, _bCollection);
   }

   @Override
   public long estimateSize()
   {
      return _aCollection.size() * _bCollection.size();
   }

   @Override
   public int characteristics()
   {
      final Spliterator<? extends A> aSpliterator = _aCollection.spliterator();
      final Spliterator<? extends B> bSpliterator = _bCollection.spliterator();

      final boolean concurrent = aSpliterator.hasCharacteristics(CONCURRENT)
            && bSpliterator.hasCharacteristics(CONCURRENT);

      int result = concurrent ? CONCURRENT : SIZED | SUBSIZED;
      result |= NONNULL;

      if (aSpliterator.hasCharacteristics(DISTINCT) && bSpliterator.hasCharacteristics(DISTINCT))
      {
         result |= DISTINCT;
      }

      if (aSpliterator.hasCharacteristics(IMMUTABLE) && bSpliterator.hasCharacteristics(IMMUTABLE))
      {
         result |= IMMUTABLE;
      }

      return result;
   }

   static class AElementSpliterator<A, B> implements Spliterator<Pair<A, B>>
   {
      private final A _aElement;
      private final Spliterator<? extends B> _bSpliterator;

      AElementSpliterator(final A aElement, final Spliterator<? extends B> bSpliterator)
      {
         _aElement = aElement;
         _bSpliterator = bSpliterator;
      }

      @Override
      public boolean tryAdvance(final Consumer<? super Pair<A, B>> action)
      {
         return _bSpliterator.tryAdvance(new AElementConsumer<A, B>(_aElement, action));
      }

      @Override
      public Spliterator<Pair<A, B>> trySplit()
      {
         final Spliterator<? extends B> bSpliterator = _bSpliterator.trySplit();

         return bSpliterator != null ? new AElementSpliterator<A, B>(_aElement, bSpliterator) : null;
      }

      @Override
      public long estimateSize()
      {
         return _bSpliterator.estimateSize();
      }

      @Override
      public int characteristics()
      {
         return _bSpliterator.characteristics();
      }
   }

   static class AElementConsumer<A, B> implements Consumer<B>
   {
      private final A _aElement;
      private final Consumer<? super Pair<A, B>> _pairConsumer;

      AElementConsumer(final A aElement, final Consumer<? super Pair<A, B>> pairConsumer)
      {
         this._aElement = aElement;
         this._pairConsumer = pairConsumer;
      }

      @Override
      public void accept(final B bElement)
      {
         _pairConsumer.accept(Pair.of(_aElement, bElement));
      }
   }
}
