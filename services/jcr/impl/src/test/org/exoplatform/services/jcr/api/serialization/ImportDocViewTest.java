/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.serialization;


import javax.jcr.*;
import javax.jcr.nodetype.ConstraintViolationException;
import org.exoplatform.services.jcr.BaseTest;
import java.io.IOException;
import java.io.ByteArrayInputStream;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 18 ao�t 2004
 */
public class ImportDocViewTest extends BaseTest{

  private String docView = "<root xmlns:nt=\"http://www.jcp.org/jcr/nt/1.0\" " +
      "xmlns:jcr=\"http://www.jcp.org/jcr/1.0\" " +
      "xmlns:pt=\"http://www.jcp.org/jcr/pt/1.0\" " +
      "xmlns:sv=\"http://www.jcp.org/jcr/sv/1.0\" " +
      "xmlns:exo=\"http://www.exoplatform.com/jcr/exo/1.0\" " +
      "xmlns:mix=\"http://www.jcp.org/jcr/mix/1.0\" " +
      "jcr:primaryType=\"nt:default\">" +
        "<childNode jcr:created=\"2004-08-18T20:07:42.626+01:00\" jcr:primaryType=\"nt:folder\" jcr:lastModified=\"2004-08-18T20:07:42.626+01:00\">" +
          "<childNode3 jcr:created=\"2004-08-18T20:07:42.636+01:00\" jcr:primaryType=\"nt:file\" jcr:lastModified=\"2004-08-18T20:07:42.636+01:00\">" +
            "<jcr:content exo:content=\"dGhpcyBpcyB0aGUgYmluYXJ5IGNvbnRlbnQ=\" jcr:primaryType=\"nt:content\" jcr:uuid=\"1092852462407\">" +
            "</jcr:content>" +
          "</childNode3>" +
          "<childNode2 jcr:created=\"2004-08-18T20:07:42.636+01:00\" jcr:primaryType=\"nt:file\" jcr:lastModified=\"2004-08-18T20:07:42.636+01:00\">" +
            "<jcr:content exo:content=\"this is the content\" jcr:primaryType=\"nt:content\" jcr:uuid=\"1092852462406\">" +
            "</jcr:content>" +
          "</childNode2>" +
        "</childNode>" +
      "</root>";

  public void testImportDocView() throws RepositoryException, InvalidSerializedDataException,
      ConstraintViolationException, IOException, ItemExistsException {
    ticket.importXML("/", new ByteArrayInputStream(docView.getBytes()));

    Node root = ticket.getRootNode();
    NodeIterator iterator = root.getNodes();
    assertEquals(1, iterator.getSize());

    iterator = root.getNode("childNode").getNodes();
    assertEquals(2, iterator.getSize());

    Property property = root.getProperty("childNode/childNode2/jcr:content/exo:content");
    assertEquals("this is the content", property.getString());

//    property = root.getProperty("childNode/childNode3/jcr:content/exo:content");
//    assertEquals("this is the binary content", property.getString());
  }


}
