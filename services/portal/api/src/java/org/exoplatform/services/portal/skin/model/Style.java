/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.skin.model;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Jun 19, 2004
 */
public class Style {
  private String name ;
  private String url ;
  
  public Style() {
    
  }
  
  public Style(String name , String url) {
    this.name = name  ;
    this.url = url ;
  }
  
  public String getName() { return name ; }
  public void   setName(String s) { name = s ;}
  
  public String getUrl() { return url ; }
  public void   setUrl(String s) { url = s ; }
}