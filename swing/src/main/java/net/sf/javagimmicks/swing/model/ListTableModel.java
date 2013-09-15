package net.sf.javagimmicks.swing.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import net.sf.javagimmicks.beans.BeanUtils;

public class ListTableModel<E> extends AbstractList<E> implements TableModel
{
    private final Class<E> _rowType;
    private final List<Class<?>> _columnTypes;
    private final List<E> _rows;

    private final List<String> _indexToProperty;
    private final Map<String, Integer> _propertyToIndex;

    private List<String> _columnNames;

    private final List<TableModelListener> _listeners;

    public ListTableModel(Class<E> rowType)
    {
        this(null, rowType, null);
    }

    public ListTableModel(Class<E> rowType, List<String> indexToProperty)
    {
        this(null, rowType, indexToProperty);
    }

    public ListTableModel(List<E> rowdata, Class<E> rowType)
    {
        this(rowdata, rowType, null);
    }

    public ListTableModel(List<E> rowdata, Class<E> rowType, List<String> indexToProperty)
    {
        if(indexToProperty == null || indexToProperty.isEmpty())
        {
            indexToProperty = BeanUtils.extractPropertyNames(rowType);
        }
        
        if(rowType == null || !rowType.isInterface())
        {
            throw new IllegalArgumentException("Row type must be an interface!");
        }

        _rowType = rowType;
        _indexToProperty = indexToProperty;
        _rows = (rowdata != null) ? rowdata : new ArrayList<E>();
        _listeners = new LinkedList<TableModelListener>();

        _columnTypes = new ArrayList<Class<?>>(_indexToProperty.size());
        for(int i = 0; i < _indexToProperty.size(); ++i)
        {
            try
            {
                _columnTypes.add(getGetter(_rowType, i).getReturnType());
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("No get-method for property '" +
                    _indexToProperty.get(i) + "' or method not accessible!");
            }
        }

        _propertyToIndex = getPropertyToIndexMap(_indexToProperty);
        
        _columnNames = getPropertyNames();
    }
    
    public Class<E> getRowType()
    {
    	return _rowType;
    }
    
    public void add(int index, E element)
    {
        _rows.add(index, element);
        fireRowAdded(index);
    }

    public boolean addAll(int index, Collection<? extends E> c)
    {
        boolean result = _rows.addAll(index, c);

        fireRowsAdded(index, index + c.size() - 1);

        return result;
    }
    
	public void clear()
	{
		int size = _rows.size();
		
		_rows.clear();
		
		fireRowsRemoved(0, size - 1);
	}

    @SuppressWarnings("unchecked")
    public E get(int index)
    {
        RowInvocationHandler invocationHandler = new RowInvocationHandler(_rows.get(index));
        return (E) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {_rowType}, invocationHandler);
    }

    public E remove(int index)
    {
        E result = _rows.remove(index);

        fireRowRemoved(index);

        return result;
    }

    @SuppressWarnings("unchecked")
    public E set(int index, E element)
    {
        if(Proxy.isProxyClass(element.getClass()))
        {
           InvocationHandler handler = Proxy.getInvocationHandler(element);
           
           if(handler instanceof ListTableModel<?>.RowInvocationHandler)
           {
              element = ((RowInvocationHandler)handler)._row;
           }
        }
       
        E result = _rows.set(index, element);

        fireRowChanged(index);

        return result;
    }

    public int size()
    {
        return _rows.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    public int getColumnCount()
    {
        return _indexToProperty.size();
    }

    public int getRowCount()
    {
        return _rows.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        try
        {
            E item = _rows.get(rowIndex);
            return getGetter(item.getClass(), columnIndex).invoke(item, new Object[0]);
        }
        catch (Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            else
            {
                throw new Error("Unexpected exception while invoking internal get method!", e);
            }
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        try
        {
            E item = _rows.get(rowIndex);
            getSetter(item.getClass(), columnIndex).invoke(item, aValue);
        }
        catch (Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            else
            {
                throw new Error("Unexpected exception while invoking internal set method!", e);
            }
        }
        fireCellChanged(rowIndex, columnIndex);
    }

    public Class<?> getColumnClass(int columnIndex)
    {
        return BeanUtils.getWrapperType(_columnTypes.get(columnIndex));
    }

    public String getColumnName(int columnIndex)
    {
        return _columnNames.isEmpty() ? null : _columnNames.get(columnIndex);
    }

    public void addTableModelListener(TableModelListener l)
    {
        _listeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        _listeners.remove(l);
    }

    public List<String> getPropertyNames()
    {
        return Collections.unmodifiableList(_indexToProperty);
    }

    public List<String> getColmunNames()
    {
    	return Collections.unmodifiableList(_columnNames);
    }
    
    @SuppressWarnings("unchecked")
    public void setColumnNames(List<String> columnNames)
    {
        if(columnNames == null || columnNames.isEmpty())
        {
            _columnNames = Collections.EMPTY_LIST;
        }
        else if(columnNames.size() != _indexToProperty.size())
        {
            throw new IllegalArgumentException("Wrong number of column names! Expected: " + _indexToProperty.size());
        }
        else
        {
            _columnNames = new ArrayList<String>(columnNames);
        }
    }

    protected void fireRowsAdded(int fromIndex, int toIndex)
    {
        fireTableModelEvent(new TableModelEvent(this, fromIndex, toIndex,
            TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    protected void fireRowAdded(int rowIndex)
    {
        fireRowsAdded(rowIndex, rowIndex);
    }

    protected void fireRowsRemoved(int fromIndex, int toIndex)
    {
        fireTableModelEvent(new TableModelEvent(this, fromIndex, toIndex,
            TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    }

    protected void fireRowRemoved(int rowIndex)
    {
        fireTableModelEvent(new TableModelEvent(this, rowIndex, rowIndex,
            TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    }

    protected void fireCellChanged(int rowIndex, int columnIndex)
    {
        fireTableModelEvent(new TableModelEvent(this, rowIndex, rowIndex,
            columnIndex, TableModelEvent.UPDATE));
    }

    protected void fireRowChanged(int rowIndex)
    {
        fireTableModelEvent(new TableModelEvent(this, rowIndex, rowIndex,
            TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
    }

    protected void fireTableModelEvent(TableModelEvent event)
    {
        for (TableModelListener l : _listeners)
        {
            l.tableChanged(event);
        }
    }

    private Method getGetter(Class<?> itemClass, int columnIndex) throws SecurityException,
            NoSuchMethodException
    {
        String methodName = "get" + _indexToProperty.get(columnIndex);
        return itemClass.getMethod(methodName, new Class[0]);
    }

    private Method getSetter(Class<?> itemClass, int columnIndex) throws SecurityException,
            NoSuchMethodException
    {
        String methodName = "set" + _indexToProperty.get(columnIndex);
        return itemClass.getMethod(methodName, _columnTypes.get(columnIndex));
    }

    private static Map<String, Integer> getPropertyToIndexMap(
            List<String> indexToProperty)
    {
        Map<String, Integer> result = new HashMap<String, Integer>();

        for (ListIterator<String> iterator = indexToProperty.listIterator(); iterator.hasNext();)
        {
            result.put(iterator.next(), iterator.previousIndex());
        }

        return result;
    }


    private class RowInvocationHandler implements InvocationHandler
    {
        private final E _row;

        public RowInvocationHandler(final E row)
        {
            _row = row;
        }

        public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable
        {
            method = _row.getClass().getMethod(method.getName(), method.getParameterTypes());
           
            Object result = method.invoke(_row, args);

            String methodName = method.getName();
            if (methodName.startsWith("set") && args.length == 1)
            {
                int columnIndex = getPropertyIndex(methodName.substring(3));
                if (columnIndex >= 0
                    && args[0].getClass().equals(_columnTypes.get(columnIndex)))
                {
                    int rowIndex = _rows.indexOf(_row);
                    if (rowIndex >= 0)
                    {
                        fireCellChanged(rowIndex, columnIndex);
                    }
                }
            }

            return result;
        }

        private int getPropertyIndex(String propertyName)
        {
            Integer result = _propertyToIndex.get(propertyName);

            return result != null ? result : -1;
        }
    }
}
