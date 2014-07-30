package net.sf.javagimmicks.collections8.mapping;

import java.io.Serializable;

import net.sf.javagimmicks.collections8.mapping.Mappings.Mapping;

/**
 * An abstract implementation of {@link Mapping} that provides a default
 * implementation for all derivable methods.
 */
public abstract class AbstractMapping<L, R> implements Mapping<L, R>, Serializable
{
   private static final long serialVersionUID = 718725888769445622L;

   protected AbstractMapping()
   {}

   @Override
   public Mapping<R, L> invert()
   {
      return new AbstractMapping<R, L>()
      {
         private static final long serialVersionUID = 3524909799166270575L;

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

         @Override
         public Mapping<L, R> invert()
         {
            return AbstractMapping.this;
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