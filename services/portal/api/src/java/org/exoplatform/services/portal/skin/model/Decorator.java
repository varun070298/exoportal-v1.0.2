/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.skin.model;

import java.util.* ;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Jun 19, 2004
 */
public class Decorator {
  private String rendererType ;
  private List styles ;
  
  public Decorator() {
    styles = new ArrayList(5) ;
  }
  
  public String getRendererType() { return rendererType ; }
  public void   setRendererType(String s) { rendererType = s ;}
  
  public List getStyles() { return styles ; }
  public void setStyles(List list) { styles = list;}
  public void addStyle(Style s) { styles.add(s) ; }
}
