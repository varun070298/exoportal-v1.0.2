/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;

/**
 * May 13, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Component.java,v 1.6 2004/11/03 01:23:55 tuan08 Exp $
 **/
abstract public class Component implements Cloneable  {
	private String id = "" ;
	private String renderer = "default";
  private String decorator = "default";
  private String width ;
  private String height ;
  
  public Component() {
    /* TODO : Test of user-agent and MIME type or markup parameters found
     * in ExoPortalInfo instance (no yet implemented) to choose correct
     * renderer (in order to adapt output with markup requested by client)
     * for example :
     * renderer = "xhtmlmp"; 
     * for a mobile phone browser.
     * 
     */  
  } 
  
  public Component(String renderer, String style, String width, String height) {
  	this.renderer = renderer ;
  	this.decorator = style ;
  	this.width = width ;
  	this.height = height ;
  }
  
  public String getId() { return id ;}
  public void   setId(String s) { id = s ; }
  
  public String getRenderer() { return renderer ; }
  public void   setRenderer(String s) {
    if(s == null) renderer = "default" ;
    else renderer = s ;
  }
  
  public String getDecorator() { return decorator ; }
  public void   setDecorator(String s) {
    if (s == null) decorator = "default" ;
    else decorator = s ;
  }
  
  public String getWidth() { return width ; }
  public void   setWidth(String s) { width = s ;}
  
  public String getHeight() { return height ; }
  public void   setHeight(String s) { height = s ;}
  
  public Object deepCloneObject() throws Exception {
  	return super.clone() ;
  }
  
  abstract public Component softCloneObject()  ;
  
  protected void copyBasicProperties(Component comp) {
  	id  = comp.getId() ;
  	renderer = comp.getRenderer() ;
  	decorator = comp.getDecorator() ;
  	width = comp.getWidth() ;
  	height = comp.getHeight() ;
  }
}