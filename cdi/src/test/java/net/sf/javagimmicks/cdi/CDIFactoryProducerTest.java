package net.sf.javagimmicks.cdi;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import net.sf.javagimmicks.cdi.injectable.Coolness;
import net.sf.javagimmicks.cdi.injectable.FunnyClass;
import net.sf.javagimmicks.cdi.qualifier.Cool;
import net.sf.javagimmicks.lang.Factory;

import org.junit.Assert;
import org.junit.Test;

public class CDIFactoryProducerTest extends WeldTestHelper
{
   @Test
   public void test()
   {
      final CoolFunnyContainerBean funnyContainer = lookup(CoolFunnyContainerBean.class);
      Assert.assertNotNull(funnyContainer);

      check(funnyContainer.getNormal(), Coolness.NORMAL);
      check(funnyContainer.getLittle(), Coolness.LITTLE);
      check(funnyContainer.getHorrible(), Coolness.HORRIBLE);
   }

   private static void check(final FunnyClass funny, final Coolness coolness)
   {
      Assert.assertNotNull(funny);
      Assert.assertSame(coolness, funny.getCoolness());
   }

   public static class CoolFunnyContainerBean
   {
      @Inject
      @Cool
      private FunnyClass _normal;

      @Inject
      @Cool(coolness = Coolness.HORRIBLE)
      private FunnyClass _horrible;

      @Inject
      @Cool(coolness = Coolness.LITTLE)
      private FunnyClass _little;

      public FunnyClass getNormal()
      {
         return _normal;
      }

      public FunnyClass getHorrible()
      {
         return _horrible;
      }

      public FunnyClass getLittle()
      {
         return _little;
      }

   }

   public static class CoolFunnyClassProducer extends CDIFactoryProducer<FunnyClass>
   {
      public CoolFunnyClassProducer()
      {
         super(getFactory());
      }

      // Unfortunately - producer methods are not inherited, so we have to
      // re-implement it (with the right qualifier annotation(s))
      @Override
      @Produces
      @Cool
      public FunnyClass produce(final InjectionPoint injectionPoint)
      {
         return super.produce(injectionPoint);
      }

      @Override
      protected void configure(final FunnyClass funny, final InjectionPoint injectionPoint)
      {
         // Get the @Cool annotation from the InjectionPoint
         final Cool cool = injectionPoint.getAnnotated().getAnnotation(Cool.class);

         // Any apply the coolness to the FunnyClass
         if (cool != null)
         {
            funny.setCoolness(cool.coolness());
         }
      }

      private static Factory<FunnyClass> getFactory()
      {
         return new Factory<FunnyClass>()
         {
            @Override
            public FunnyClass create()
            {
               return new FunnyClass("");
            }
         };
      }
   }
}
