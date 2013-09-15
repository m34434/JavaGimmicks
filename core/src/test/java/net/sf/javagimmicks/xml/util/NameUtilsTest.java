package net.sf.javagimmicks.xml.util;

import static net.sf.javagimmicks.xml.util.NameUtils.isNCName;
import static net.sf.javagimmicks.xml.util.NameUtils.isQualifiedName;
import static net.sf.javagimmicks.xml.util.NameUtils.splitQualifiedName;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NameUtilsTest
{
   @Test
   public void test()
   {
      assertNameCheck(true, true, "hello123-world");
      assertNameCheck(false, false, "?hello123-world");
      assertNameCheck(false, false, "2hello123-world");
      assertNameCheck(false, true, "tns:hello123-world");
      assertNameCheck(false, true, "_tns:hello123-world");
      assertNameCheck(false, false, "1tns:hello123-world");
      assertNameCheck(false, false, "-tns:hello123-world");
      assertNameCheck(false, false, "?tns:hello123-world");
         
      assertArrayEquals(new String[]{"xs", "schema"}, splitQualifiedName("xs:schema"));
   }
   
   private static void assertNameCheck(boolean isNCName, boolean isQualifiedName, String s)
   {
      assertEquals("NCName check failed for string " + s, isNCName, isNCName(s));
      assertEquals("Qualified name check failed for string " + s, isQualifiedName, isQualifiedName(s));
   }
}
