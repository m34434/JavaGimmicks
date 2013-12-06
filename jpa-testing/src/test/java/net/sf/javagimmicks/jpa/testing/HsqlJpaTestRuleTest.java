package net.sf.javagimmicks.jpa.testing;

import net.sf.javagimmicks.jpa.testing.entity.FooEntity;

import org.junit.Rule;

public class HsqlJpaTestRuleTest extends AbstractJpaTestRuleTest
{
   @Rule
   public HsqlJpaTestRule _jpa = new HsqlJpaTestRule(FooEntity.class.getPackage().getName());

   @Override
   protected AbstractJpaTestRule getTestRule()
   {
      return _jpa;
   }
}
