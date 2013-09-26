package net.sf.javagimmicks.collections.mapping;


public abstract class AbstractMapping<L, R> implements Mapping<L, R>
{
   private static final long serialVersionUID = 718725888769445622L;

   public Mapping<R, L> getInverseMapping()
   {
      return new AbstractMapping<R, L>()
      {
         private static final long serialVersionUID = 1277735552925631376L;

         public Mapping<L, R> getInverseMapping()
         {
            return AbstractMapping.this;
         }

         public R getLeft()
         {
            return AbstractMapping.this.getRight();
         }

         public L getRight()
         {
            return AbstractMapping.this.getLeft();
         }
      };
   }

   @Override
   public boolean equals(Object obj)
   {
      if(this == obj)
      {
         return true;
      }
      
      if(!(obj instanceof Mapping<?, ?>))
      {
         return false;
      }
      
      Mapping<?, ?> other = (Mapping<?, ?>)obj;
      
      return getLeft().equals(other.getLeft()) && getRight().equals(other.getRight());
   }

   @Override
   public int hashCode()
   {
      return 5 * getLeft().hashCode() + 7 * getRight().hashCode() + 3872123;
   }

   @Override
   public String toString()
   {
      return new StringBuilder()
         .append("[")
         .append(getLeft())
         .append(", ")
         .append(getRight())
         .append("]")
         .toString();
   }
}