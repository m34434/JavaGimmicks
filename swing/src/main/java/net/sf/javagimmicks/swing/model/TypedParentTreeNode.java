package net.sf.javagimmicks.swing.model;

import java.util.Enumeration;
import java.util.List;

/**
 * A special {@link TypedTreeNode} that serves as a parent for some
 * {@link TypedChildTreeNode}s.
 * 
 * @param <CV>
 *           the type of values that the child {@link TypedChildTreeNode}s carry
 * @param <C>
 *           the type of the child {@link TypedChildTreeNode}s
 */
public interface TypedParentTreeNode<V, CV, C extends TypedChildTreeNode<? extends CV, V, ? extends TypedParentTreeNode<?, ?, ?>>>
      extends TypedTreeNode<V>
{
   @Override
   public Enumeration<? extends C> children();

   @Override
   public C getChildAt(int iChildIndex);

   /**
    * Returns the {@link List} of values that the child
    * {@link TypedChildTreeNode}s contain.
    * 
    * @return the {@link List} of values that the child
    *         {@link TypedChildTreeNode}s contain
    */
   public List<CV> getChildValues();
}
