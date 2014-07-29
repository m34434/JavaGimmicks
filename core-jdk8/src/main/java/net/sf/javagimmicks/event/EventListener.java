package net.sf.javagimmicks.event;

/**
 * A generic event listener (observer) that can be registered within some
 * {@link Observable} and can handle {@link Event}s fired by it.
 * 
 * @param <Evt>
 *           the {@link Event} type to be observed
 */
@FunctionalInterface
public interface EventListener<Evt extends Event<Evt>>
{
   /**
    * Triggered by the {@link Observable} when an {@link Event} occurs.
    * 
    * @param event
    *           the {@link Event} that occurred
    */
   void eventOccured(Evt event);
}
