package net.sf.javagimmicks.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import net.sf.javagimmicks.xml.DOMUtils;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DOMUtilsTest
{
   private Document _document;

   @Before
   public void setup() throws SAXException, IOException, ParserConfigurationException
   {
      _document = DOMUtils.parse(getResource("Example1.xml"));
   }

   @Test
   public void getChildElementsAll()
   {
      final List<Element> children = DOMUtils.getChildElements(_document.getDocumentElement());

      assertEquals(5, children.size());
      // for now only checking the first child
      assertEquals("child", children.get(0).getNodeName());
      assertEquals("No namespace", children.get(0).getAttribute("comment"));
   }

   @Test
   public void getChildElementsWithName()
   {
      final List<Element> children = DOMUtils.getChildElements(_document.getDocumentElement(), "child");
      assertEquals(3, children.size());

      for (final Element element : children)
      {
         assertEquals("child", element.getNodeName());
      }
   }

   @Test
   public void getChildElementsWithNamespace()
   {
      final List<Element> children = DOMUtils.getChildElements(_document.getDocumentElement(), "urn:ns1", "child");
      assertEquals(1, children.size());

      for (final Element element : children)
      {
         assertEquals("urn:ns1", element.getNamespaceURI());
         assertEquals("child", element.getLocalName());
      }
   }

   @Test
   public void getAttributes() throws XPathExpressionException
   {
      final List<Attr> attributes = DOMUtils.getAttributes((Element) DOMUtils.selectSingleNode(_document, "//child"));
      assertEquals(1, attributes.size());
      assertEquals("comment", attributes.get(0).getNodeName());
      assertEquals("No namespace", attributes.get(0).getValue());
   }

   @Test
   public void selectSingleNode() throws XPathExpressionException
   {
      final Node node = DOMUtils.selectSingleNode(_document, "//withSubChild");

      assertNotNull(node);
      assertEquals("withSubChild", node.getNodeName());
   }

   @Test
   public void newDocument() throws ParserConfigurationException
   {
      final Document document = DOMUtils.newDocument();
      assertNotNull(document);
      assertEquals(false, document.hasChildNodes());
   }

   @Test
   public void newDocumentWithNamespaceAndPrefix() throws ParserConfigurationException
   {
      final Document document = DOMUtils.newDocument("urn:ns1", "ns1", "test");
      assertNotNull(document);

      final Element oElement = document.getDocumentElement();
      assertEquals("ns1:test", oElement.getNodeName());
      assertEquals("urn:ns1", oElement.getNamespaceURI());
      assertEquals("test", oElement.getLocalName());
   }

   @Test
   public void newDocumentWithNamespace() throws ParserConfigurationException
   {
      final Document document = DOMUtils.newDocument("urn:ns1", "test");
      assertNotNull(document);

      final Element element = document.getDocumentElement();
      assertEquals("test", element.getNodeName());
      assertEquals("urn:ns1", element.getNamespaceURI());
   }

   @Test
   public void newDocumentWithName() throws ParserConfigurationException
   {
      final Document document = DOMUtils.newDocument("test");
      assertNotNull(document);

      final Element element = document.getDocumentElement();
      assertEquals("test", element.getNodeName());
      assertEquals(null, element.getNamespaceURI());
   }

   @Test
   public void serialize() throws ParserConfigurationException
   {
      final String content = DOMUtils.serialize(_document, false);
      assertNotNull(content);
      // TODO: check content
      System.out.println(content);
   }

   @Test
   public void serializeWithWriter() throws ParserConfigurationException
   {
      final StringWriter writer = new StringWriter();
      DOMUtils.serialize(_document, writer, false);

      final String content = writer.getBuffer().toString();
      assertNotNull(content);
      // TODO: check content
      System.out.println(content);
   }

   @Test
   public void serializeWithOutputStream() throws ParserConfigurationException
   {
      final ByteArrayOutputStream stream = new ByteArrayOutputStream();
      DOMUtils.serialize(_document, stream, false);

      final String content = new String(stream.toByteArray());
      assertNotNull(content);
      // TODO: check content
      System.out.println(content);
   }

   @Test
   public void selectNodes() throws XPathExpressionException
   {
      final List<Node> nodes = DOMUtils.selectNodes(_document, "//child[1]");

      assertEquals(1, nodes.size());
      for (final Node node : nodes)
      {
         assertEquals("SomeText", node.getNodeValue());
      }
   }

   @Test
   public void addElement()
   {
      final Element element = DOMUtils.addElement(_document.getDocumentElement(), "test");
      assertEquals("test", element.getNodeName());
      assertEquals("root", element.getParentNode().getLocalName());
   }

   @Test
   public void addAttribute()
   {
      final Element oEle = DOMUtils.addAttribute(_document.getDocumentElement(), "testAttr", "testVal");
      assertEquals("testVal", oEle.getAttribute("testAttr"));
   }

   private static InputStream getResource(final String reference)
   {
      return DOMUtilsTest.class.getResourceAsStream(reference);
   }
}
