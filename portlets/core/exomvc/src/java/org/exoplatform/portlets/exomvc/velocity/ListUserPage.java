/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.exomvc.velocity;

import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.portlet.exomvc.VelocityPage;
import org.exoplatform.services.organization.OrganizationService;

public class ListUserPage extends VelocityPage {
  
  public void processAction(ActionRequest req, ActionResponse res)  {
 
  }
  
  public void render(RenderRequest req, RenderResponse res) throws Exception {
    PortalContainer container = PortalContainer.getInstance() ;
    OrganizationService orgService = 
      (OrganizationService)container.getComponentInstanceOfType(OrganizationService.class) ;
    PageList pageList = orgService.getUserPageList(15) ; // 15 users per page
    //List page = pageList.currentPage() ; //get current use in page 1
    List all = pageList.getAll() ; // get all users in db
    Context ctx = new VelocityContext() ; 
    ctx.put("users", all) ;
    setVelocityContext(req, ctx) ;
    super.render(req, res) ;
  }
}