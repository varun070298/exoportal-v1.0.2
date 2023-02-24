/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.test;


import java.io.InputStream;
import java.net.URL;
import java.util.* ;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.*;
import org.exoplatform.test.BasicTestCase;

/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestSkinConfigService.java,v 1.7 2004/07/24 16:34:06 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestSkinConfigService  extends BasicTestCase {
  static protected SkinConfigService   service_ ;

  public TestSkinConfigService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    if (service_ == null) {
      PortalContainer manager  = PortalContainer.getInstance();
      service_ = 
        (SkinConfigService) manager.getComponentInstanceOfType(SkinConfigService.class) ;
    }
  }

  public void tearDown() throws Exception {
  }
  
  public void testService() throws Exception {
    
    URL url = new URL("file:./src/test/org/exoplatform/services/portal/test/skin.xml");
    InputStream is = url.openStream() ;
    service_.addConfiguration(is) ;
    
    Decorator decorator = service_.getPageDecorator("default");
    assertTrue("Expect decorator is found", decorator != null) ;
    Collection list = service_.getPageDecorators();
    assertEquals("Expect 1 decorator in the list", 1, list.size()) ;

    
    decorator = service_.getContainerDecorator("default");
    assertTrue("Expect rendere info is found", decorator != null) ;
    list = service_.getContainerDecorators();
    assertEquals("Expect 3 decorators in the list", 3, list.size()) ;

    decorator = service_.getPortletDecorator("default");
    assertTrue("Expect decorator is found", decorator != null) ;
    list = service_.getPortletDecorators();
    assertEquals("Expect 4 Renderer info in the list", 3, list.size())  ;
    
    list = service_.getPortletStyles("default") ;
    assertEquals("Expect 1 porlet style config in the list", 2, list.size()) ;
    Style style = service_.getPortletStyle("default", "exo") ;
    assertTrue("Expect style is found", style != null) ;
  }
  
  protected String getDescription() {
    return "Test Portal Configuration service" ;
  }
}