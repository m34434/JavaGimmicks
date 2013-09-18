package net.sf.javagimmicks.collections.event.cdi;

import java.util.Set;

import javax.enterprise.event.Event;
import javax.enterprise.util.TypeLiteral;

import net.sf.javagimmicks.cdi.InjectionSpec;
import net.sf.javagimmicks.collections.event.AbstractEventSet;
import net.sf.javagimmicks.collections.event.cdi.CDISetEvent.Type;

public class CDIEventSet<E> extends AbstractEventSet<E>
{
   private static final long serialVersionUID = 4799365684601532982L;

   private final Event<CDISetEvent> _eventHandle;

   @SuppressWarnings({ "serial", "unchecked" })
   public CDIEventSet(final Set<E> decorated)
   {
      super(decorated);

      _eventHandle = ((Event<Object>) InjectionSpec.build()
            .setType(new TypeLiteral<Event<Object>>() {}.getType())
            .addAnnotations(CDIEventCollection.ANY_LITERAL)
            .getInstance()).select(CDISetEvent.class);
   }

   @Override
   protected void fireElementAdded(final E element)
   {
      fireEvent(new CDISetEvent(this, Type.ADDED, element));
   }

   @Override
   protected void fireElementReadded(final E element)
   {
      fireEvent(new CDISetEvent(this, Type.READDED, element));
   }

   @Override
   protected void fireElementRemoved(final E element)
   {
      fireEvent(new CDISetEvent(this, Type.REMOVED, element));
   }

   private void fireEvent(final CDISetEvent event)
   {
      _eventHandle.fire(event);
   }

}
