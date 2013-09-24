package net.sf.javagimmicks.cdi.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.inject.Inject;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class ValidationTest
{
   @Inject
   private ValidatedClass _validated;

   @Test
   public void test()
   {
      // Input validation: @NotNull
      try
      {
         _validated.greet(null);
         fail(ValidationException.class.getName() + " expected!");
      }
      catch (final ValidationException ex)
      {
      }

      // Result validation: @Size(max = 16)
      try
      {
         _validated.greet("VeryVeryLongName");
         fail(ValidationException.class.getName() + " expected!");
      }
      catch (final ValidationException ex)
      {
      }

      // Good case
      assertEquals("Hello 'Michael'!", _validated.greet("Michael"));
   }

   public static class ValidatedClass
   {
      @Size(max = 16, message = "Fail!")
      public String greet(@NotNull final String name)
      {
         return String.format("Hello '%1$s'!", name);
      }
   }
}
