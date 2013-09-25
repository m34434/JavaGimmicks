package net.sf.javagimmicks.xml;

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

/**
 * Provides several helper methods for advances dealing with W3C XML
 * {@link Document}s.
 */
public class DOMUtils
{
   private static final String PARAM_PRETTY_PRINT = "format-pretty-print";

   private static DocumentBuilderFactory _dbf = DocumentBuilderFactory.newInstance();
   private static XPathFactory _xpf = XPathFactory.newInstance();

   static final Map<String, Map<String, Namespace>> _namespaceCache = new HashMap<String, Map<String, Namespace>>();

   static
   {
      _dbf.setNamespaceAware(true);
   }

   private DOMUtils()
   {}

   /**
    * Retrieves all child {@link Element}s from a given parent {@link Element}
    * as {@link List}.
    * 
    * @param element
    *           the parent {@link Element}
    * @return the {@link List} of child {@link Element}s
    */
   public static List<Element> getChildElements(final Element element)
   {
      final List<Element> result = new ArrayList<Element>();

      for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
      {
         if (!(child instanceof Element))
         {
            continue;
         }

         result.add((Element) child);
      }

      return result;
   }

   /**
    * Retrieves all child {@link Element}s of a given namespace URI and local
    * name from a given parent {@link Element} as {@link List}.
    * 
    * @param element
    *           the parent {@link Element}
    * @param namespaceURI
    *           the namespace URI of the child {@link Element}s to retrieve
    * @param localName
    *           the local name of the child {@link Element}s to retrieve
    * @return the respective {@link List} of child {@link Element}s
    */
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

   /**
    * Retrieves all child {@link Element}s of a given local name from a given
    * parent {@link Element} as {@link List}.
    * 
    * @param element
    *           the parent {@link Element}
    * @param localName
    *           the local name of the child {@link Element}s to retrieve
    * @return the respective {@link List} of child {@link Element}s
    */
   public static List<Element> getChildElements(final Element element, final String localName)
   {
      final List<Element> result = new ArrayList<Element>();

      for (Node child = element.getFirstChild(); child != null; child = child.getNextSibling())
      {
         if (!(child instanceof Element))
         {
            continue;
         }

         if (!localName.equals(child.getNodeName()))
         {
            continue;
         }

         result.add((Element) child);
      }

      return result;
   }

   /**
    * Retrieves all {@link Attr Attribute}s from a given parent {@link Element}
    * as {@link List}.
    * 
    * @param element
    *           the parent {@link Element}
    * @return the {@link List} of {@link Attr Attribute}s
    */
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

   /**
    * Creates and returns a new {@link DocumentBuilder}.
    * 
    * @return a new {@link DocumentBuilder}
    * @throws ParserConfigurationException
    *            if the creation fails
    */
   public static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException
   {
      return _dbf.newDocumentBuilder();
   }

   /**
    * Creates and returns a new {@link Document}.
    * 
    * @return a new {@link Document}
    * @throws ParserConfigurationException
    *            if the creation fails
    */
   public static Document newDocument() throws ParserConfigurationException
   {
      return newDocumentBuilder().newDocument();
   }

   /**
    * Creates and returns a new {@link Document} with a root {@link Element}
    * according to the given parameters.
    * 
    * @param nsUri
    *           the namespace URI for the root {@link Element}
    * @param prefix
    *           the prefix for the root {@link Element}
    * @param localName
    *           the local name for the root {@link Element}
    * @return a new according {@link Document}
    * @throws ParserConfigurationException
    *            if the creation fails
    */
   public static Document newDocument(final String nsUri, final String prefix, final String localName)
         throws ParserConfigurationException
   {
      final String qualifiedName = (prefix == null ? "" : (prefix + ":")) + localName;

      return getDOMImplementation().createDocument(nsUri, qualifiedName, null);
   }

   /**
    * Creates and returns a new {@link Document} with a root {@link Element}
    * according to the given parameters.
    * 
    * @param nsUri
    *           the namespace URI for the root {@link Element}
    * @param localName
    *           the local name for the root {@link Element}
    * @return a new according {@link Document}
    * @throws ParserConfigurationException
    *            if the creation fails
    */
   public static Document newDocument(final String nsUri, final String localName)
         throws ParserConfigurationException
   {
      return newDocument(nsUri, null, localName);
   }

   /**
    * Creates and returns a new {@link Document} with a root {@link Element}
    * according to the given parameters.
    * 
    * @param localName
    *           the local name URI for the root {@link Element}
    * @return a new according {@link Document}
    * @throws ParserConfigurationException
    *            if the creation fails
    */
   public static Document newDocument(final String localName) throws ParserConfigurationException
   {
      return newDocument(null, localName);
   }

   /**
    * Creates and returns a new {@link DOMImplementation}.
    * 
    * @return a new {@link DOMImplementation}
    * @throws ParserConfigurationException
    *            if the creation fails
    */
   public static DOMImplementation getDOMImplementation() throws ParserConfigurationException
   {
      return newDocumentBuilder().getDOMImplementation();
   }

   /**
    * Parses a new XML {@link Document} from a given {@link InputSource}
    * 
    * @param inputSource
    *           the {@link InputSource} to parse from
    * @return the parsed {@link Document}
    * @throws SAXException
    *            if the XML source is not valid
    * @throws IOException
    *            if the source could not be read
    * @throws ParserConfigurationException
    *            if the parser could not be created
    */
   public static Document parse(final InputSource inputSource) throws SAXException, IOException,
         ParserConfigurationException
   {
      return newDocumentBuilder().parse(inputSource);
   }

   /**
    * Parses a new XML {@link Document} from a given {@link InputStream}
    * 
    * @param inputStream
    *           the {@link InputStream} to parse from
    * @return the parsed {@link Document}
    * @throws SAXException
    *            if the XML source is not valid
    * @throws IOException
    *            if the source could not be read
    * @throws ParserConfigurationException
    *            if the parser could not be created
    */
   public static Document parse(final InputStream inputStream) throws SAXException, IOException,
         ParserConfigurationException
   {
      return parse(new InputSource(inputStream));
   }

   /**
    * Parses a new XML {@link Document} from a given {@link Reader}
    * 
    * @param reader
    *           the {@link Reader} to parse from
    * @return the parsed {@link Document}
    * @throws SAXException
    *            if the XML source is not valid
    * @throws IOException
    *            if the source could not be read
    * @throws ParserConfigurationException
    *            if the parser could not be created
    */
   public static Document parse(final Reader reader) throws SAXException, IOException, ParserConfigurationException
   {
      return parse(new InputSource(reader));
   }

   /**
    * Parses a new XML {@link Document} from a given XML {@link String}.
    * 
    * @param xmlContent
    *           the XML {@link String} to parse from
    * @return the parsed {@link Document}
    * @throws SAXException
    *            if the XML source is not valid
    * @throws IOException
    *            if the source could not be read
    * @throws ParserConfigurationException
    *            if the parser could not be created
    */
   public static Document parse(final String xmlContent) throws SAXException, IOException,
         ParserConfigurationException
   {
      return parse(new StringReader(xmlContent));
   }

   /**
    * Serializes a given XML {@link Node} into a {@link String} with optionally
    * pretty-printing.
    * 
    * @param node
    *           the {@link Node} to serialize
    * @param prettyPrint
    *           if pretty-printing should be applied
    * @return the serialized XML {@link String}
    * @throws ParserConfigurationException
    *            if the serializer could not be created
    */
   public static String serialize(final Node node, final boolean prettyPrint) throws ParserConfigurationException
   {
      final DOMImplementationLS domImpl = (DOMImplementationLS) getDOMImplementation();

      final LSSerializer serializer = domImpl.createLSSerializer();
      serializer.getDomConfig().setParameter(PARAM_PRETTY_PRINT, prettyPrint);
      return serializer.writeToString(node);
   }

   /**
    * Serializes a given XML {@link Node} into a {@link Writer} with optionally
    * pretty-printing.
    * 
    * @param node
    *           the {@link Node} to serialize
    * @param writer
    *           the {@link Writer} where the {@link Node} should be serialized
    * @param prettyPrint
    *           if pretty-printing should be applied
    * @throws ParserConfigurationException
    *            if the serializer could not be created
    */
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

   /**
    * Serializes a given XML {@link Node} into a {@link OutputStream} with
    * optionally pretty-printing.
    * 
    * @param node
    *           the {@link Node} to serialize
    * @param outputStream
    *           the {@link OutputStream} where the {@link Node} should be
    *           serialized
    * @param prettyPrint
    *           if pretty-printing should be applied
    * @throws ParserConfigurationException
    *            if the serializer could not be created
    */
   public static void serialize(final Node node, final OutputStream outputStream, final boolean prettyPrint)
         throws ParserConfigurationException
   {
      serialize(node, new OutputStreamWriter(outputStream), prettyPrint);
   }

   /**
    * Adds a new child {@link Element} of the given namespaceUri, prefix and
    * local name to a given parent {@link Element}.
    * 
    * @param parent
    *           the parent {@link Element} where to add a new child
    * @param namespaceUri
    *           the namespace URI for the new {@link Element}
    * @param prefix
    *           the prefix for the new {@link Element}
    * @param localName
    *           the local name for the new {@link Element}
    * @return the new child {@link Element}
    */
   public static Element addElement(final Element parent, final String namespaceUri, final String prefix,
         final String localName)
   {
      final Element child = parent.getOwnerDocument().createElementNS(namespaceUri,
            NameUtils.buildQualifiedName(prefix, localName));

      parent.appendChild(child);

      return child;
   }

   /**
    * Adds a new child {@link Element} of the given namespaceUri and local name
    * to a given parent {@link Element}.
    * 
    * @param parent
    *           the parent {@link Element} where to add a new child
    * @param namespaceUri
    *           the namespace URI for the new {@link Element}
    * @param localName
    *           the local name for the new {@link Element}
    * @return the new child {@link Element}
    */
   public static Element addElement(final Element parent, final String namespaceUri, final String localName)
   {
      return addElement(parent, namespaceUri, null, localName);
   }

   /**
    * Adds a new child {@link Element} of the given local name to a given parent
    * {@link Element}.
    * 
    * @param parent
    *           the parent {@link Element} where to add a new child
    * @param localName
    *           the local name (tag name) for the new {@link Element}
    * @return the new child {@link Element}
    */
   public static Element addElement(final Element parent, final String localName)
   {
      final Element child = parent.getOwnerDocument().createElement(localName);

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

   static String getLocalName(final Node node)
   {
      final String result = node.getLocalName();
      return result != null ? result : node.getNodeName();
   }
}
