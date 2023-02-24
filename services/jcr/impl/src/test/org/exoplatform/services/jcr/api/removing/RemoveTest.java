/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.removing;


import javax.jcr.RepositoryException;
import javax.jcr.Node;
import javax.jcr.StringValue;
import javax.jcr.PathNotFoundException;
import org.exoplatform.services.jcr.BaseTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 2 ao�t 2004
 */
public class RemoveTest extends BaseTest{

  public void initRepository() throws RepositoryException {
    Node root = ticket.getRootNode();
    Node file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    Node contentNode = file.getNode("jcr:content");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    ticket.save();
  }

  public void tearDown() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.remove("childNode");
    ticket.save();
  }

  public void testRemove() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.remove("childNode/childNode2");
    ticket.save();

    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();
    try {
      root.getNode("childNode/childNode2");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }
  }

  public void testRecursiveRemove() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.remove("childNode");
    ticket.save();

    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();
    try {
      root.getNode("childNode/childNode2");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }
  }

  public void testRemoveProperty() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.remove("childNode/childNode2/jcr:content/exo:content");
    ticket.save();

    ticket = repository.login(credentials, WORKSPACE);
    root = ticket.getRootNode();
    try {
      root.getProperty("childNode/childNode2/jcr:content/exo:content");
      fail("exception should have been thrown");
    } catch (PathNotFoundException e) {
    }

  }

}
