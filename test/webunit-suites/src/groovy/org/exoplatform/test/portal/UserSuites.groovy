/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.test.portal;

import org.exoplatform.test.web.* ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 2, 2004
 * @version $Id: UserSuites.java,v 1.4 2004/10/21 15:21:50 tuan08 Exp $
 */
public class UserSuites extends TestSuites {
  public UserSuites() {
    String[] roles = new String[] {"user"} ;
    addSuite(new NewAccountSuite()) ;
    addSuite(new UserLoginSuite("#{client.name}", "#{client.name}", roles)) ;
    addSuite(new CustomizationSuite()) ;
    addSuite(new RandomPageBrowseSuite()) ;
    addSuite(new ForumPortletUserSuite("/Community/Forum")) ;
    addSuite(new MessagePortletUserSuite("/Demo/Mail")) ;
    addSuite(new ResourceBundlePortletSuite("/Demo/Resources")) ;
    addSuite(new WorkflowPortletUserSuite("/Demo/Workflow")) ;
    addSuite(new FileExplorerPortletUserSuite("/Demo/File Explorer")) ;
    addSuite(new ExoMVCPortletSuite("/Demo/eXo Platform MVC")) ;
    addSuite(new WSRPConsummerPortletSuite("/Demo/WSRP/Consumer Portlet")) ;
    addSuite(new ContentPortletSuite()) ;
  }
}
