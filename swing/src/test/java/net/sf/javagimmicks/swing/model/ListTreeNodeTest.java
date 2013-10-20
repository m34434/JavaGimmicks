package net.sf.javagimmicks.swing.model;

import static net.sf.javagimmicks.swing.model.ListTreeModelBuilderTest.buildSampleModel;
import static net.sf.javagimmicks.testing.JUnitListAssert.assertListEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ListTreeNodeTest
{
   @Test
   public void testBasicReadOperations()
   {
      final ListTreeNode<String> r = buildSampleModel().getRoot();

      assertEquals(-1, r.getIndex(r));
      assertEquals("Root", r.getValue());
      assertNull(r.getParent());
      assertFalse(r.isDetached());
      assertFalse(r.isDedicatedLeaf());
      assertFalse(r.isLeaf());
      assertTrue(r.getAllowsChildren());

      assertEquals(4, r.getChildCount());
      assertEquals(4, r.getChildList().size());
      assertEquals(4, r.getChildValues().size());
      assertListEquals(r.getChildValues(), "A", "B", "C", "D");

      final ListTreeNode<String> a = r.getChildAt(0);
      assertEquals(0, r.getIndex(a));
      assertNotNull(a.getParent());
      assertEquals("Root", a.getParentValue());
      assertFalse(a.isDetached());
      assertFalse(a.isDedicatedLeaf());
      assertFalse(a.isLeaf());
      assertTrue(a.getAllowsChildren());

      assertEquals(3, a.getChildCount());
      assertEquals(3, a.getChildList().size());
      assertEquals(3, a.getChildValues().size());
      assertListEquals(a.getChildValues(), "1", "2", "3");
   }
}
