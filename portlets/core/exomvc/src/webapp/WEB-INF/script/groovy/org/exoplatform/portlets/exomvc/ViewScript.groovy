package org.exoplatform.portlets.exomvc ;

import java.util.* ;
import java.io.IOException;
import javax.portlet.* ;
import org.exoplatform.portlet.exomvc.GroovyPage ;
import org.exoplatform.portlet.exomvc.config.* ;
import groovy.xml.MarkupBuilder ;
import org.exoplatform.commons.utils.HtmlUtil ;
//import org.codehaus.groovy.sandbox.markup.StreamingMarkupBuilder ;


public class ViewScript extends GroovyPage {

  public void render(RenderRequest req, RenderResponse res) {
    String script = (String)req.getAttribute("page.script") ;
    String pageURL = getPageURL(res)  ;
    MarkupBuilder html = new MarkupBuilder(res.getWriter()) ; 
    if(script == null) { 
      html.div("The script is not available")  ;
    } else {
      html.pre(HtmlUtil.showXmlTags(script)) ;
    }
  }
  
  public void processAction(ActionRequest req, ActionResponse res)  {
    String action = req.getParameter("action") ;
    println "action =====> " + action
  }
}
