/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;

import java.util.* ;
/**
 * May 13, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Container.java,v 1.8 2004/11/03 01:23:55 tuan08 Exp $
 **/
public class Container extends Component {
  private String title ;
  private List children = new ArrayList(5) ;
  
  public String getTitle() { return title ; }
  public void   setTitle(String s) { title = s ; } 
  
  public List   getChildren() {  return children ; }
  public void   setChildren(List l) { children = l ; }
  public void   addChild(Component comp) { children.add(comp) ; }
  
  public Portlet findPortletByWindowId(String windowId) {
  	if(children == null) return null ;
  	for(int i = 0 ; i < children.size(); i++) {
  		Object child = children.get(i) ;
  		if(child instanceof Portlet) {
  			Portlet portlet = (Portlet) child ;
  			if(windowId.equals(portlet.getWindowId())) return portlet ; 
  		} else if (child instanceof Container){
  			Container container = (Container) child ;
  			Portlet portlet = container.findPortletByWindowId(windowId) ;
  			if(portlet != null) return portlet ;
  		}
  	}
  	return null ;
  }
  
  public Component softCloneObject() { 
  	Container container = new Container() ;
  	container.copyBasicProperties(this) ;
    container.setTitle(title) ;
  	container.getChildren().addAll(children) ;
  	return container ;
  }
}