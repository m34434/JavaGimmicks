package net.sf.javagimmicks.swing.model;


public abstract class AbstractTypedChildTreeNode<Value, ParentValue, ParentNode extends TypedParentTreeNode<ParentValue, ? super Value, ? extends TypedChildTreeNode<?, ?, ?>>> extends AbstractTypedTreeNode<Value> implements TypedChildTreeNode<Value, ParentValue, ParentNode>
{
    protected final ParentNode _parentNode;
    
    protected AbstractTypedChildTreeNode(Value value, ParentNode parentNode, boolean allowsChildren, boolean noChildrenMeansLeaf)
    {
        super(value, allowsChildren, noChildrenMeansLeaf);
        _parentNode = parentNode;
    }
    
    protected AbstractTypedChildTreeNode(Value value, ParentNode parentNode, boolean allowsChildren)
    {
        this(value, parentNode, allowsChildren, false);
    }
    
    protected AbstractTypedChildTreeNode(Value value, ParentNode parentNode)
    {
        this(value, parentNode, false);
    }
    
    @Override
    public ParentNode getParent()
    {
        return _parentNode;
    }

    public ParentValue getParentValue()
    {
        return _parentNode.getValue();
    }
}
