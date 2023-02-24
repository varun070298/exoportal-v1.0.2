/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization;

import org.exoplatform.services.database.XResources;
/**
 * Author : Tuan Nguyen
 *          tuan08@groups.sourceforge.net
 * Wed, Feb 18, 2004 @ 21:33 
 */
public class GroupEventListener {
  public void preSave(Group group, boolean isNew, XResources xresources) throws Exception {
  }

  public void postSave(Group group, boolean isNew, XResources xresources) throws Exception {
  }

  public void preDelete(Group group,  XResources xresources) throws Exception {
  }

  public void postDelete(Group group,  XResources xresources) throws Exception {
  }
}
