package net.sf.javagimmicks.math;

import org.junit.Test;

public class CombinatorTest
{
   @Test
   public void test()
   {
      new CombinationBuilder<String>()
            .add("a", "b")
            .add("a", "c")
            .add("a", "d")
            .add("b", "c")
            .add("b", "d")
            .add("c", "d")
            .assertEquals(new Combinator<String>(2, "a", "b", "c", "d"));
   }
}
