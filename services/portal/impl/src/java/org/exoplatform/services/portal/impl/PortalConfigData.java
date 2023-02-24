/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import org.exoplatform.services.portal.model.PortalConfig;
import com.thoughtworks.xstream.XStream;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="EXO_PORTAL_CONFIG"
 */
public class PortalConfigData extends PortalConfigDescriptionData {
  transient private PortalConfig config_ ; 

  public PortalConfigData() { }

  public PortalConfigData(String xml ) throws Exception { 
    setData(xml) ;
  }

  public PortalConfigData(PortalConfig config) throws Exception { 
    setPortalConfig(config) ;
  }
  
  /**
   * @hibernate.property length="65535" type="org.exoplatform.services.database.impl.TextClobType"
   **/
  public String getData() throws Exception { 
    XStream xstream = PortalConfigServiceImpl.getXStreamInstance() ;
    String xml =  xstream.toXML(config_) ;
    return xml ;
  }

  public void  setData(String s) throws Exception { 
    XStream xstream = PortalConfigServiceImpl.getXStreamInstance() ;
    config_ = (PortalConfig) xstream.fromXML(s) ;
    setOwner(config_.getOwner()) ;
    viewPermission = config_.getViewPermission() ;
    editPermission = config_.getViewPermission() ;
  }

  public PortalConfig getPortalConfig() { return config_ ; }
  public void         setPortalConfig(PortalConfig config) { 
  	config_ = config ;
  	setOwner(config_.getOwner()) ;
    viewPermission = config_.getViewPermission() ;
    editPermission = config_.getViewPermission() ;
  }
}