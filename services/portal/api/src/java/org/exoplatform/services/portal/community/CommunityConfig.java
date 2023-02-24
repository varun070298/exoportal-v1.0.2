/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.community;

import java.util.* ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 4, 2004
 * @version $Id$
 */
public class CommunityConfig {
  private List communityPortals = new ArrayList(3);
  private List communityNavigations = new ArrayList();
  
  public List getCommunityPortals() { return communityPortals ; }
  public void setCommunityPortals(List list) { communityPortals = list ; }
  
  public List getCommunityNavigations() { return communityNavigations ; }
  public void setCommunityNavigations(List list) { communityNavigations = list ; }
}
