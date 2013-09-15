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

public class SortedTableUtils
{
   protected static class SortTableSuite<E>
   {
      protected final List<E> _rowData;
      protected final HeaderSortMouseListener _headerSortMouseListener;
      protected final ResortTableModelListener _resortTableModelListener;

      protected BeanPropertyComparator<E> _comparator;
      protected ListTableModel<E> _tableModel;

      protected List<E> _rowDataInterface;
      
      protected SortTableSuite(Class<E> rowType, List<E> rowData)
      {
         _rowData = rowData;
         _headerSortMouseListener = new HeaderSortMouseListener(this);

         _comparator = new BeanPropertyComparator<E>();

         _tableModel = new ListTableModel<E>(_rowData, rowType);
         _rowDataInterface = SortedListUtils.decorate(_tableModel, _comparator);
         
         _resortTableModelListener = new ResortTableModelListener(this);
         _tableModel.addTableModelListener(_resortTableModelListener);
      }

      protected List<E> applyTo(JTable table)
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

      protected ResortTableModelListener(SortTableSuite<?> sortTableSuite)
      {
         _sortTableSuite = sortTableSuite;
      }

      protected void setEnabled(boolean enabled)
      {
         _enabled = enabled;
      }

      public void tableChanged(TableModelEvent e)
      {
         if(!_enabled)
         {
            return;
         }
         
         int column = e.getColumn();
         List<String> tablePropertyNames = _sortTableSuite._tableModel.getPropertyNames();
         List<String> sortedProperties = _sortTableSuite._comparator.getBeanPropertyNames();
         
         if(column == TableModelEvent.ALL_COLUMNS && !sortedProperties.isEmpty())
         {
            _sortTableSuite.resort();
         }
         else
         {
            String propertyName = tablePropertyNames.get(column);
            
            if(sortedProperties.contains(propertyName))
            {
               _sortTableSuite.resort();
            }
         }
      }
   }

   protected static class HeaderSortMouseListener extends MouseAdapter
   {
      protected final SortTableSuite<?> _sortTableSuite;

      protected HeaderSortMouseListener(SortTableSuite<?> sortTableSuite)
      {
         _sortTableSuite = sortTableSuite;
      }

      @Override
      public void mouseClicked(MouseEvent e)
      {
         JTableHeader tableHeader = (JTableHeader) e.getSource();

         TableColumnModel columnModel = tableHeader.getColumnModel();
         int viewColumn = columnModel.getColumnIndexAtX(e.getX());
         int column = columnModel.getColumn(viewColumn).getModelIndex();

         if (column == -1)
         {
            return;
         }

         String propertyName = _sortTableSuite._tableModel.getPropertyNames().get(column);
         SortOrder sortOrder = _sortTableSuite._comparator.getSortOrder(propertyName);

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
