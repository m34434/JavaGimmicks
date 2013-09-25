package net.sf.javagimmicks.collections.event.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;

import net.sf.javagimmicks.cdi.AnnotationLiteralHelper;
import net.sf.javagimmicks.cdi.CDIContext;
import net.sf.javagimmicks.cdi.InjectionSpec;

abstract class CDIBrigeBase
{
   @SuppressWarnings("rawtypes")
   private static final InjectionSpec<Event> EVENT_INJECTION = InjectionSpec
         .<Event> build()
         .setClass(Event.class)
         .addTypeParameters(Object.class)
         .addAnnotations(AnnotationLiteralHelper.annotation(Any.class))
         .getInjection();

   @SuppressWarnings("unchecked")
   static <EventType> Event<EventType> buildEvent(final Class<EventType> eventType, final Annotation... annotations)
   {
      return EVENT_INJECTION.getInstance(CDIContext.getBeanManager()).select(eventType, annotations);
   }
}
