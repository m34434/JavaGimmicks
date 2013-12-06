package net.sf.javagimmicks.jpa.testing;

import net.sf.javagimmicks.jpa.testing.entity.FooEntity;

import org.junit.Rule;

public class DerbyJpaTestRuleTest extends AbstractJpaTestRuleTest
{
   @Rule
   public DerbyJpaTestRule _jpa = new DerbyJpaTestRule(FooEntity.class.getPackage().getName());

   @Override
   protected AbstractJpaTestRule getTestRule()
   {
      return _jpa;
   }
}
