/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.portlet.component.model;

import java.util.* ;
import org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData;
/**
 * May 4, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortletApplicationData.java,v 1.1 2004/05/05 21:35:16 tuan08 Exp $
 **/
public class PortletApplicationData {
	private Map applications_ ;
	private boolean select_ ;
	private String name_ ;
  
  public PortletApplicationData(String name) {
    applications_ = new HashMap(10) ;
    select_ = false ;
    name_ = name ;
  }
  
  public String getPortletAppName() { return name_ ; }
  
  public boolean isSelect() {return select_ ;}
  public void    setSelect(boolean b) { select_ = b ; }
  
  public void put(PortletRuntimeData data) {
    applications_.put(data.getPortletName(), data) ;
  }
  
  public PortletRuntimeData getPortletRuntimeData(String portletName) {
    return (PortletRuntimeData) applications_.get(portletName) ; 
  }
  
  public Collection getPortletRuntimeDatas() {
    return  applications_.values() ; 
  }
}