package net.sf.javagimmicks.collections.event.cdi;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.util.TypeLiteral;

import net.sf.javagimmicks.cdi.AnnotationLiteralHelper;
import net.sf.javagimmicks.cdi.InjectionSpec;
import net.sf.javagimmicks.collections.event.AbstractEventCollection;

public class CDIEventCollection<E> extends AbstractEventCollection<E>
{
   private static final long serialVersionUID = -4055919694275882002L;

   static final Annotation ANY_LITERAL = AnnotationLiteralHelper.annotation(Any.class);

   private final Event<CDICollectionEvent> _eventHandle;

   @SuppressWarnings({ "unchecked", "serial" })
   public CDIEventCollection(final Collection<E> decorated)
   {
      super(decorated);

      _eventHandle = ((Event<Object>) InjectionSpec.build()
            .setType(new TypeLiteral<Event<Object>>() {}.getType())
            .addAnnotations(ANY_LITERAL)
            .getInstance()).select(CDICollectionEvent.class);
   }

   @Override
   protected void fireElementsAdded(final Collection<? extends E> elements)
   {
      fireEvent(new CDICollectionEvent(this, CDICollectionEvent.Type.ADDED,
            Collections.unmodifiableCollection(new ArrayList<E>(
                  elements))));
   }

   @Override
   protected void fireElementRemoved(final E element)
   {
      fireEvent(new CDICollectionEvent(this, CDICollectionEvent.Type.REMOVED, Collections.singleton(element)));
   }

   private void fireEvent(final CDICollectionEvent event)
   {
      _eventHandle.fire(event);
   }
}
