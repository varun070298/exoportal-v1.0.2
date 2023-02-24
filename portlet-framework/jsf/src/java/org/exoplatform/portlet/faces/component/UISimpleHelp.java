/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.faces.component;

import java.io.IOException  ;
import java.io.InputStream ;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.portlet.PortletConfig ;

import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.portlet.faces.context.ExternalContextImpl ;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UISimpleHelp.java,v 1.4 2004/07/06 13:38:18 oranheim Exp $
 */
public class UISimpleHelp extends UIExoComponentBase {
  public static final String COMPONENT_TYPE = "UISimpleHelp";
  public static final String RENDERER_TYPE = "NoRenderer";
  
  private String content_ ;
  public UISimpleHelp() {
    try {
      ExternalContextImpl eContext = 
        (ExternalContextImpl) FacesContext.getCurrentInstance().getExternalContext() ; 
      PortletConfig config = eContext.getConfig() ;
      String path = config.getInitParameter("portlet-help-file") ;
      if (path != null) {
        InputStream is = eContext.getResourceAsStream(path) ;
        if (is != null) {
	        byte[] buf = new byte[is.available()] ;
	        is.read(buf) ;
	        content_ = new String(buf) ;
        } else {
            content_ = "The help file is not available" ;
        }
      } else {
        //should add default help file
        content_ = "The help file is not available" ;
      }
    } catch (Exception ex) {
      content_ = ex.getMessage() ;
      ex.printStackTrace() ;
    }
  }

  public String getComponentType() { return COMPONENT_TYPE; }

  public String getRendererType() { return RENDERER_TYPE; }


  public void decode(FacesContext context) {
  }

  final public void encodeBegin(FacesContext context) throws IOException {
    ResponseWriter w = context.getResponseWriter() ;
    w.write(content_);
  }

  final public void encodeChildren(FacesContext context) throws IOException {
  }

  final public void encodeEnd(FacesContext context) throws IOException {
  }
}
