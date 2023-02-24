package org.exoplatform.portlets.exomvc ;

import java.util.* ;
import java.io.IOException;
import javax.portlet.* ;
import org.exoplatform.portlet.exomvc.GroovyPage ;
import org.exoplatform.portlet.exomvc.config.* ;
import org.exoplatform.portlet.exomvc.XHTMLBuilder ;
import org.exoplatform.portlet.exomvc.Page ;
import org.exoplatform.commons.xhtml.* ;

public class ListScript extends GroovyPage {
  final static public Attributes LIST_PAGES_TABLE_ATTRS = new Attributes("class:list-page");
  
  public void processAction(ActionRequest req, ActionResponse res)  {
    String action = req.getParameter("action") ;
    String page = req.getParameter("page") ;
    if("view" ==  action) {
      String scriptText = "Probable your page type a plain old java object style page and you cannot view the source code" ;
      config = getConfiguration() ;
      pageConfig  = config.getViewModePages().get(page) ;
      if(pageConfig instanceof GroovyPageConfig) {
        scriptResource = pageConfig.getGroovyScript() ;
        gmanager = config.getGroovyResourceManager().getGroovyManager();
        scriptText = gmanager.getGroovyObjectAsText(scriptResource) ;
      } else if(pageConfig instanceof JSPPageConfig) {
        scriptResource = pageConfig.getJSPPage() ;
        jspmanager = config.getJSPResourceManager() ;
        scriptText = jspmanager.getResourceAsText(scriptResource) ;
      } else if(pageConfig instanceof VelocityPageConfig) {
        scriptResource = pageConfig.getTemplate() ;
        vmanager = config.getVelocityResourceManager() ;
        scriptText = vmanager.getResourceAsText(scriptResource) ;
      }
      req.setAttribute("page.script", scriptText)
      setForwardPage(res, "groovy.view.script.page") ;
    } else if ("edit" == action) {
      String scriptText = "Probable your page type a plain old java object style page and you cannot view the source code" ;
      scriptResource = "N/A" ;
      config = getConfiguration() ;
      pageConfig  = config.getViewModePages().get(page) ;
      if(pageConfig instanceof GroovyPageConfig) {
        scriptResource = pageConfig.getGroovyScript() ;
        gmanager = config.getGroovyResourceManager().getGroovyManager();
        scriptText = gmanager.getGroovyObjectAsText(scriptResource) ;
      } else if(pageConfig instanceof JSPPageConfig) {
      	scriptResource = pageConfig.getJSPPage() ;
        jspmanager = config.getJSPResourceManager() ;
        scriptText = jspmanager.getResourceAsText(scriptResource) ;
      } else if(pageConfig instanceof VelocityPageConfig) {
        scriptResource = pageConfig.getTemplate() ;
        vmanager = config.getVelocityResourceManager() ;
        scriptText = vmanager.getResourceAsText(scriptResource) ;
      }
      req.setAttribute("page.name", page) ;
      req.setAttribute("page.resource.url", scriptResource) ;
      req.setAttribute("page.script", scriptText);
      setForwardPage(res, "groovy.edit.script.page") ;
    } else if ("run" == action)  {
      setForwardPage(res, page) ;
    }
  }

  public void render(RenderRequest req, RenderResponse res) {
    Configuration config = getConfiguration() ;
    Map map = config.getViewModePages() ;
    String pageURL = getPageURL(res)  ;
    ListScriptBuilder builder = new ListScriptBuilder(res, this) ; 
    builder.
      startTABLE(LIST_PAGES_TABLE_ATTRS).
        startTR().
          TH("Script Name").
          TH("Script URL").
          TH("Action").
        closeTR().
        listAvailablePages(map).
      closeTABLE() ;
  }
}

static public class ListScriptBuilder extends XHTMLBuilder {
  final static public Attributes ACTION_TD_ATTRS = new Attributes("class:action");
  public ListScriptBuilder(RenderResponse res,  Page page) { super(res, page) ; }

  public ListScriptBuilder listAvailablePages(Map map) {
    Iterator i = map.entrySet().iterator() ;
    while(i.hasNext()) {
      Map.Entry entry = (Map.Entry) i.next();
      String pageName = entry.getKey() ;
      pageConfig = entry.getValue() ;
      scriptURL = "N/A"
      if(pageConfig instanceof GroovyPageConfig) {
        scriptURL = pageConfig.getGroovyScript() ;
      } else  if(pageConfig instanceof JSPPageConfig) {
        scriptURL = pageConfig.getJSPPage()
      } else if(pageConfig instanceof VelocityPageConfig) {
        scriptURL = pageConfig.getTemplate()
      }
      startTR().
        startTD().text(pageName).closeTD().
        startTD().text(scriptURL).closeTD().
        startTD(ACTION_TD_ATTRS).
          link("action=view&page=" + pageName, "view").
          link("action=edit&page=" + pageName, "edit").
          link("action=run&page="  + pageName, "run").
        closeTD().
      closeTR() ;
    }
    return this ;
  }
}
