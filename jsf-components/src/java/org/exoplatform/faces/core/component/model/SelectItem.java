/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: SelectItem.java,v 1.2 2004/08/11 02:22:16 tuan08 Exp $
 */
public class SelectItem {
  public String display_ ;
  public String value_ ;

  public SelectItem(String display, String value) {
    display_ = display ;
    value_ = value ;
  }
}