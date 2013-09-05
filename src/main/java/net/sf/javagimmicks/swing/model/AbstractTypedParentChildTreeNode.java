package net.sf.javagimmicks.swing.model;


public abstract class AbstractTypedParentChildTreeNode<Value, ParentValue, ParentNode extends TypedParentTreeNode<ParentValue, ? super Value, ? extends TypedChildTreeNode<?, ?, ?>>, ChildValue, ChildNode extends TypedChildTreeNode<? extends ChildValue, Value, ? extends TypedParentTreeNode<?, ?, ?>>> extends AbstractTypedParentTreeNode<Value, ChildValue, ChildNode> implements TypedChildTreeNode<Value, ParentValue, ParentNode>, TypedParentTreeNode<Value, ChildValue, ChildNode>
{
    protected final ParentNode _parentNode;
    
    protected AbstractTypedParentChildTreeNode(Value value, ParentNode parentNode, boolean noChildrenMeansLeaf)
    {
        super(value, noChildrenMeansLeaf);
        
        _parentNode = parentNode;
    }

    protected AbstractTypedParentChildTreeNode(Value value, ParentNode parentNode)
    {
        super(value);
        
        _parentNode = parentNode;
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
