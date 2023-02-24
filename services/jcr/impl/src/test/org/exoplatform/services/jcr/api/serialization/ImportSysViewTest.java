/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.serialization;


import javax.jcr.*;
import javax.jcr.nodetype.ConstraintViolationException;
import org.exoplatform.services.jcr.BaseTest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 17 ao�t 2004
 */
public class ImportSysViewTest extends BaseTest{

  private String sysView = "<sv:node xmlns:nt=\"http://www.jcp.org/jcr/nt/1.0\" " +
      "xmlns:jcr=\"http://www.jcp.org/jcr/1.0\" " +
      "xmlns:pt=\"http://www.jcp.org/jcr/pt/1.0\" " +
      "xmlns:sv=\"http://www.jcp.org/jcr/sv/1.0\" " +
      "xmlns:exo=\"http://www.exoplatform.com/jcr/exo/1.0\" " +
      "xmlns:mix=\"http://www.jcp.org/jcr/mix/1.0\" sv:name=\"sv:root\">" +
        "<sv:property sv:name=\"jcr:primaryType\" sv:type=\"pt:string\">nt:default</sv:property>" +
        "<sv:node sv:name=\"childNode\">" +
          "<sv:property sv:name=\"jcr:created\" sv:type=\"pt:date\">2004-08-18T15:17:00.856+01:00</sv:property>" +
          "<sv:property sv:name=\"jcr:lastModified\" sv:type=\"pt:date\">2004-08-18T15:17:00.856+01:00</sv:property>" +
          "<sv:property sv:name=\"jcr:primaryType\" sv:type=\"pt:string\">nt:folder</sv:property>" +
          "<sv:node sv:name=\"childNode3\">" +
            "<sv:property sv:name=\"jcr:created\" sv:type=\"pt:date\">2004-08-18T15:17:00.856+01:00</sv:property>" +
            "<sv:property sv:name=\"jcr:lastModified\" sv:type=\"pt:date\">2004-08-18T15:17:00.856+01:00</sv:property>" +
            "<sv:property sv:name=\"jcr:primaryType\" sv:type=\"pt:string\">nt:file</sv:property>" +
            "<sv:node sv:name=\"jcr:content\">" +
              "<sv:property sv:name=\"jcr:primaryType\" sv:type=\"pt:string\">nt:content</sv:property>" +
              "<sv:property sv:name=\"jcr:uuid\" sv:type=\"pt:string\">1092835020617</sv:property>" +
              "<sv:property sv:name=\"exo:content\" sv:type=\"pt:binary\">dGhpcyBpcyB0aGUgYmluYXJ5IGNvbnRlbnQ=</sv:property>" +
            "</sv:node>" +
          "</sv:node>" +
          "<sv:node sv:name=\"childNode2\">" +
            "<sv:property sv:name=\"jcr:created\" sv:type=\"pt:date\">2004-08-18T15:17:00.856+01:00</sv:property>" +
            "<sv:property sv:name=\"jcr:lastModified\" sv:type=\"pt:date\">2004-08-18T15:17:00.856+01:00</sv:property>" +
            "<sv:property sv:name=\"jcr:primaryType\" sv:type=\"pt:string\">nt:file</sv:property>" +
            "<sv:node sv:name=\"jcr:content\"><sv:property sv:name=\"jcr:primaryType\" sv:type=\"pt:string\">nt:content</sv:property>" +
              "<sv:property sv:name=\"jcr:uuid\" sv:type=\"pt:string\">1092835020616</sv:property>" +
              "<sv:property sv:name=\"exo:content\" sv:type=\"pt:string\">this is the content</sv:property>" +
            "</sv:node>" +
          "</sv:node>" +
        "</sv:node>" +
      "</sv:node>";

  public void testImportSysView() throws RepositoryException, InvalidSerializedDataException,
      ConstraintViolationException, IOException, ItemExistsException {
    ticket.importXML("/", new ByteArrayInputStream(sysView.getBytes()));

    Node root = ticket.getRootNode();
    NodeIterator iterator = root.getNodes();
    assertEquals(1, iterator.getSize());

    iterator = root.getNode("childNode").getNodes();
    assertEquals(2, iterator.getSize());

    Property property = root.getProperty("childNode/childNode3/jcr:content/exo:content");
    assertEquals("this is the binary content", property.getString());

    property = root.getProperty("childNode/childNode2/jcr:content/exo:content");
    assertEquals("this is the content", property.getString());    
  }

}
