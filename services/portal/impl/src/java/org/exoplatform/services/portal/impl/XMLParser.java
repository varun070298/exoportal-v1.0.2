/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import java.io.InputStream;
import org.exoplatform.commons.xml.ExoXPPParser;
import org.exoplatform.services.portal.model.*;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Preference;


/**
 * Jul 8, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: XMLParser.java,v 1.4 2004/09/11 01:58:10 tuan08 Exp $
 */
public class XMLParser {
    
	static public  PortalConfig readPortalConfig(ExoXPPParser xpp ) throws Exception {
    PortalConfig pc = new PortalConfig() ;
    xpp.mandatoryNode("owner"); pc.setOwner(xpp.getContent()) ;
    xpp.mandatoryNode("locale"); pc.setLocale(xpp.getContent()) ;
    xpp.mandatoryNode("security"); pc.setViewPermission(xpp.getContent()) ;
		return pc ;
	}
  
  static public NodeImpl readNode(ExoXPPParser xpp ) throws Exception {
  	NodeImpl node = new NodeImpl() ;
    xpp.mandatoryNode("uri"); node.setUri(xpp.getContent()) ;
    xpp.mandatoryNode("name"); node.setName(xpp.getContent()) ;
    xpp.mandatoryNode("label"); node.setLabel(xpp.getContent()) ;
    if(xpp.node("description")) node.setDescription(xpp.getContent()) ;
    xpp.mandatoryNode("pageReference"); node.setUri(xpp.getContent()) ;
    int eventType = xpp.getEventType() ;
    String tagName = xpp.getName() ;
    while(!(eventType == ExoXPPParser.END_TAG && "node".equals(tagName))) {
    	if(eventType == ExoXPPParser.START_TAG && "node".equals(tagName)) {
        xpp.nextTag() ;
    		node.addChild(readNode(xpp)) ;
    	} else {
    		xpp.nextTag() ;
    		eventType = xpp.getEventType() ;
    		tagName = xpp.getName() ;
    	}
    }
    xpp.nextTag() ;
    return node ;
  }
  
  static public Container readContainer(ExoXPPParser xpp ) throws Exception {
  	Container c = new Container() ;
  	readBasicAttribute(c, xpp)  ;
  	int eventType = xpp.getEventType() ;
  	String tagName = xpp.getName() ;
  	while(eventType != ExoXPPParser.END_TAG && !"container".equals(tagName)) {
  		if(eventType == ExoXPPParser.START_TAG && "portlet".equals(tagName))  {
  			c.addChild(readPortlet(xpp)) ;
  		} else if(eventType == ExoXPPParser.START_TAG && "container".equals(tagName)) {
  			c.addChild(readContainer(xpp)) ;
  		} else if(eventType == ExoXPPParser.START_TAG && "body".equals(tagName)) {
  			c.addChild(readBody(xpp)) ;
  		} else {
  			xpp.next() ;
  			eventType = xpp.getEventType() ;
  			tagName = xpp.getName() ;
  		}
  	}
  	return c ;
  }
  
  static public Portlet readPortlet(ExoXPPParser xpp ) throws Exception {
    Portlet p = new Portlet() ; 
    readBasicAttribute(p, xpp)  ;
    if(xpp.node("portlet-style")); p.setPortletStyle(xpp.getContent()) ;
    if(xpp.node("showInfoBar")); p.setShowInfoBar("true".equals(xpp.getContent())) ;
    if(xpp.node("showWindowState")); p.setShowWindowState("true".equals(xpp.getContent()));
    xpp.mandatoryNode("windowId"); p.setWindowId(xpp.getContent()) ;
    if(xpp.node("portlet-preferences")); p.setPortletPreferences(readPortletPreferences(xpp));
    return p ;
  }
  
  static public Body readBody(ExoXPPParser xpp ) throws Exception {
    Body b = new Body() ; 
    readBasicAttribute(b, xpp)  ;
    xpp.mandatoryNode("componentType"); b.setComponentType(xpp.getContent()) ;
    xpp.mandatoryNode("componentId"); b.setComponentType(xpp.getContent()) ;
    return b ;
  }
  
  static public ExoPortletPreferences readPortletPreferences(ExoXPPParser xpp) throws Exception {
    ExoPortletPreferences prefs = new ExoPortletPreferences() ;
    while(xpp.node("preference"))prefs.addPreference(readPreference(xpp)) ;
    return prefs ;
  }
  
  static public Preference readPreference(ExoXPPParser xpp) throws Exception {
    Preference pref = new Preference() ;
    xpp.mandatoryNode("name"); pref.setName(xpp.getContent()) ;
    while(xpp.node("value"))pref.addValue(xpp.getContent()) ;
    if(xpp.node("read-only")) pref.setReadOnly(xpp.getContent()) ;
    return pref ;
  }
  
  static private void readBasicAttribute(Component comp, ExoXPPParser xpp) throws Exception {
    comp.setId(xpp.getNodeAttributeValue("id")) ;
    comp.setRenderer(xpp.getNodeAttributeValue("renderer")) ;
    comp.setDecorator(xpp.getNodeAttributeValue("decorator")) ;
    comp.setWidth(xpp.getNodeAttributeValue("width")) ;
    comp.setHeight(xpp.getNodeAttributeValue("height")) ;
  }
  
	static public PortalConfig parsePortal(InputStream is) throws Exception {
		ExoXPPParser xpp = ExoXPPParser.getInstance()  ;
		xpp.setInput (is, "UTF8");
		xpp.mandatoryNode("portal-config") ;
		return readPortalConfig(xpp) ;
	}
}