package net.sf.javagimmicks.xml.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMUtils
{
   private static final String PARAM_PRETTY_PRINT = "format-pretty-print";

   private static DocumentBuilderFactory _dbf = DocumentBuilderFactory.newInstance();
   private static XPathFactory _xpf = XPathFactory.newInstance();

   private static final Map<String, Map<String, Namespace>> _ns = new HashMap<String, Map<String, Namespace>>();

   static
   {
      _dbf.setNamespaceAware(true);
   }

   public static List<Element> getChildElements(final Element element)
   {
      final List<Element> oResult = new ArrayList<Element>();

      for (Node oChild = element.getFirstChild(); oChild != null; oChild = oChild.getNextSibling())
      {
         if (!(oChild instanceof Element))
         {
            continue;
         }

         oResult.add((Element) oChild);
      }

      return oResult;
   }

   public static List<Element> getChildElements(final Element element, final String namespaceURI,
         final String localName)
   {
      final List<Element> result = new ArrayList<Element>();

      for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
      {
         if (!(child instanceof Element))
         {
            continue;
         }

         if (namespaceURI != null && !namespaceURI.equals(child.getNamespaceURI()))
         {
            continue;
         }

         if (!localName.equals(child.getLocalName()))
         {
            continue;
         }

         result.add((Element) child);
      }

      return result;
   }

   public static List<Element> getChildElements(final Element element, final String name)
   {
      final List<Element> result = new ArrayList<Element>();

      for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
      {
         if (!(child instanceof Element))
         {
            continue;
         }

         if (!name.equals(child.getNodeName()))
         {
            continue;
         }

         result.add((Element) child);
      }

      return result;
   }

   public static List<Attr> getAttributes(final Element element)
   {
      final NamedNodeMap attributes = element.getAttributes();

      return new AbstractList<Attr>()
      {
         @Override
         public Attr get(final int index)
         {
            return (Attr) attributes.item(index);
         }

         @Override
         public int size()
         {
            return attributes.getLength();
         }
      };
   }

   public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException
   {
      return _dbf.newDocumentBuilder();
   }

   public static Document newDocument() throws ParserConfigurationException
   {
      return newDocumentBuilder().newDocument();
   }

   public static Document newDocument(final String nsUri, final String prefix, final String elementName)
         throws ParserConfigurationException
   {
      final String qualifiedName = (prefix == null ? "" : (prefix + ":")) + elementName;

      return getDOMImplementation().createDocument(nsUri, qualifiedName, null);
   }

   public static Document newDocument(final String nsUri, final String elementName)
         throws ParserConfigurationException
   {
      return newDocument(nsUri, null, elementName);
   }

   public static Document newDocument(final String elementName) throws ParserConfigurationException
   {
      return newDocument(null, elementName);
   }

   public static DOMImplementation getDOMImplementation() throws ParserConfigurationException
   {
      return newDocumentBuilder().getDOMImplementation();
   }

   public static Document parse(final InputSource inputSource) throws SAXException, IOException,
         ParserConfigurationException
   {
      return newDocumentBuilder().parse(inputSource);
   }

   public static Document parse(final InputStream inputStream) throws SAXException, IOException,
         ParserConfigurationException
   {
      return parse(new InputSource(inputStream));
   }

   public static Document parse(final Reader reader) throws SAXException, IOException, ParserConfigurationException
   {
      return parse(new InputSource(reader));
   }

   public static Document parse(final String xmlContent) throws SAXException, IOException,
         ParserConfigurationException
   {
      return parse(new StringReader(xmlContent));
   }

   public static String serialize(final Node node, final boolean prettyPrint) throws ParserConfigurationException
   {
      final DOMImplementationLS domImpl = (DOMImplementationLS) getDOMImplementation();

      final LSSerializer serializer = domImpl.createLSSerializer();
      serializer.getDomConfig().setParameter(PARAM_PRETTY_PRINT, prettyPrint);
      return serializer.writeToString(node);
   }

   public static void serialize(final Node node, final Writer writer, final boolean prettyPrint)
         throws ParserConfigurationException
   {
      final DOMImplementationLS domImpl = (DOMImplementationLS) newDocumentBuilder().getDOMImplementation();

      final LSSerializer serializer = domImpl.createLSSerializer();
      serializer.getDomConfig().setParameter(PARAM_PRETTY_PRINT, prettyPrint);

      final LSOutput output = domImpl.createLSOutput();
      output.setCharacterStream(writer);

      serializer.write(node, output);
   }

   public static void serialize(final Node node, final OutputStream outputStream, final boolean prettyPrint)
         throws ParserConfigurationException
   {
      serialize(node, new OutputStreamWriter(outputStream), prettyPrint);
   }

   public static Element addElement(final Element parent, final String tagName)
   {
      final Element child = parent.getOwnerDocument().createElement(tagName);

      parent.appendChild(child);

      return child;
   }

   public static Element addAttribute(final Element element, final String attributeName, final String value)
   {
      element.setAttribute(attributeName, value);

      return element;
   }

   public static List<Node> wrapNodeList(final NodeList nodeList)
   {
      return new AbstractList<Node>()
      {
         @Override
         public Node get(final int index)
         {
            return nodeList.item(index);
         }

         @Override
         public int size()
         {
            return nodeList.getLength();
         }
      };
   }

   public static Node selectSingleNode(final Node node, final String xPath) throws XPathExpressionException
   {
      return (Node) _xpf.newXPath().evaluate(xPath, node, XPathConstants.NODE);
   }

   public static List<Node> selectNodes(final Node node, final String xPath) throws XPathExpressionException
   {
      return wrapNodeList((NodeList) _xpf.newXPath().evaluate(xPath, node, XPathConstants.NODE));
   }

   public static Element replaceNamespaces(Element element, final Map<Namespace, Namespace> namespaceMap,
         final boolean exchangeElementNames, final boolean exchangeAttributeNames)
   {
      // Handle the element name
      if (exchangeElementNames)
      {
         // Get the Namespace from it and lookup the replacement in the map
         final Namespace elementNamespace = Namespace.of(element);
         final Namespace newNamespace = namespaceMap.get(elementNamespace);

         // If there is a replacement, set the new QName
         if (newNamespace != null)
         {
            element = newNamespace.applyTo(element);
         }
      }

      // Iterate over all Namespaces declared in this element
      // TODO: Check how/if this can be done with W3C
      // List<Namespace> oDelcaredNamespaces = new
      // ArrayList<Namespace>(oElement.declaredNamespaces());
      // for(Namespace oDeclaredNamespace : oDelcaredNamespaces)
      // {
      // // Lookup the replacement in the map
      // Namespace oNewDeclaredNamespace =
      // oNamespaceMap.get(oDeclaredNamespace);
      //
      // // If there is a replacement, make the exchange
      // if(oNewDeclaredNamespace != null)
      // {
      // oElement.remove(oDeclaredNamespace);
      // oElement.add(oNewDeclaredNamespace);
      // }
      // }

      // Handle the attribute names
      if (exchangeAttributeNames)
      {
         for (final Attr attribute : getAttributes(element))
         {
            // Get the Namespace from the attribute and lookup the replacement
            // in the map
            final Namespace attributeNamespace = Namespace.of(attribute);
            final Namespace newAttributeNamespace = namespaceMap.get(attributeNamespace);

            // If there is a replacement, replace the whole attribute
            if (newAttributeNamespace != null)
            {
               newAttributeNamespace.applyTo(attribute);
            }
         }
      }

      // Make the recursive call
      for (final Element childElement : getChildElements(element))
      {
         replaceNamespaces(childElement, namespaceMap, exchangeElementNames, exchangeAttributeNames);
      }

      element.getOwnerDocument().normalizeDocument();

      return element;
   }

   public static Element replaceNamespaces(final Element element, final Namespace fromNs, final Namespace toNs)
   {
      // Replace element names but not attribute names
      return replaceNamespaces(element, fromNs, toNs, true, false);
   }

   public static Element replaceNamespaces(final Element element, final Namespace fromNs, final Namespace toNs,
         final boolean exchangeElementNames, final boolean exchangeAttributeNames)
   {
      return replaceNamespaces(element, Collections.singletonMap(fromNs, toNs), exchangeElementNames,
            exchangeAttributeNames);
   }

   public static Element replaceNamespaces(final Element element, final String fromURI, final Namespace toNs,
         final boolean exchangeElementNames, final boolean exchangeAttributeNames)
   {
      final Map<Namespace, Namespace> namespaceMap = new HashMap<Namespace, Namespace>();
      for (final Namespace fromNs : findNamespaces(element, fromURI))
      {
         namespaceMap.put(fromNs, toNs);
      }

      return replaceNamespaces(element, namespaceMap, exchangeElementNames,
            exchangeAttributeNames);
   }

   public static Element replaceNamespaces(final Element element, final String fromURI, final Namespace toNs)
   {
      return replaceNamespaces(element, fromURI, toNs, true, false);
   }

   public static Element replaceNamespaces(final Element element, final Map<Namespace, Namespace> namespaceMap)
   {
      // Replace element names but not attribute names
      return replaceNamespaces(element, namespaceMap, true, false);
   }

   public static Set<Namespace> findNamespaces(final Element element, final String fromURI)
   {
      final Set<Namespace> oResult = new HashSet<Namespace>();

      findNamespaces(element, fromURI, oResult);

      return oResult;
   }

   private static void findNamespaces(final Element element, String uri, final Set<Namespace> namespaces)
   {
      if (uri == null)
      {
         uri = "";
      }

      final String currentUri = element.getNamespaceURI();

      if ((currentUri == null && uri.length() == 0) || uri.equals(currentUri))
      {
         namespaces.add(Namespace.of(element));
      }

      // TODO: lookupPrefix delivers at maximum ONE prefix even though more
      // might exist for the given URI! Is this a problem?
      final String prefix = element.lookupPrefix(uri);
      if (prefix != null)
      {
         namespaces.add(Namespace.get(prefix, uri));
      }

      for (final Element childElement : getChildElements(element))
      {
         findNamespaces(childElement, uri, namespaces);
      }
   }

   private static String getLocalName(final Node node)
   {
      final String result = node.getLocalName();
      return result != null ? result : node.getNodeName();
   }

   public static class Namespace implements Comparable<Namespace>
   {
      private final String _nsURI;
      private final String _prefix;

      public static final Namespace NO_NAMESPACE = Namespace.get("", "");

      public static Namespace get(String prefix, String uri)
      {
         if (prefix == null)
         {
            prefix = "";
         }

         if (uri == null)
         {
            uri = "";
         }

         Map<String, Namespace> prefixMap;
         synchronized (_ns)
         {
            prefixMap = _ns.get(uri);
            if (prefixMap == null)
            {
               prefixMap = new HashMap<String, Namespace>();
               _ns.put(uri, prefixMap);
            }
         }

         Namespace result;
         synchronized (prefixMap)
         {
            result = prefixMap.get(prefix);
            if (result == null)
            {
               result = new Namespace(uri, prefix);
               prefixMap.put(prefix, result);
            }
         }

         return result;
      }

      public static Namespace get(final String uri)
      {
         return get("", uri);
      }

      public static Namespace of(final Element element)
      {
         return get(element.getPrefix(), element.getNamespaceURI());
      }

      public static Namespace of(final Attr attribute)
      {
         return get(attribute.getPrefix(), attribute.getNamespaceURI());
      }

      private Namespace(final String uri, final String prefix)
      {
         _nsURI = uri;
         _prefix = prefix;
      }

      public String getUri()
      {
         return _nsURI;
      }

      public String getPrefix()
      {
         return _prefix;
      }

      public String getQualifiedNameFor(final String localName)
      {
         return _prefix.length() == 0 ? localName : _prefix + ":" + localName;
      }

      public Element applyTo(final Element element)
      {
         final String qualifiedName = getQualifiedNameFor(getLocalName(element));
         final Document ownerDocument = element.getOwnerDocument();

         return (Element) ownerDocument.renameNode(element, _nsURI, qualifiedName);
      }

      public Attr applyTo(final Attr attribute)
      {
         final String qualifiedName = getQualifiedNameFor(getLocalName(attribute));
         final Document ownerDocument = attribute.getOwnerDocument();

         return (Attr) ownerDocument.renameNode(attribute, _nsURI, qualifiedName);
      }

      @Override
      public int hashCode()
      {
         final int prime = 17;
         int result = -247982732;
         result = prime * result + ((_prefix == null) ? 0 : _prefix.hashCode());
         result = prime * result + ((_nsURI == null) ? 0 : _nsURI.hashCode());
         return result;
      }

      @Override
      public boolean equals(final Object obj)
      {
         if (this == obj)
         {
            return true;
         }
         if (obj == null)
         {
            return false;
         }
         if (getClass() != obj.getClass())
         {
            return false;
         }

         final Namespace other = (Namespace) obj;
         if (_prefix == null)
         {
            if (other._prefix != null)
            {
               return false;
            }
         }
         else if (!_prefix.equals(other._prefix))
         {
            return false;
         }
         if (_nsURI == null)
         {
            if (other._nsURI != null)
            {
               return false;
            }
         }
         else if (!_nsURI.equals(other._nsURI))
         {
            return false;
         }
         return true;
      }

      @Override
      public int compareTo(final Namespace o)
      {
         final int result = _nsURI.compareTo(o._nsURI);

         return result != 0 ? result : _prefix.compareTo(o._prefix);
      }
   }
}
