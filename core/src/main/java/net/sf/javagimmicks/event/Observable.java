package net.sf.javagimmicks.event;

/**
 * A generic interfaces the describes an observable object that is able to fire
 * and report {@link Event}s to any interested {@link EventListener}.
 * 
 * @param <Evt>
 *           the {@link Event} type that will be fired
 * @param <L>
 *           the type {@link EventListener}s that can be registered
 */
public interface Observable<Evt extends Event<Evt, L>, L extends EventListener<Evt, L>>
{
   /**
    * Adds a new {@link EventListener} (or sub-type) to this {@link Observable}
    * 
    * @param listener
    *           the {@link EventListener} (or sub-type) to add
    */
   void addEventListener(L listener);

   /**
    * Removes a registered {@link EventListener} (or sub-type) from this
    * {@link Observable}
    * 
    * @param listener
    *           the {@link EventListener} (or sub-type) to remove
    */
   void removeEventListener(L listener);
}
