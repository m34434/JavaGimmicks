package net.sf.javagimmicks.xml.util;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Represents an XML namespace - the combination of a namespace URI and an
 * optional prefix.
 * <p>
 * Instances are cached and reused internally.
 */
public class Namespace implements Comparable<Namespace>
{
   private final String _nsURI;
   private final String _prefix;

   /**
    * The default instance for the empty namespace.
    */
   public static final Namespace NO_NAMESPACE = Namespace.get("", "");

   /**
    * Retrieves a new instance for the given prefix and URI.
    * 
    * @param prefix
    *           the (optional) prefix for the namespace
    * @param uri
    *           the URI for the namespace
    * @return an according {@link Namespace} instance
    */
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
      synchronized (DOMUtils._namespaceCache)
      {
         prefixMap = DOMUtils._namespaceCache.get(uri);
         if (prefixMap == null)
         {
            prefixMap = new HashMap<String, Namespace>();
            DOMUtils._namespaceCache.put(uri, prefixMap);
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

   /**
    * Retrieves a new instance for the given URI with an empty prefix.
    * 
    * @param uri
    *           the URI for the namespace
    * @return an according {@link Namespace} instance
    */
   public static Namespace get(final String uri)
   {
      return get("", uri);
   }

   /**
    * Retrieve the {@link Namespace} of a given {@link Element}.
    * 
    * @param element
    *           the {@link Element} to get the {@link Namespace} from
    * @return the {@link Namespace} of the {@link Element}
    */
   public static Namespace of(final Element element)
   {
      return get(element.getPrefix(), element.getNamespaceURI());
   }

   /**
    * Retrieve the {@link Namespace} of a given {@link Attr Attribute}.
    * 
    * @param attribute
    *           the {@link Attr Attribute} to get the {@link Namespace} from
    * @return the {@link Namespace} of the {@link Attr Attribute}
    */
   public static Namespace of(final Attr attribute)
   {
      return get(attribute.getPrefix(), attribute.getNamespaceURI());
   }

   private Namespace(final String uri, final String prefix)
   {
      _nsURI = uri;
      _prefix = prefix;
   }

   /**
    * Returns the namespace URI
    * 
    * @return the namespace URI
    */
   public String getUri()
   {
      return _nsURI;
   }

   /**
    * Returns the prefix
    * 
    * @return the prefix
    */
   public String getPrefix()
   {
      return _prefix;
   }

   /**
    * Applies the current prefix (if present) to a given local name and returns
    * the resulting XML qualified name.
    * 
    * @param localName
    *           the local name to use for building the qualified name
    * @return the resulting XML qualified name - matches the local name if no
    *         prefix is set on this {@link Namespace}
    */
   public String getQualifiedNameFor(final String localName)
   {
      return _prefix.length() == 0 ? localName : _prefix + ":" + localName;
   }

   /**
    * Applies the current prefix and URI to a given {@link Element}.
    * 
    * @param element
    *           the {@link Element} to modify
    * @return the modified {@link Element}
    */
   public Element applyTo(final Element element)
   {
      final String qualifiedName = getQualifiedNameFor(DOMUtils.getLocalName(element));
      final Document ownerDocument = element.getOwnerDocument();

      return (Element) ownerDocument.renameNode(element, _nsURI, qualifiedName);
   }

   /**
    * Applies the current prefix and URI to a given {@link Attr Attribute}.
    * 
    * @param attribute
    *           the {@link Attr Attribute} to modify
    * @return the modified {@link Attr Attribute}
    */
   public Attr applyTo(final Attr attribute)
   {
      final String qualifiedName = getQualifiedNameFor(DOMUtils.getLocalName(attribute));
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