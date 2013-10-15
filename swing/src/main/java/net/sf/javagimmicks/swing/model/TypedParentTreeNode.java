package net.sf.javagimmicks.swing.model;

import java.util.Enumeration;
import java.util.List;

/**
 * A special {@link TypedTreeNode} that serves as a parent for some
 * {@link TypedChildTreeNode}s.
 * 
 * @param <ChildValue>
 *           the type of values that the child {@link TypedChildTreeNode}s carry
 * @param <ChildNode>
 *           the type of the child {@link TypedChildTreeNode}s
 */
public interface TypedParentTreeNode<Value, ChildValue, ChildNode extends TypedChildTreeNode<? extends ChildValue, Value, ? extends TypedParentTreeNode<?, ?, ?>>>
      extends TypedTreeNode<Value>
{
   @Override
   public Enumeration<? extends ChildNode> children();

   @Override
   public ChildNode getChildAt(int iChildIndex);

   /**
    * Returns the {@link List} of values that the child
    * {@link TypedChildTreeNode}s contain.
    * 
    * @return the {@link List} of values that the child
    *         {@link TypedChildTreeNode}s contain
    */
   public List<ChildValue> getChildValues();
}
