package net.sf.javagimmicks.swing.model;

import java.sql.SQLException;

import org.junit.Test;

public class ListTableModelTest
{
   @Test
   public void testTrivialStuff()
   {
      final ListTableModel<RowIF> m = ListTableModel.builder(RowIF.class).addProperties("A", "B").build();
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
