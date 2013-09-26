package net.sf.javagimmicks.collections.mapping;

public class DefaultMapping<L, R> extends AbstractMapping<L, R>
{
   private static final long serialVersionUID = 2942113751287943014L;

   private final L _left;
   private final R _right;
   
   public DefaultMapping(L left, R right)
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
