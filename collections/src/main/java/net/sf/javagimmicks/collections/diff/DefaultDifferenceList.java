package net.sf.javagimmicks.collections.diff;

import java.util.ArrayList;
import java.util.List;

import net.sf.javagimmicks.collections.decorators.AbstractUnmodifiableListDecorator;

/**
 * Provides a default implementation for {@link DifferenceList}.
 * <p>
 * <b>Note:</b> a modifiable view to this {@link DifferenceList} can be obtained
 * via the inherited {@link #getDecorated()} method - but this should
 * <b>never</b> be called by non-API clients!
 */
public class DefaultDifferenceList<T> extends AbstractUnmodifiableListDecorator<Difference<T>> implements
      DifferenceList<T>
{
   private static final long serialVersionUID = -8782622138787742405L;

   /**
    * Creates a new empty instance
    */
   public DefaultDifferenceList()
   {
      super(new ArrayList<Difference<T>>());
   }

   @Override
   public void applyTo(final List<T> list)
   {
      DifferenceUtils.applyDifferenceList(this, list);
   }

   @Override
   public DifferenceList<T> invert()
   {
      return DifferenceUtils.getInvertedDifferenceList(this);
   }

   @Override
   public List<Difference<T>> getDecorated()
   {
      return super.getDecorated();
   }
}
