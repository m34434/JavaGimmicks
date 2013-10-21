package net.sf.javagimmicks.swing.model;

import static net.sf.javagimmicks.testing.JUnitListAssert.assertListEquals;
import static org.easymock.EasyMock.and;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.notNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.junit.Test;

public class ListTreeNodeTest
{
   @Test
   public void testBasicReadOperations()
   {
      final ListTreeNode<String> r = ListTreeNodeTest.buildSampleModel().getRoot();

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

      // Check the children() enumeration
      assertListEquals(Collections.list(a.children()), a.getChildList().toArray());
   }

   @Test
   public void testBasicWriteOperations()
   {
      final ListTreeNode<String> r = new ListTreeNode<String>("Root");
      assertEquals("Root", r.getValue());
      assertEquals(0, r.getChildCount());
      assertTrue(r.isDetached());

      r.setValue("root");
      assertEquals("root", r.getValue());

      r.addChild("1");
      assertEquals(1, r.getChildCount());
      checkChild(r, 0, "1");

      r.addChild("2");
      assertEquals(2, r.getChildCount());
      checkChild(r, 0, "1");
      checkChild(r, 1, "2");

      r.addChild("3");
      r.removeChildAt(1);
      assertEquals(2, r.getChildCount());
      checkChild(r, 0, "1");
      checkChild(r, 1, "3");

      r.addChildAt(1, "2");
      assertEquals(3, r.getChildCount());
      checkChild(r, 0, "1");
      checkChild(r, 1, "2");
      checkChild(r, 2, "3");
   }

   @Test
   public void testEvents()
   {
      final ListTreeModel<String> m = ListTreeNodeTest.buildSampleModel();
      final ListTreeNode<String> r = m.getRoot();

      // Create and record a Mock for a TableModelListener
      final TreeModelListener l = createMock(TreeModelListener.class);
      m.addTreeModelListener(l);
      final Capture<TreeModelEvent> c = new Capture<TreeModelEvent>(CaptureType.ALL);
      l.treeNodesRemoved(and(capture(c), notNull(TreeModelEvent.class)));
      l.treeNodesInserted(and(capture(c), notNull(TreeModelEvent.class)));
      l.treeNodesInserted(and(capture(c), notNull(TreeModelEvent.class)));
      l.treeNodesInserted(and(capture(c), notNull(TreeModelEvent.class)));
      l.treeNodesInserted(and(capture(c), notNull(TreeModelEvent.class)));
      l.treeNodesChanged(and(capture(c), notNull(TreeModelEvent.class)));
      l.treeNodesChanged(and(capture(c), notNull(TreeModelEvent.class)));
      replay(l);

      // Perform different change operations
      r.removeChildAt(3);
      r.addChild("D");
      final ListTreeNode<String> c0 = r.addChildAt(0, "__");
      c0.addChild("XXX");
      r.getChildValues().addAll(Arrays.asList("E", "F"));
      r.getChildAt(0).setValue("0");
      r.getChildAt(0).setValue("1");

      // Verify the received TreeModelEvents
      verify(l);
      verifyTreeModelEvent(c.getValues().get(0), m, r, 3);
      verifyTreeModelEvent(c.getValues().get(1), m, r, 3);
      verifyTreeModelEvent(c.getValues().get(2), m, r, 0);
      verifyTreeModelEvent(c.getValues().get(3), m, c0, 0);
      verifyTreeModelEvent(c.getValues().get(4), m, r, 5, 6);
      verifyTreeModelEvent(c.getValues().get(5), m, r, 0);
      verifyTreeModelEvent(c.getValues().get(6), m, r, 0);
      reset(l);
   }

   static ListTreeModel<String> buildSampleModel()
   {
      // @formatter:off
      return new ListTreeModelBuilder<String>(true)
         .add("Root").children()
            .add("A").children()
               .add("1")
               .add("2")
               .add("3").parent()
            .add("B").children()
               .add("4")
               .add("5")
               .add("6").parent()
            .add("C").children()
               .add("7")
               .add("8")
               .add("9").parent()
            .add("D")
         .getModel();
      // @formatter:on
   }

   private static void verifyTreeModelEvent(final TreeModelEvent e, final ListTreeModel<String> m,
         final ListTreeNode<String> n, final int... indices)
   {
      assertSame("Source did not match", m, e.getSource());
      assertEquals("TreePath did not match", m.getPathToRoot(n), e.getTreePath());
      assertArrayEquals("Indices did not match", indices, e.getChildIndices());
   }

   private static void checkChild(final ListTreeNode<String> r, final int index, final Object value)
   {
      assertEquals(value, r.getChildList().get(index).getValue());
      assertEquals(value, r.getChildValues().get(index));
      assertEquals(value, r.getChildAt(index).getValue());
   }
}
