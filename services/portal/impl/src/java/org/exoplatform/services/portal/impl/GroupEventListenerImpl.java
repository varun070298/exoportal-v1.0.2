/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portal.impl;

import java.util.* ;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.organization.GroupEventListener;
import net.sf.hibernate.Session;
import net.sf.hibernate.Hibernate;

/**
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Wed, Feb 18, 2004 @ 21:33 
 */
public class GroupEventListenerImpl extends GroupEventListener {
  private ExoCache userConfigCache_ ;
  private ExoCache groupConfigCache_ ;

  public GroupEventListenerImpl(ExoCache userConfigCache, ExoCache groupConfigCache) {
    userConfigCache_ = userConfigCache;
    groupConfigCache_ = groupConfigCache ;
  }
   
  public void preDelete(Group group,  Object transaction) throws Exception {
  }
}
