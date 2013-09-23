package net.sf.javagimmicks.collections.event;

import java.util.LinkedList;
import java.util.Queue;

import net.sf.javagimmicks.event.Event;
import net.sf.javagimmicks.event.EventListener;
import net.sf.javagimmicks.event.Observable;

import org.junit.Assert;

public class EventCollector<Evt extends Event<Evt>> implements EventListener<Evt>
{
   private final Class<? super Evt> _eventClass;
   private final Observable<Evt> _source;

   private Queue<Evt> _events = new LinkedList<Evt>();

   public EventCollector(final Class<? super Evt> eventClass, final Observable<Evt> source)
   {
      _eventClass = eventClass;
      _source = source;
   }

   @Override
   public void eventOccured(final Evt event)
   {
      _events.offer(event);
   }

   public void assertEventOccured(final Validator<Evt> customValidator)
   {
      Assert.assertFalse("No (more) events in queue!", _events.isEmpty());
      final Evt event = _events.poll();

      Assert.assertTrue("Event has wrong type", _eventClass.isAssignableFrom(event.getClass()));
      Assert.assertSame("Event has wrong source", _source, event.getSource());

      if (customValidator != null)
      {
         customValidator.validate(event);
      }
   }

   public void assertEventOccured()
   {
      assertEventOccured(null);
   }

   public void assertEmpty()
   {
      Assert.assertTrue("Event queue is not empty!", _events.isEmpty());
   }

   public static interface Validator<Evt extends Event<Evt>>
   {
      void validate(Evt event);
   }
}
