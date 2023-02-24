/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.resources.test;

import java.util.* ;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.resources.*;
import org.exoplatform.test.BasicTestCase;
/*
 * Thu, May 15, 2004 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestResourceBundleService.java,v 1.2 2004/10/27 03:04:43 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestResourceBundleService extends BasicTestCase {
  final static private String PROPERTIES =  "language=en\nproperty=property" ;
  final static private String PROPERTIES_FR =  "language=fr" ;
  final static private String PROPERTIES_FR_UPDATE=  "language=fr\nproperty=fr-property" ;
  
  private ResourceBundleService service_ ;
  
  public TestResourceBundleService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    PortalContainer manager  = PortalContainer.getInstance();
    service_ = (ResourceBundleService) manager.getComponentInstanceOfType(ResourceBundleService.class) ;
    LogUtil.setLevel("org.exoplatform.services.resources", LogService.DEBUG, true) ;
    LogUtil.setLevel("org.exoplatform.services.database", LogService.DEBUG, true) ;
  }
  
  public void tearDown() throws Exception {
    PortalContainer manager  = PortalContainer.getInstance();
    HibernateService hservice = 
        (HibernateService) manager.getComponentInstanceOfType(HibernateService.class) ;
    hservice.closeSession();
  }
  
  public void testResourceBundleService() throws Exception {
  	ResourceBundle res = 
      service_.getResourceBundle("locale.test.resources.test", Locale.ENGLISH) ;
  	assertTrue("Expect to find the ResourceBundle", res != null);
  	res = service_.getResourceBundle("locale.test.resources.test", Locale.FRANCE) ;
  	assertTrue("Expect to find the ResourceBundle", res != null);
  	assertEquals("Expect the french resource bundle", "French", res.getString("language"));
  	
    ResourceBundleData data = service_.createResourceBundleDataInstance();
    data.setName("locale.test");  data.setData(PROPERTIES); 
    service_.saveResourceBundle(data) ;
    data = service_.createResourceBundleDataInstance();
    data.setName("locale.test");  
    data.setLanguage(Locale.FRANCE.getLanguage());  
    data.setData(PROPERTIES_FR); 
    service_.saveResourceBundle(data) ;
    
  	res = service_.getResourceBundle("locale.test", Locale.ENGLISH) ;
  	assertTrue("Expect to find the ResourceBundle", res != null);
  	
  	res = service_.getResourceBundle("locale.test", Locale.FRANCE) ;
  	assertTrue("Expect to find the ResourceBundle", res != null);
  	assertEquals("Expect French locale bundle", "fr", res.getString("language"));
  	assertEquals("Expect French locale bundle", "property", res.getString("property"));
  	
    data.setData(PROPERTIES_FR_UPDATE); 
    service_.saveResourceBundle(data) ;
  	res = service_.getResourceBundle("locale.test", Locale.FRANCE) ;
  	assertEquals("Expect French locale bundle", "fr-property", res.getString("property"));
  	
    
    Query q = new Query(null, null) ;
    List l = service_.findResourceDescriptions(q).getAll() ;
    for(int i = 0; i < l.size() ; i ++) {
      ResourceBundleDescription impl = (ResourceBundleDescription)l.get(i);
      System.out.println("====> " + impl.getId()) ;
    }
  	assertTrue("Expect at least 4 locale properties resources",  l.size() >= 4);
  	
    int sizeBeforeRemove = l.size() ;
    data = service_.getResourceBundleData(data.getId());
    service_.removeResourceBundleData(data.getId());
    l = service_.findResourceDescriptions(q).getAll() ;
  	assertEquals("Expect 2 locale properties resources", sizeBeforeRemove - 1 , l.size());
  }
  
  protected String getDescription() {
    return "Test Resource Bundle Service" ;
  }
}