/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.nav.renderer.html;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.container.monitor.ActionData;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portal.session.RequestInfo;
import org.exoplatform.portlets.nav.component.UIVisitedPages;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 31 aout 2004
 */
public class VisitedPagesRenderer extends HtmlBasicRenderer {
  final static protected Parameter CHANGE_NODE = new Parameter(ACTION, "changeNode");

  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    UIVisitedPages uiComponent = (UIVisitedPages) component;
    RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
    String ownerURI = rinfo.getOwnerURI() ;
    ResponseWriter w = context.getResponseWriter() ;

    w.write("<div class='UIVisitedPages'>");
    w.	write("<div>"); w.write(res.getString("UIVisitedPages.label.visited-pages")); w.write("</div>");
    int numberOfPages = uiComponent.getNumberOfPages() ;
    int limit = 0 ; //show all
    List history = SessionContainer.getInstance().getMonitor().getHistory();
    if(numberOfPages < 0) limit =  0 ; // show all
    else if( history.size() > numberOfPages) limit = history.size() - numberOfPages ;
    Iterator  i =  history.iterator() ;
    int count = 0 ;
    while(i.hasNext()){
      count++ ;
      if(count < limit ) {
        i.next() ;
        continue ;
      }
      ActionData data = (ActionData) i.next();
      String page = data.getPage() ;
      w.write("<div>");
      if(page == null || page.length() == 0) {
        w.  write("N/A") ;
      } else {
        w.write("<a href='"); w.write(ownerURI); w.write(data.getPage()); w.write("'>") ;
        w.  write(data.getPage()) ;
        w. write("</a>") ;
      }
      w.write("</div>");
    }
    if(numberOfPages < 0) {
      linkRenderer_.render(w, uiComponent, res.getString("UIVisitedPages.button.less"), 
                           UIVisitedPages.lessParams);
    } else {
      linkRenderer_.render(w, uiComponent, res.getString("UIVisitedPages.button.more"), 
                           UIVisitedPages.moreParams);
    }
    w.write("</div>");
  }
}
