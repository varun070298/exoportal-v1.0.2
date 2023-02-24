/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.exomvc.jsp;

import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.portlet.exomvc.JSPPage;
import org.exoplatform.services.organization.OrganizationService;

public class ListUserPage extends JSPPage {
  
  public void processAction(ActionRequest req, ActionResponse res)  {
 
  }
  
  public void render(RenderRequest req, RenderResponse res) throws Exception {
    PortalContainer container = PortalContainer.getInstance() ;
    OrganizationService orgService = 
      (OrganizationService)container.getComponentInstanceOfType(OrganizationService.class) ;
    PageList pageList = orgService.getUserPageList(15) ; // 15 users per page
    //List page = pageList.currentPage() ; //get current use in page 1
    List all = pageList.getAll() ; // get all users in db
    req.setAttribute("jsp.list.user.page.users" , all) ; 
    super.render(req, res) ;
  }
}