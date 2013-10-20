package net.sf.javagimmicks.swing.model;

import static net.sf.javagimmicks.testing.JUnitListAssert.assertListEquals;
import static org.easymock.EasyMock.and;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.notNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.junit.Test;

public class ListTableModelTest
{
   private final RowClass ROW_A = new RowClass("a", 1);
   private final RowClass ROW_B = new RowClass("b", 2);
   private final RowClass ROW_C = new RowClass("c", 3);
   private final RowClass ROW_D = new RowClass("d", 4);

   @Test
   public void testBasicReadOperationsStuff()
   {
      final ListTableModel<RowIF> m = ListTableModel.builder(RowIF.class).addProperties("A", "B")
            .addRows(ROW_A, ROW_B).build();

      assertEquals(2, m.size());

      assertEquals(2, m.getColumnCount());
      assertListEquals(m.getColmunNames(), "A", "B");
      assertEquals("A", m.getColumnName(0));
      assertEquals("B", m.getColumnName(1));
      assertEquals(String.class, m.getColumnClass(0));
      assertEquals(Integer.class, m.getColumnClass(1));

      assertEquals(2, m.getRowCount());
      assertEquals(RowIF.class, m.getRowType());

      assertEquals("a", m.getValueAt(0, 0));
      assertEquals(1, m.getValueAt(0, 1));
      assertEquals("b", m.getValueAt(1, 0));
      assertEquals(2, m.getValueAt(1, 1));
   }

   @Test
   public void testWriteOperations()
   {
      final ListTableModel<RowIF> m = ListTableModel.builder(RowIF.class).addProperties("A", "B")
            .addRows(ROW_A, ROW_B).build();

      // Create and record a Mock for a TableModelListener
      final TableModelListener l = createMock(TableModelListener.class);
      m.addTableModelListener(l);
      final Capture<TableModelEvent> c = new Capture<TableModelEvent>(CaptureType.LAST);

      // Replace an existing row
      l.tableChanged(and(capture(c), notNull(TableModelEvent.class)));
      replay(l);

      m.set(1, ROW_C);

      assertEquals(2, m.size());
      assertEquals("a", m.get(0).getA());
      assertEquals("c", m.get(1).getA());

      verify(l);
      verifyTableModelEvent(c.getValue(), 1, 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
      reset(l);

      // Remove a row
      l.tableChanged(and(capture(c), notNull(TableModelEvent.class)));
      replay(l);

      assertSame(ROW_C, m.remove(1));

      assertEquals(1, m.size());
      assertEquals("a", m.get(0).getA());

      verify(l);
      verifyTableModelEvent(c.getValue(), 1, 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
      reset(l);

      // Add a single row at the end
      l.tableChanged(and(capture(c), notNull(TableModelEvent.class)));
      replay(l);

      m.add(ROW_C);

      assertEquals(2, m.size());
      assertEquals("a", m.get(0).getA());
      assertEquals("c", m.get(1).getA());

      verify(l);
      verifyTableModelEvent(c.getValue(), 1, 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
      reset(l);

      // Add multiple rows in the middle
      l.tableChanged(and(capture(c), notNull(TableModelEvent.class)));
      replay(l);

      m.addAll(1, Arrays.asList(ROW_B, ROW_D));

      assertEquals(4, m.size());
      assertEquals("a", m.get(0).getA());
      assertEquals("b", m.get(1).getA());
      assertEquals("d", m.get(2).getA());
      assertEquals("c", m.get(3).getA());

      verify(l);
      verifyTableModelEvent(c.getValue(), 1, 2, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
      reset(l);

      // Clear
      l.tableChanged(and(capture(c), notNull(TableModelEvent.class)));
      replay(l);

      m.clear();

      assertEquals(0, m.size());

      verify(l);
      verifyTableModelEvent(c.getValue(), 0, 3, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
      reset(l);
   }

   @Test
   public void testProxyReadView()
   {
      final RowClass row0Raw = ROW_A;
      final RowClass row1Raw = ROW_B;

      final ListTableModel<RowIF> m = ListTableModel.builder(RowIF.class).addProperties("A", "B")
            .addRows(row0Raw, row1Raw).build();

      final RowIF row0 = m.get(0);
      final RowIF row1 = m.get(1);

      assertTrue(row0 instanceof RowIF);
      assertTrue(row1 instanceof RowIF);

      assertFalse(row0 instanceof RowClass);
      assertFalse(row1 instanceof RowClass);

      assertTrue(Proxy.isProxyClass(row0.getClass()));
      assertTrue(Proxy.isProxyClass(row1.getClass()));

      // Create and record a Mock for a TableModelListener
      final TableModelListener l = createMock(TableModelListener.class);
      m.addTableModelListener(l);
      final Capture<TableModelEvent> c = new Capture<TableModelEvent>(CaptureType.ALL);
      l.tableChanged(and(capture(c), notNull(TableModelEvent.class)));
      l.tableChanged(and(capture(c), notNull(TableModelEvent.class)));
      l.tableChanged(and(capture(c), notNull(TableModelEvent.class)));
      l.tableChanged(and(capture(c), notNull(TableModelEvent.class)));
      replay(l);

      // Do some changes on the row beans
      row0.setA("aa");
      row0.setB(11);
      row1.setA("bb");
      row1.setB(22);

      // Verify changes on the original beans
      assertEquals("aa", row0Raw.getA());
      assertEquals(11, row0Raw.getB());
      assertEquals("bb", row1Raw.getA());
      assertEquals(22, row1Raw.getB());

      // Verify changes on the table model
      assertEquals("aa", m.getValueAt(0, 0));
      assertEquals(11, m.getValueAt(0, 1));
      assertEquals("bb", m.getValueAt(1, 0));
      assertEquals(22, m.getValueAt(1, 1));

      // Verify calls to the mocked TableModelListener
      verify(l);
      assertEquals(4, c.getValues().size());
      verifyTableModelEvent(c.getValues().get(0), 0, 0, 0, TableModelEvent.UPDATE);
      verifyTableModelEvent(c.getValues().get(1), 0, 0, 1, TableModelEvent.UPDATE);
      verifyTableModelEvent(c.getValues().get(2), 1, 1, 0, TableModelEvent.UPDATE);
      verifyTableModelEvent(c.getValues().get(3), 1, 1, 1, TableModelEvent.UPDATE);
   }

   @Test
   public void testExceptionCases()
   {
      // Provided a non-interface without disabling read proxy mode
      try
      {
         ListTableModel.builder(RowClass.class).addProperties("A", "B").build();
         fail("IllegalArgumentException expected!");
      }
      catch (final IllegalArgumentException ex)
      {
      }

      // Setter has different param-type than return-type of getter
      try
      {
         ListTableModel.builder(RowIF.class).addProperties("WrongSetterParamType").build();
         fail("IllegalArgumentException expected!");
      }
      catch (final IllegalArgumentException ex)
      {
      }

      // Setter has a non-void return-type
      try
      {
         ListTableModel.builder(RowIF.class).addProperties("WrongSetterReturnType").build();
         fail("IllegalArgumentException expected!");
      }
      catch (final IllegalArgumentException ex)
      {
      }

      // Getter declares unchecked Exceptions
      try
      {
         ListTableModel.builder(RowIF.class).addProperties("GetterWithException").build();
         fail("IllegalArgumentException expected!");
      }
      catch (final IllegalArgumentException ex)
      {
      }

      // Getter declares unchecked Exceptions
      try
      {
         ListTableModel.builder(RowIF.class).addProperties("SetterWithException").build();
         fail("IllegalArgumentException expected!");
      }
      catch (final IllegalArgumentException ex)
      {
      }
   }

   private static void verifyTableModelEvent(final TableModelEvent event, final int firstRow, final int lastRow,
         final int column, final int type)
   {
      assertEquals("Unexpected first row", firstRow, event.getFirstRow());
      assertEquals("Unexpected last row", lastRow, event.getLastRow());
      assertEquals("Unexpected column", column, event.getColumn());
      assertEquals("Unexpected type", type, event.getType());
   }

   public static interface RowIF
   {
      String getA();

      void setA(String a);

      int getB();

      void setB(int b);

      int getWrongSetterParamType();

      void setWrongSetterParamType(String xxx);

      int getWrongSetterReturnType();

      String setWrongSetterReturnType(int xxx);

      int getGetterWithException() throws SQLException;

      void setGetterWithException(int value);

      int getSetterWithException();

      void setSetterWithException(int xxx) throws SQLException;
   }

   public static class RowClass implements RowIF
   {
      private String _a;
      private int _b;

      public RowClass()
      {}

      public RowClass(final String a, final int b)
      {
         _a = a;
         _b = b;
      }

      @Override
      public String getA()
      {
         return _a;
      }

      @Override
      public void setA(final String a)
      {
         _a = a;
      }

      @Override
      public int getB()
      {
         return _b;
      }

      @Override
      public void setB(final int b)
      {
         _b = b;
      }

      @Override
      public int getWrongSetterParamType()
      {
         return 0;
      }

      @Override
      public void setWrongSetterParamType(final String xxx)
      {}

      @Override
      public int getWrongSetterReturnType()
      {
         return 0;
      }

      @Override
      public String setWrongSetterReturnType(final int xxx)
      {
         return null;
      }

      @Override
      public int getGetterWithException() throws SQLException
      {
         return 0;
      }

      @Override
      public void setGetterWithException(final int value)
      {}

      @Override
      public int getSetterWithException()
      {
         return 0;
      }

      @Override
      public void setSetterWithException(final int xxx) throws SQLException
      {}
   }
}
