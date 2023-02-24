package org.exoplatform.portlets.exomvc ;

import java.util.* ;
import java.io.IOException;
import javax.portlet.* ;
import org.exoplatform.portlet.exomvc.GroovyPage ;
import org.exoplatform.portlet.exomvc.config.* ;
import groovy.xml.MarkupBuilder ;

import org.exoplatform.container.PortalContainer ;
import org.exoplatform.services.organization.* ;

public class ListUser extends GroovyPage {

  public void render(RenderRequest req, RenderResponse res) {
    PortalContainer container = PortalContainer.getInstance() ;
    OrganizationService orgService = 
      (OrganizationService)container.getComponentInstanceOfType(OrganizationService.class) ;
    pageList = orgService.getUserPageList(15) ; // 15 users per page
    //List page = pageList.currentPage() ; //get current use in page 1
    List all = pageList.currentPage() ; // get all users in db
    MarkupBuilder html = new MarkupBuilder(res.getWriter()) ; 
    html.table  {
      tr {
        th("User name")
        th("First Name")
        th("Last Name")
        th("Email")
      }
      for (user in all) { 
      	tr {
          td(user.userName)
          td(user.firstName)
          td(user.lastName)
          td(user.email)
        }
      }
    }
  }
  
  public void processAction(ActionRequest req, ActionResponse res)  {
  }
}
