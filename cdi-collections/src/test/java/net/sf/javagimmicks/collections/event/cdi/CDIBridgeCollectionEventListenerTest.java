package net.sf.javagimmicks.collections.event.cdi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import net.sf.javagimmicks.collections.event.cdi.qualifier.Groovy;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldJUnit4TestRunner.class)
public class CDIBridgeCollectionEventListenerTest
{
   private static Annotation FUNKY_LITERAL = AnnotationLiteralHelper.annotation(Funky.class);
   private static Annotation GROOVY_LITERAL = AnnotationLiteralHelper.annotation(Groovy.class);

   private static final String FOO = "Foo";

   private static CDICollectionEvent _event;

   private static CDICollectionEvent _funkyEvent;
   private static CDICollectionEvent _groovyEvent;

   @After
   public void cleanup()
   {
      _event = null;
      _funkyEvent = null;
      _groovyEvent = null;
   }

   @Test
   public void testFireAndReceive()
   {
      final ObservableEventCollection<String> collection = build();
      CDIBridgeCollectionEventListener.install(collection);

      collection.add(FOO);
      assertNotNull(_event);

      final Collection<?> addedElements = _event.getElements();

      assertSame(collection, _event.getSource());
      assertSame(CollectionEvent.Type.ADDED, _event.getType());
      assertEquals(1, addedElements.size());
      assertSame(FOO, addedElements.iterator().next());
   }

   @Test
   public void testQualifiers()
   {
      final ObservableEventCollection<String> funkyCollection = build();
      CDIBridgeCollectionEventListener.install(funkyCollection, FUNKY_LITERAL);

      final ObservableEventCollection<String> groovyCollection = build();
      CDIBridgeCollectionEventListener.install(groovyCollection, GROOVY_LITERAL);

      funkyCollection.add(FOO);
      assertNotNull(_funkyEvent);
      assertNotNull(_event);
      assertNull(_groovyEvent);
      _funkyEvent = null;
      _event = null;

      groovyCollection.add(FOO);
      assertNotNull(_groovyEvent);
      assertNotNull(_event);
      assertNull(_funkyEvent);
      _groovyEvent = null;
      _event = null;
   }

   private ObservableEventCollection<String> build()
   {
      return new ObservableEventCollection<String>(new ArrayList<String>());
   }

   // This will receive ALL CDICollectionEvents events - per CDI specification
   public static void receiveEvent(@Observes final CDICollectionEvent event)
   {
      _event = event;
   }

   public static void receiveFunkyEvent(@Observes @Funky final CDICollectionEvent event)
   {
      _funkyEvent = event;
   }

   public static void receiveGroovyEvent(@Observes @Groovy final CDICollectionEvent event)
   {
      _groovyEvent = event;
   }

}
