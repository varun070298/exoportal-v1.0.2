/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.portletregistery.impl;


import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import org.exoplatform.Constants;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.portletcontainer.monitor.*;
import org.exoplatform.services.portletregistery.*;
import org.exoplatform.test.BasicTestCase;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 16 juin 2004
 */
public class TestPortletRegistery extends BasicTestCase {
  static protected PortletRegisteryService service_ ;

  public TestPortletRegistery(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    if (service_ == null) {
      PortalContainer manager  = PortalContainer.getInstance();
      service_ = (PortletRegisteryService) manager.
          getComponentInstanceOfType(PortletRegisteryService.class) ;
    }
  }

  public void tearDown() throws Exception {
    PortalContainer manager  = PortalContainer.getInstance();
    HibernateService hservice = 
        (HibernateService) manager.getComponentInstanceOfType(HibernateService.class) ;
    hservice.closeSession();
  }
  
  public void testPortletService() throws Exception {
    List portletCategoriesList = service_.getPortletCategories() ;
    assertTrue(portletCategoriesList.isEmpty());
    PortletCategory cat = createPortletCategory("portletCategory", "this is a test") ;
    assertEquals("check portletCategory name: ", "portletCategory", cat.getPortletCategoryName()) ;
    assertEquals("check portletCategory desc: ", "this is a test", cat.getDescription()) ;
    portletCategoriesList = service_.getPortletCategories() ;
    assertEquals("check number of portlet categories: ", 1 , portletCategoriesList.size()) ;

    Portlet portlet = createPortlet(cat, "portlet", "this is a test") ;
    assertEquals("check portlet name: ", "portlet", portlet.getPortletName()) ;
    assertEquals("check portlet desc: ", "this is a test", portlet.getDescription()) ;

    PortletRole portletRole = createPortletRole(portlet, "portletRole", "this is a test") ;
    assertEquals("check portletRole : ", "portletRole", portletRole.getPortletRoleName()) ;

    portlet = service_.getPortlet(portlet.getId()) ;
    portletRole = service_.getPortletRole(portletRole.getId()) ;

    portletRole = service_.getPortletRole(portletRole.getId()) ;
    portlet = service_.getPortlet(portlet.getId()) ;

    try {
      service_.removePortletRole(portletRole.getId()) ;
      portletRole = service_.getPortletRole(portletRole.getId()) ;
    } catch (PortletRegisteryException ex) {
      assertEquals("check portletRole not found exception ",
          PortletRegisteryException.PORTLET_ROLE_NOT_FOUND, ex.getErrorCode()) ;
    }

    try {
      service_.removePortlet(portlet.getId()) ;
      portlet = service_.getPortlet(portlet.getId()) ;
    } catch (PortletRegisteryException ex) {
      assertEquals("check portlet not found exception ",
          PortletRegisteryException.PORTLET_NOT_FOUND, ex.getErrorCode()) ;
    }

    portlet = createPortlet(cat, "portlet", "this is a test") ;
    String portletId = portlet.getId();
    String portletRoleId = "";
    for (int i = 0; i < 25; i++) {
      portletRoleId = createPortletRole(portlet, "portletRole" + i, "this is a test").getId() ;
    }
    List portletRoleList = service_.getPortletRoles(portlet.getId()) ;
    assertEquals("check number of portletRoles in portlet: ", 25 , portletRoleList.size()) ;

    try {
      service_.removePortletCategory(cat.getId()) ;
      assertNull(service_.getPortletCategory(cat.getId())) ;
    } catch (PortletRegisteryException ex) {
      assertEquals("check portletCategory not found exception ",
          PortletRegisteryException.PORTLET_CATEGORY_NOT_FOUND, ex.getErrorCode()) ;
    }

    //test cascade delete
    try {
      service_.getPortlet(portletId);
      fail("exception should have been thrown");
    } catch (PortletRegisteryException e) {
    }
    try {
      service_.getPortletRole(portletRoleId);
      fail("exception should have been thrown");
    } catch (Exception e) {
    }
  }

  public void testClearPortletRoles() throws Exception {
    PortletCategory cat = createPortletCategory("portletCategory", "this is a test") ;
    Portlet portlet = createPortlet(cat, "portlet", "this is a test") ;
    PortletRole portletRole1 = createPortletRole(portlet, "portletRole", "this is a test") ;
    PortletRole portletRole2 = createPortletRole(portlet, "portletRole2", "this is a test") ;

    assertNotNull(service_.getPortletRole(portletRole1.getId()));

    service_.clearPortletRoles(portlet.getId());

    try {
      service_.getPortletRole(portletRole1.getId());
      fail("exception should have been thrown");
    } catch (Exception e) {
    }
    try {
      service_.getPortletRole(portletRole2.getId());
      fail("exception should have been thrown");
    } catch (Exception e) {
    }
  }

  public void testUpdateRoles() throws Exception {
    PortletCategory cat = createPortletCategory("portletCategory", "this is a test") ;
    Portlet portlet = createPortlet(cat, "portlet", "this is a test") ;
    createPortletRole(portlet, "portletRole", "this is a test") ;
    createPortletRole(portlet, "portletRole2", "this is a test") ;

    Collection newRoles = new ArrayList();
    newRoles.add("newRole1");
    newRoles.add("newRole2");
    newRoles.add("newRole3");

    service_.updatePortletRoles(portlet.getId(), newRoles);

    List roles = service_.getPortletRoles(portlet.getId());
    assertTrue(roles.size() == 3);
    for (Iterator iterator = roles.iterator(); iterator.hasNext();) {
      PortletRole portletRole = (PortletRole) iterator.next();
      assertTrue(portletRole.getPortletRoleName().startsWith("newRole"));
    }
  }

  public void testImportPortlets() throws Exception {
    Collection mocks = new ArrayList();
    MockPortletRuntimeData mock = new MockPortletRuntimeData("app1", "name1");
    mocks.add(mock);
    mock = new MockPortletRuntimeData("app1", "name2");
    mocks.add(mock);
    mock = new MockPortletRuntimeData("app2", "name21");
    mocks.add(mock);

    service_.importPortlets(mocks);

    assertNotNull(service_.findPortletCategoryByName("app1"));

    PortletCategory portletCategory = service_.findPortletCategoryByName("app2");
    assertNotNull(portletCategory);
    List portlets = service_.getPortlets(portletCategory.getId());
    for (Iterator iterator = portlets.iterator(); iterator.hasNext();) {
      Portlet portlet = (Portlet) iterator.next();
      assertTrue(portlet.getDisplayName().startsWith("name2"));
      List roles = service_.getPortletRoles(portlet.getId());
      for (Iterator iterator1 = roles.iterator(); iterator1.hasNext();) {
        PortletRole portletRole = (PortletRole) iterator1.next();
        assertEquals(Constants.USER_ROLE, portletRole.getPortletRoleName());
      }
    }
  }

  public void testClearRepository() throws Exception {
    PortletCategory cat = createPortletCategory("portletCategory", "this is a test") ;
    Portlet portlet = createPortlet(cat, "portlet", "this is a test") ;
    PortletRole portletRole = createPortletRole(portlet, "portletRole", "this is a test") ;


    service_.clearRepository();

    try {
      service_.getPortletCategory(cat.getId());
      fail("exception should have been thrown");
    } catch (Exception e) {
    }
    try {
      service_.getPortlet(portlet.getId());
      fail("exception should have been thrown");
    } catch (Exception e) {
    }
    try {
      service_.getPortletRole(portletRole.getId());
      fail("exception should have been thrown");
    } catch (Exception e) {
    }
  }

  private PortletCategory createPortletCategory(String name, String desc) throws Exception {
    PortletCategory portletCategory = service_.createPortletCategoryInstance() ;
    portletCategory.setPortletCategoryName(name) ;
    portletCategory.setDescription(desc) ;
    portletCategory = service_.addPortletCategory(portletCategory) ;
    return portletCategory ;
  }

  private Portlet createPortlet(PortletCategory cat, String name, String desc) throws Exception {
    Portlet portlet = service_.createPortletInstance() ;
    portlet.setPortletName(name) ;
    portlet.setDescription(desc) ;
    portlet = service_.addPortlet(cat, portlet) ;
    return portlet ;
  }

  private PortletRole createPortletRole(Portlet portlet, String name, String desc) throws Exception {
    PortletRole portletRole = service_.createPortletRoleInstance() ;
    portletRole.setPortletRoleName(name) ;
    portletRole = service_.addPortletRole(portlet, portletRole) ;
    return portletRole ;
  }

  protected String getDescription() {
    return "Test Portlet Registery Service" ;
  }

  private class MockPortletRuntimeData implements PortletRuntimeData {
    private String portletAppName;
    private String portletName;

    public MockPortletRuntimeData(String appName , String name) {
      this.portletAppName = appName;
      this.portletName = name;
    }

    public String getPortletAppName() {
      return portletAppName;
    }

    public String getPortletName() {
      return portletName;
    }

    public boolean isInitialized() {
      return false;
    }

    public long getInitializationTime() {
      return 0;
    }

    public long getLastAccessTime() {
      return 0;
    }

    public long getLastFailureAccessTime() {
      return 0;
    }

    public long getLastInitFailureAccessTime() {
      return 0;
    }

    public void setLastInitFailureAccessTime(long lastInitFailureAccessTime) {
    }

    public long getUnavailabilityPeriod() {
      return 0;
    }

    public boolean isDataCached(String key, boolean isCacheGlobal) {
      return false;
    }

    public CachedData getCachedData(String key, boolean isCacheGlobal) {
      return null;
    }

    public int getCacheExpirationPeriod() {
      return 0;
    }

    public PortletRequestMonitorData[] getPortletRequestMonitorData() {
      return new PortletRequestMonitorData[0];
    }

  }
}