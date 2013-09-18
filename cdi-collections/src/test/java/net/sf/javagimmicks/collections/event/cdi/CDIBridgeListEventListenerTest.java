package net.sf.javagimmicks.collections.event.cdi;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;

import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;
import net.sf.javagimmicks.collections.event.ListEvent;
import net.sf.javagimmicks.collections.event.ObservableEventList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIBridgeListEventListenerTest
{
   private static CDIListEvent _event;

   @Test
   public void testFireAndReceive()
   {
      final ObservableEventList<String> list = new ObservableEventList<String>(new ArrayList<String>());
      CDIBridgeListEventListener.install(list);

      final String addedString = "Foo";
      list.add(addedString);

      final List<?> elements = _event.getElements();

      Assert.assertNotNull(_event);
      Assert.assertSame(list, _event.getSource());
      Assert.assertSame(ListEvent.Type.ADDED, _event.getType());
      Assert.assertEquals(0, _event.getFromIndex());
      Assert.assertEquals(1, _event.getToIndex());
      Assert.assertNotNull(elements);
      Assert.assertEquals(1, elements.size());
      Assert.assertSame(addedString, elements.get(0));
   }

   public void receiveEvent(@Observes final CDIListEvent event)
   {
      _event = event;
   }
}
