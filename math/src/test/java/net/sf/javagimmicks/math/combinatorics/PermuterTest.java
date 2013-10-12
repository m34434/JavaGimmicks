package net.sf.javagimmicks.math.combinatorics;

import net.sf.javagimmicks.math.combinatorics.Permuter;

import org.junit.Test;

public class PermuterTest
{
   @Test
   public void test()
   {
      new CombinationBuilder<String>()
            .add("a", "b", "c")
            .add("a", "c", "b")
            .add("b", "a", "c")
            .add("b", "c", "a")
            .add("c", "a", "b")
            .add("c", "b", "a")
            .assertEquals(new Permuter<String>("a", "b", "c"));
   }
}
