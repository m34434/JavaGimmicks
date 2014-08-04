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

   private Integer _characteristics;

   private Iterator<? extends A> _aIterator;
   private int _aElementsUsed;

   private AElementSpliterator<A, B> _currentAElementSpliterator;

   CrossProductSpliterator(final Collection<? extends A> aCollection, final Collection<? extends B> bCollection)
   {
      this._aCollection = aCollection;
      this._bCollection = bCollection;

      _aIterator = aCollection.iterator();
   }

   @Override
   public boolean tryAdvance(final Consumer<? super Pair<A, B>> action)
   {
      while (_currentAElementSpliterator != null || _aIterator.hasNext())
      {
         if (_currentAElementSpliterator == null)
         {
            _currentAElementSpliterator = new AElementSpliterator<A, B>(getNextA(), _bCollection.spliterator());
         }

         if (_currentAElementSpliterator.tryAdvance(action))
         {
            return true;
         }

         _currentAElementSpliterator = null;
      }

      return false;
   }

   @Override
   public Spliterator<Pair<A, B>> trySplit()
   {
      final int aRemaining = getRemainingSize();
      if (aRemaining == 0)
      {
         return null;
      }

      if (aRemaining == 1)
      {
         if (_currentAElementSpliterator == null)
         {
            return null;
         }

         final AElementSpliterator<A, B> result = _currentAElementSpliterator;

         _currentAElementSpliterator = new AElementSpliterator<A, B>(
               getNextA(), _bCollection.spliterator());

         return result;
      }

      final List<A> aFirstHalf = new ArrayList<>(aRemaining / 2);
      for (int i = 0; i < aRemaining / 2; ++i)
      {
         aFirstHalf.add(getNextA());
      }

      final CrossProductSpliterator<A, B> result = new CrossProductSpliterator<A, B>(aFirstHalf, _bCollection);
      result._currentAElementSpliterator = this._currentAElementSpliterator;
      this._currentAElementSpliterator = null;

      return result;
   }

   @Override
   public long estimateSize()
   {
      long result = (long) getRemainingSize() * _bCollection.size();
      if (_currentAElementSpliterator != null)
      {
         result += _currentAElementSpliterator.estimateSize();
      }

      return result;
   }

   @Override
   public int characteristics()
   {
      if (_characteristics == null)
      {
         final Spliterator<? extends A> aSpliterator = _aCollection.spliterator();
         final Spliterator<? extends B> bSpliterator = _bCollection.spliterator();

         // Result it not SORTED or CONCURRENT

         int result = NONNULL;
         result = updateCharacteristics(result, ORDERED, aSpliterator, bSpliterator);
         result = updateCharacteristics(result, DISTINCT, aSpliterator, bSpliterator);
         result = updateCharacteristics(result, SIZED, aSpliterator, bSpliterator);
         result = updateCharacteristics(result, SUBSIZED, aSpliterator, bSpliterator);
         result = updateCharacteristics(result, IMMUTABLE, aSpliterator, bSpliterator);

         _characteristics = result;
      }

      return _characteristics;
   }

   private int getRemainingSize()
   {
      return _aCollection.size() - _aElementsUsed;
   }

   private A getNextA()
   {
      final A result = _aIterator.next();
      ++_aElementsUsed;
      return result;
   }

   private static int updateCharacteristics(final int currentCharacteristics, final int checkedCharacteristic,
         final Spliterator<?> aSpliterator, final Spliterator<?> bSpliterator)
   {
      return aSpliterator.hasCharacteristics(checkedCharacteristic)
            && bSpliterator.hasCharacteristics(checkedCharacteristic) ? currentCharacteristics | checkedCharacteristic
            : currentCharacteristics;
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
