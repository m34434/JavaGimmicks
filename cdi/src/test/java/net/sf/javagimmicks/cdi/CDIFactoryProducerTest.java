package net.sf.javagimmicks.cdi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import net.sf.javagimmicks.cdi.injectable.Coolness;
import net.sf.javagimmicks.cdi.injectable.FunnyClass;
import net.sf.javagimmicks.cdi.qualifier.Cool;
import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;
import net.sf.javagimmicks.lang.Factory;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIFactoryProducerTest
{
   @Inject
   private CoolFunnyContainerBean _funnyContainer;

   @Test
   public void test()
   {
      check(_funnyContainer.getNormal(), Coolness.NORMAL);
      check(_funnyContainer.getLittle(), Coolness.LITTLE);
      check(_funnyContainer.getHorrible(), Coolness.HORRIBLE);
   }

   private static void check(final FunnyClass funny, final Coolness coolness)
   {
      assertNotNull(funny);
      assertSame(coolness, funny.getCoolness());
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
         super(new Factory<FunnyClass>()
         {
            @Override
            public FunnyClass create()
            {
               return new FunnyClass("");
            }
         });
      }

      // Unfortunately - producer methods are not inherited, so we have to
      // write a wrapper (with the right qualifier annotation(s))
      @Produces
      @Cool
      public FunnyClass produce(final InjectionPoint injectionPoint)
      {
         return produceInternal(injectionPoint);
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
   }
}
