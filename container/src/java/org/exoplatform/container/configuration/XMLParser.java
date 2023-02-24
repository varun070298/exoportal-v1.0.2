/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.container.configuration;

import java.io.InputStream;
import org.exoplatform.commons.xml.ExoXPPParser;

/**
 * Jul 8, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: XMLParser.java,v 1.5 2004/10/29 01:55:23 tuan08 Exp $
 */
public class XMLParser {
    
	static public Configuration readConfiguration(ExoXPPParser xpp ) throws Exception {
		Configuration conf = new Configuration()  ;
    if(xpp.node("global-param")) readGlobalInitParam(conf, xpp)  ;
    while(xpp.node("service-configuration")) {
      conf.addServiceConfiguration(readServiceConfiguration(xpp)) ;
    }
    while(xpp.node("import")) { 
      conf.addImportConfiguration(xpp.getContent()) ;
    }
    while(xpp.node("remove-configuration")) { 
      conf.addRemoveConfiguration(xpp.getContent()) ;
    }
		return conf ;
	}
	
  static public void readGlobalInitParam(Configuration conf, ExoXPPParser xpp) throws Exception {
    while(xpp.node("init-param")) conf.addInitParam(readValuesParam(xpp)) ;
  }
  
  static public ValuesParam readValuesParam(ExoXPPParser xpp) throws Exception {
    ValuesParam param = new ValuesParam() ;
    param.setName(xpp.mandatoryNodeContent("name")) ;
    param.setDescription(xpp.nodeContent("description")) ;
    while(xpp.node("value")) param.addValue(xpp.getContent()) ; 
    return param ;
  }
  
  static public ValueParam readValueParam(ExoXPPParser xpp) throws Exception {
    ValueParam param = new ValueParam() ;
    param.setName(xpp.mandatoryNodeContent("name")) ;
    param.setDescription(xpp.nodeContent("description")) ;
    param.setValue(xpp.nodeContent("value")) ; 
    return param ;
  }
  
  static public PropertiesParam readPropertiesParam(ExoXPPParser xpp) throws Exception {
    PropertiesParam param = new PropertiesParam() ;
    param.setName(xpp.mandatoryNodeContent("name")) ;
    param.setDescription(xpp.nodeContent("description")) ;
    while(xpp.node("property"))  {
      param.setProperty(xpp.getNodeAttributeValue("name"), xpp.getNodeAttributeValue("value")) ; 
    }
    return param ;
  }
  
  static public ObjectParam readObjectParam(ExoXPPParser xpp) throws Exception {
    ObjectParam param = new ObjectParam() ;
    param.setName(xpp.mandatoryNodeContent("name")) ;
    param.setType(xpp.mandatoryNodeContent("type")) ;
    param.setDescription(xpp.nodeContent("description")) ;
    while(xpp.node("property"))  {
      param.addProperty(xpp.getNodeAttributeValue("name"), xpp.getNodeAttributeValue("value")) ; 
    }
    return param ;
  }
	
  static public ServiceConfiguration readServiceConfiguration(ExoXPPParser xpp) throws Exception {
    ServiceConfiguration sconf = new ServiceConfiguration() ;
    sconf.setServiceType(xpp.getNodeAttributeValue("type"));
    sconf.setServiceKey(xpp.getNodeAttributeValue("key"));
    boolean paramNode = true ;
    while(paramNode) {
      if(xpp.node("values-param")) {
        sconf.addParameter(readValuesParam(xpp)) ;
      } else if(xpp.node("value-param")) {
        sconf.addParameter(readValueParam(xpp)) ;
      } else if(xpp.node("properties-param")) {
        sconf.addParameter(readPropertiesParam(xpp)) ;
      } else if(xpp.node("object-param")) {
        sconf.addParameter(readObjectParam(xpp)) ;
      } else {
        paramNode = false ;
      }
    }
    return sconf ;
  }
  
  
	static public Configuration parse(InputStream is) throws Exception {
		ExoXPPParser xpp = ExoXPPParser.getInstance()  ;
		xpp.setInput(is, "UTF8");
		xpp.mandatoryNode("configuration") ;
		return readConfiguration(xpp) ;
	}
}