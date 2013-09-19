package net.sf.javagimmicks.collections.event;

/**
 * A generic event listener (observer) that can be registered within some
 * {@link Observable} and can handle {@link Event}s fired by it.
 * 
 * @param <Evt>
 *           the {@link Event} type to be observed
 * @param <L>
 *           the concrete {@link EventListener} type
 */
public interface EventListener<Evt extends Event<Evt, L>, L extends EventListener<Evt, L>>
{
   /**
    * Triggered by the {@link Observable} when an {@link Event} occurs.
    * 
    * @param event
    *           the {@link Event} that occurred
    */
   void eventOccured(Evt event);
}
