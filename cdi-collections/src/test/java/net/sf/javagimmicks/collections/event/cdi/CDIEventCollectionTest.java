package net.sf.javagimmicks.collections.event.cdi;

import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.event.Observes;

import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIEventCollectionTest
{
   private static CDICollectionEvent _event;

   @Test
   public void testFireAndReceive()
   {
      final CDIEventCollection<String> collection = new CDIEventCollection<String>(new ArrayList<String>());

      final String addedString = "Foo";
      collection.add(addedString);

      final Collection<?> addedElements = _event.getElements();

      Assert.assertNotNull(_event);
      Assert.assertSame(collection, _event.getSource());
      Assert.assertSame(CDICollectionEvent.Type.ADDED, _event.getType());
      Assert.assertEquals(1, addedElements.size());
      Assert.assertSame(addedString, addedElements.iterator().next());
   }

   public void receiveEvent(@Observes final CDICollectionEvent event)
   {
      _event = event;
   }
}
