/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.test;

import java.util.List;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.portal.PortalConfigDescription;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.Query;
import org.exoplatform.services.portal.impl.NodeImpl;
import org.exoplatform.services.portal.model.Node;
import org.exoplatform.services.portal.model.Page;
import org.exoplatform.services.portal.model.PageReference;
import org.exoplatform.services.portal.model.PortalConfig;
import org.exoplatform.test.BasicTestCase;

/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestPortalConfigService.java,v 1.19 2004/10/20 20:56:28 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestPortalConfigService  extends BasicTestCase {
  static protected PortalConfigService   service_ ;
  static protected OrganizationService orgService_ ;

  public TestPortalConfigService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    if (service_ == null) {
      PortalContainer manager  = PortalContainer.getInstance();
      service_ = 
        (PortalConfigService) manager.getComponentInstanceOfType(PortalConfigService.class) ;
      orgService_ = (OrganizationService) manager.getComponentInstanceOfType(OrganizationService.class);
    }
  }
  
  public void tearDown() throws Exception {
    PortalContainer manager  = PortalContainer.getInstance();
    HibernateService hservice = 
        (HibernateService) manager.getComponentInstanceOfType(HibernateService.class) ;
    hservice.closeSession();
  }
  
  public void testService() throws Exception {
  	String[] users = {"exo", "admin", "demo", "exotest"} ;
    createUser("exotest") ;
  	for(int i = 0 ; i < users.length; i++) {
  		String owner = users[i] ;
  		PortalConfig config = service_.getPortalConfig(owner) ;
  		Node root = service_.getNodeNavigation(owner) ; 
  		assertNodeData(root) ;
  		Query q = new Query(owner , null , null ) ;
  		List list = service_.findAllPageDescriptions(q).getAll() ;
  		assertTrue("Expect at least one page ",  list.size() > 0) ;
  		
  		list = service_.findAllPortalConfigDescriptions(q).getAll() ;
  		assertTrue("Expect at least one portal config ",  list.size() == 1) ;
  		PortalConfigDescription desc = (PortalConfigDescription) list.get(0);
  		assertTrue("View Permission is not null ",  desc.getViewPermission() != null ) ;
  		assertTrue("View Permission is not null ",  desc.getEditPermission() != null ) ;
  		
  		String xml = service_.getPortalConfigAsXmlString(owner);
  		assertTrue("portal congig in xml ",  xml != null ) ;
      
  		orgService_.removeUser(owner) ;
  		list = service_.findAllPageDescriptions(q).getAll() ;
  		assertTrue("Expect no page ",  list.size() == 0) ;
  	  config = service_.getPortalConfig(owner) ;
  		assertTrue("should not find the PortalConfig object", config == null) ; 
  	}  
  }
 
  public void testSharePortal() throws Exception {
    String sharePortalUser = "shareportal" ;
    User user = createUser(sharePortalUser) ;
    PortalConfig config = service_.getPortalConfig(sharePortalUser) ;
    Node root = service_.getNodeNavigation(sharePortalUser) ; 
    assertNodeData(root) ;
    assertEquals("No user can edit the portal layout", "noone", config.getEditPermission()) ;
  }
  
  private  User createUser(String userName) throws Exception {
    User user = orgService_.createUserInstance() ;
    user.setUserName(userName) ;
    user.setPassword("exo") ;
    user.setFirstName("Exo") ;
    user.setLastName("Platform") ;
    user.setEmail("exo@exoportal.org") ;
    orgService_.createUser(user);
    return user ;
  }
  
  private void assertNodeData(Node node) throws Exception  {
    PageReference pageReference = node.getPageReference("text/xhtml") ;
    String pageReferenceId = pageReference.getPageReference() ;
    if(pageReferenceId != null && pageReferenceId.length() > 0) {
      System.out.println("page reference = " + pageReferenceId);
      Page page = service_.getPage(pageReferenceId) ;
      assertTrue("Page reference should exsit " , page != null) ;
    }
    for (int i = 0; i < node.getChildrenSize(); i++) {
      NodeImpl child = (NodeImpl) node.getChild(i) ;
      assertNodeData(child) ;
    }
  }
  
  protected String getDescription() {
    return "Test User Portal Configuration service" ;
  }
}