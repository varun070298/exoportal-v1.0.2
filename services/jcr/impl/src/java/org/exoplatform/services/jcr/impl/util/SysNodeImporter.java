/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.util;

import org.xml.sax.*;
import org.apache.commons.codec.binary.Base64;
import org.exoplatform.services.jcr.impl.core.NodeImpl;

import javax.jcr.*;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.Stack;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: SysNodeImporter.java,v 1.8 2004/08/23 15:28:47 benjmestrallet Exp $
 */

public class SysNodeImporter implements ContentHandler {

  private Stack tree;
  private String propertyName;
  private int propertyType;
  private NodeImpl currentNode;


  private SysNodeImporter(String rootPath, Ticket ticket) throws RepositoryException {
    tree = new Stack();
    tree.push(ticket.getNodeByAbsPath(rootPath));
  }

  public static void fillItems(String rootPath, InputStream stream, Ticket ticket)
      throws IOException, SAXException, RepositoryException {
    SysNodeImporter importer = new SysNodeImporter(rootPath, ticket);
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

  public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
    NodeImpl parent = (NodeImpl) tree.peek();
    String name = (atts.getValue("sv:name") == null) ? atts.getValue("name") : atts.getValue("sv:name");
    if("sv:root".equals(name)){
      return;
    }
    String curPath = parent.getPath() + (parent.getPath().endsWith("/") ? "" : "/") + name;
    if (qName.equals("sv:node")) {
      try {
        Node node = parent.addNode(curPath);
        tree.push(node);
      } catch (Exception e) {
        throw new SAXException(e.getMessage());
      }
    } else if (qName.equals("sv:property")) {
      //TODO manage UUID      
      try {
        String st = atts.getValue("sv:type");
        st = getType(st.substring(3));
        currentNode = parent;
        propertyName = name;
        propertyType = PropertyType.valueFromName(st);
      } catch (Exception e) {
        throw new SAXException(e.getMessage(), e);
      }
    } else
      throw new SAXException("Only sv:node and sv:property are allowed!");
  }

  private String getType(String name) {
    if (name.equalsIgnoreCase(PropertyType.TYPENAME_STRING)) {
      return PropertyType.TYPENAME_STRING;
    } else if (name.equalsIgnoreCase(PropertyType.TYPENAME_BINARY)) {
      return PropertyType.TYPENAME_BINARY;
    } else if (name.equalsIgnoreCase(PropertyType.TYPENAME_BOOLEAN)) {
      return PropertyType.TYPENAME_BOOLEAN;
    } else if (name.equalsIgnoreCase(PropertyType.TYPENAME_LONG)) {
      return PropertyType.TYPENAME_LONG;
    } else if (name.equalsIgnoreCase(PropertyType.TYPENAME_DOUBLE)) {
      return PropertyType.TYPENAME_DOUBLE;
    } else if (name.equalsIgnoreCase(PropertyType.TYPENAME_DATE)) {
      return PropertyType.TYPENAME_DATE;
    } else if (name.equalsIgnoreCase(PropertyType.TYPENAME_SOFTLINK)) {
      return PropertyType.TYPENAME_SOFTLINK;
    } else if (name.equalsIgnoreCase(PropertyType.TYPENAME_REFERENCE)) {
      return PropertyType.TYPENAME_REFERENCE;
    } else {
      throw new IllegalArgumentException("unknown type: " + name);
    }
  }

  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    if (qName.equals("sv:node"))
      tree.pop();
    else if (qName.equals("sv:property")) {
      propertyName = null;
      currentNode = null;
      propertyType = -1;
    }
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
    if (propertyName != null) {
      StringBuffer str = new StringBuffer();
      str.append(ch, start, length);
      try {
        if(propertyType == PropertyType.BINARY){
          currentNode.setProperty(propertyName,
              new ByteArrayInputStream(Base64.decodeBase64(str.toString().getBytes())));
        } else {
          currentNode.setProperty(propertyName, new StringValue(new String(str)), propertyType);
        }
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
      }
    }
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
