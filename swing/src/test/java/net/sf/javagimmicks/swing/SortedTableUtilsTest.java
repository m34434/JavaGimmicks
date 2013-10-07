package net.sf.javagimmicks.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SortedTableUtilsTest
{
   public static void main(final String[] args)
   {
      final List<IBean> beans = new ArrayList<IBean>();

      beans.add(new Bean("a", "a", "a"));
      beans.add(new Bean("a", "a", "b"));
      beans.add(new Bean("a", "b", "a"));
      beans.add(new Bean("a", "b", "b"));
      beans.add(new Bean("b", "a", "a"));
      beans.add(new Bean("b", "a", "b"));
      beans.add(new Bean("b", "b", "a"));
      beans.add(new Bean("b", "b", "b"));

      final SortedTableUtils.SortTableSuite<IBean> suite = new SortedTableUtils.SortTableSuite<IBean>(IBean.class,
            beans);
      final JTable table = new JTable();

      suite.applyTo(table);

      final JFrame window = new JFrame("Test");
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.getContentPane().add(new JScrollPane(table));

      window.pack();
      window.setVisible(true);
   }

   interface IBean
   {
      public String getA();

      public String getB();

      public String getC();

      public void setA(String a);

      public void setB(String b);

      public void setC(String c);

   }

   protected static class Bean implements IBean
   {
      private String a;
      private String b;
      private String c;

      protected Bean(final String a, final String b, final String c)
      {
         this.a = a;
         this.b = b;
         this.c = c;
      }

      @Override
      public String getA()
      {
         return a;
      }

      @Override
      public String getB()
      {
         return b;
      }

      @Override
      public String getC()
      {
         return c;
      }

      @Override
      public void setA(final String a)
      {
         this.a = a;
      }

      @Override
      public void setB(final String b)
      {
         this.b = b;
      }

      @Override
      public void setC(final String c)
      {
         this.c = c;
      }
   }
}
