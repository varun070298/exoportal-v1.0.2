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
 * @hibernate.class  table="EXO_PAGE"
 */
public class PageData extends PageDescriptionData {

  transient private Page page_ ; 

  public PageData() { }

  public PageData(Page page) throws Exception { 
  	setPage(page) ;
  }

  public PageData(String xml) throws Exception { 
    setData(xml) ; 
  }

  /**
   * @hibernate.property length="65535" type="org.exoplatform.services.database.impl.TextClobType"
   **/
  public String getData() throws Exception { 
    XStream xstream = PortalConfigServiceImpl.getXStreamInstance() ;
    String xml = xstream.toXML(page_) ;
    return xml ;
  }

  public void  setData(String s) throws Exception { 
    XStream xstream = PortalConfigServiceImpl.getXStreamInstance() ;
    page_ = (Page)xstream.fromXML(s) ;
    owner_ = page_.getOwner() ;
    name_ = page_.getName() ;
    title_ = page_.getTitle() ;
    id_ = getId(page_);
    viewPermission_ = page_.getViewPermission() ;
    editPermission_ = page_.getEditPermission() ;
  }
  
  public Page getPage() { return page_ ; }
  public void    setPage(Page obj) { 
    page_ = obj ; 
    owner_ = page_.getOwner() ;
    name_ = page_.getName() ;
    title_ = page_.getTitle() ;
    id_ = getId(page_);
    viewPermission_ = page_.getViewPermission() ;
    editPermission_ = page_.getEditPermission() ;
  }
  
  static public String getId(Page page) {
    return page.getOwner() + ":" + page.getName() ;
  }
}