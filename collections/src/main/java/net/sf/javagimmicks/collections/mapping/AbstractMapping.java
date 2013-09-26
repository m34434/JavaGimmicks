package net.sf.javagimmicks.collections.mapping;

import net.sf.javagimmicks.collections.mapping.Mappings.Mapping;

public abstract class AbstractMapping<L, R> implements Mapping<L, R>
{
   private static final long serialVersionUID = 718725888769445622L;

   @Override
   public Mapping<R, L> getInverseMapping()
   {
      return new AbstractMapping<R, L>()
      {
         private static final long serialVersionUID = 1277735552925631376L;

         @Override
         public Mapping<L, R> getInverseMapping()
         {
            return AbstractMapping.this;
         }

         @Override
         public R getLeftKey()
         {
            return AbstractMapping.this.getRightKey();
         }

         @Override
         public L getRightKey()
         {
            return AbstractMapping.this.getLeftKey();
         }
      };
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }

      if (!(obj instanceof Mapping<?, ?>))
      {
         return false;
      }

      final Mapping<?, ?> other = (Mapping<?, ?>) obj;

      return getLeftKey().equals(other.getLeftKey()) && getRightKey().equals(other.getRightKey());
   }

   @Override
   public int hashCode()
   {
      return 5 * getLeftKey().hashCode() + 7 * getRightKey().hashCode() + 3872123;
   }

   @Override
   public String toString()
   {
      return new StringBuilder()
            .append("[")
            .append(getLeftKey())
            .append(", ")
            .append(getRightKey())
            .append("]")
            .toString();
   }
}