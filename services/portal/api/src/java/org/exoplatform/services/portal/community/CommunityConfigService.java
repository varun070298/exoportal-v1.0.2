/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.community;

import java.util.List ;
import org.exoplatform.services.organization.Group ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 4, 2004
 * @version $Id$
 */
public interface CommunityConfigService {
  public CommunityPortal findCommunityPortal(String user) throws Exception ;
  public CommunityPortal getCommunityPortal(Group group) throws Exception ;
  public void addCommunityPortal(CommunityPortal cp) throws Exception ;
  public void removeCommunityPortal(CommunityPortal cp) throws Exception ;
  
  
  public List findCommunityNavigation(String user) throws Exception ;
  public CommunityNavigation getCommunityNavigation(Group group) throws Exception ;
  public void addCommunityNavigation(CommunityNavigation cn) throws Exception ;
  public void removeCommunityNavigation(CommunityNavigation cn) throws Exception ;
}
