/**************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved. *
 * Please look at license.txt in info directory for more license detail.  *
 *************************************************************************/
package org.exoplatform.faces.core.renderer.html;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import org.exoplatform.commons.utils.Formater;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.faces.context.PortletExternalContext;
import org.exoplatform.faces.core.component.UIExoComponent;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.portal.session.PortalResources;
import org.exoplatform.portal.session.RequestInfo;
/**
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: HtmlBasicRenderer.java,v 1.22 2004/10/16 21:13:51 tuan08 Exp $
 */
public  class HtmlBasicRenderer extends Renderer {
	final static public String ACTION = UIExoComponent.ACTION  ;
  final static public String SAVE_ACTION = UIExoComponent.SAVE_ACTION  ;
  final static public String CANCEL_ACTION =  UIExoComponent.CANCEL_ACTION ;
  final static public String EDIT_ACTION = UIExoComponent.EDIT_ACTION  ;
  final static public String BACK_ACTION = UIExoComponent.BACK_ACTION  ;
  final static public String DELETE_ACTION = UIExoComponent.DELETE_ACTION  ;
  final static public String REMOVE_ACTION = UIExoComponent.DELETE_ACTION  ;
  
  final static public Parameter[] EMPTY_PARAMS = new Parameter[0] ;
  protected static Formater ft_ = Formater.getFormater(null) ;
  final static public LinkRenderer defaultLinkRenderer = new LinkRenderer() ;
  final static public ButtonRenderer defaultButtonRenderer = new ButtonRenderer() ;
  
  protected LinkRenderer linkRenderer_ = defaultLinkRenderer ;
  protected ButtonRenderer buttonRenderer_ = defaultButtonRenderer ;
	
  public void decode(FacesContext context, UIComponent uiComponent) {
  
  }

  public void encodeBegin( FacesContext context, UIComponent component ) throws IOException {    
  }

  public void encodeChildren( FacesContext context, UIComponent component ) throws IOException {
    //throw new IOException("user Children Renderer for class " + component.getClass().getName()) ;
  }

  public void encodeEnd( FacesContext context, UIComponent component ) throws IOException {
  }
  
  public void renderChildren(FacesContext context, UIComponent component ) throws IOException {
    List children = component.getChildren() ;
    for(int i = 0 ; i < children.size() ; i++) {
      UIComponent uiChild = (UIComponent) children.get(i) ;
      if(uiChild.isRendered()) {
        uiChild.encodeBegin(context) ;
        uiChild.encodeChildren(context) ;
        uiChild.encodeEnd(context) ;
      }
    }
  }
  
  static public ResourceBundle getApplicationResourceBundle(ExternalContext eContext) {
		if (eContext instanceof PortletExternalContext) {
	    PortletExternalContext econtext =  (PortletExternalContext) eContext ;
	    return econtext.getApplicationResourceBundle() ;
		}
    PortalResources appres = 
      (PortalResources)SessionContainer.getComponent(PortalResources.class);
    return appres.getApplicationResource();
	}
  
  static public String getBaseURL(FacesContext context) {
    ExternalContext econtext = context.getExternalContext();
  	if (econtext instanceof PortletExternalContext) {
  		return econtext.encodeActionURL("") ;
  	}
  	RequestInfo rinfo = (RequestInfo)SessionContainer.getComponent(RequestInfo.class);
  	return rinfo.getPageURI();
  }
 
  static public void appendLink(ResponseWriter w, String text, String baseURL, 
                                Parameter[] params, String tooltip) throws IOException {
    w.write("<a");
    w.write(" href='");w.write(baseURL) ;
    for(int i = 0; i < params.length; i++) {
      w.write("&amp;");w.write(params[i].getName());w.write('=');w.write(params[i].getValue()) ;
    }
    w.write("'");
    if(tooltip == null || tooltip.length() == 0){
      w.write(">");
    }else {
      w.write(" alt='");w.write(tooltip);w.write("'");
      w.write(" title='");w.write(tooltip);w.write("'>");      
    }
    w.write(text);
    w.write("</a>") ;
  }
  
  public  String convertClientId(FacesContext context, String clientId) {          
    return clientId;
  }
}