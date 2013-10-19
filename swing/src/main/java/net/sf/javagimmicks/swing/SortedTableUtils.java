package net.sf.javagimmicks.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import net.sf.javagimmicks.beans.BeanPropertyComparator;
import net.sf.javagimmicks.beans.BeanPropertyComparator.SortOrder;
import net.sf.javagimmicks.collections.SortedListUtils;
import net.sf.javagimmicks.swing.model.ListTableModel;

/*
 * TODO: Not yet finished - next task: show sort order in table header
 */
class SortedTableUtils
{
   protected static class SortTableSuite<E>
   {
      protected final List<E> _rowData;
      protected final HeaderSortMouseListener _headerSortMouseListener;
      protected final ResortTableModelListener _resortTableModelListener;

      protected BeanPropertyComparator<E> _comparator;
      protected ListTableModel<E> _tableModel;

      protected List<E> _rowDataInterface;

      protected SortTableSuite(final Class<E> rowType, final List<E> rowData)
      {
         _rowData = rowData;
         _headerSortMouseListener = new HeaderSortMouseListener(this);

         _comparator = new BeanPropertyComparator<E>();

         _tableModel = ListTableModel.build(rowType).addRows(_rowData).build();
         _rowDataInterface = SortedListUtils.decorate(_tableModel, _comparator);

         _resortTableModelListener = new ResortTableModelListener(this);
         _tableModel.addTableModelListener(_resortTableModelListener);
      }

      protected List<E> applyTo(final JTable table)
      {
         table.setModel(_tableModel);
         table.getTableHeader().addMouseListener(_headerSortMouseListener);

         return _rowDataInterface;
      }

      protected void resort()
      {
         _resortTableModelListener.setEnabled(false);

         SortedListUtils.resort(_rowDataInterface);

         _resortTableModelListener.setEnabled(true);
      }
   }

   protected static class ResortTableModelListener implements TableModelListener
   {
      protected final SortTableSuite<?> _sortTableSuite;
      protected boolean _enabled = true;

      protected ResortTableModelListener(final SortTableSuite<?> sortTableSuite)
      {
         _sortTableSuite = sortTableSuite;
      }

      protected void setEnabled(final boolean enabled)
      {
         _enabled = enabled;
      }

      @Override
      public void tableChanged(final TableModelEvent e)
      {
         if (!_enabled)
         {
            return;
         }

         final int column = e.getColumn();
         final List<String> tablePropertyNames = _sortTableSuite._tableModel.getPropertyNames();
         final List<String> sortedProperties = _sortTableSuite._comparator.getBeanPropertyNames();

         if (column == TableModelEvent.ALL_COLUMNS && !sortedProperties.isEmpty())
         {
            _sortTableSuite.resort();
         }
         else
         {
            final String propertyName = tablePropertyNames.get(column);

            if (sortedProperties.contains(propertyName))
            {
               _sortTableSuite.resort();
            }
         }
      }
   }

   protected static class HeaderSortMouseListener extends MouseAdapter
   {
      protected final SortTableSuite<?> _sortTableSuite;

      protected HeaderSortMouseListener(final SortTableSuite<?> sortTableSuite)
      {
         _sortTableSuite = sortTableSuite;
      }

      @Override
      public void mouseClicked(final MouseEvent e)
      {
         final JTableHeader tableHeader = (JTableHeader) e.getSource();

         final TableColumnModel columnModel = tableHeader.getColumnModel();
         final int viewColumn = columnModel.getColumnIndexAtX(e.getX());
         final int column = columnModel.getColumn(viewColumn).getModelIndex();

         if (column == -1)
         {
            return;
         }

         final String propertyName = _sortTableSuite._tableModel.getPropertyNames().get(column);
         final SortOrder sortOrder = _sortTableSuite._comparator.getSortOrder(propertyName);

         switch (sortOrder)
         {
            case ASCENDING:
               _sortTableSuite._comparator.setSortOrder(propertyName, SortOrder.DESCENDING);
               break;
            case DESCENDING:
               _sortTableSuite._comparator.setSortOrder(propertyName, SortOrder.NONE);
               break;
            case NONE:
               _sortTableSuite._comparator.setSortOrder(propertyName, SortOrder.ASCENDING);
               break;
         }

         _sortTableSuite.resort();
      }
   }
}
