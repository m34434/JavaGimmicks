package net.sf.javagimmicks.swing.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.junit.Test;

public class ListTreeModelBuilderTest
{
   public static void main(final String[] args)
   {
      final ListTreeModel<String> model = ListTreeNodeTest.buildSampleModel();

      final JFrame window = new JFrame("Tree test");
      window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      window.getContentPane().add(new JScrollPane(new JTree(model)));
      window.pack();

      window.setVisible(true);
   }

   @Test
   public void test()
   {
      final ListTreeModel<String> m = ListTreeNodeTest.buildSampleModel();
      final ListTreeNode<String> r = m.getRoot();

      assertNotNull(r);
      assertEquals("Root", r.getValue());
      assertEquals(4, r.getChildCount());

      // Child "A"
      ListTreeNode<String> l2 = r.getChildAt(0);
      assertEquals("A", l2.getValue());
      assertEquals(3, l2.getChildCount());

      assertEquals("1", l2.getChildAt(0).getValue());
      assertEquals(0, l2.getChildAt(0).getChildCount());
      assertEquals("2", l2.getChildAt(1).getValue());
      assertEquals(0, l2.getChildAt(1).getChildCount());
      assertEquals("3", l2.getChildAt(2).getValue());
      assertEquals(0, l2.getChildAt(2).getChildCount());

      // Child "B"
      l2 = r.getChildAt(1);
      assertEquals("B", l2.getValue());
      assertEquals(3, l2.getChildCount());

      assertEquals("4", l2.getChildAt(0).getValue());
      assertEquals(0, l2.getChildAt(0).getChildCount());
      assertEquals("5", l2.getChildAt(1).getValue());
      assertEquals(0, l2.getChildAt(1).getChildCount());
      assertEquals("6", l2.getChildAt(2).getValue());
      assertEquals(0, l2.getChildAt(2).getChildCount());

      // Child "C"
      l2 = r.getChildAt(2);
      assertEquals("C", l2.getValue());
      assertEquals(3, l2.getChildCount());

      assertEquals("7", l2.getChildAt(0).getValue());
      assertEquals(0, l2.getChildAt(0).getChildCount());
      assertEquals("8", l2.getChildAt(1).getValue());
      assertEquals(0, l2.getChildAt(1).getChildCount());
      assertEquals("9", l2.getChildAt(2).getValue());
      assertEquals(0, l2.getChildAt(2).getChildCount());

      // Child "D"
      l2 = r.getChildAt(3);
      assertEquals("D", l2.getValue());
      assertEquals(0, l2.getChildCount());
   }
}
