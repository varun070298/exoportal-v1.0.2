package org.exoplatform.portlets.exomvc ;

import java.util.* ;
import java.io.IOException;
import java.io.Writer ;
import javax.portlet.* ;
import org.exoplatform.portlet.exomvc.XHTMLBuilder ;
import org.exoplatform.portlet.exomvc.GroovyPage ;
import org.exoplatform.portlet.exomvc.config.* ;
import groovy.xml.MarkupBuilder ;
//import org.codehaus.groovy.sandbox.markup.StreamingMarkupBuilder ;


public class EditScript extends GroovyPage {

  public void render(RenderRequest req, RenderResponse res) {
    MarkupBuilder html = new MarkupBuilder(res.getWriter()) ; 
    String script = (String)req.getAttribute("page.script") ;
    String pageName = (String)req.getAttribute("page.name") ;
    String resourceURL = (String)req.getAttribute("page.resource.url") ;
    XHTMLBuilder builder = new XHTMLBuilder(res, this) ;
    if(script == null || pageName == null || resourceURL == null) { 
      builder.text("The script is not available")  ;
    } else {
	  builder.
        startTABLE().
          startTR().
            TD("page name: ").TD(pageName).
          closeTR().
          startTR().
            TD("page resource url: ").TD(resourceURL).
          closeTR().
          startTR().
          	startTD("colspan='2'").
          	  startFORM("method='post'").
          	    INPUT("type='hidden' name='pageName' value='${pageName}'").
          	    INPUT("type='hidden' name='resourceURL' value='${resourceURL}'").
          	    TEXTAREA("style='width: 750px; height: 500px'", script).
          	    startDIV("align='center'").
          	      INPUT("type='submit' name='cancel' value='cancel'").
          	      INPUT("type='submit' name='save' value='save'").
          	    closeDIV().
          	  closeFORM().
          	closeTD().
          closeTR().
        closeTABLE() ;
    }
  }
  
  public void processAction(ActionRequest req, ActionResponse res)  {
    String action = req.getParameter("action") ;
    println "action =====> " + action
  }
}