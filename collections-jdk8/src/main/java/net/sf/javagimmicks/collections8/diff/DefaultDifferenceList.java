package net.sf.javagimmicks.collections8.diff;

import java.util.ArrayList;
import java.util.List;

import net.sf.javagimmicks.collections8.decorators.AbstractUnmodifiableListDecorator;

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
   protected List<Difference<T>> getDecorated()
   {
      return super.getDecorated();
   }
}
