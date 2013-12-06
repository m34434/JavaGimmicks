package net.sf.javagimmicks.sql.testing;

import org.junit.Rule;

public class H2DbTestRuleTest extends AbstractDbTestRuleTest
{
   @Rule
   public H2DbTestRule _db = new H2DbTestRule();

   @Override
   protected AbstractDbTestRule getTestRule()
   {
      return _db;
   }
}
