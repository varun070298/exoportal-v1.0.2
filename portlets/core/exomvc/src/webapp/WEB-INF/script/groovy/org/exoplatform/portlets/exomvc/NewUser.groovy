package org.exoplatform.portlets.exomvc ;

import java.util.* ;
import java.io.IOException;
import javax.portlet.* ;
import org.exoplatform.portlet.exomvc.GroovyPage ;
import org.exoplatform.portlet.exomvc.config.* ;
import groovy.xml.MarkupBuilder ;

public class NewUser extends GroovyPage {

  public void render(RenderRequest req, RenderResponse res) {
    MarkupBuilder html = new MarkupBuilder(res.getWriter()) ; 
    html.div("This is the new user page") 
  }
  
  public void processAction(ActionRequest req, ActionResponse res)  {

  }
}
