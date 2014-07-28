package net.sf.javagimmicks.collections.mapping;

import org.junit.Test;

public class DualMapMappingsPackageInfoExampleTest
{
   public static enum Person
   {
      Alice, Bob, Charles
   };

   public static enum Hobby
   {
      Astrology, Biking, Chess;
   }

   @Test
   public void test()
   {
      final Mappings<Person, Hobby> m = DualMapMappings.<Person, Hobby> createTreeTreeInstance();

      // Alice has hobbies Biking and Chess
      m.put(Person.Alice, Hobby.Biking);
      m.put(Person.Alice, Hobby.Chess);

      // Bob has hobbies Astrology and Chess
      m.put(Person.Bob, Hobby.Astrology);
      m.put(Person.Bob, Hobby.Chess);

      // Charles has hobbies Astrology and Biking
      m.put(Person.Charles, Hobby.Astrology);
      m.put(Person.Charles, Hobby.Biking);

      System.out.println(m.getLeftView());
      System.out.println(m.getRightView());
   }
}
