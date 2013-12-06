package net.sf.javagimmicks.sql.testing;

import org.junit.Rule;

public class HsqlDbTestRuleTest extends AbstractDbTestRuleTest
{
   @Rule
   public HsqlDbTestRule _db = new HsqlDbTestRule();

   @Override
   protected AbstractDbTestRule getTestRule()
   {
      return _db;
   }
}
