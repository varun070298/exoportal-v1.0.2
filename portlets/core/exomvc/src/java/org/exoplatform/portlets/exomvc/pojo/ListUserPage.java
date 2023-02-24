/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.exomvc.pojo;

import java.io.IOException;
import java.util.List;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.commons.xhtml.Attributes ;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.portlet.exomvc.Page;
import org.exoplatform.portlet.exomvc.XHTMLBuilder;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;

public class ListUserPage extends Page {
  static private Attributes USER_TABLE_ATTRS = new Attributes("border:1|width:100%") ; 
  
  public void processAction(ActionRequest req, ActionResponse res)  {
 
  }
  
  public void render(RenderRequest req, RenderResponse res) throws Exception {
    PortalContainer container = PortalContainer.getInstance() ;
    OrganizationService orgService = 
      (OrganizationService)container.getComponentInstanceOfType(OrganizationService.class) ;
    PageList pageList = orgService.getUserPageList(15) ; // 15 users per page
    //List page = pageList.currentPage() ; //get current use in page 1
    List all = pageList.getAll() ; // get all users in db
    ListUserBuilder builder = new ListUserBuilder(res, this) ;
    builder.
      startTABLE(USER_TABLE_ATTRS).
        startTR().
          startTH().text("User Name").closeTH().
          startTH().text("First Name").closeTH().
          startTH().text("Last Name").closeTH().
          startTH().text("Email").closeTH().
        closeTR();
        builder.createUsersBlock(all).
      closeTABLE() ;       
  }
  
  static public class ListUserBuilder extends XHTMLBuilder {
    public ListUserBuilder(RenderResponse res,  Page page) throws Exception { 
      super(res, page) ; 
    }

    public ListUserBuilder createUsersBlock(List users) throws IOException {
      for (int i =0; i < users.size(); i++) {
        User user = (User) users.get(i) ;
        startTR().
          TD(user.getUserName()).
          TD(user.getFirstName()).
          TD(user.getLastName()).
          TD(user.getEmail()).
        closeTR();
      }
      return this ;
    }
  }
}