/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import org.exoplatform.services.portal.PageDescription;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="EXO_PAGE"
 */
public class PageDescriptionData  implements PageDescription {
  protected String id_ ;
  protected String name_ ;
  protected String owner_ ;
  protected String title_ ;
  protected String viewPermission_ ;
  protected String editPermission_ ;

  /**
   * @hibernate.id  generator-class="assigned" unsaved-value="null"
   ***/
  public String   getId() { return id_ ; }
  public void     setId(String id) { id_ = id ; }
  
  /**
   * @hibernate.property
   ***/
  public String   getName() { return name_ ; }
  public void     setName(String name) { name_ = name ; }

  /**
   * @hibernate.property
   ***/
  public String   getOwner() { return owner_ ; }
  public void     setOwner(String owner) { owner_ = owner ; }
  
  /**
   * @hibernate.property
   ***/
  public String   getTitle() { return title_ ; }
  public void     setTitle(String title) { title_ = title ; }

  /**
   * @hibernate.property
   ***/
  public String getViewPermission() { return viewPermission_ ; }
  public void   setViewPermission(String s) { viewPermission_ = s ; }

  /**
   * @hibernate.property
   ***/
  public String getEditPermission() {
  	if(editPermission_ == null) return "owner"; 
  	return editPermission_ ; 
  }
  public void   setEditPermission(String s) { editPermission_ = s ; }
}
