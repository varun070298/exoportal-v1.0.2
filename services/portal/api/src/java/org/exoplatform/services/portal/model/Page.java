/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;

/**
 * May 13, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Page.java,v 1.9 2004/11/03 01:23:55 tuan08 Exp $
 **/
public class Page extends Container {
  final static public String MAXIMIZE_STATE = "maximized" ;
  final static public String NORMAL_STATE = "normal" ;
  
  private String owner ;
  private String name ;
  private String icon ;
  private String state ;
  private String viewPermission ;
  private String editPermission ;
  
  public Page() {
  	setId("page") ;
  }
  
  public String getOwner() { return owner ; }
  public void   setOwner(String s) { owner = s ; } 
  
  public String getName() { return name ; }
  public void   setName(String s) { name = s ; } 
  
  public String getIcon() { return icon ; }
  public void   setIcon(String s) { icon = s ; } 
  
  public String getState() { return state ; }
  public void   setState(String s) { state = s ; } 
  
  public String getViewPermission() { return viewPermission ; }
  public void   setViewPermission(String s) { viewPermission = s ; } 
  
  public String getEditPermission() { return editPermission ; }
  public void   setEditPermission(String s) { editPermission = s ; } 

  public String getPageId() {	return owner + ":" + name ; }
  
  public Page clonePage() throws Exception {
  	return (Page) this.clone() ;
  }
  
  public Component softCloneObject() { 
  	Page page = new Page() ;
  	page.copyBasicProperties(this) ;
  	page.setOwner(owner) ;
  	page.setName(name) ;
		page.setTitle(getTitle()) ;
		page.setIcon(icon) ;
		page.setState(state) ;
		page.setViewPermission(viewPermission) ;
		page.setEditPermission(editPermission) ;
		page.getChildren().addAll(getChildren()) ;
  	return page ;
  }
}