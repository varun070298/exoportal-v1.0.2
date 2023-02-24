/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.community;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 4, 2004
 * @version $Id$
 */
public class CommunityNavigation {
  private String  groupId ;
  private String  membership ;
  private String  navigation ;
  private String  description ;
  
  public String getGroupId() { return groupId ; }
  public void   setGroupId(String s) { groupId = s ; }
  
  public String getMembership() { return membership ; }
  public void   setMembership(String s) { membership = s ; }
  
  public String getNavigation() { return navigation ; }
  public void   setNavigation(String s) { navigation = s ; }
  
  public String getDescription()  { return description ; }
  public void   setDescription(String s)  { description = s ; }
}
