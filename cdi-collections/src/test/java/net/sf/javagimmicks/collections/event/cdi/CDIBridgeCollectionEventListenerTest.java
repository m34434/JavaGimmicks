package net.sf.javagimmicks.collections.event.cdi;

import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.event.Observes;

import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;
import net.sf.javagimmicks.collections.event.CollectionEvent;
import net.sf.javagimmicks.collections.event.ObservableEventCollection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIBridgeCollectionEventListenerTest
{
   private static CDICollectionEvent _event;

   @Test
   public void testFireAndReceive()
   {
      final ObservableEventCollection<String> collection = new ObservableEventCollection<String>(
            new ArrayList<String>());
      CDIBridgeCollectionEventListener.install(collection);

      final String addedString = "Foo";
      collection.add(addedString);

      final Collection<?> addedElements = _event.getElements();

      Assert.assertNotNull(_event);
      Assert.assertSame(collection, _event.getSource());
      Assert.assertSame(CollectionEvent.Type.ADDED, _event.getType());
      Assert.assertEquals(1, addedElements.size());
      Assert.assertSame(addedString, addedElements.iterator().next());
   }

   public void receiveEvent(@Observes final CDICollectionEvent event)
   {
      _event = event;
   }
}
