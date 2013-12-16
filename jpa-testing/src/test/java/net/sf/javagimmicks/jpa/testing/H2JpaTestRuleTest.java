package net.sf.javagimmicks.jpa.testing;

import net.sf.javagimmicks.jpa.testing.entity.FooEntity;

import org.junit.Rule;

public class H2JpaTestRuleTest extends AbstractJpaTestRuleTest
{
   @Rule
   public H2JpaTestRule _jpa = new H2JpaTestRule(FooEntity.class.getPackage().getName());

   @Override
   protected AbstractJpaTestRule getTestRule()
   {
      return _jpa;
   }
}
