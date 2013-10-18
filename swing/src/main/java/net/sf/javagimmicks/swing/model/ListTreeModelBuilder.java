package net.sf.javagimmicks.swing.model;

/**
 * A builder implementation that allows to create and fill a new
 * {@link ListTreeModel} with a fluent API.
 * <p>
 * <b>Attention:</b> instances of this class are not reusable to build more than
 * one {@link ListTreeModel}. Successive calls to {@link #getModel()} will
 * always return the same internal {@link ListTreeModel}.
 * <p>
 * The fluent API is very simple: {@link #add(Object)} adds a child
 * {@link ListTreeNode} to the currently built {@link ListTreeNode},
 * {@link #children()} internally switches down one level and allows to specify
 * children for the last added {@link ListTreeNode} (and so on) until a call to
 * {@link #parent()} which comes back to the original level and allows to
 * specify more children there. The resulting {@link ListTreeModel} can finally
 * retrieved via {@link #getModel()}.
 * <p>
 * For a better understanding, please have a look at this example from the test
 * code of this class:
 * 
 * <pre>
 * // @formatter:off
 * return new ListTreeModelBuilder&lt;String&gt;(true)
 *    .add(&quot;Root&quot;).children()
 *    
 *       .add(&quot;A&quot;).children()
 *          .add(&quot;1&quot;)
 *          .add(&quot;2&quot;)
 *          .add(&quot;3&quot;).parent()
 *          
 *       .add(&quot;B&quot;).children()
 *          .add(&quot;4&quot;)
 *          .add(&quot;5&quot;)
 *          .add(&quot;6&quot;).parent()
 *          
 *       .add(&quot;C&quot;).children()
 *          .add(&quot;7&quot;)
 *          .add(&quot;8&quot;)
 *          .add(&quot;9&quot;).parent()
 *          
 *       .add(&quot;D&quot;)
 *       
 *    .getModel();
 * // @formatter:on
 * </pre>
 * 
 * @param <E>
 *           the types of values of the {@link ListTreeModel} to build
 */
public class ListTreeModelBuilder<E>
{
   protected final ListTreeModel<E> _model;

   protected ListTreeNode<E> _currentNode;
   protected ListTreeNode<E> _lastChild;

   /**
    * Creates a new instance with the given setting for leaf interpretation for
    * the generated {@link ListTreeModel} (see
    * {@link ListTreeModel#ListTreeModel(boolean)}).
    * 
    * @param noChildrenMeansLeaf
    *           if a node is always considered a leaf if it has no children or
    *           only then when it is in {@link ListTreeNode#isDedicatedLeaf()
    *           dedicated leaf mode}
    */
   public ListTreeModelBuilder(final boolean noChildrenMeansLeaf)
   {
      _model = new ListTreeModel<E>(noChildrenMeansLeaf);
   }

   /**
    * Creates a new instance using a default {@link ListTreeModel} (see
    * {@link ListTreeModel#ListTreeModel()}).
    */
   public ListTreeModelBuilder()
   {
      _model = new ListTreeModel<E>();
   }

   /**
    * Adds a new child {@link ListTreeNode} to the currently built
    * {@link ListTreeNode}.
    * 
    * @param value
    *           the value to set within the new child {@link ListTreeNode}
    * @return the builder itself
    * @throws IllegalStateException
    *            if more than one call to this method is done on the root level
    */
   public ListTreeModelBuilder<E> add(final E value) throws IllegalStateException
   {
      if (_currentNode == null && _lastChild == null)
      {
         _lastChild = _model.createRoot(value);
      }
      else if (_currentNode == null)
      {
         throw new IllegalStateException(
               "Can only add ONE child on the root level! Please ensure to call children() after the first call to add()!");
      }
      else
      {
         _lastChild = _currentNode.addChild(value);
      }

      return this;
   }

   /**
    * Switches to child mode for the last {@link #add(Object) added}
    * {@link ListTreeNode} allowing to specify children for it until the next
    * call to {@link #parent()}.
    * 
    * @return the builder itself but switched down one level
    * @throws IllegalStateException
    *            if no child {@link ListTreeNode} was {@link #add(Object) added}
    *            yet
    */
   public ListTreeModelBuilder<E> children() throws IllegalStateException
   {
      if (_lastChild == null)
      {
         throw new IllegalStateException("No node created yet!");
      }

      _currentNode = _lastChild;
      _lastChild = null;

      return this;
   }

   /**
    * Finished building the current {@link ListTreeNode} and goes up one level
    * to it's parent.
    * 
    * @return the builder itself but switched up one level
    */
   public ListTreeModelBuilder<E> parent() throws IllegalStateException
   {
      if (_currentNode == null)
      {
         throw new IllegalStateException("There is no parent on the root level!");
      }

      _lastChild = _currentNode;
      _currentNode = _currentNode.getParent();

      return this;
   }

   public ListTreeModel<E> getModel()
   {
      return _model;
   }
}
