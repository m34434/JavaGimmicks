package net.sf.javagimmicks.event.testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.Queue;

import net.sf.javagimmicks.event.Event;
import net.sf.javagimmicks.event.EventListener;
import net.sf.javagimmicks.event.Observable;

/**
 * A generic {@link EventListener} usable for testing that remembers all
 * observed {@link Event}s in a {@link Queue} and provides easy assertions calls
 * against them.
 * 
 * @param <Evt>
 *           the type of {@link Event} that is observed
 */
public class EventCollector<Evt extends Event<Evt>> implements EventListener<Evt>
{
   private final Class<? super Evt> _eventClass;
   private final Observable<Evt> _source;

   private Queue<Evt> _events = new LinkedList<Evt>();

   /**
    * Creates a new instance for the given {@link Event} class and source
    * {@link Observable}.
    * 
    * @param eventClass
    *           the {@link Class} of the events to evaluate
    * @param source
    *           the source {@link Observable} that creates the {@link Event}s
    */
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

   /**
    * Makes an assertion to the next {@link Event} in the internal {@link Queue}
    * and removes from there. The following assertions are automatically done -
    * more can be specified with a given {@link Validator}.
    * <ul>
    * <li>There is an {@link Event} in the internal {@link Queue}</li>
    * <li>The {@link Event} is an instance of the type provided within the
    * constructor (see {@link #EventCollector(Class, Observable)})</li>
    * <li>The {@link Event#getSource()} call returns the same {@link Observable}
    * that was given within the constructor (see
    * {@link #EventCollector(Class, Observable)})</li>
    * </ul>
    * 
    * @param customValidator
    *           a custom {@link Validator} that can be used to specify
    *           {@link Event}-specific validation logic
    * @see Validator
    */
   public void assertEventOccured(final Validator<Evt> customValidator)
   {
      assertFalse("No (more) events in queue!", _events.isEmpty());
      final Evt event = _events.poll();

      assertTrue("Event has wrong type", _eventClass.isAssignableFrom(event.getClass()));
      assertSame("Event has wrong source", _source, event.getSource());

      if (customValidator != null)
      {
         customValidator.validate(event);
      }
   }

   /**
    * Convenience method to {@link #assertEventOccured(Validator)} but without a
    * custom {@link Validator}.
    * 
    * @see #assertEventOccured(Validator)
    */
   public void assertEventOccured()
   {
      assertEventOccured(null);
   }

   /**
    * Clear the internal {@link Event} {@link Queue}.
    */
   public void clear()
   {
      _events.clear();
   }

   /**
    * Makes an assertion that the internal {@link Event} {@link Queue} is empty.
    */
   public void assertEmpty()
   {
      assertTrue("Event queue is not empty!", _events.isEmpty());
   }

   /**
    * A generic validator that allows specification of {@link Event}-specific
    * assertions.
    * 
    * @param <Evt>
    *           the type {@link Event}s that should be validated
    */
   public static interface Validator<Evt extends Event<Evt>>
   {
      /**
       * Make any assertions against the given {@link Event}.
       * 
       * @param event
       *           the {@link Event} to validate by making some assertions
       *           against it
       */
      void validate(Evt event);
   }
}
