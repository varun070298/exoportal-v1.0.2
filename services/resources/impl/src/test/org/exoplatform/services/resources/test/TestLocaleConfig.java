/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.resources.test;

import org.exoplatform.services.resources.LocaleConfigService;
import org.exoplatform.test.BasicTestCase;
import org.exoplatform.container.PortalContainer ;

/**
 * Thu, May 15, 2004 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestUIComponentFactory.java,v 1.4 2004/08/05 14:58:42 tuan08 Exp $
 * @email: tuan08@yahoo.com
 */
public class TestLocaleConfig extends BasicTestCase {
  private LocaleConfigService service_ ;
  
  public TestLocaleConfig(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    service_ = (LocaleConfigService) PortalContainer.getInstance().
                                     getComponentInstanceOfType(LocaleConfigService.class) ;
  }
  
  public void tearDown() throws Exception {

  }
  
  public void testLocaleConfigManager() throws Exception {
    System.out.println(service_) ;
  }
}