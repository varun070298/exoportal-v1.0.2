/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.jmx.renderer.html;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.management.ObjectName;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.renderer.html.HtmlBasicRenderer;
import org.exoplatform.portlets.jmx.component.UIMBeanServer;
import org.exoplatform.portlets.jmx.component.UINavigator;
import org.exoplatform.portlets.jmx.component.model.MBeanDomain;

/**
 * Jul 29, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: NavigatorRenderer.java,v 1.2 2004/08/01 04:18:28 tuan08 Exp $
 */
public class NavigatorRenderer extends HtmlBasicRenderer {
  private static Parameter selectMServerParam = new Parameter(ACTION, UINavigator.SELECT_ACTION) ;
  private static Parameter selectDomainParam = new Parameter(ACTION, UIMBeanServer.SELECT_DOMAIN_ACTION);
  private static Parameter selectMBeanParam = new Parameter(ACTION, UIMBeanServer.SELECT_MBEAN_ACTION);

  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
		UINavigator uiNavigator = (UINavigator) component ;
    java.util.List children = uiNavigator.getChildren() ;
    ResourceBundle res = getApplicationResourceBundle(context.getExternalContext()) ;
    ResponseWriter w = context.getResponseWriter()  ;
    Parameter domainParam = new Parameter("domain", "") ;
    Parameter[] selectDomainParams = {selectDomainParam, domainParam} ;
    Parameter mbeanParam = new Parameter("mbean", "") ;
    Parameter[] selectMBeanParams = {selectMBeanParam, mbeanParam} ;
    Parameter mserverParam = new Parameter("mserver", "") ;
    Parameter[] selectMServerParams = {selectMServerParam, mserverParam} ;
    for(int i = 0 ; i < children.size(); i++) {
      UIMBeanServer uiServer = (UIMBeanServer) children.get(i) ;
      mserverParam.setValue(uiServer.getId()) ;
      w.write("<div>\n") ;
      if(uiServer.isRendered()) w.write(res.getString("UINavigator.icon.collapse")) ;
      else  w.write(res.getString("UINavigator.icon.expand")) ;
      linkRenderer_.render(w,uiNavigator , uiServer.getName(), selectMServerParams) ;
      if(uiServer.isRendered()) {
        MBeanDomain rootDomain = uiServer.getRootDomain() ;
        List childrenDomains = rootDomain.getDomains() ;
        for(int j = 0 ; j < childrenDomains.size(); j++) { 
          MBeanDomain child = (MBeanDomain) childrenDomains.get(j) ;
          renderDomain(w, uiServer, child, selectDomainParams, selectMBeanParams, res) ;
        }
      }
      w.write("</div>\n") ;
    }
	}	
  
  public void renderDomain(ResponseWriter w,  UIMBeanServer uiServer, MBeanDomain domain, 
                           Parameter[] selectDomainParams,
                           Parameter[] selectMBeanParams,
                           ResourceBundle res) throws IOException {
     w.write("<ul>") ;
     selectDomainParams[1].setValue(domain.getDomainName()) ;
     if(domain.isSelect()) w.write(res.getString("UINavigator.icon.collapse")) ;
     else  w.write(res.getString("UINavigator.icon.expand")) ;
     linkRenderer_.render(w,uiServer, domain.getLabel(), selectDomainParams) ;
     if(domain.isSelect()) {
      List childrenDomains = domain.getDomains() ;
      for(int i = 0 ; i < childrenDomains.size(); i++) { 
      	MBeanDomain child = (MBeanDomain) childrenDomains.get(i) ;
        renderDomain(w, uiServer, child, selectDomainParams, selectMBeanParams, res) ;
      }
      List mbeans = domain.getMBeanNames() ;
      for(int i = 0 ; i < mbeans.size(); i++) { 
      	MBeanDomain.MBeanDescription  mbean = (MBeanDomain.MBeanDescription ) mbeans.get(i) ;
        selectMBeanParams[1].setValue(mbean.getId()) ;
        w.write("<li class='leaf'>") ;
        linkRenderer_.render(w, uiServer,mbean.getLabel(), selectMBeanParams) ;
        w.write("</li>") ;
      }
     }
     w.write("</ul>\n") ;
  }
}