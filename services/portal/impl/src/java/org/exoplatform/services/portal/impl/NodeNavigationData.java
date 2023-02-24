/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import org.exoplatform.services.portal.model.*;
import com.thoughtworks.xstream.XStream;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="EXO_NODE_NAVIGATION"
 */ 
public class NodeNavigationData  {
	
  private String owner_ ;
  transient private NodeNavigation node_ ; 

  public NodeNavigationData() { }
  
  public NodeNavigationData(String owner, PageNode node) { 
  	owner_ = owner ;
    node_ = new NodeNavigation() ;
    node_.setOwner(owner) ;
    node_.setNode(node) ;
  }

  public NodeNavigationData(NodeNavigation node) throws Exception {
    setNodeNavigation(node) ;
  }

  public NodeNavigationData(String xml) throws Exception { 
     setData(xml) ;
  }

  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public String   getId() { return owner_ ; }
  public void     setId(String owner) { owner_ = owner ; }
  
  public String   getOwner() { return owner_ ; }
  public void     setOwner(String owner) { owner_ = owner ; }
  
  /**
   * @hibernate.property length="65535" type="org.exoplatform.services.database.impl.TextClobType"
   **/
  public String getData() throws Exception { 
    XStream xstream = PortalConfigServiceImpl.getXStreamInstance() ;
    String xml = xstream.toXML(node_) ;
    return xml ;
  }

  public void  setData(String s) throws Exception { 
    XStream xstream = PortalConfigServiceImpl.getXStreamInstance() ;
    node_ = (NodeNavigation)xstream.fromXML(s) ;
    owner_ = node_.getOwner() ;
  }
  
  public NodeNavigation getNodeNavigation() { return node_ ; }
  public void  setNodeNavigation(NodeNavigation obj) {
    owner_ = obj.getOwner() ;
    node_ = obj ; 
  }
}