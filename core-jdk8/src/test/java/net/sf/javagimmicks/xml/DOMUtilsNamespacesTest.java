package net.sf.javagimmicks.xml;

import static net.sf.javagimmicks.xml.DOMUtils.parse;
import static net.sf.javagimmicks.xml.DOMUtils.replaceNamespaces;
import static net.sf.javagimmicks.xml.DOMUtils.serialize;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;


import net.sf.javagimmicks.xml.Namespace;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DOMUtilsNamespacesTest
{
   private static final String EXAMPLE1_INPUT = "Example1.xml";
   private static final String EXAMPLE1_REF1 = "Example1_Ref1.xml";
   private static final String EXAMPLE1_REF2 = "Example1_Ref2.xml";
   private static final String EXAMPLE1_REF3 = "Example1_Ref3.xml";
   private static final String EXAMPLE1_REF4 = "Example1_Ref4.xml";
   private static final String EXAMPLE1_REF5 = "Example1_Ref5.xml";
   private static final String EXAMPLE1_REF6 = "Example1_Ref6.xml";
   private static final String EXAMPLE1_REF7 = "Example1_Ref7.xml";

   private static final Namespace NS_NS1 = Namespace.get("ns1", "urn:ns1");
   private static final Namespace NS_NS1_FAKE_1 = Namespace.get("ns1", "urn:ns1:fake");
   private static final Namespace NS_NS1_FAKE_2 = Namespace.get("ns1fake", "urn:ns1");

   private static final Namespace NS_NS2 = Namespace.get("ns2", "urn:ns2");

   private static final Namespace NS_OTHER = Namespace.get("other", "urn:other");
   private static final Namespace NS_EMTPY = Namespace.NO_NAMESPACE;

   private static Element _example1;
   private Element _converted;

   @BeforeClass
   public static void setUpClass() throws Exception
   {
      _example1 = parse(getResource(EXAMPLE1_INPUT)).getDocumentElement();
   }

   @Test
   public void testNamespaceConversion_Normal_1() throws Exception
   {
      executeStandardConversionTest(_example1, EXAMPLE1_REF1, NS_NS1, NS_OTHER, true, true);
   }

   @Test
   public void testNamespaceConversion_Normal_2() throws Exception
   {
      executeStandardConversionTest(_example1, EXAMPLE1_REF2, NS_NS1, NS_OTHER, true, false);
   }

   @Test
   public void testNamespaceConversion_Normal_3() throws Exception
   {
      executeStandardConversionTest(_example1, EXAMPLE1_REF3, NS_NS1, NS_OTHER, false, true);
   }

   @Test
   public void testNamespaceConversion_Normal_4() throws Exception
   {
      executeStandardConversionTest(_example1, EXAMPLE1_REF4, NS_NS1, NS_OTHER, false, false);
   }

   @Test
   public void testNamespaceConversion_Normal_5() throws Exception
   {
      executeStandardConversionTest(_example1, EXAMPLE1_REF5, NS_EMTPY, NS_OTHER, true, false);
   }

   @Test
   public void testNamespaceConversion_Normal_6() throws Exception
   {
      executeStandardConversionTest(_example1, EXAMPLE1_REF6, NS_EMTPY, NS_NS2, true, false);
   }

   @Test
   public void testNamespaceConversion_Normal_7() throws Exception
   {
      executeStandardConversionTest(_example1, EXAMPLE1_REF7, NS_NS1, NS_NS2, true, true);
   }

   @Test
   public void testNamespaceConversion_Fake_1() throws Exception
   {
      executeStandardConversionTest(_example1, EXAMPLE1_INPUT, NS_NS1_FAKE_1, NS_OTHER, true, true);
   }

   @Test
   public void testNamespaceConversion_Fake_2() throws Exception
   {
      executeStandardConversionTest(_example1, EXAMPLE1_INPUT, NS_NS1_FAKE_2, NS_OTHER, true, true);
   }

   private void executeStandardConversionTest(final Element sourceElement, final String referenceFile,
         final Namespace sourceNamespace, final Namespace targetNamespace, final boolean convertElements,
         final boolean convertAttributes)
         throws Exception
   {
      // Convert the source document
      _converted = clone(sourceElement);
      _converted = replaceNamespaces(_converted, sourceNamespace, targetNamespace, convertElements,
            convertAttributes);

      // Parse the reference file
      final Element referenceElement = parse(getResource(referenceFile)).getDocumentElement();

      // Compare the contents
      assertEqualsXML(serialize(referenceElement, false), serialize(_converted, false));
   }

   private static InputStream getResource(final String sReference)
   {
      return DOMUtilsNamespacesTest.class.getResourceAsStream(sReference);
   }

   @SuppressWarnings("unchecked")
   private static <N extends Node> N clone(final N node)
   {
      return (N) node.cloneNode(true);
   }

   private static void assertEqualsXML(final String reference, final String actual) throws SAXException, IOException,
         ParserConfigurationException
   {
      final Diff compareXML = XMLUnit.compareXML(reference, actual);
      assertTrue(compareXML.toString(), compareXML.identical());
   }
}