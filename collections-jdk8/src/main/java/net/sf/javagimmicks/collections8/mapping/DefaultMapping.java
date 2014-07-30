package net.sf.javagimmicks.collections8.mapping;

import net.sf.javagimmicks.collections8.mapping.Mappings.Mapping;

/**
 * A default immutable {@link Mapping} implementation that stores left and right
 * key in private fields.
 */
public class DefaultMapping<L, R> extends AbstractMapping<L, R>
{
   private static final long serialVersionUID = 2942113751287943014L;

   private final L _left;
   private final R _right;

   /**
    * Creates a new instance for the given left and right key.
    * 
    * @param left
    *           the left key of the {@link Mapping}
    * @param right
    *           the right key of the {@link Mapping}
    */
   public DefaultMapping(final L left, final R right)
   {
      _left = left;
      _right = right;
   }

   @Override
   public L getLeftKey()
   {
      return _left;
   }

   @Override
   public R getRightKey()
   {
      return _right;
   }

}
