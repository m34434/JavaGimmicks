package net.sf.javagimmicks.swing.model;

import javax.swing.tree.TreeNode;

public interface TypedTreeNode<Value> extends TreeNode
{
    public Value getValue();
}
