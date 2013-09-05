package net.sf.javagimmicks.swing.model;

public class ListTreeModelBuilder<E>
{  
   protected final ListTreeModel<E> _model;
   
   protected ListTreeNode<E> _currentNode;
   protected ListTreeNode<E> _lastChild;
   
   public ListTreeModelBuilder(boolean noChildrenMeansLeaf)
   {
      _model = new ListTreeModel<E>(noChildrenMeansLeaf);
   }
   
   public ListTreeModelBuilder()
   {
      _model = new ListTreeModel<E>();
   }
   
   public ListTreeModelBuilder<E> child(E value)
   {
      if(_currentNode == null && _lastChild == null)
      {
         _lastChild = _model.createRoot(value);
      }
      else if(_currentNode == null)
      {
         throw new IllegalStateException("Can only add one child on the root level!");
      }
      else
      {
         _lastChild = _currentNode.addChild(value);
      }
      
      return this;
   }
   
   public ListTreeModelBuilder<E> children()
   {
      if(_lastChild == null)
      {
         throw new IllegalStateException("No node created yet!");
      }
      
      _currentNode = _lastChild;
      _lastChild = null;
      
      return this;
   }
   
   public ListTreeModelBuilder<E> parent()
   {
      if(_currentNode == null)
      {
         throw new IllegalStateException("There is no parent on the root level!");
      }
      
      _lastChild = _currentNode;
      _currentNode = _currentNode.getParent();
      
      return this;
   }
   
   public ListTreeModel<E> buildModel()
   {
      return _model;
   }
}
