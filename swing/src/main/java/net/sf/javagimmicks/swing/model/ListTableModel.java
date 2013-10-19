package net.sf.javagimmicks.swing.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
import net.sf.javagimmicks.collections.transformer.TransformerUtils;
import net.sf.javagimmicks.transform.Transformer;

public class ListTableModel<E> extends AbstractList<E> implements TableModel
{
   private final Class<E> _rowType;
   private final List<E> _rows;

   private final List<ColumnProperty> _columnProperties;

   private final Map<Method, Integer> _setterIndex;
   private List<String> _columnNames;

   private final List<TableModelListener> _listeners;

   public ListTableModel(final Class<E> rowType)
   {
      this(null, rowType, null);
   }

   public ListTableModel(final Class<E> rowType, final List<String> propertyOrder)
   {
      this(null, rowType, propertyOrder);
   }

   public ListTableModel(final List<E> rowdata, final Class<E> rowType)
   {
      this(rowdata, rowType, null);
   }

   public ListTableModel(final List<E> rowdata, final Class<E> rowType, List<String> propertyOrder)
   {
      if (propertyOrder == null || propertyOrder.isEmpty())
      {
         propertyOrder = BeanUtils.extractPropertyNames(rowType);
      }

      if (rowType == null || !rowType.isInterface())
      {
         throw new IllegalArgumentException("Row type must be an interface!");
      }

      _rowType = rowType;

      _columnProperties = parseColumns(_rowType, propertyOrder);

      _rows = (rowdata != null) ? rowdata : new ArrayList<E>();
      _listeners = new LinkedList<TableModelListener>();

      _setterIndex = builtSetterIndexMap(_columnProperties);

      _columnNames = getPropertyNames();
   }

   public Class<E> getRowType()
   {
      return _rowType;
   }

   @Override
   public void add(final int index, final E element)
   {
      _rows.add(index, element);
      fireRowAdded(index);
   }

   @Override
   public boolean addAll(final int index, final Collection<? extends E> c)
   {
      final boolean result = _rows.addAll(index, c);

      fireRowsAdded(index, index + c.size() - 1);

      return result;
   }

   @Override
   public void clear()
   {
      final int size = _rows.size();

      _rows.clear();

      fireRowsRemoved(0, size - 1);
   }

   @Override
   @SuppressWarnings("unchecked")
   public E get(final int index)
   {
      final RowInvocationHandler invocationHandler = new RowInvocationHandler(_rows.get(index));
      return (E) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { _rowType }, invocationHandler);
   }

   @Override
   public E remove(final int index)
   {
      final E result = _rows.remove(index);

      fireRowRemoved(index);

      return result;
   }

   @Override
   @SuppressWarnings("unchecked")
   public E set(final int index, E element)
   {
      if (Proxy.isProxyClass(element.getClass()))
      {
         final InvocationHandler handler = Proxy.getInvocationHandler(element);

         if (handler instanceof ListTableModel<?>.RowInvocationHandler)
         {
            element = ((RowInvocationHandler) handler)._row;
         }
      }

      final E result = _rows.set(index, element);

      fireRowChanged(index);

      return result;
   }

   @Override
   public int size()
   {
      return _rows.size();
   }

   @Override
   public boolean isCellEditable(final int rowIndex, final int columnIndex)
   {
      return true;
   }

   @Override
   public int getColumnCount()
   {
      return _columnProperties.size();
   }

   @Override
   public int getRowCount()
   {
      return _rows.size();
   }

   @Override
   public Object getValueAt(final int rowIndex, final int columnIndex)
   {
      return _columnProperties.get(columnIndex).invokeGetter(_rows.get(rowIndex));
   }

   @Override
   public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
   {
      _columnProperties.get(columnIndex).invokeSetter(_rows.get(rowIndex), aValue);
      fireCellChanged(rowIndex, columnIndex);
   }

   @Override
   public Class<?> getColumnClass(final int columnIndex)
   {
      return BeanUtils.getWrapperType(_columnProperties.get(columnIndex).getType());
   }

   @Override
   public String getColumnName(final int columnIndex)
   {
      return _columnNames.isEmpty() ? null : _columnNames.get(columnIndex);
   }

   @Override
   public void addTableModelListener(final TableModelListener l)
   {
      _listeners.add(l);
   }

   @Override
   public void removeTableModelListener(final TableModelListener l)
   {
      _listeners.remove(l);
   }

   public List<String> getPropertyNames()
   {
      return Collections.unmodifiableList(TransformerUtils.decorate(_columnProperties,
            new ColumnPropertyNameTransformer()));
   }

   public List<String> getColmunNames()
   {
      return Collections.unmodifiableList(_columnNames);
   }

   @SuppressWarnings("unchecked")
   public void setColumnNames(final List<String> columnNames)
   {
      if (columnNames == null || columnNames.isEmpty())
      {
         _columnNames = Collections.EMPTY_LIST;
      }
      else if (columnNames.size() != _columnProperties.size())
      {
         throw new IllegalArgumentException("Wrong number of column names! Expected: " + _columnProperties.size());
      }
      else
      {
         _columnNames = new ArrayList<String>(columnNames);
      }
   }

   protected void fireRowsAdded(final int fromIndex, final int toIndex)
   {
      fireTableModelEvent(new TableModelEvent(this, fromIndex, toIndex,
            TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
   }

   protected void fireRowAdded(final int rowIndex)
   {
      fireRowsAdded(rowIndex, rowIndex);
   }

   protected void fireRowsRemoved(final int fromIndex, final int toIndex)
   {
      fireTableModelEvent(new TableModelEvent(this, fromIndex, toIndex,
            TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
   }

   protected void fireRowRemoved(final int rowIndex)
   {
      fireTableModelEvent(new TableModelEvent(this, rowIndex, rowIndex,
            TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
   }

   protected void fireCellChanged(final int rowIndex, final int columnIndex)
   {
      fireTableModelEvent(new TableModelEvent(this, rowIndex, rowIndex,
            columnIndex, TableModelEvent.UPDATE));
   }

   protected void fireRowChanged(final int rowIndex)
   {
      fireTableModelEvent(new TableModelEvent(this, rowIndex, rowIndex,
            TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
   }

   protected void fireTableModelEvent(final TableModelEvent event)
   {
      for (final TableModelListener l : _listeners)
      {
         l.tableChanged(event);
      }
   }

   private static Map<Method, Integer> builtSetterIndexMap(
         final List<ColumnProperty> indexToProperty)
   {
      final Map<Method, Integer> result = new HashMap<Method, Integer>();

      for (final ListIterator<ColumnProperty> iterator = indexToProperty.listIterator(); iterator.hasNext();)
      {
         final Method setter = iterator.next().getSetter();
         result.put(setter, iterator.previousIndex());
      }

      return result;
   }

   private static List<ColumnProperty> parseColumns(final Class<?> rowClass, final List<String> propertyNames)
   {
      if (propertyNames == null || propertyNames.isEmpty())
      {
         throw new IllegalArgumentException("No property order specified!");
      }

      final ArrayList<ColumnProperty> result = new ArrayList<ListTableModel.ColumnProperty>(propertyNames.size());
      for (final String propertyName : propertyNames)
      {
         result.add(new ColumnProperty(rowClass, propertyName));
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

      @Override
      public Object invoke(final Object proxy, Method method, final Object[] args)
            throws Throwable
      {
         final Integer columnIndex = _setterIndex.get(method);

         // Replace the originally called interface method with it's
         // implementation of the concrete row object
         // TODO: check if this is necessary
         method = _row.getClass().getMethod(method.getName(), method.getParameterTypes());

         // invoke the method on the concrete row object
         final Object result = method.invoke(_row, args);

         // If we had just called a setter, we fire a respective event
         if (columnIndex != null)
         {
            final int rowIndex = _rows.indexOf(_row);
            if (rowIndex >= 0)
            {
               fireCellChanged(rowIndex, columnIndex);
            }
         }

         return result;
      }
   }

   private static class ColumnProperty
   {
      private final String _propertyName;

      private final Class<?> _type;

      private final Method _getter;
      private final Method _setter;

      public ColumnProperty(final Class<?> rowClass, final String propertyName) throws IllegalArgumentException
      {
         _propertyName = propertyName;
         try
         {
            _getter = rowClass.getMethod("get" + propertyName);
         }
         catch (final Exception ex)
         {
            throw new IllegalArgumentException(String.format(
                  "Could not find or access getter for property '%2$s' in row class '%1$s'", rowClass, propertyName),
                  ex);
         }

         checkMethod(rowClass, _getter);

         _type = _getter.getReturnType();

         try
         {
            _setter = rowClass.getMethod("set" + propertyName, _type);
         }
         catch (final Exception ex)
         {
            throw new IllegalArgumentException(String.format(
                  "Could not find or access setter for property '%2$s' in row class '%1$s'", rowClass, propertyName),
                  ex);
         }

         checkMethod(rowClass, _setter);
      }

      public String getPropertyName()
      {
         return _propertyName;
      }

      public Class<?> getType()
      {
         return _type;
      }

      public Method getSetter()
      {
         return _setter;
      }

      public Object invokeGetter(final Object target)
      {
         try
         {
            return _getter.invoke(target);
         }
         catch (final InvocationTargetException e)
         {
            final Throwable t = e.getTargetException();
            if (t instanceof RuntimeException)
            {
               throw (RuntimeException) t;
            }
            else if (t instanceof Error)
            {
               throw (Error) t;
            }

            // Cannot occur - was checked before
            else
            {
               return null;
            }
         }

         // Cannot occur - was checked before
         catch (final IllegalAccessException e)
         {
            return null;
         }
      }

      public Object invokeSetter(final Object target, final Object value)
      {
         try
         {
            return _setter.invoke(target, value);
         }
         catch (final InvocationTargetException e)
         {
            final Throwable t = e.getTargetException();
            if (t instanceof RuntimeException)
            {
               throw (RuntimeException) t;
            }
            else if (t instanceof Error)
            {
               throw (Error) t;
            }

            // Cannot occur - was checked before
            else
            {
               return null;
            }
         }

         // Cannot occur - was checked before
         catch (final IllegalAccessException e)
         {
            return null;
         }
      }

      private void checkMethod(final Class<?> rowClass, final Method method)
      {
         if (Modifier.isPublic(method.getModifiers()))
         {
            throw new IllegalArgumentException(String.format("Method '%1$s' is not public!", method));
         }

         if (!method.isAccessible())
         {
            method.setAccessible(true);
         }

         for (final Class<?> clazz : method.getExceptionTypes())
         {
            if (!RuntimeException.class.isAssignableFrom(clazz) && !Error.class.isAssignableFrom(clazz))
            {
               throw new IllegalArgumentException(String.format(
                     "Getter for property '%2$s' in row class '%1$s' declares unallowed exception type '%3$s'",
                     rowClass,
                     _propertyName, clazz));
            }
         }
      }
   }

   private static class ColumnPropertyNameTransformer implements Transformer<ColumnProperty, String>
   {
      @Override
      public String transform(final ColumnProperty source)
      {
         return source.getPropertyName();
      }
   }
}
