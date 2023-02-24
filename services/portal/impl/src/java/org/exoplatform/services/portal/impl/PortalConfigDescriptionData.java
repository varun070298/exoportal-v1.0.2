/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import org.exoplatform.services.portal.PortalConfigDescription;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 *
 * @hibernate.class  table="EXO_PORTAL_CONFIG"
 */
public class PortalConfigDescriptionData implements PortalConfigDescription {
  protected String id ;
  protected String viewPermission ;
  protected String editPermission ;  
  
  public PortalConfigDescriptionData() { }

  /**
   * @hibernate.id  generator-class="assigned"
   ***/
  public String   getId() { return id ; }
  public void     setId(String owner) { id = owner ; }
  
  public String   getOwner() { return id ; }
  public void     setOwner(String owner) { id = owner ; }
  
  /**
   * @hibernate.property
   **/
  public String getViewPermission()  { return viewPermission ; }
  public void   setViewPermission(String s)  { viewPermission = s ; }

  /**
   * @hibernate.property
   **/
  public String getEditPermission()  { return editPermission ; }
  public void   setEditPermission(String s)  { editPermission = s ; }
}