package net.sf.javagimmicks.collections.bidimap;

import java.util.TreeMap;

import org.junit.Before;

public class SortedBidiMapTest extends BidiMapTest
{
   @Override
   @Before
   public void setUp()
   {
      _bidiMap = new DualSortedBidiMap<Integer, String>(new TreeMap<Integer, String>(), new TreeMap<String, Integer>());
   }
}
