package net.sf.javagimmicks.collections8.mapping;

import java.io.Serializable;
import java.util.Objects;

import net.sf.javagimmicks.collections8.mapping.ValueMappings.Mapping;

/**
 * An abstract implementation of {@link Mapping} that provides a default
 * implementation for all derivable methods.
 */
public abstract class AbstractValueMapping<L, R, E> implements Mapping<L, R, E>, Serializable
{
   private static final long serialVersionUID = -293860609319776316L;

   protected AbstractValueMapping()
   {}
   
   @Override
   public Mapping<R, L, E> invert()
   {
      return new AbstractValueMapping<R, L, E>()
      {
         private static final long serialVersionUID = 3460811385187106748L;

         @Override
         public R getLeftKey()
         {
            return AbstractValueMapping.this.getRightKey();
         }

         @Override
         public L getRightKey()
         {
            return AbstractValueMapping.this.getLeftKey();
         }
         
         public Mapping<L, R, E> invert()
         {
            return AbstractValueMapping.this;
         }

         @Override
         public E getValue()
         {
            // TODO Auto-generated method stub
            return null;
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

      if (!(obj instanceof Mapping<?, ?, ?>))
      {
         return false;
      }

      final Mapping<?, ?, ?> other = (Mapping<?, ?, ?>) obj;

      return getLeftKey().equals(other.getLeftKey()) && getRightKey().equals(other.getRightKey())
            && Objects.equals(getValue(), other.getValue());
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
            .append("/")
            .append(getRightKey())
            .append(": ")
            .append(getValue())
            .append("]")
            .toString();
   }
}