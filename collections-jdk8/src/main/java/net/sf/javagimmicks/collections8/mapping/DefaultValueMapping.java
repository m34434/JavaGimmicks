package net.sf.javagimmicks.collections8.mapping;

import net.sf.javagimmicks.collections8.mapping.Mappings.Mapping;

/**
 * A default immutable {@link Mapping} implementation that stores left key,
 * right key and value in private fields.
 */
public final class DefaultValueMapping<L, R, E> extends AbstractValueMapping<L, R, E>
{
   private static final long serialVersionUID = -893342873662381319L;

   private final L left;
   private final R right;
   private final E value;

   /**
    * Creates a new instance for the given left key, right key and value.
    * 
    * @param left
    *           the left key of the {@link Mapping}
    * @param right
    *           the right key of the {@link Mapping}
    * @param value
    *           the value of the {@link Mapping}
    */
   public DefaultValueMapping(final L left, final R right, final E value)
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