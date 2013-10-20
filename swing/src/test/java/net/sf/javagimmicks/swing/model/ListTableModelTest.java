package net.sf.javagimmicks.swing.model;

import static net.sf.javagimmicks.testing.JUnitListAssert.assertListEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.SQLException;

import org.junit.Test;

public class ListTableModelTest
{
   @Test
   public void testBasicReadOperationsStuff()
   {
      final ListTableModel<RowIF> m = ListTableModel.builder(RowIF.class).addProperties("A", "B")
            .addRows(new RowClass("a", 1), new RowClass("b", 2)).build();

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
   public void testProxyReadView()
   {
      final RowClass row0Raw = new RowClass("a", 1);
      final RowClass row1Raw = new RowClass("b", 2);

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

      row0.setA("aa");
      row0.setB(11);
      row1.setA("bb");
      row1.setB(22);
      assertEquals("aa", row0Raw.getA());
      assertEquals(11, row0Raw.getB());
      assertEquals("bb", row1Raw.getA());
      assertEquals(22, row1Raw.getB());
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
