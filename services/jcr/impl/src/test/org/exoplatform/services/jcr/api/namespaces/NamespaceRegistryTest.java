/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.api.namespaces;


import javax.jcr.NamespaceRegistry;
import javax.jcr.RepositoryException;
import javax.jcr.NamespaceException;

import org.apache.commons.lang.ArrayUtils;
import org.exoplatform.services.jcr.BaseTest;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 14 aout 2004
 */
public class NamespaceRegistryTest extends BaseTest{

  private NamespaceRegistry namespaceRegistry;

  public void init() throws Exception {
    workspace = ticket.getWorkspace();
    namespaceRegistry = workspace.getNamespaceRegistry();
  }

  public void testSetMapping() throws RepositoryException {
    try {
      namespaceRegistry.setMapping("jcr", null);
      fail("exception should have been thrown");
    } catch (NamespaceException e) {
    }
    try {
      namespaceRegistry.setMapping("nt", null);
      fail("exception should have been thrown");
    } catch (NamespaceException e) {
    }
    try {
      namespaceRegistry.setMapping("mix", null);
      fail("exception should have been thrown");
    } catch (NamespaceException e) {
    }
    try {
      namespaceRegistry.setMapping("pt", null);
      fail("exception should have been thrown");
    } catch (NamespaceException e) {
    }
    try {
      namespaceRegistry.setMapping("sv", null);
      fail("exception should have been thrown");
    } catch (NamespaceException e) {
    }

    try {
      namespaceRegistry.setMapping("jcr", "http://dumb.uri/jcr");
      fail("exception should have been thrown");
    } catch (NamespaceException e) {
    }

    namespaceRegistry.setMapping("newMapping", null);
    namespaceRegistry.setMapping("newMapping", "http://dumb.uri/jcr");
    assertNotNull(namespaceRegistry.getURI("newMapping"));
    assertEquals("http://dumb.uri/jcr", namespaceRegistry.getURI("newMapping"));
    namespaceRegistry.setMapping("newMapping", null);
    assertNull(namespaceRegistry.getURI("newMapping"));

    namespaceRegistry.setMapping("newMapping", "http://dumb.uri/jcr");
    namespaceRegistry.setMapping("newMapping2", "http://dumb.uri/jcr");
    assertNull(namespaceRegistry.getURI("newMapping"));
    assertNotNull(namespaceRegistry.getURI("newMapping2"));
    assertEquals("http://dumb.uri/jcr", namespaceRegistry.getURI("newMapping2"));
  }

  public void testGetPrefixes() throws RepositoryException {
    namespaceRegistry.setMapping("newMapping", "http://dumb.uri/jcr");
    String[] namespaces = {"jcr", "nt", "mix","pt", "sv", "exo", "newMapping"};

    String[] prefixes = namespaceRegistry.getPrefixes();
    for (int i = 0; i < prefixes.length; i++) {
      String prefix = prefixes[i];
      assertTrue(ArrayUtils.contains(namespaces, prefix));
    }
    assertEquals(7, prefixes.length);
  }

  public void testGetURIs() throws RepositoryException {
    namespaceRegistry.setMapping("newMapping", "http://dumb.uri/jcr");
    String[] namespacesURIs = {"http://www.jcp.org/jcr/1.0", "http://www.jcp.org/jcr/nt/1.0",
                               "http://www.jcp.org/jcr/mix/1.0", "http://www.jcp.org/jcr/pt/1.0",
                               "http://www.jcp.org/jcr/sv/1.0", "http://www.exoplatform.com/jcr/exo/1.0",
                               "http://dumb.uri/jcr"};

    String[] uris = namespaceRegistry.getURIs();
    for (int i = 0; i < uris.length; i++) {
      String uri = uris[i];
      assertTrue(ArrayUtils.contains(namespacesURIs, uri));
    }
    assertEquals(7, uris.length);
  }

  public void testGetURI() throws RepositoryException {
    namespaceRegistry.setMapping("newMapping", "http://dumb.uri/jcr");

    assertNotNull(namespaceRegistry.getURI("mix"));
    assertNotNull(namespaceRegistry.getURI("newMapping"));
  }

  public void testGetPrefix() throws RepositoryException {
    namespaceRegistry.setMapping("newMapping", "http://dumb.uri/jcr");

    assertNotNull(namespaceRegistry.getPrefix("http://www.jcp.org/jcr/mix/1.0"));
    assertNotNull(namespaceRegistry.getPrefix("http://dumb.uri/jcr"));

    try {
      namespaceRegistry.getPrefix("http://dumb.uri/jcr2");
      fail("exception should have been thrown");
    } catch (RepositoryException e) {
    }
  }

}
