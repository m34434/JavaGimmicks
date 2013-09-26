package net.sf.javagimmicks.collections.mapping;

public final class DefaultValueMapping<L, R, E> extends AbstractValueMapping<L, R, E>
{
   private static final long serialVersionUID = -893342873662381319L;

   private final L left;
   private final R right;
   private final E value;

   DefaultValueMapping(final L left, final R right, final E value)
   {
      this.right = right;
      this.value = value;
      this.left = left;
   }

   @Override
   public L getLeftKey()
   {
      return left;
   }

   @Override
   public R getRightKey()
   {
      return right;
   }

   @Override
   public E getValue()
   {
      return value;
   }
}