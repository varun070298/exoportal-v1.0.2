/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;
/**
 * May 15, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Body.java,v 1.3 2004/06/26 20:00:10 tuan08 Exp $
 **/
public class Body extends Component {
  final static public String PAGE_NODE_TYPE = "page-node" ;
  
  private String componentType ;
  private String componentId ;
  
  public Body() {
  	setId("body") ;
  }
  
  public String getComponentId() { return componentId ;}
  public void   setComponentId(String s) { componentId = s ; } 
  
  public String getComponentType() { return componentType ;}
  public void   setComponentType(String s) { componentType = s ; } 
  
  public Component softCloneObject() { 
  	Body body = new Body() ;
  	body.copyBasicProperties(this) ;
  	setComponentType(componentType) ;
  	setComponentId(componentId) ;
  	return body ;
  }
}