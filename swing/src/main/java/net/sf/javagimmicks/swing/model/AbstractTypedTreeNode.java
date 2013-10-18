package net.sf.javagimmicks.swing.model;

import java.util.Collections;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

/**
 * A very basic implementation of {@link TypedTreeNode} without child or parent
 * support.
 */
public abstract class AbstractTypedTreeNode<V> implements TypedTreeNode<V>
{
   protected final V _value;

   protected final boolean _allowsChildren;
   protected final boolean _noChildrenMeansLeaf;

   protected AbstractTypedTreeNode(final V value, final boolean allowsChildren, final boolean noChildrenMeansLeaf)
   {
      _value = value;

      _allowsChildren = allowsChildren;
      _noChildrenMeansLeaf = noChildrenMeansLeaf;
   }

   protected AbstractTypedTreeNode(final V value, final boolean allowsChildren)
   {
      this(value, allowsChildren, false);
   }

   protected AbstractTypedTreeNode(final V value)
   {
      this(value, false);
   }

   @Override
   public TreeNode getParent()
   {
      return null;
   }

   @Override
   public V getValue()
   {
      return _value;
   }

   @Override
   public boolean getAllowsChildren()
   {
      return _allowsChildren;
   }

   @Override
   public boolean isLeaf()
   {
      return _noChildrenMeansLeaf ? !children().hasMoreElements() : !_allowsChildren;
   }

   @Override
   public Enumeration<?> children()
   {
      return Collections.enumeration(Collections.emptySet());
   }

   @Override
   public TreeNode getChildAt(final int childIndex)
   {
      return null;
   }

   @Override
   public int getChildCount()
   {
      return 0;
   }

   @Override
   public int getIndex(final TreeNode node)
   {
      return -1;
   }

   @Override
   public String toString()
   {
      return _value.toString();
   }
}
