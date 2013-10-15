package net.sf.javagimmicks.swing.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

public class ListTreeNode<E> implements TypedTreeNode<E>
{
   protected ArrayList<ListTreeNode<E>> _children;
   protected ChildrenListView _childrenListView;
   protected ChildrenValueListView _childrenValueListView;

   protected ListTreeModel<E> _model;
   protected ListTreeNode<E> _parent;
   protected E _value;

   public ListTreeNode(final E value, final boolean leaf)
   {
      this(null, null, leaf, value);
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
      setLeaf(leaf);
      _value = value;

      _childrenListView = new ChildrenListView();
      _childrenValueListView = new ChildrenValueListView();
   }

   public ListTreeNode<E> addChildAt(final int index, final E value, final boolean leaf)
   {
      final ListTreeNode<E> result = new ListTreeNode<E>(value, leaf);
      getChildListView().add(index, result);

      return result;
   }

   public ListTreeNode<E> addChildAt(final int index, final E value)
   {
      return addChildAt(index, value, false);
   }

   public ListTreeNode<E> addChild(final E value, final boolean leaf)
   {
      return addChildAt(getChildCount(), value, leaf);
   }

   public ListTreeNode<E> addChild(final E value)
   {
      return addChild(value, false);
   }

   public ListTreeNode<E> removeChildAt(final int index)
   {
      return getChildListView().remove(index);
   }

   public List<ListTreeNode<E>> getChildListView()
   {
      return _childrenListView;
   }

   public List<E> getChildValueListView()
   {
      return _childrenValueListView;
   }

   @Override
   public E getValue()
   {
      return _value;
   }

   public void setValue(final E value)
   {
      _value = value;

      if (_model != null)
      {
         _model.fireNodeChanged(_parent, _parent == null ? 0 : _parent._childrenListView.indexOf(this));
      }
   }

   public void setLeaf(final boolean leaf)
   {
      if (isLeaf() == leaf)
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
      return !isLeaf();
   }

   @Override
   public ListTreeNode<E> getChildAt(final int childIndex)
   {
      if (!getAllowsChildren())
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
      return getAllowsChildren() ? _children.indexOf(node) : -1;
   }

   @Override
   public ListTreeNode<E> getParent()
   {
      return _parent;
   }

   @Override
   public boolean isLeaf()
   {
      return _children == null;
   }

   public void detach()
   {
      if (_parent == null && _model == null)
      {
         throw new IllegalStateException("This node cannot be detached. It has no parent and is not a root node!");
      }

      if (_parent != null)
      {
         _parent._childrenListView.remove(this);
      }
      else if (_model != null)
      {
         updateModel(null);
         _model.fireNodesRemoved(null, 0, Collections.singleton(this));
      }
   }

   @Override
   public String toString()
   {
      return _value == null ? null : _value.toString();
   }

   protected void updateModel(final ListTreeModel<E> model)
   {
      _model = model;

      if (!isLeaf())
      {
         for (final ListTreeNode<E> child : _children)
         {
            child.updateModel(model);
         }
      }
   }

   protected class ChildrenListView extends AbstractList<ListTreeNode<E>>
   {
      @Override
      public boolean addAll(final int index, final Collection<? extends ListTreeNode<E>> c)
      {
         checkLeaf();

         for (final ListTreeNode<E> newChild : c)
         {
            if (newChild._model != null)
            {
               throw new IllegalArgumentException("Cannot add a node which already belongs to a model!");
            }
            else if (newChild._parent != null)
            {
               throw new IllegalArgumentException("Cannot add a non-detached node!");
            }

            newChild._parent = ListTreeNode.this;
            newChild.updateModel(_model);
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
         checkLeaf();

         clear(true);
      }

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
         removedNode.updateModel(null);
         removedNode._parent = null;

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

         final ListTreeNode<E> result = _children.set(index, element);

         if (_model != null)
         {
            _model.fireNodeChanged(ListTreeNode.this, index);
         }

         return result;
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

      private void checkLeaf()
      {
         if (isLeaf())
         {
            throw new UnsupportedOperationException("Node is a leaf!");
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
