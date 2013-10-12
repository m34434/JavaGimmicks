package net.sf.javagimmicks.math.combinatorics;

import net.sf.javagimmicks.math.combinatorics.VariatorWithRepetition;

import org.junit.Test;

public class VariatorWithRepetitionTest
{
   @Test
   public void test()
   {
      new CombinationBuilder<String>()
            .add("a", "a")
            .add("a", "b")
            .add("a", "c")
            .add("b", "a")
            .add("b", "b")
            .add("b", "c")
            .add("c", "a")
            .add("c", "b")
            .add("c", "c")
            .assertEquals(new VariatorWithRepetition<String>(2, "a", "b", "c"));
   }
}
