/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portal.faces.component.model;

import java.util.* ;
import org.exoplatform.services.portletregistery.Portlet;
import org.exoplatform.services.portletregistery.PortletCategory;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 16 juin 2004
 */
public class PortletCategoryData {

	private boolean select_ ;
  private PortletCategory portletCategory;
  private Collection portlets;


  public PortletCategoryData(PortletCategory portletCategory, Collection portlets) {
    this.portletCategory = portletCategory;
    this.portlets = portlets;
  }
  
  public void reset(PortletCategory portletCategory, Collection portlets) {
  	this.portletCategory = portletCategory;
    this.portlets = portlets;
  }
  
  public String getPortletCategoryName() { return portletCategory.getPortletCategoryName(); }

  public PortletCategory getPortletCategory() {
    return portletCategory;
  }

  public Collection getPortlets() {
    return portlets;
  }

  public boolean isSelect() {return select_ ;}
  public void    setSelect(boolean b) { select_ = b ; }
  
  public Portlet findPortlet(String portletId) {
  	Iterator i = portlets.iterator() ;
  	while(i.hasNext()) {
  		Portlet portlet = (Portlet) i.next() ;
  		if(portlet.getId().equals(portletId)) return portlet ;
  	}
  	return null ;
  }
}