package net.sf.javagimmicks.util8;

import java.util.Objects;

class PairImpl<A, B> implements Pair<A, B>
{
   private final A _a;
   private final B _b;

   PairImpl(final A a, final B b)
   {
      this._a = a;
      this._b = b;
   }

   @Override
   public A getA()
   {
      return _a;
   }

   @Override
   public B getB()
   {
      return _b;
   }

   @Override
   public int hashCode()
   {
      return Objects.hash(_a, _b);
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (obj == null)
      {
         return false;
      }

      if (!(obj instanceof Pair<?, ?>))
      {
         return false;
      }

      final Pair<?, ?> otherPair = (Pair<?, ?>) obj;

      return Objects.deepEquals(getA(), otherPair.getA()) && Objects.deepEquals(getB(), otherPair.getB());
   }

   @Override
   public String toString()
   {
      return String.format("%s / %s", _a, _b);
   }
}