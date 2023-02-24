/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.jcr.BinaryValue;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.StringValue;
import org.apache.commons.codec.binary.Base64;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.BaseTest;
import org.exoplatform.services.xml.querying.InvalidSourceException;
import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.QueryRunTimeException;
import org.exoplatform.services.xml.querying.UniFormTransformationException;
import org.exoplatform.services.xml.querying.XMLQuery;
import org.exoplatform.services.xml.querying.XMLQueryingService;
import org.exoplatform.services.xml.querying.helper.SimpleStatementHelper;
import org.exoplatform.services.xml.querying.helper.XMLDataManager;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 ao�t 2004
 */
public class ExportDocViewTest extends BaseTest {

  private XMLQueryingService xmlQueryingService;

  public void initRepository() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    file = root.getNode("childNode").addNode("childNode3", "nt:file");
    contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new BinaryValue("this is the binary content"));
    ticket.save();


    xmlQueryingService = (XMLQueryingService) PortalContainer.getInstance().
        getComponentInstanceOfType(XMLQueryingService.class);
  }

  public void tearDown() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.remove("childNode");
    ticket.save();
  }

  public void testWithOutputStream() throws RepositoryException, IOException, InvalidSourceException,
      InvalidStatementException, QueryRunTimeException, UniFormTransformationException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    workspace.exportDocView("/", out, false, false);
    byte[] bArray = out.toByteArray();
    SimpleStatementHelper sHelper = xmlQueryingService.createStatementHelper();
    XMLDataManager dManager = xmlQueryingService.createXMLDataManager();
    XMLQuery query = xmlQueryingService.createQuery();
    query.setInputStream(new ByteArrayInputStream(bArray));
    query.prepare(sHelper.select("//childNode"));
    query.execute();
    NodeList nodes = dManager.toFragment(query.getResult()).getAsNodeList();
    assertEquals(1, nodes.getLength());
    query.prepare(sHelper.select("//*[name()='childNode2']"));
    query.execute();
    nodes = dManager.toFragment(query.getResult()).getAsNodeList();
    assertEquals(1, nodes.getLength());
    query.prepare(sHelper.select("//*[name()='childNode3']"));
    query.execute();
    nodes = dManager.toFragment(query.getResult()).getAsNodeList();
    assertEquals(1, nodes.getLength());

    query.prepare(sHelper.select("//jcr:content"));
    query.execute();
    nodes = dManager.toFragment(query.getResult()).getAsNodeList();
    assertEquals(2, nodes.getLength());
    for (int i = 0; i < nodes.getLength(); i++) {
      Element node = (Element) nodes.item(i);
      String value = node.getAttribute("exo:content");
      if (!(new String(Base64.encodeBase64("this is the binary content".getBytes())).
          equals(value) || "this is the content".equals(value))) {
        fail("incorrect property value");
      }
    }

    out = new ByteArrayOutputStream();
    workspace.exportDocView("/", out, true, false);
    bArray = out.toByteArray();
    query.setInputStream(new ByteArrayInputStream(bArray));
    query.prepare(sHelper.select("//jcr:content"));
    query.execute();
    nodes = dManager.toFragment(query.getResult()).getAsNodeList();
    assertEquals(2, nodes.getLength());
    for (int i = 0; i < nodes.getLength(); i++) {
      Element node = (Element) nodes.item(i);
      String value = node.getAttribute("exo:content");
      if (!("/childNode/childNode3/jcr:content/exo:content".equals(value) ||
          "this is the content".equals(value))) {
        fail("incorrect property value");
      }
    }

    out = new ByteArrayOutputStream();
    workspace.exportDocView("/childNode", out, true, true);
    bArray = out.toByteArray();
    query.setInputStream(new ByteArrayInputStream(bArray));
    query.prepare(sHelper.select("childNode"));
    query.execute();
    nodes = dManager.toFragment(query.getResult()).getAsNodeList();
    assertEquals(1, nodes.getLength());
    for (int i = 0; i < nodes.getLength(); i++) {
      Element node = (Element) nodes.item(i);
      assertEquals(9, node.getAttributes().getLength());
    }
    query.prepare(sHelper.select("//*[name()='childNode3']"));
    query.execute();
    nodes = dManager.toFragment(query.getResult()).getAsNodeList();
    assertEquals(0, nodes.getLength());
  }

  public void testWithContentHandler() throws RepositoryException, SAXException {
    MockContentHandler mock = new MockContentHandler();
    workspace.exportDocView("/", mock, false, false);

    assertTrue(mock.reached);
    assertEquals(6, mock.docElement);

    mock = new MockContentHandler();
    workspace.exportDocView("/childNode", mock, false, true);
    assertEquals(1, mock.docElement);
  }

}
