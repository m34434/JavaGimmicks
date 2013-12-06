package net.sf.javagimmicks.sql.testing;

import org.junit.Rule;

public class DerbyDbTestRuleTest extends AbstractDbTestRuleTest
{
   @Rule
   public DerbyDbTestRule _db = new DerbyDbTestRule();

   @Override
   protected AbstractDbTestRule getTestRule()
   {
      return _db;
   }
}
