/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public class ForumServiceContext  {
  private String serviceOwner_ ;
  
  public ForumServiceContext(String owner) {
    serviceOwner_ = owner ;
  }
  
  public String getServiceOwner() {
    return serviceOwner_ ;
  }

  public void  setServiceOwner(String name) {
    serviceOwner_ = name ;
  }
}
