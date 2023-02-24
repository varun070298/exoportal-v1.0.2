/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 11, 2004
 * @version $Id: PageReference.java,v 1.1 2004/08/12 01:48:49 tuan08 Exp $
 */
public class PageReference {
  private String  type;
  private boolean visible;
  private String  pageReference;

  public PageReference() {
    visible = true ;
    type = "text/xhtml" ;
  }
  
  public PageReference(PageReference pref) {
    type = pref.getType() ;
    visible = pref.isVisible() ;
    pageReference = pref.getPageReference() ;
  }
  
  public String getPageReference() {
    return pageReference;
  }

  public void setPageReference(String pageReference) {
    this.pageReference = pageReference;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visibility) {
    this.visible = visibility;
  }
}