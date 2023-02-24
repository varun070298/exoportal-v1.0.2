/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.resources;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: May 14, 2004
 * Time: 1:12:22 PM
 */
public interface ResourceBundleData extends ResourceBundleDescription {
  public String  getData()  ;
  public void setData(String name) ; 
}