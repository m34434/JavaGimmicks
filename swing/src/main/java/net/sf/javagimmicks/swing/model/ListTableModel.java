package net.sf.javagimmicks.swing.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
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

/**
 * A {@link TableModel} implementation that is basically a {@link List} of a
 * given {@link Class row type} and maps (chosen ones from) it's properties to
 * table columns.
 * <p>
 * Instances have to be created via the static {@link #builder(Class)} method.
 * <p>
 * As {@link ListTableModel} takes care about proper firing of
 * {@link TableModelEvent}s changes to row properties must be watched in some
 * way. This is done by proxying row beans upon calls to {@link #get(int)} (or
 * any derived method) which means furhter that the given row type must be an
 * interface.
 * <p>
 * If the row type cannot be an interface or row manipulation via row beans is
 * not necessary, there is also a non-proxy mode which can be enabled via
 * {@link Builder#setProxyReadMode(boolean)}. Though in this case, calls to
 * {@link #get(int)} (or derived methods) will cause an
 * {@link IllegalStateException}.
 * 
 * @param <E>
 *           the {@link Class row type}
 */
public class ListTableModel<E> extends AbstractList<E> implements TableModel
{
   private final Class<E> _rowType;
   private final List<E> _rows;

   private final List<ColumnProperty> _columnProperties;
   private final Map<Method, Integer> _setterIndex;

   private final List<TableModelListener> _listeners;

   private final boolean _proxyReadMode;

   private List<String> _columnNames;

   /**
    * Allows to create {@link ListTableModel} instances via a {@link Builder
    * builder API}.
    * 
    * @param rowType
    *           {@link Class row type} for the {@link ListTableModel} to build
    * @param <E>
    *           the {@link Class row type}
    * @return a {@link Builder} for building a {@link ListTableModel} instance
    */
   public static <E> Builder<E> builder(final Class<E> rowType)
   {
      return new Builder<E>(rowType);
   }

   private ListTableModel(final Class<E> rowType, final List<ColumnProperty> columnProperties, final List<E> rowData,
         final boolean proxyReadMode)
   {
      _rowType = rowType;
      _proxyReadMode = proxyReadMode;

      _columnProperties = columnProperties;

      _rows = rowData;
      _listeners = new LinkedList<TableModelListener>();

      if (_proxyReadMode)
      {
         _setterIndex = builtSetterIndexMap(_columnProperties);
      }
      else
      {
         _setterIndex = null;
      }

      _columnNames = getPropertyNames();
   }

   /**
    * Returns if row beans returned by read operations are proxied to be able to
    * report changes as {@link TableModelEvent}s.
    * 
    * @return if row beans returned by read operations are proxied
    */
   public boolean isProxyReadMode()
   {
      return _proxyReadMode;
   }

   /**
    * Returns the {@link Class row type} of this instance.
    * 
    * @return the {@link Class row type} of this instance
    */
   public Class<E> getRowType()
   {
      return _rowType;
   }

   @Override
   public void add(final int index, final E element)
   {
      _rows.add(index, unwrap(element));
      fireRowAdded(index);
   }

   @Override
   public boolean addAll(final int index, final Collection<? extends E> c)
   {
      if (c.isEmpty())
      {
         return false;
      }

      int addIndex = index;
      for (final E element : c)
      {
         _rows.add(addIndex++, unwrap(element));
      }

      fireRowsAdded(index, addIndex - 1);

      return true;
   }

   @Override
   public void clear()
   {
      final int size = _rows.size();

      _rows.clear();

      fireRowsRemoved(0, size - 1);
   }

   /**
    * Returns the row bean at the given index.
    * 
    * @return the row bean at the given index
    * @throws IllegalStateException
    *            if this instance is in {@link #isProxyReadMode() proxy read
    *            mode}
    */
   @Override
   @SuppressWarnings("unchecked")
   public E get(final int index) throws IllegalStateException
   {
      if (!_proxyReadMode)
      {
         throw new IllegalStateException(
               "Not in proxy read mode! You can try to access row bean from your provided source - but mind that updates there will not cause table events!");
      }

      final RowInvocationHandler invocationHandler = new RowInvocationHandler(_rows.get(index));
      return (E) Proxy.newProxyInstance(_rowType.getClassLoader(), new Class[] { _rowType }, invocationHandler);
   }

   @Override
   public E remove(final int index)
   {
      final E result = _rows.remove(index);

      fireRowRemoved(index);

      return result;
   }

   /**
    * Sets the row bean at the given index unpacking any potential proxy
    * instances created by {@link #get(int)}.
    * 
    * @param index
    *           the row index of the bean to set
    */
   @Override
   public E set(final int index, E element)
   {
      element = unwrap(element);

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

   /**
    * Returns an unmodifiable (capitalized) name-{@link List} of the row type's
    * properties that are matched to columns within this instance. The order of
    * names returned matches that of the columns.
    * 
    * @return the name-{@link List} of column-mapped properties
    */
   public List<String> getPropertyNames()
   {
      return Collections.unmodifiableList(TransformerUtils.decorate(_columnProperties,
            new ColumnPropertyNameTransformer()));
   }

   /**
    * Returns an unmodifiable {@link List} of the names of the columns of this
    * {@link TableModel} (which are the base for {@link #getColumnName(int)}).
    * <p>
    * Note: if not modified via {@link #setColumnNames(List)} the contents of
    * this {@link List} will match {@link #getPropertyNames()}.
    * 
    * @return the {@link List} of all column names
    */
   public List<String> getColmunNames()
   {
      return Collections.unmodifiableList(_columnNames);
   }

   /**
    * Provides a new {@link List} of column names (will be used by
    * {@link #getColumnName(int)}).
    * 
    * @param columnNames
    *           the {@link List} of new column names
    */
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

   @SuppressWarnings("unchecked")
   private E unwrap(E element)
   {
      if (Proxy.isProxyClass(element.getClass()))
      {
         final InvocationHandler handler = Proxy.getInvocationHandler(element);

         if (handler instanceof ListTableModel<?>.RowInvocationHandler)
         {
            element = ((RowInvocationHandler) handler)._row;
         }
      }
      return element;
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

   private static List<ColumnProperty> parseColumns(final Class<?> rowClass, final Collection<String> propertyNames)
   {
      final ArrayList<ColumnProperty> result = new ArrayList<ColumnProperty>(propertyNames.size());
      for (final String propertyName : propertyNames)
      {
         result.add(new ColumnProperty(rowClass, propertyName));
      }

      return result;
   }

   /**
    * A builder implementation for creating {@link ListTableModel} instances.
    * Instances are created by {@link ListTableModel#builder(Class)}.
    * <p>
    * Note that instances of this class can be used repeatedly to build more
    * than one {@link ListTableModel} BUT are not thread-safe!
    * 
    * @param <E>
    *           the {@link Class row type} for the created
    *           {@link ListTableModel}
    */
   public static class Builder<E>
   {
      private final Class<E> _rowClass;
      private final List<ColumnProperty> _columnProperties = new ArrayList<ColumnProperty>();
      private final List<E> _rowData = new ArrayList<E>();

      private boolean _proxyReadMode = true;

      private Builder(final Class<E> rowClass)
      {
         if (rowClass == null)
         {
            throw new IllegalArgumentException("Row type may not be null!");
         }
         _rowClass = rowClass;
      }

      /**
       * Registers the given properties of the internal row type - specified via
       * their capitalized names - as mapped columns.
       * <p>
       * Any property will be denied by an {@link IllegalArgumentException} if
       * it does not meet the following requirements:
       * <ul>
       * <li>At has a public (accessible) getter</li>
       * <li>At has a public (accessible) setter</li>
       * <li>Non of those declare any unchecked {@link Exception} within their
       * signature</li>
       * </ul>
       * <p>
       * If no properties are added to this {@link Builder}, upon a call to
       * {@link #build()} it will make a simple reflective analysis of the row
       * {@link Class} using {@link BeanUtils#extractPropertyNames(Class)} and
       * use all found properties instead. Note that this might cause an
       * {@link IllegalArgumentException} as
       * {@link BeanUtils#extractPropertyNames(Class)} reflects only getters and
       * is not so restrictive (e.g. regarding declared {@link Exception}
       * types).
       * 
       * @param properties
       *           the capitalized property names to register as columns
       * @return the builder itself
       * @throws IllegalArgumentException
       *            if one if the properties does not match the bean
       *            requirements described above
       */
      public Builder<E> addProperties(final Collection<String> properties) throws IllegalArgumentException
      {
         _columnProperties.addAll(parseColumns(_rowClass, properties));

         return this;
      }

      /**
       * Registers the given properties of the internal row type - specified via
       * their capitalized names - as mapped columns.
       * <p>
       * Any property will be denied by an {@link IllegalArgumentException} if
       * it does not meet the following requirements:
       * <ul>
       * <li>At has a public (accessible) getter</li>
       * <li>At has a public (accessible) setter</li>
       * <li>Non of those declare any unchecked {@link Exception} within their
       * signature</li>
       * </ul>
       * <p>
       * If no properties are added to this {@link Builder}, upon a call to
       * {@link #build()} it will make a simple reflective analysis of the row
       * {@link Class} using {@link BeanUtils#extractPropertyNames(Class)} and
       * use all found properties instead. Note that this might cause an
       * {@link IllegalArgumentException} as
       * {@link BeanUtils#extractPropertyNames(Class)} reflects only getters and
       * is not so restrictive (e.g. regarding declared {@link Exception}
       * types).
       * 
       * @param properties
       *           the capitalized property names to register as columns
       * @return the builder itself
       * @throws IllegalArgumentException
       *            if one if the properties does not match the bean
       *            requirements described above
       */
      public Builder<E> addProperties(final String... properties)
      {
         return addProperties(Arrays.asList(properties));
      }

      /**
       * Adds the given row beans to be contained later within the generated
       * {@link ListTableModel}.
       * 
       * @param rows
       *           the row beans to add
       * @return the builder itself
       */
      public Builder<E> addRows(final Collection<E> rows)
      {
         for (final E row : rows)
         {
            if (row != null)
            {
               _rowData.add(row);
            }
         }

         return this;
      }

      /**
       * Adds the given row beans to be contained later within the generated
       * {@link ListTableModel}.
       * 
       * @param rows
       *           the row beans to add
       * @return the builder itself
       */
      public Builder<E> addRows(final E... rows)
      {
         return addRows(Arrays.asList(rows));
      }

      /**
       * Enables or disables the {@link ListTableModel#isProxyReadMode() proxy
       * read mode} within the built {@link ListTableModel}.
       * 
       * @param mode
       *           if the mode is enabled or not
       * @return the builder itself
       * @see ListTableModel#isProxyReadMode()
       */
      public Builder<E> setProxyReadMode(final boolean mode)
      {
         _proxyReadMode = mode;

         return this;
      }

      /**
       * Builds a new {@link ListTableModel} for the configuration done so far
       * on this {@link Builder}.
       * 
       * @return the built {@link ListTableModel}
       * @throws IllegalArgumentException
       *            if
       *            <ul>
       *            <li>{@link #setProxyReadMode(boolean) read proxy mode} is
       *            on, but the given row type is not an interface</li>
       *            <li>no column were registered an automatic lookup via
       *            {@link BeanUtils#extractPropertyNames(Class)} returns some
       *            non-valid properties
       *            </uL>
       */
      public ListTableModel<E> build() throws IllegalArgumentException
      {
         if (_columnProperties.isEmpty())
         {
            _columnProperties.addAll(parseColumns(_rowClass, BeanUtils.extractPropertyNames(_rowClass)));
         }

         if (_proxyReadMode && !_rowClass.isInterface())
         {
            throw new IllegalArgumentException("Row type must be an interface if proxy read mode is enabled!");
         }

         return new ListTableModel<E>(_rowClass, new ArrayList<ColumnProperty>(_columnProperties), new ArrayList<E>(
               _rowData), _proxyReadMode);
      }
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
            Method getter = null;
            try
            {
               getter = rowClass.getMethod("get" + propertyName);
            }
            catch (final NoSuchMethodException ex)
            {
               getter = rowClass.getMethod("is" + propertyName);
            }

            _getter = getter;
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

         if (!Void.TYPE.equals(_setter.getReturnType()) && !Void.class.equals(_setter.getReturnType()))
         {
            throw new IllegalArgumentException(String.format(
                  "Setter for property '%2$s' in row class '%1$s' has a non-void return type", rowClass, propertyName));
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
         if (!rowClass.isInterface() && Modifier.isPublic(method.getModifiers()))
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
