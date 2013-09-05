package net.sf.javagimmicks.swing.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.AbstractListModel;

public class ListModelAdapter<E> extends AbstractListModel implements List<E>
{
    private static final long serialVersionUID = -8453375309510933002L;

    protected final List<E> _internalList;

    public ListModelAdapter(final List<E> oInternalList)
    {
        _internalList = oInternalList;
    }

    public ListModelAdapter()
    {
        this(new ArrayList<E>());
    }

    public Object getElementAt(int iIndex)
    {
        return _internalList.get(iIndex);
    }

    public int getSize()
    {
        return _internalList.size();
    }

    public boolean add(E oElement)
    {
        add(size(), oElement);
        
        return true;
    }

    public void add(int iIndex, E oElement)
    {
        _internalList.add(iIndex, oElement);

        fireIntervalAdded(this, iIndex, iIndex);
    }

    public boolean addAll(Collection<? extends E> oCollection)
    {
        addAll(size(), oCollection);

        return true;
    }

    public boolean addAll(int iIndex, Collection<? extends E> oCollection)
    {
        boolean bResult = _internalList.addAll(iIndex, oCollection);

        fireIntervalAdded(this, iIndex, iIndex + oCollection.size() - 1);

        return bResult;
    }

    public void clear()
    {
        int iSizeBefore = size();

        _internalList.clear();

        fireIntervalRemoved(this, 0, iSizeBefore - 1);
    }

    public E remove(int iIndex)
    {
        E oResult = _internalList.remove(iIndex);

        fireIntervalRemoved(this, iIndex, iIndex);

        return oResult;
    }

    public boolean remove(Object o)
    {
        int iIndex = indexOf(o);

        if(iIndex < 0)
        {
            return false;
        }

        remove(iIndex);

        return true;
    }

    public E set(int iIndex, E oElement)
    {
        E oResult = _internalList.set(iIndex, oElement);

        fireContentsChanged(this, iIndex, iIndex);

        return oResult;
    }

    public boolean removeAll(Collection<?> oCollection)
    {
        boolean bChanged = false;

        for(Object oElement : oCollection)
        {
            bChanged |= remove(oElement);
        }

        return bChanged;
    }

    public boolean retainAll(Collection<?> oCollection)
    {
        boolean bResult = false;

        for(Iterator<E> iterElements = iterator(); iterElements.hasNext();)
        {
            E oElement = iterElements.next();

            if(!oCollection.contains(oElement))
            {
                bResult = true;
                iterElements.remove();
            }
        }

        return bResult;
    }

    public ListModelAdapter<E> subList(int iFromIndex, int iToIndex)
    {
        return new SubListDecorator<E>(this, iFromIndex, iToIndex);
    }

    public Iterator<E> iterator()
    {
        return listIterator();
    }

    public ListIterator<E> listIterator()
    {
        return new ListIteratorDecorator(_internalList.listIterator());
    }

    public ListIterator<E> listIterator(int iIndex)
    {
        return new ListIteratorDecorator(_internalList.listIterator(iIndex));
    }

    public boolean contains(Object o)
    {
        return _internalList.contains(o);
    }

    public boolean containsAll(Collection<?> c)
    {
        return _internalList.containsAll(c);
    }

    public E get(int index)
    {
        return _internalList.get(index);
    }

    public int indexOf(Object o)
    {
        return _internalList.indexOf(o);
    }

    public boolean isEmpty()
    {
        return _internalList.isEmpty();
    }

    public int lastIndexOf(Object o)
    {
        return _internalList.lastIndexOf(o);
    }

    public int size()
    {
        return _internalList.size();
    }

    public Object[] toArray()
    {
        return _internalList.toArray();
    }

    public <T> T[] toArray(T[] a)
    {
        return _internalList.toArray(a);
    }

    public boolean equals(Object o)
    {
        if(!(o instanceof ListModelAdapter<?>))
        {
            return false;
        }

        ListModelAdapter<?> other = (ListModelAdapter<?>) o;

        return this._internalList.equals(other._internalList);
    }

    public int hashCode()
    {
        int listHashCode = _internalList.hashCode();
        
        return listHashCode + (1 << 15);
    }
    
    protected class ListIteratorDecorator implements ListIterator<E>
    {
        protected final ListIterator<E> _internalIterator;
        protected int _lastIndex;

        public ListIteratorDecorator(ListIterator<E> internalIterator)
        {
            _internalIterator = internalIterator;
        }

        public void add(E element)
        {
            _internalIterator.add(element);

            int index = _internalIterator.previousIndex();
            fireIntervalAdded(ListModelAdapter.this, index, index);
        }

        public void remove()
        {
            _internalIterator.remove();

            int index = _internalIterator.nextIndex();
            fireIntervalRemoved(this, index, index);
        }

        public void set(E element)
        {
            _internalIterator.set(element);

            fireContentsChanged(ListModelAdapter.this, _lastIndex, _lastIndex);
        }

        public E next()
        {
            E result = _internalIterator.next();

            _lastIndex = _internalIterator.previousIndex();

            return result;
        }

        public E previous()
        {
            E result = _internalIterator.previous();

            _lastIndex = _internalIterator.nextIndex();

            return result;
        }

        public boolean hasNext()
        {
            return _internalIterator.hasNext();
        }

        public boolean hasPrevious()
        {
            return _internalIterator.hasPrevious();
        }

        public int nextIndex()
        {
            return _internalIterator.nextIndex();
        }

        public int previousIndex()
        {
            return _internalIterator.previousIndex();
        }
    }

    protected static class SubListDecorator<E> extends ListModelAdapter<E>
    {
        private static final long serialVersionUID = -3760181638551658142L;

        protected final int _offset;
        protected final ListModelAdapter<E> _parent;
        
        protected SubListDecorator(ListModelAdapter<E> parent, int fromIndex, int toIndex)
        {
            super(parent._internalList.subList(fromIndex, toIndex));
            
            _parent = parent;
            _offset = fromIndex;
        }

        protected void fireContentsChanged(Object source, int index0, int index1)
        {
            super.fireContentsChanged(source, index0, index1);
            
            if(source == this && index0 >= 0 && index1 >= 0)
            {
                _parent.fireContentsChanged(_parent, _offset + index0, _offset + index1);
            }
        }

        protected void fireIntervalAdded(Object source, int index0, int index1)
        {
            super.fireIntervalAdded(source, index0, index1);

            if(source == this)
            {
                _parent.fireIntervalAdded(_parent, _offset + index0, _offset + index1);
            }
        }

        protected void fireIntervalRemoved(Object source, int index0, int index1)
        {
            super.fireIntervalRemoved(source, index0, index1);

            if(source == this)
            {
                _parent.fireIntervalRemoved(_parent, _offset + index0, _offset + index1);
            }
        }
    }
}
