/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.iframe.component;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.portlet.PortletRequest ;
import javax.portlet.PortletPreferences ;

import org.apache.commons.logging.Log;
import org.exoplatform.faces.core.component.UIExoComponentBase;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIIframe.java,v 1.1.1.1 2004/03/02 19:00:43 benjmestrallet Exp $
 */
public class UIIframe extends UIExoComponentBase {
  public static final String COMPONENT_TYPE = "UIIframe";
  public static final String VIEW_ID = "rss";
  
  private String frameSource_  ;
  private String frameWidth_  ;
  private String frameHeight_  ;
  
  public UIIframe() throws Exception {
    FacesContext context = FacesContext.getCurrentInstance() ;
    PortletRequest request = (PortletRequest) context.getExternalContext().getRequest();
    PortletPreferences prefs = request.getPreferences() ;
    frameSource_ = prefs.getValue("iframe-src" , "/web/iframe-info.html") ;
    frameWidth_ = prefs.getValue("iframe-width", "*") ;
    frameHeight_ = prefs.getValue("iframe-height", "*") ;
    setId(VIEW_ID) ;
  }
  
  public String getFrameSource() { return frameSource_ ; }
  public String getFrameWidth() { return frameWidth_ ; }
  public String getFrameHeight() { return frameHeight_ ; }
  
  public String getComponentType() { return COMPONENT_TYPE; }
  public String getRendererType() { return "IframeRenderer"; }

  public void decode(FacesContext context) {
  }
}
