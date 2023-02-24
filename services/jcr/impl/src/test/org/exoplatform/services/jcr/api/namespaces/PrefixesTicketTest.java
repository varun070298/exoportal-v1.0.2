/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.namespaces;


import javax.jcr.NamespaceRegistry;
import javax.jcr.NamespaceException;

import org.apache.commons.lang.ArrayUtils;
import org.exoplatform.services.jcr.BaseTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 14 ao�t 2004
 */
public class PrefixesTicketTest extends BaseTest {

  private NamespaceRegistry namespaceRegistry;

  public void init() throws Exception {
    workspace = ticket.getWorkspace();
    namespaceRegistry = workspace.getNamespaceRegistry();
  }

  public void testSetPrefix() throws NamespaceException {
    try {
      ticket.setPrefix("exo2", "http://dummy.com");
      fail("exception should have been thrown");
    } catch (NamespaceException e) {
    }

    ticket.setPrefix("exo2", "http://www.exoplatform.com/jcr/exo/1.0");
    assertEquals("http://www.exoplatform.com/jcr/exo/1.0", ticket.getURI("exo2"));
    assertNull(ticket.getURI("exo"));

    assertEquals("http://www.jcp.org/jcr/1.0", ticket.getURI("jcr"));
  }

  public void testGetPrefixes() throws NamespaceException {
    String[] protectedNamespaces = {"jcr", "nt", "mix","pt", "sv", "exo2"};
    ticket.setPrefix("exo2", "http://www.exoplatform.com/jcr/exo/1.0");
    String[] prefixes = ticket.getPrefixes();
    assertEquals(prefixes.length, protectedNamespaces.length);
    for (int i = 0; i < prefixes.length; i++) {
      String prefix = prefixes[i];
      assertTrue(ArrayUtils.contains(protectedNamespaces, prefix));
    }    
  }

  public void testGetURI() throws NamespaceException {
    ticket.setPrefix("exo2", "http://www.exoplatform.com/jcr/exo/1.0");
    assertEquals("http://www.exoplatform.com/jcr/exo/1.0", ticket.getURI("exo2"));
    assertNull(ticket.getURI("exo"));
    assertEquals("http://www.jcp.org/jcr/1.0", ticket.getURI("jcr"));
  }


}
