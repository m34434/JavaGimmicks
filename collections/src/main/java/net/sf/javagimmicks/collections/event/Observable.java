package net.sf.javagimmicks.collections.event;

public interface Observable<Evt extends Event<Evt, L>, L extends EventListener<Evt, L>>
{
   void addEventListener(L listener);

   void removeEventListener(L listener);
}
