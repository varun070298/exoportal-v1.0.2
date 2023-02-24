/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portletcontainer.imp;

import java.io.InputStream;
import java.net.URL;
import org.exoplatform.services.portletcontainer.impl.config.*;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.test.BasicTestCase;

/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestXPPParser.java,v 1.3 2004/10/29 02:21:50 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestXPPParser  extends BasicTestCase {

  public TestXPPParser(String name) {
    super(name);
  }

  public void setUp() throws Exception {  }
  
  
  public void testPortletContainerConfigParser() throws Exception {
    URL url = new URL("file:./src/java/conf/portal/portlet-container.xml");
    InputStream is = url.openStream() ;
    PortletContainer pc = XMLParser.parse(is) ;
    System.out.println(XMLSerializer.toXML(pc)) ;
  }
  
  public void testPortletAppParser() throws Exception {
    URL url = new URL("file:./war_template/WEB-INF/portlet.xml");
    InputStream is = url.openStream() ;
    PortletApp pa = 
      org.exoplatform.services.portletcontainer.pci.model.XMLParser.parse(is) ;
  }
  
  protected String getDescription() {
    return "Test Converter" ;
  }
}