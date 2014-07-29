package net.sf.javagimmicks.event;

/**
 * A generic interfaces the describes an observable object that is able to fire
 * and report {@link Event}s to any interested {@link EventListener}.
 * 
 * @param <Evt>
 *           the {@link Event} type that will be fired
 */
public interface Observable<Evt extends Event<Evt>>
{
   /**
    * Adds a new {@link EventListener} (or sub-type) to this {@link Observable}
    * 
    * @param listener
    *           the {@link EventListener} (or sub-type) to add
    * @param <L>
    *           the type of {@link EventListener} to add
    */
   <L extends EventListener<Evt>> void addEventListener(L listener);

   /**
    * Removes a registered {@link EventListener} (or sub-type) from this
    * {@link Observable}
    * 
    * @param listener
    *           the {@link EventListener} (or sub-type) to remove
    * @param <L>
    *           the type of {@link EventListener} to add
    */
   <L extends EventListener<Evt>> void removeEventListener(L listener);
}
