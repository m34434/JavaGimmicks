package net.sf.javagimmicks.collections.event.cdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.event.Observes;

import net.sf.javagimmicks.cdi.AnnotationLiteralHelper;
import net.sf.javagimmicks.cdi.testing.WeldJUnit4TestRunner;
import net.sf.javagimmicks.collections.event.CollectionEvent;
import net.sf.javagimmicks.collections.event.ObservableEventCollection;
import net.sf.javagimmicks.collections.event.cdi.qualifier.Funky;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIBridgeCollectionEventListenerTest
{
   private static Annotation FUNKY_LITERAL = AnnotationLiteralHelper.annotation(Funky.class);

   private static CDICollectionEvent _event;

   @Test
   public void testFireAndReceive()
   {
      final ObservableEventCollection<String> collection = new ObservableEventCollection<String>(
            new ArrayList<String>());
      CDIBridgeCollectionEventListener.install(collection, FUNKY_LITERAL);

      final String addedString = "Foo";
      collection.add(addedString);
      assertNotNull(_event);

      final Collection<?> addedElements = _event.getElements();

      assertSame(collection, _event.getSource());
      assertSame(CollectionEvent.Type.ADDED, _event.getType());
      assertEquals(1, addedElements.size());
      assertSame(addedString, addedElements.iterator().next());
   }

   public void receiveEvent(@Observes @Funky final CDICollectionEvent event)
   {
      _event = event;
   }
}
