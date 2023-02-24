/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization;

import org.exoplatform.services.database.XResources;

/**
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 22, 2003
 * Time: 4:46:04 PM
 */
public class UserProfileEventListener {
  public void preSave(UserProfile user, boolean isNew, XResources xresources) throws Exception {
  }

  public void postSave(UserProfile user, boolean isNew, XResources xresources) throws Exception {
  }

  public void preDelete(UserProfile user, XResources xresources) throws Exception {
  }

  public void postDelete(UserProfile user, XResources xresources) throws Exception {
  }
}
