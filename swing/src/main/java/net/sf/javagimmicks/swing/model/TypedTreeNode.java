package net.sf.javagimmicks.swing.model;

import javax.swing.tree.TreeNode;

/**
 * A small {@link TreeNode} extension that carries a parameterized value.
 * 
 * @param <V>
 *           the type of values that this instance can carry
 */
public interface TypedTreeNode<V> extends TreeNode
{
   /**
    * Returns the value of this {@link TypedTreeNode}.
    * 
    * @return the value of this {@link TypedTreeNode}
    */
   public V getValue();
}
