package net.sf.javagimmicks.collections8.mapping;

import net.sf.javagimmicks.collections8.mapping.DualMapValueMappings;
import net.sf.javagimmicks.collections8.mapping.ValueMappings;

import org.junit.Test;

public class DualMapValueMappingsPackageInfoExampleTest
{
   public static enum Person
   {
      Alice, Bob, Charles
   };

   public static enum Hobby
   {
      Astrology, Biking, Chess;
   }

   public static enum Likes
   {
      Little, Much;
   }

   @Test
   public void test()
   {
      final ValueMappings<Person, Hobby, Likes> m = DualMapValueMappings
            .<Person, Hobby, Likes> createTreeTreeInstance();

      // Alice has hobbies Biking and Chess
      m.put(Person.Alice, Hobby.Biking, Likes.Little);
      m.put(Person.Alice, Hobby.Chess, Likes.Much);

      // Bob has hobbies Astrology and Chess
      m.put(Person.Bob, Hobby.Astrology, Likes.Much);
      m.put(Person.Bob, Hobby.Chess, Likes.Little);

      // Charles has hobbies Astrology and Biking
      m.put(Person.Charles, Hobby.Astrology, Likes.Little);
      m.put(Person.Charles, Hobby.Biking, Likes.Much);

      System.out.println(m.getLeftView());
      System.out.println(m.getRightView());
   }
}
