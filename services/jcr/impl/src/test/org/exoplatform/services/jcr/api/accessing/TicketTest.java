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
public class TicketTest extends BaseTest{

  public void testGetRepository(){
    assertEquals(ticket.getRepository(), repository);
  }

  public void testGetCredential(){
    assertEquals(ticket.getCredentials(), credentials);
  }

  public void testGetWorkspace(){
    assertEquals(ticket.getWorkspace().getTicket(), ticket);
  }

  public void testImpersonate() throws LoginException {
    assertNotSame(ticket, ticket.impersonate(new SimpleCredentials("user", new char[0])));
  }

  public void testGetRootNode() throws RepositoryException {
    assertEquals("/", ticket.getRootNode().getPath());
  }

  public void testGetNodeByAbsolutePath() throws RepositoryException {
    Node root = ticket.getRootNode();
    root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");
    assertNotNull(ticket.getNodeByAbsPath("/childNode/childNode2"));
  }

  public void testGetNodeByUUID() throws RepositoryException {

    Node root = ticket.getRootNode();
    try {
      Node child = root.addNode("childNode", "nt:folder").addNode("childNode2", "nt:file");

      Node contentNode = child.getNode("jcr:content");
      contentNode.setProperty("exo:content", new StringValue("this is the content"));
//      child.setProperty("jcr:content/exo:content", "content of file");
      ticket.save();
      child = child.getNode("jcr:content");
      assertNotNull(child.getUUID());
      Node n = ticket.getNodeByUUID(child.getUUID());
      assertNotNull(n);
      assertEquals(child.getPath(), n.getPath());
    } finally {
      root.remove("childNode");
      ticket.save();
    }
    
  }

  public void testGetPrefixes(){
    String[] prefixes = ticket.getPrefixes();
    for (int i = 0; i < prefixes.length; i++) {
      String prefixe = prefixes[i];
      if(!isInNamespaceRegistery(prefixe))
        fail("Prefix " + prefixe + " should be in registry");
    }
  }

  public void testSetPrefix() throws NamespaceException {
    ticket.setPrefix("exo", "http://www.jcp.org/jcr/1.0");
    assertTrue(isInNamespaceRegistery("exo"));

    try {
      ticket.setPrefix("unknown", "http://www.exoplatform.com/jcr/unknown/1.0");
      fail("exception should have been thrown");
    } catch (NamespaceException e) {
    }
  }

  public void testGetURI() throws NamespaceException {
    assertNotNull(ticket.getURI("jcr"));

    try {
      ticket.getURI("ano");
      fail("exception should have been thrown");
    } catch (NamespaceException e) {
    }
  }

  private boolean isInNamespaceRegistery(String namespace){
    NamespaceRegistry registry = ticket.getWorkspace().getNamespaceRegistry();
    String[] namespaces = registry.getPrefixes();
    for (int i = 0; i < namespaces.length; i++) {
      String s = namespaces[i];
      if(s.equals(namespace))
        return true;
    }
    return false;
  }

}
