/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public class Query {
  private String owner_ ;
  private String viewPermission_ ;
  private String editPermission_ ;

  public Query(String owner, String vp , String ep ) {
    owner_ = owner ;
    viewPermission_ = vp ;
    editPermission_ = ep ;
  }
  
  public String getOwner() { return owner_ ; }
  public void   setOwner(String s) { owner_ = s ; }
  
  public String getViewPermission() { return viewPermission_ ; }
  public void   setViewPermission(String s) { viewPermission_ = s ; }
  
  public String getEditPermission() { return editPermission_ ; }
  public void   setEditPermission(String s) { editPermission_ = s ; }
}
