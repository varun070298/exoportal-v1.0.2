/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.portlet.renderer.html;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.monitor.portlet.component.UIPortletMenu;
import org.exoplatform.portlets.monitor.portlet.component.model.PortletApplicationData;
import org.exoplatform.services.portletcontainer.monitor.PortletRuntimeData;

/**
 * Apr 28, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortletMenuRenderer.java,v 1.4 2004/07/22 17:08:16 tuan08 Exp $
 **/
public class PortletMenuRenderer extends HtmlBasicRenderer {

  private static Parameter SELECT_PORTLET_APP =  new Parameter( ACTION , "selectPortletApp")  ;
  private static Parameter SHOW_PORTLET_APP_MONITOR =  new Parameter( ACTION , "showPortletAppMonitor")  ;
  private static Parameter SHOW_PORTLET_MONITOR =  new Parameter(ACTION , "showPortletMonitor")  ;
  final public static String EXPAND =   "<img class='expand' src='/skin/blank.gif'/>";
  final public static String COLLAPSE = "<img class='collapse' src='/skin/blank.gif'/>";


  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    UIPortletMenu uiMenu = (UIPortletMenu) component ;
    ResponseWriter w = context.getResponseWriter() ;
    String baseURL = context.getExternalContext().encodeActionURL(null) ;
    Parameter portletAppParam = new Parameter("portletAppName" , "");
    Parameter portletParam = new Parameter("portletName" , "");
    Parameter[] selectPortletAppParams = { SELECT_PORTLET_APP , portletAppParam} ;
    Parameter[] showPortletAppMonitorParams = { SHOW_PORTLET_APP_MONITOR , portletAppParam} ;
    Parameter[] showPortletMonitorParams = { SHOW_PORTLET_MONITOR , portletAppParam, portletParam} ;
    Collection apps = uiMenu.getPortletApplications() ;
    Iterator appsIterator = apps.iterator() ;
    w.write("<table>") ;
    while(appsIterator.hasNext()) {
      PortletApplicationData appData = (PortletApplicationData) appsIterator.next() ;
      String portletAppName = appData.getPortletAppName() ;
      portletAppParam.setValue(portletAppName);
      String sign = EXPAND ;
      if (appData.isSelect()) sign = COLLAPSE;
    	w.write("<tr>") ;
    	w.  write("<th>");
      appendLink(w, sign , baseURL, selectPortletAppParams, "") ;
      appendLink(w, portletAppName , baseURL, showPortletAppMonitorParams, "") ;
      w.write("</th>") ;
    	w.write("</tr>") ;
    	if(appData.isSelect()) {
    		Iterator portletDatasItr = appData.getPortletRuntimeDatas().iterator();
    		while(portletDatasItr.hasNext()) {
    			PortletRuntimeData rtd = (PortletRuntimeData)portletDatasItr.next() ;
    			String portletName = rtd.getPortletName();
    			portletParam.setValue(portletName);
    			w.write("<tr>") ;
    			w.  write("<td>");
          appendLink(w, portletName , baseURL, showPortletMonitorParams, "") ;
          w.  write("</td>") ;
    			w.write("</tr>") ;
    		}
      }
    }
    w.write("</table>") ;
  }
}