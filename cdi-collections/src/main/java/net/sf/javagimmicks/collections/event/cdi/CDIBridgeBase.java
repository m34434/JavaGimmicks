package net.sf.javagimmicks.collections.event.cdi;

import java.lang.annotation.Annotation;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.util.TypeLiteral;

import net.sf.javagimmicks.cdi.AnnotationLiteralHelper;
import net.sf.javagimmicks.cdi.InjectionSpec;

abstract class CDIBrigeBase
{
   static Annotation ANY_LITERAL = AnnotationLiteralHelper.annotation(Any.class);

   @SuppressWarnings({ "unchecked", "serial" })
   static <EventType> Event<EventType> buildEvent(final Class<EventType> eventType)
   {
      return ((Event<Object>) InjectionSpec.build()
            .setType(new TypeLiteral<Event<Object>>() {}.getType())
            .addAnnotations(ANY_LITERAL)
            .getInstance()).select(eventType);
   }
}
