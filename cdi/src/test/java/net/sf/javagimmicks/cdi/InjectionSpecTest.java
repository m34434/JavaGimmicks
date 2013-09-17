package net.sf.javagimmicks.cdi;

import static net.sf.javagimmicks.cdi.AnnotationLiteralHelper.annotationWithMembers;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import javax.enterprise.inject.Produces;

import net.sf.javagimmicks.cdi.injectable.Coolness;
import net.sf.javagimmicks.cdi.qualifier.Cool;
import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class InjectionSpecTest
{
   private static final Cool COOL_LITERAL = annotationWithMembers(Cool.class).member("coolness", Coolness.NORMAL)
         .get();

   @SuppressWarnings({ "rawtypes", "unchecked" })
   @Test
   public void testNormal() throws InstantiationException, IllegalAccessException
   {
      // Normal ArrayList<String>
      final ArrayList<String> cdiGeneratedArrayList =
            InjectionSpec.<ArrayList> build()
                  .setClass(ArrayList.class)
                  .addTypeParameters(String.class)
                  .getInstance();

      assertNotNull(cdiGeneratedArrayList);
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   @Test
   public void testCool() throws InstantiationException, IllegalAccessException
   {
      // @Cool annotated ArrayList<String>
      final ArrayList<String> cdiGeneratedCoolArrayList =
            InjectionSpec
                  .<ArrayList> build()
                  .setClass(ArrayList.class)
                  .addTypeParameters(String.class)
                  .addAnnotations(COOL_LITERAL)
                  .getInstance();

      assertNotNull(cdiGeneratedCoolArrayList);
   }

   @Produces
   public static ArrayList<String> produceArrayList()
   {
      return new ArrayList<String>();
   }

   @Produces
   @Cool
   public static ArrayList<String> produceCoolArrayList()
   {
      return new ArrayList<String>();
   }
}
