/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.test;

import java.io.InputStream;
import java.net.URL;
import org.exoplatform.services.portal.impl.*;
import org.exoplatform.services.portal.impl.converter.*;
import org.exoplatform.services.portal.model.*;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Preference;
import org.exoplatform.test.BasicTestCase;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestConverter.java,v 1.6 2004/07/20 12:41:09 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestConverter  extends BasicTestCase {
  private XStream xstream_ ; 
  
  public TestConverter(String name) {
    super(name);
  }

  public void setUp() throws Exception {
  	xstream_ = new XStream(new XppDriver());
    xstream_.alias("user-portal-config", Backup.class);
    xstream_.alias("portal-config", PortalConfig.class);
    xstream_.alias("page-set", PageSet.class);
    xstream_.alias("page", Page.class);
    xstream_.alias("container", Container.class);
    xstream_.alias("portlet", Portlet.class);
    xstream_.alias("body", Body.class);
    xstream_.alias("node", NodeImpl.class);
    xstream_.registerConverter(new PortletConverter());
    xstream_.registerConverter(new ExoPortletPreferencesConverter());
    xstream_.registerConverter(new PreferenceConverter());
  }
  
  public void testConverter() throws Exception {
  	Portlet portlet = new Portlet() ;
  	portlet.setRenderer("renderer") ;
  	portlet.setDecorator("decorator") ;
  	Preference pref = new Preference() ;
  	pref.setName("test") ;
  	pref.addValue("test") ;
  	ExoPortletPreferences prefs = new ExoPortletPreferences() ;
  	prefs.put(pref.getName(), pref) ;
  	portlet.setPortletPreferences(prefs) ;
  	String xml = xstream_.toXML(portlet);
  	System.out.println(xml) ;
  	portlet = (Portlet) xstream_.fromXML(xml) ;
  	assertEquals("Expect renderer value", "renderer", portlet.getRenderer()) ;
  	assertEquals("Expect decorator   value", "decorator", portlet.getDecorator()) ;
  }
  
  public void testParser() throws Exception {
    /*
     URL url = new URL("file:./src/java/exo/services/portal/conf/exo-config.xml");
     InputStream is = url.openStream() ;
     PortalConfig pc = XMLParser.parsePortal(is) ;
     */
  }
  protected String getDescription() {
    return "Test Converter" ;
  }
}