/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.util;

import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.xml.sax.*;

import javax.jcr.StringValue;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: DocNodeImporter.java,v 1.6 2004/08/18 23:07:00 benjmestrallet Exp $
 */

// WARNING: Experimantal implementation
public class DocNodeImporter implements ContentHandler {

  Stack tree;
  String[] uris;

  private DocNodeImporter(NodeImpl parent, String[] URIs) {
    tree = new Stack();
    tree.push(parent);
    uris = URIs;
  }

  private boolean isAllowedUri(String uri) {
    for (int i = 0; i < uris.length; i++)
      if (uris[i].equals(uri))
        return true;
    return false;
  }

  public static void fillNode(NodeImpl parent, InputStream stream, String[] nsURIs)
      throws IOException, SAXException {
    DocNodeImporter importer = new DocNodeImporter(parent, nsURIs);
    XMLReader reader;
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      factory.setNamespaceAware(true);
      reader = factory.newSAXParser().getXMLReader();

    } catch (Exception e) {
      throw new SAXException(e.getMessage());
    }
    reader.setContentHandler(importer);
    reader.parse(new InputSource(stream));
  }

  public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
      throws SAXException {
    //TODO manage UUID
    //TODO manage auto created properties
    NodeImpl parent = (NodeImpl) tree.peek();
    if("root".equals(qName))
      return;
    NodeImpl node;
    HashMap props = new HashMap();
    String nodeType = "nt:default";

    if (atts != null) {
      int length = atts.getLength();
      for (int i = 0; i < length; i++) {
        String propName;
        String attrQName = atts.getQName(i);
        if (!"".equals(attrQName))
          propName = attrQName;
        else
          propName = atts.getLocalName(i);

        if (propName.equals("jcr:primaryType"))
          nodeType = atts.getValue(i);
        else
          props.put(propName, atts.getValue(i));
      }
    }

    try {
      node = (NodeImpl) parent.addNode(qName, nodeType);
      Iterator keys = props.keySet().iterator();
      while (keys.hasNext()) {
        String key = (String) keys.next();
        String value = (String) props.get(key);
        node.setProperty(key, new StringValue(value));
      }
    } catch (Exception e) {
      throw new SAXException(e.getMessage(), e);
    }
    tree.push(node);
  }

  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    tree.pop();
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
  }

  public void setDocumentLocator(Locator locator) {
  }

  public void startDocument() throws SAXException {
  }

  public void endDocument() throws SAXException {
  }

  public void startPrefixMapping(String prefix, String uri) throws SAXException {
  }

  public void endPrefixMapping(String prefix) throws SAXException {
  }

  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
  }

  public void processingInstruction(String target, String data) throws SAXException {
  }

  public void skippedEntity(String name) throws SAXException {
  }
}
