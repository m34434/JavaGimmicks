package net.sf.javagimmicks.collections8.composite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import net.sf.javagimmicks.collections8.builder.CollectionBuilder;
import net.sf.javagimmicks.collections8.builder.ListBuilder;

import org.junit.Before;
import org.junit.Test;

public class CompositeCollectionTest
{
   private Collection<String> _c1;
   private Collection<String> _c2;
   private Collection<String> _c3;
   private Collection<String> _c4;
   private Collection<String> _c5;
   private Collection<String> _c6;
   private Collection<String> _c7;

   private Collection<String> _composite;

   @Before
   @SuppressWarnings("unchecked")
   public void prepare()
   {
      _c1 = CollectionBuilder.create(new HashSet<String>()).add("A").add("B").get();
      _c2 = Collections.emptySet();
      _c3 = new ArrayList<String>();
      _c4 = ListBuilder.create(new LinkedList<String>()).add("C").add("D").add("E").get();
      _c5 = Collections.emptyList();
      _c6 = Collections.singleton("F");
      _c7 = Collections.singletonList("G");

      _composite = CompositeUtils.collection(_c1, _c2, _c3, _c4, _c5, _c6, _c7);
   }

   @Test
   @SuppressWarnings("unchecked")
   public void testEquality()
   {
      final Collection<String> reference = buildReference(_c1, _c2, _c3, _c4, _c5, _c6, _c7);

      assertEquals(reference.size(), _composite.size());
      assertTrue(_composite.containsAll(reference));
   }

   @Test
   public void testRemoveA()
   {
      _composite.remove("A");
      assertFalse(_composite.contains("A"));
      assertFalse(_c1.contains("A"));
   }

   @Test
   public void testRemoveB()
   {
      _composite.remove("B");
      assertFalse(_composite.contains("B"));
      assertFalse(_c1.contains("B"));
   }

   @Test
   public void testRemoveC()
   {
      _composite.remove("C");
      assertFalse(_composite.contains("C"));
      assertFalse(_c4.contains("C"));
   }

   @Test
   public void testRemoveD()
   {
      _composite.remove("D");
      assertFalse(_composite.contains("D"));
      assertFalse(_c4.contains("D"));
   }

   @Test
   public void testRemoveE()
   {
      _composite.remove("E");
      assertFalse(_composite.contains("E"));
      assertFalse(_c4.contains("E"));
   }

   @Test
   public void testRemoveF()
   {
      try
      {
         _composite.remove("F");
         fail(UnsupportedOperationException.class.getSimpleName() + " expected!");
      }
      catch (final UnsupportedOperationException ex)
      {
      }
   }

   @Test
   public void testRemoveG()
   {
      try
      {
         _composite.remove("G");
         fail(UnsupportedOperationException.class.getSimpleName() + " expected!");
      }
      catch (final UnsupportedOperationException ex)
      {
      }
   }

   @Test
   public void testRemoveMany()
   {
      _composite.remove("A");
      assertFalse(_composite.contains("A"));
      assertFalse(_c1.contains("A"));

      _composite.remove("B");
      assertFalse(_composite.contains("B"));
      assertFalse(_c1.contains("B"));

      _composite.remove("C");
      assertFalse(_composite.contains("C"));
      assertFalse(_c4.contains("C"));

      _composite.remove("D");
      assertFalse(_composite.contains("D"));
      assertFalse(_c4.contains("D"));

      _composite.remove("E");
      assertFalse(_composite.contains("E"));
      assertFalse(_c4.contains("E"));
   }

   @Test
   public void testAdd()
   {
      int sizeBefore;

      // Add an element to _c1
      sizeBefore = _composite.size();
      _c1.add("X");
      assertEquals(sizeBefore + 1, _composite.size());
      assertTrue(_composite.contains("X"));

      // Add an element to _c3
      sizeBefore = _composite.size();
      _c3.add("Y");
      assertEquals(sizeBefore + 1, _composite.size());
      assertTrue(_composite.contains("Y"));

      // Add an element to _c4
      sizeBefore = _composite.size();
      _c4.add("Z");
      assertEquals(sizeBefore + 1, _composite.size());
      assertTrue(_composite.contains("Z"));
   }

   private static <E> Collection<E> buildReference(final Collection<? extends E>... collections)
   {
      final ArrayList<E> result = new ArrayList<E>();

      for (final Collection<? extends E> c : collections)
      {
         result.addAll(c);
      }

      return result;
   }
}
