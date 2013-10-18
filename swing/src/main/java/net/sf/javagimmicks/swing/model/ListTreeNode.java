package net.sf.javagimmicks.swing.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 * A very powerful implementation of {@link TypedTreeNode} (and it's child
 * interfaces) dedicated to act as a leaf, intermediate or root node within a
 * {@link ListTreeModel} (but works as well for other {@link TreeModel}s) that
 * allows easy {@link List}-style and fluent API access to child nodes and
 * values.
 * <p>
 * Nevertheless - it is only suitable, if all nodes within the {@link TreeModel}
 * carry the same type of values (like in a {@link ListTreeModel}). This is
 * basically no problem, because you can choose {@link Object} as value type -
 * but you might have many type-casts then.
 * 
 * @param <E>
 *           the type of values that <b>ALL</b> nodes within the represented
 *           {@link TreeModel} or {@link ListTreeModel} can carry
 */
public class ListTreeNode<E> implements TypedTreeNode<E>, TypedChildTreeNode<E, E, ListTreeNode<E>>,
      TypedParentTreeNode<E, E, ListTreeNode<E>>
{
   protected ArrayList<ListTreeNode<E>> _children;
   protected ChildrenListView _childrenListView;
   protected ChildrenValueListView _childrenValueListView;

   protected ListTreeModel<E> _model;
   protected ListTreeNode<E> _parent;
   protected E _value;

   /**
    * Creates a new instance with the given value and {@link #isDedicatedLeaf()
    * dedicated leaf mode} setting.
    * 
    * @param value
    *           the value that this node should carry
    * @param leaf
    *           if this node is a {@link #isDedicatedLeaf() dedicated leaf} or
    *           not
    */
   public ListTreeNode(final E value, final boolean leaf)
   {
      this(null, null, leaf, value);
   }

   /**
    * Creates a new instance with the given value in {@link #isDedicatedLeaf()
    * non-dedicated leaf mode}.
    * 
    * @param value
    *           the value that this node should carry
    */
   public ListTreeNode(final E value)
   {
      this(value, false);
   }

   protected ListTreeNode(final ListTreeModel<E> model, final boolean leaf, final E value)
   {
      this(model, null, leaf, value);
   }

   protected ListTreeNode(final ListTreeNode<E> parent, final boolean leaf, final E value)
   {
      this(parent._model, parent, leaf, value);
   }

   private ListTreeNode(final ListTreeModel<E> model, final ListTreeNode<E> parent, final boolean leaf, final E value)
   {
      _model = model;
      _parent = parent;
      setDedicatedLeaf(leaf);
      _value = value;

      _childrenListView = new ChildrenListView();
      _childrenValueListView = new ChildrenValueListView();
   }

   /**
    * Adds and returns a new child {@link ListTreeNode} for the given value at
    * the given position and with the given {@link #isDedicatedLeaf() dedicated
    * leaf mode} setting.
    * 
    * @param index
    *           the index within the internal child list where to add the new
    *           child {@link ListTreeNode}
    * @param value
    *           the value to set for the new child {@link ListTreeNode}
    * @param leaf
    *           the {@link #isDedicatedLeaf() dedicated leaf mode} setting for
    *           the new child {@link ListTreeNode}
    * @return the new child {@link ListTreeNode}
    */
   public ListTreeNode<E> addChildAt(final int index, final E value, final boolean leaf)
   {
      final ListTreeNode<E> result = new ListTreeNode<E>(value, leaf);
      getChildList().add(index, result);

      return result;
   }

   /**
    * Adds and returns a new child {@link ListTreeNode} for the given value at
    * the given position in {@link #isDedicatedLeaf() non-leaf} mode.
    * 
    * @param index
    *           the index within the internal child list where to add the new
    *           child {@link ListTreeNode}
    * @param value
    *           the value to set for the new child {@link ListTreeNode}
    * @return the new child {@link ListTreeNode}
    */
   public ListTreeNode<E> addChildAt(final int index, final E value)
   {
      return addChildAt(index, value, false);
   }

   /**
    * Appends and returns a new child {@link ListTreeNode} for the given value
    * with the given {@link #isDedicatedLeaf() dedicated leaf mode} setting.
    * 
    * @param value
    *           the value to set for the new child {@link ListTreeNode}
    * @param leaf
    *           the {@link #isDedicatedLeaf() dedicated leaf mode} setting for
    *           the new child {@link ListTreeNode}
    * @return the new child {@link ListTreeNode}
    */
   public ListTreeNode<E> addChild(final E value, final boolean leaf)
   {
      return addChildAt(getChildCount(), value, leaf);
   }

   /**
    * Appends and returns a new child {@link ListTreeNode} for the given value
    * in {@link #isDedicatedLeaf() non-dedicated leaf mode}.
    * 
    * @param value
    *           the value to set for the new child {@link ListTreeNode}
    * @return the new child {@link ListTreeNode}
    */
   public ListTreeNode<E> addChild(final E value)
   {
      return addChild(value, false);
   }

   /**
    * Removes the child {@link ListTreeNode} at the given position,
    * {@link #detach() detaches} and returns it.
    * 
    * @param index
    *           the index of the child {@link ListTreeNode} to remove
    * @return the removed and {@link #isDetached() detached}
    *         {@link ListTreeNode}
    */
   public ListTreeNode<E> removeChildAt(final int index)
   {
      return getChildList().remove(index);
   }

   /**
    * Returns a {@link List} view of the internal child list of
    * {@link ListTreeNode}s that takes care about proper state
    * checking/manipulation upon any called operation on it.
    * <p>
    * This means the following:
    * <ul>
    * <li>Upon add operations (including new nodes added via
    * {@link List#set(int, Object) set()})</li>
    * <ul>
    * <li>Make a consistency check, if the new {@link ListTreeNode} is fully
    * {@link #isDetached() detached} - if not, throw an
    * {@link IllegalArgumentException}</li>
    * <li>Attach the new {@link ListTreeNode} - i.e. set it's parent to this
    * {@link ListTreeNode} and apply recursively our internal
    * {@link ListTreeModel} if existing</li>
    * <li>Fire respective {@link TreeModelEvent}s if this instance is internally
    * attached to a {@link ListTreeModel}</li>
    * </ul>
    * <li>Upon remove operations (including old nodes removed via
    * {@link List#set(int, Object) set()})</li>
    * <ul>
    * <li>{@link #detach() Detach} the removed {@link ListTreeNode}</li>
    * <li>Fire respective {@link TreeModelEvent}s if this instance is internally
    * attached to a {@link ListTreeModel}</li>
    * </ul>
    * <li>Upon all operations except {@link List#isEmpty() isEmpty()} and
    * {@link List#size() size()}
    * <ul>
    * <li>Throw an {@link IllegalStateException} if the current instance is in
    * {@link #isDedicatedLeaf() dedicated leaf mode}.
    * </ul>
    * </ul>
    * 
    * @return the {@link List} view all child {@link ListTreeNode}s
    */
   public List<ListTreeNode<E>> getChildList()
   {
      return _childrenListView;
   }

   /**
    * Returns a {@link List} view to the values of the internally managed child
    * {@link ListTreeNode}s whose operations mostly behave like that of
    * {@link #getChildList()}.
    * <p>
    * The {@link List} view is completely backed - so for example any
    * {@code add()} operation will actually add a new {@link ListTreeNode} with
    * the given value to the internal list of {@link ListTreeNode}s, and so on.
    * The only exception is the {@link List#set(int, Object) set()} operation
    * which does not remove the existing {@link ListTreeNode} and add a new one
    * - instead simply the value of the existing {@link ListTreeNode} is
    * {@link ListTreeNode#setValue(Object) updated}.
    */
   @Override
   public List<E> getChildValues()
   {
      return _childrenValueListView;
   }

   @Override
   public E getValue()
   {
      return _value;
   }

   /**
    * Updates the internal value of this node - eventually firing a respective
    * {@link TreeModelEvent} if there is a surrounding {@link ListTreeModel}.
    * 
    * @param value
    *           the new value to apply
    */
   public void setValue(final E value)
   {
      _value = value;

      if (_model != null)
      {
         _model.fireNodeChanged(_parent, _parent == null ? 0 : _parent._childrenListView.indexOf(this));
      }
   }

   @Override
   public boolean isLeaf()
   {
      return isDedicatedLeaf() || _children.isEmpty();
   }

   /**
    * Returns if this instance is a dedicated leaf which means that it is not
    * allowed to add children (see {@link #getAllowsChildren()}).
    * 
    * @return if the instance is a dedicated leaf
    * @see #getAllowsChildren()
    */
   public boolean isDedicatedLeaf()
   {
      return _children == null;
   }

   /**
    * Set the given dedicated leaf mode on this instance.
    * 
    * @param leaf
    *           if the instance is a dedicated leaf or not
    */
   public void setDedicatedLeaf(final boolean leaf)
   {
      if (isDedicatedLeaf() == leaf)
      {
         return;
      }

      if (!leaf)
      {
         _children = new ArrayList<ListTreeNode<E>>();
      }
      else if (_children.isEmpty())
      {
         _children = null;
      }
      else
      {
         throw new IllegalStateException("Node still has children. Remove them before setting the node to leaf mode.");
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public Enumeration<ListTreeNode<E>> children()
   {
      return Collections.enumeration(isLeaf() ? Collections.EMPTY_LIST : _children);
   }

   @Override
   public boolean getAllowsChildren()
   {
      return !isDedicatedLeaf();
   }

   @Override
   public ListTreeNode<E> getChildAt(final int childIndex)
   {
      if (isDedicatedLeaf())
      {
         throw new ArrayIndexOutOfBoundsException("Node allows no children!");
      }

      return _children.get(childIndex);
   }

   @Override
   public int getChildCount()
   {
      return isLeaf() ? 0 : _children.size();
   }

   @Override
   public int getIndex(final TreeNode node)
   {
      return isDedicatedLeaf() ? -1 : _children.indexOf(node);
   }

   @Override
   public ListTreeNode<E> getParent()
   {
      return _parent;
   }

   @Override
   public E getParentValue()
   {
      if (_parent == null)
      {
         throw new IllegalStateException("Node has no parent!");
      }

      return _parent.getValue();
   }

   /**
    * Returns if this instance is fully detached, which means it has no link to
    * a surrounding {@link ListTreeModel} and also has no parent
    * {@link ListTreeNode}.
    * <p>
    * A fully detached {@link ListTreeNode} can be added as a child to an
    * existing {@link ListTreeNode} via it's {@link #getChildList() child list
    * view}
    * <p>
    * A node can be fully detached using {@link #detach()}
    * 
    * @return if the current instance is fully detached
    * @see #detach()
    */
   public boolean isDetached()
   {
      return _parent == null && _model == null;
   }

   /**
    * Fully detaches this node, which removes the internal links from and to
    * it's parent {@link ListTreeNode} (if existing) and removes the link to the
    * surrounding {@link ListTreeModel} (if existing) eventually firing a
    * respective {@link TreeModelEvent}.
    * 
    * @throws IllegalStateException
    *            if the node is already fully detached
    */
   public void detach() throws IllegalStateException
   {
      if (isDetached())
      {
         throw new IllegalStateException("This node is already fully detached!");
      }

      // If there is a parent, remove this node via its child list view - it
      // will take care of everything
      if (_parent != null)
      {
         _parent._childrenListView.remove(this);
      }

      // We had no parent but a model means, that we were the root node
      else if (_model != null)
      {
         final ListTreeModel<E> model = updateModel(null);
         model._root = null;
         model.fireNodesRemoved(null, 0, Collections.singleton(this));
      }
   }

   @Override
   public String toString()
   {
      return _value == null ? null : _value.toString();
   }

   protected ListTreeModel<E> updateModel(final ListTreeModel<E> model)
   {
      final ListTreeModel<E> result = _model;

      _model = model;

      if (!isLeaf())
      {
         for (final ListTreeNode<E> child : _children)
         {
            child.updateModel(model);
         }
      }

      return result;
   }

   protected class ChildrenListView extends AbstractList<ListTreeNode<E>>
   {
      @Override
      public boolean addAll(final int index, final Collection<? extends ListTreeNode<E>> c)
      {
         checkLeaf();

         for (final ListTreeNode<E> newChild : c)
         {
            preProcessAdd(newChild);
         }

         _children.addAll(index, c);

         if (_model != null)
         {
            _model.fireNodesInserted(ListTreeNode.this, index, c);
         }

         return true;
      }

      @Override
      public boolean addAll(final Collection<? extends ListTreeNode<E>> c)
      {
         return addAll(_children.size(), c);
      }

      @Override
      public void add(final int index, final ListTreeNode<E> element)
      {
         addAll(index, Collections.singleton(element));
      }

      @Override
      public void clear()
      {
         super.clear();

         /*
          * The following implementation of the clear() operation whould
          * recursively clear child nodes! I think this would break the contract
          * of List where clear() is the same as a buld remove()
          */
         // checkLeaf();

         // clear(true);
      }

      @Override
      public ListTreeNode<E> get(final int index)
      {
         checkLeaf();

         return _children.get(index);
      }

      @Override
      public ListTreeNode<E> remove(final int index)
      {
         checkLeaf();

         final ListTreeNode<E> removedNode = _children.remove(index);
         postProcessRemove(removedNode);

         if (_model != null)
         {
            _model.fireNodesRemoved(ListTreeNode.this, index, Collections.singleton(removedNode));
         }

         return removedNode;
      }

      @Override
      public ListTreeNode<E> set(final int index, final ListTreeNode<E> element)
      {
         checkLeaf();

         preProcessAdd(element);

         final ListTreeNode<E> removedNode = _children.set(index, element);
         postProcessRemove(removedNode);

         if (_model != null)
         {
            _model.fireNodeChanged(ListTreeNode.this, index);
         }

         return removedNode;
      }

      @Override
      public int size()
      {
         if (isLeaf())
         {
            return 0;
         }

         return _children.size();
      }

      @SuppressWarnings("unused")
      private void clear(final boolean isTop)
      {
         if (isLeaf())
         {
            return;
         }

         for (final ListTreeNode<E> newChild : _children)
         {
            newChild._childrenListView.clear(false);

            newChild._model = null;
            newChild._parent = null;
         }

         if (isTop && _model != null)
         {
            final ArrayList<ListTreeNode<E>> oldChildren = new ArrayList<ListTreeNode<E>>(_children);
            _children.clear();

            _model.fireNodesRemoved(ListTreeNode.this, 0, oldChildren);
         }
         else
         {
            _children.clear();
         }
      }

      private void preProcessAdd(final ListTreeNode<E> newChild)
      {
         if (!newChild.isDetached())
         {
            throw new IllegalArgumentException("Can only add fully deatched nodes!");
         }

         newChild._parent = ListTreeNode.this;
         newChild.updateModel(_model);
      }

      private void postProcessRemove(final ListTreeNode<E> removedNode)
      {
         removedNode.updateModel(null);
         removedNode._parent = null;
      }

      private void checkLeaf()
      {
         if (isLeaf())
         {
            throw new IllegalStateException("Node is a leaf!");
         }
      }
   }

   protected class ChildrenValueListView extends AbstractList<E>
   {
      @Override
      public void add(final int index, final E element)
      {
         final ListTreeNode<E> newNode = new ListTreeNode<E>(element, false);
         _childrenListView.add(index, newNode);
      }

      @Override
      public boolean addAll(final Collection<? extends E> c)
      {
         return addAll(_childrenListView.size(), c);
      }

      @Override
      public boolean addAll(final int index, final Collection<? extends E> c)
      {
         final ArrayList<ListTreeNode<E>> newNodeCollection = new ArrayList<ListTreeNode<E>>(c.size());
         for (final E element : c)
         {
            newNodeCollection.add(new ListTreeNode<E>(element, false));
         }

         return _childrenListView.addAll(index, newNodeCollection);
      }

      @Override
      public void clear()
      {
         _childrenListView.clear();
      }

      @Override
      public E get(final int index)
      {
         return _childrenListView.get(index).getValue();
      }

      @Override
      public E remove(final int index)
      {
         return _childrenListView.remove(index).getValue();
      }

      @Override
      public E set(final int index, final E element)
      {
         final ListTreeNode<E> childNode = _childrenListView.get(index);

         final E oldValue = childNode.getValue();

         childNode.setValue(element);

         return oldValue;
      }

      @Override
      public int size()
      {
         return _childrenListView.size();
      }
   }
}