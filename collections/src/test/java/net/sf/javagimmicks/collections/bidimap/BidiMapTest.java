package net.sf.javagimmicks.collections.bidimap;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BidiMapTest
{
   protected BidiMap<Integer, String> _bidiMap;
   
   @Before
   public void setUp()
   {
      _bidiMap = new DualBidiMap<Integer, String>(new HashMap<Integer, String>(), new HashMap<String, Integer>());
   }

   @Test
   public void test()
   {
      addReferencePairs();
      assertEquals(_referencePairs.size(), _bidiMap.size());
      validateSize();
      validateReferencePairs();

      addNewPairs();
      assertEquals(_referencePairs.size(), _bidiMap.size());
      validateSize();
      validateNewPairs();

//      System.out.println(_bidiMap.remove(1));
//      System.out.println(_bidiMap);
//      System.out.println(_bidiMap.inverseBidiMap());
//      System.out.println("===");
//
//      System.out.println(_bidiMap.inverseBidiMap().remove("c"));
//      System.out.println(_bidiMap);
//      System.out.println(_bidiMap.inverseBidiMap());
//      System.out.println("===");
//
//      Iterator<Integer> keyIterator = _bidiMap.keySet().iterator();
//      keyIterator.next();
//      keyIterator.remove();
//      System.out.println(_bidiMap);
//      System.out.println(_bidiMap.inverseBidiMap());
//      System.out.println("===");
//
//      Iterator<String> reverseKeyIterator = _bidiMap.inverseBidiMap().keySet().iterator();
//      reverseKeyIterator.next();
//      reverseKeyIterator.remove();
//      System.out.println(_bidiMap);
//      System.out.println(_bidiMap.inverseBidiMap());
//      System.out.println("===");
//
//      Iterator<String> valueIterator = _bidiMap.values().iterator();
//      valueIterator.next();
//      valueIterator.remove();
//      System.out.println(_bidiMap);
//      System.out.println(_bidiMap.inverseBidiMap());
//      System.out.println("===");
//
//      Iterator<Integer> reverseValueIterator = _bidiMap.inverseBidiMap().values().iterator();
//      reverseValueIterator.next();
//      reverseValueIterator.remove();
//      System.out.println(_bidiMap);
//      System.out.println(_bidiMap.inverseBidiMap());
//      System.out.println("===");
//
//      _bidiMap.entrySet().iterator().next().setValue("xxx");
//      System.out.println(_bidiMap);
//      System.out.println(_bidiMap.inverseBidiMap());
//      System.out.println("===");
//      
//      _bidiMap.inverseBidiMap().entrySet().iterator().next().setValue(999);
//      System.out.println(_bidiMap);
//      System.out.println(_bidiMap.inverseBidiMap());
//      System.out.println("===");
   }

   protected void addReferencePairs()
   {
      for(Iterator<Pair<Integer, String>> iterator = _referencePairs.iterator(); iterator.hasNext();)
      {
         assertNull(put(iterator.next()));
         
         if(iterator.hasNext())
         {
            assertNull(putInverted(iterator.next()));
         }
      }
   }

   protected void validateReferencePairs()
   {
      for(Pair<Integer, String> pair : _referencePairs)
      {
         validate(pair);
      }
      
      for(Pair<Integer, String> pair : _referencePairs)
      {
         validateInverted(pair);
      }
   }

   protected void addNewPairs()
   {
      for(Iterator<Pair<Integer, String>> iterator = _newPairs.iterator(); iterator.hasNext();)
      {
         Pair<Integer, String> pair = iterator.next();
         String value = get(pair.a);
         
         assertEquals(value, put(pair));
         
         if(iterator.hasNext())
         {
            pair = iterator.next();
            Integer invertedValue = getInverted(pair.b);
            assertEquals(invertedValue, putInverted(pair));
         }
      }
   }

   protected void validateNewPairs()
   {
      for(Pair<Integer, String> pair : _newPairs)
      {
         validate(pair);
      }
      
      for(Pair<Integer, String> pair : _newPairs)
      {
         validateInverted(pair);
      }
   }

   private void validateSize()
   {
      assertEquals(_bidiMap.size(), _bidiMap.inverseBidiMap().size());
   }
   
   protected String get(Integer a)
   {
      return _bidiMap.get(a);
   }
   
   protected Integer getInverted(String a)
   {
      return _bidiMap.inverseBidiMap().get(a);
   }
   
   protected void validate(Integer a, String b)
   {
      assertEquals(b, get(a));
   }
   
   protected void validateInverted(Integer a, String b)
   {
      assertEquals(a, getInverted(b));
   }
   
   protected void validate(Pair<Integer, String> p)
   {
      validate(p.a, p.b);
   }
   
   protected void validateInverted(Pair<Integer, String> p)
   {
      validateInverted(p.a, p.b);
   }
   
   protected String put(Integer a, String b)
   {
      return _bidiMap.put(a, b);
   }
   
   protected String put(Pair<Integer, String> p)
   {
      return put(p.a, p.b);
   }
   
   protected Integer putInverted(Integer a, String b)
   {
      return _bidiMap.inverseBidiMap().put(b, a);
   }
   
   protected Integer putInverted(Pair<Integer, String> p)
   {
      return putInverted(p.a, p.b);
   }
   
   protected static <A, B> void add(Collection<Pair<A, B>> c, Pair<A, B> p)
   {
      c.add(p);
   }
   
   protected static <A, B> void add(Collection<Pair<A, B>> c, A a, B b)
   {
      add(c, new Pair<A, B>(a, b));
   }
   
   protected static List<Pair<Integer, String>> _referencePairs = new ArrayList<Pair<Integer,String>>();
   protected static List<Pair<Integer, String>> _newPairs = new ArrayList<Pair<Integer,String>>();

   static
   {
      add(_referencePairs, 1, "h");
      add(_referencePairs, 2, "g");
      add(_referencePairs, 3, "f");
      add(_referencePairs, 4, "e");
      add(_referencePairs, 5, "d");
      add(_referencePairs, 6, "c");
      add(_referencePairs, 7, "b");
      add(_referencePairs, 8, "a");
      
      add(_newPairs, 1, "hh");
      add(_newPairs, 2, "gg");
   }

   public static class Pair<A, B>
   {
      public final A a;
      public final B b;
      
      public Pair(A a, B b)
      {
         this.a = a;
         this.b = b;
      }
      
      public Pair<B, A> invert()
      {
         return new Pair<B, A>(b, a);
      }
   }
}
