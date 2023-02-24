/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.comparing;


import javax.jcr.*;
import org.exoplatform.services.jcr.BaseTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 13 ao�t 2004
 */
public class IdenticalItemsTest extends BaseTest{

  public void tearDown() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.remove("childNode");
    ticket.save();
  }

  public void testIsIdentical() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    ticket.save();

    Ticket ticket2 = repository.login(credentials, WORKSPACE);
    root = ticket2.getRootNode();
    Item contentNode2 = root.getNode("childNode/childNode2/jcr:content");

    assertTrue(contentNode2.isIdentical(contentNode));
    assertFalse(contentNode2.isIdentical(root));
    assertFalse(contentNode2.isIdentical(file));
    assertFalse(contentNode2.isIdentical(contentNode.getProperty("exo:content")));
  }

}
