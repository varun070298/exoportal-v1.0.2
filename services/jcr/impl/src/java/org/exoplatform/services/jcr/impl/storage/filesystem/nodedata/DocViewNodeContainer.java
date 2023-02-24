/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.filesystem.nodedata;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.jcr.ItemExistsException;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.StringValue;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.impl.core.NamespaceRegistryImpl;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.jcr.impl.core.PropertyImpl;
import org.exoplatform.services.jcr.impl.util.DocNodeExporter;
import org.exoplatform.services.jcr.impl.util.XMLWriter;
import org.exoplatform.services.xml.querying.InvalidSourceException;
import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.QueryRunTimeException;
import org.exoplatform.services.xml.querying.Statement;
import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLFragmentData;
import org.exoplatform.services.xml.querying.XMLQuery;
import org.exoplatform.services.xml.querying.XMLQueryingService;
import org.exoplatform.services.xml.querying.XMLWellFormedData;
import org.exoplatform.services.xml.querying.helper.SimpleStatementHelper;
import org.exoplatform.services.xml.querying.helper.XMLDataManager;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: DocViewNodeContainer.java,v 1.13 2004/09/03 09:59:40 geaz Exp $
 */

public class DocViewNodeContainer extends BaseNodeContainer {
  private String resourceId;
  private XMLQuery query;
  private SimpleStatementHelper statHelper;
  private XMLDataManager xmlDataManager;
  private NamespaceRegistryImpl namespaceRegistry;

  public DocViewNodeContainer(String jcrPath, String resourceId) throws RepositoryException {
     this.namespaceRegistry = (NamespaceRegistryImpl) PortalContainer.getInstance().
        getComponentInstancesOfType(NamespaceRegistry.class);
    this.containerPath = jcrPath;
    this.nodeType = "exo:jcrdocfile";
    this.resourceId = resourceId;
    this.storage = new File(resourceId);
    try {
      XMLQueryingService qService = (XMLQueryingService) PortalContainer.getInstance().getComponentInstanceOfType(XMLQueryingService.class);
      query = qService.createQuery();
      statHelper = qService.createStatementHelper();
      xmlDataManager = qService.createXMLDataManager();
    } catch (Exception e) {
      throw new RepositoryException("DocViewNodeContainer() failed. Reason: " + e.getMessage());
    }
  }

  public NodeData getNodeByPath(String relPath) {
    if (relPath.length() == 0)
      return getRootNode();
    try {
      XMLWellFormedData result = getNodeData(jcrToXPathNode(relPath));
      org.w3c.dom.Element dom = ((org.w3c.dom.Document) result.getAsDOM()).getDocumentElement();
      NamedNodeMap attrs = dom.getAttributes();
      ArrayList props = new ArrayList();
      for (int i = 0; i < attrs.getLength(); i++) {
        String name = ((Attr) attrs.item(i)).getName();
        String val = ((Attr) attrs.item(i)).getValue();
        PropertyImpl p = new PropertyImpl(getJcrPath(relPath) + "/" + name, new StringValue(val), PropertyType.STRING);
        props.add(p);
      }
      return new NodeImpl(getJcrPath(relPath), props);
    } catch (PathNotFoundException e) {
      return null;
    } catch (Exception e) {
      throw new RuntimeException("getNodeBypath failed. Reason:" + e.getMessage());
    }
  }

  public List getChildren(String relPath) {
    ArrayList list = new ArrayList();
    try {
      XMLFragmentData result = getChildrenData(jcrToXPathChildren(relPath));

      NodeList nodes = result.getAsNodeList();
      for (int i = 0; i < nodes.getLength(); i++) {
        String name = nodes.item(i).getNodeName();
//        NodeImpl node = new NodeImpl(getJcrPath(relPath) + "/" + name);
//        list.add(node);
          list.add(getJcrPath(relPath) + "/" + name);
      }
    } catch (PathNotFoundException e) {
      return list;
    } catch (Exception e) {
      throw new RuntimeException("getChildren failed. Reason:" + e.getMessage());
    }
    return list;
  }

  public void add(Node node) throws ItemExistsException, RepositoryException {
    try {
      String relPath = parseRelPath(node.getPath());
      System.out.println("Rel Path --- " + relPath);
      XMLWriter writer = new XMLWriter(namespaceRegistry.getURIMap());
      if (relPath.length() == 0)
        return;
      else {
        if (relPath.equals("/jcr:content")) {
          if (storage.length() > 0)
            throw new ItemExistsException("File <" + resourceId + "> is not empty!");
          insertContent((NodeImpl) node, writer);
          FileOutputStream fos = new FileOutputStream(storage);
          fos.write(writer.getBytes());
          fos.close();
          return;
        }
      }
      insertContent((NodeImpl) node, writer);
      ByteArrayInputStream stream = new ByteArrayInputStream(writer.getBytes());
      XMLWellFormedData data = xmlDataManager.create(new InputSource(stream));
      Statement stat = statHelper.append(getParentRelPath(parseRelPath(node.getPath())) + getAppendSuffix(), resourceId, data);
      query.prepare(stat);
      query.execute();
      query.serialize();
    } catch (Exception e) {
      throw new RepositoryException("add node failed. Reason:" + e.getMessage());
    }
  }

  public void update(Node node) {
    try {
      String relPath = parseRelPath(node.getPath());
      System.out.println("Rel Path --- " + relPath);
      XMLWriter writer;
      if (relPath.length() == 0)
        return;

      writer = new XMLWriter();

      updateContent((NodeImpl) node, writer);
      ByteArrayInputStream stream = new ByteArrayInputStream(writer.getBytes());
      XMLWellFormedData data = xmlDataManager.create(new InputSource(stream));
      Statement stat = statHelper.update(parseRelPath(node.getPath()), resourceId, data);
      query.prepare(stat);
      query.execute();
      query.serialize();
    } catch (Exception e) {
      throw new RuntimeException("update node failed. Reason:" + e.getMessage());
    }

  }

  public void delete(String absPath) {
    try {
      String relPath = parseRelPath(absPath);
      System.out.println("Delete RelPath <" + relPath + ">");
      if (relPath.length() == 0)
        storage.delete();
      else if (relPath.equals("/jcr:content"))
        cleanContent();
      else
        cleanNode(relPath);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  ///////////////////////////////////////
  private String jcrToXPathNode(String path) {
    return path;
  }

  private String jcrToXPathChildren(String path) {
    //        return path + "/@*|*";
    return path + "/*";
  }

  private String getAppendSuffix() {
    return "/text()[last()]";
  }

  private XMLWellFormedData getNodeData(String xPath) throws PathNotFoundException {
    try {
      Statement stat = statHelper.select(xPath, resourceId);
      System.out.println("getNodeData Stat --- " + stat);
      query.prepare(stat);
      query.execute();
      System.out.println("getNodeData Res --- " + query.getResult());
      if (query.getResult().isEmpty())
        throw new PathNotFoundException("getNodeData failed. Path<" + xPath + "> not found in " + resourceId);
      else {
        return xmlDataManager.toWellFormed(query.getResult());
      }
    } catch (InvalidStatementException e) {
      throw new RuntimeException("getNodeData failed. Reason:" + e.getMessage());
    } catch (InvalidSourceException e) {
      throw new RuntimeException("getNodeData failed. Reason:" + e.getMessage());
    } catch (UniFormTransformationException e) {
      throw new RuntimeException("getNodeData failed. Reason:" + e.getMessage());
    } catch (QueryRunTimeException e) {
      throw new RuntimeException("getNodeData failed. Reason:" + e.getMessage());
    }
  }

  private XMLFragmentData getChildrenData(String xPath) throws PathNotFoundException {
    try {
      Statement stat = statHelper.select(xPath, resourceId);
      System.out.println("getChildrenData Stat --- " + stat);
      query.prepare(stat);
      query.execute();
      System.out.println("getChildrenData Res --- " + query.getResult());
      if (query.getResult().isEmpty())
        throw new PathNotFoundException("getChildrenData failed. Path<" + xPath + "> not found in " + resourceId);
      else {
        return xmlDataManager.toFragment(query.getResult());
      }
    } catch (InvalidStatementException e) {
      throw new RuntimeException("getChildrenData failed. Reason:" + e.getMessage());
    } catch (InvalidSourceException e) {
      throw new RuntimeException("getChildrenData failed. Reason:" + e.getMessage());
    } catch (UniFormTransformationException e) {
      throw new RuntimeException("getChildrenData failed. Reason:" + e.getMessage());
    } catch (QueryRunTimeException e) {
      throw new RuntimeException("getChildrenData failed. Reason:" + e.getMessage());
    }
  }

  private void updateContent(NodeImpl node, XMLWriter writer) {
    try {
      System.out.println("Update Child --- " + node);

      //            hasChildren = false;
      Properties attrs = new Properties();
      PropertyIterator props = node.getProperties();
      while (props.hasNext()) {
        PropertyImpl prop = (PropertyImpl) props.next();
        String strPropVal = DocNodeExporter.getStrPropValue(prop, false);
        attrs.setProperty(prop.getName(), strPropVal);
      }
      writer.startElement(node.getName(), attrs);

      try {
        writer.writeText(getChildrenData(jcrToXPathChildren(parseRelPath(node.getPath()))).toString());
      } catch (PathNotFoundException e) {
      }

/*
            Iterator nodes = getChildren(parseRelPath(node.getPath())).iterator();
            System.out.println("Update Child --- " + nodes);
//            NodeIterator nodes = node.getNodes();
            while (nodes.hasNext()) {
                NodeImpl child = (NodeImpl)nodes.next();
            System.out.println("Update Child --- " + child);

                updateContent(child, writer);

            }
*/
      writer.writeText(" ");
      writer.endElement();
      System.out.println("Update Child --- " + writer);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  private void insertContent(NodeImpl node, XMLWriter writer) {
    try {
      boolean hasChildren = false;
      Properties attrs = new Properties();
      PropertyIterator props = node.getProperties();
      while (props.hasNext()) {
        PropertyImpl prop = (PropertyImpl) props.next();
        String strPropVal = DocNodeExporter.getStrPropValue(prop, false);
        attrs.setProperty(prop.getName(), strPropVal);
      }
      writer.startElement(node.getName(), attrs);
//            Iterator nodes = node.getChangedNodes().iterator();
      NodeIterator nodes = node.getNodes();
      while (nodes.hasNext()) {
        NodeImpl child = (NodeImpl) nodes.next();
        insertContent(child, writer);
      }
//            if (hasChildren == false) {
      String relPath = jcrToXPathChildren(parseRelPath(node.getPath())); //getParentRelPath(parseRelPath(node.getPath()));
      if (getParentRelPath(parseRelPath(node.getPath())).length() > 0) {
        try {
//                        getNodeData(relPath + "/node()");
          getChildrenData(relPath);
          hasChildren = true;
        } catch (PathNotFoundException e) {
          // still false if not found
          System.out.println("getNodeData not found --- " + relPath);
        }
//                }
      }
//            System.out.println("HAS Children --- " + hasChildren);
      if (!hasChildren)
        writer.writeText("eXo");
      writer.endElement();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }


  private void cleanNode(String relPath) {
    try {
      Statement stat = statHelper.delete(jcrToXPathNode(relPath), resourceId);
      System.out.println("clean Content Stat --- " + stat);
      query.prepare(stat);
      query.execute();
      query.serialize();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  private void cleanContent() {
    try {
      FileOutputStream fos = new FileOutputStream(storage);
      fos.write(new byte[0]);
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

}
