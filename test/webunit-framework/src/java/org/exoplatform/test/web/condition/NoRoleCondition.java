/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.web.condition;

import org.exoplatform.test.web.ExoWebClient;
import com.meterware.httpunit.*;

/**
 * May 21, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: NoRoleCondition.java,v 1.1 2004/10/11 23:36:04 tuan08 Exp $
 **/
public class NoRoleCondition implements Condition {
  private String role_ ;
  
  public NoRoleCondition(String role) {
    role_ = role ;
  }
  
  public boolean checkCondition(WebResponse response, WebTable block, ExoWebClient client) throws Exception {
    return !client.getRoles().containsKey(role_) ;
  }
  
  public String getName() { return "NoRoleCondition" ; }
  public String getDescription() {
    return "This unit test should run only if the remote user do not have the role '" + role_ + "'" ;
  }
}