/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.organization;

import java.util.Map;

/**
 * Created by The eXo Platform SARL
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Aug 21, 2003
 * Time: 3:22:54 PM
 */

public interface UserProfile {
	
	public String getUserName() ;
	
  public Map  getUserInfoMap()  ;
  public void setUserInfoMap(Map map)  ;
  
  public String getAttribute(String attName) ;
  public void setAttribute(String key, String value) ;
}