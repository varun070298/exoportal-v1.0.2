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
public class AdminSuites  extends TestSuites {
  public AdminSuites() {
    addSuite(new UserLoginSuite("admin", "exo", new String[]{"user", "admin"})) ;
    addSuite(new PortletRegistrySuite("/Portlet Registry")) ;
    addSuite(new OrganizationPortletSuite("/Organization/User Registration")) ;
    addSuite(new ResourceBundlePortletSuite("/Portal/Internationalization")) ;
    addSuite(new ForumPortletAdminSuite("/Portlets/Forum")) ;
    addSuite(new WorkflowPortletAdminSuite("/Portlet/Workflow")) ;
    addSuite(new FileExplorerPortletAdminSuite("/Portlets/File Explorer")) ;
    addSuite(new WSRPAdminPortletSuite("/Portal/WSRP Admin")) ;
  }
}
