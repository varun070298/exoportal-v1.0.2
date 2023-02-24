/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.accessing;


import javax.jcr.*;
import org.exoplatform.services.jcr.BaseTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 15 juil. 2004
 */
public class AccessRepositoryTest extends BaseTest{

  public void testLogin() throws NoSuchWorkspaceException, LoginException {
    Credentials cred = new SimpleCredentials("exo", "exo".toCharArray());
    repository.login(cred, WORKSPACE);
  }

  public void testGetAnonymousTicket() throws NoSuchWorkspaceException, LoginException {
    Ticket ticket = repository.login(null, WORKSPACE) ;
    assertNotNull(ticket);
    assertEquals("anonymous", ticket.getCredentials().getUserId());
  }

  public void testGetDefaultWorkspace() throws NoSuchWorkspaceException, LoginException {
    Credentials cred = new SimpleCredentials("exo", "exo".toCharArray());
    Ticket ticket = repository.login(cred, null);
    assertNotNull(ticket);    
  }

  public void testWrongLogin() throws NoSuchWorkspaceException {
    Credentials cred = new SimpleCredentials("benj", "psw".toCharArray());
    try {
      repository.login(cred, WORKSPACE);
      fail("Exception should have been thrown");
    } catch (LoginException e) {
    }
  }

  public void testWrongWorkspace() throws LoginException {
    Credentials cred = new SimpleCredentials("exo", "exo".toCharArray());
    try {
      repository.login(cred, "wrong_workspace");
      fail("Exception should have been thrown");
    } catch (NoSuchWorkspaceException e) {
    }
  }

  public void testWorkspaceSplit() throws RepositoryException{
    Ticket ticket1 = repository.login(null, WORKSPACE) ;
    Node root = ticket1.getRootNode();
System.out.println("WS ------------------- "+root);

    Node file = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    Node contentNode = file.getNode("jcr:content");
    root.getNode("childNode");
    contentNode.setProperty("exo:content", new StringValue("this is the content"));
    ticket1.save();

    Ticket ticket2 = repository.login(null, "ws2");
    root = ticket2.getRootNode();
System.out.println("WS2 ------------------- "+root);

    root.addNode("child1");
    try {
      root.getNode("childNode");
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }
    root.addNode("childNodeinWS2", "nt:folder");
    ticket2.save();

    root = ticket1.getRootNode();
    try {
      root.getNode("childNodeinWS2");
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }

    root = ticket1.getRootNode();
    root.remove("childNode");
    ticket1.save();

    root = ticket2.getRootNode();
    root.remove("childNodeinWS2");
    ticket2.save();
  }

}
