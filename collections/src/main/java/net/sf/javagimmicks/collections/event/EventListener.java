package net.sf.javagimmicks.collections.event;

public interface EventListener<Evt extends Event<Evt, L>, L extends EventListener<Evt, L>>
{
   void eventOccured(Evt event);
}
