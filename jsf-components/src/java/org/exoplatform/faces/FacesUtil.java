/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.servlet.ServletContext;
/**
 * Fri, May 30, 2003 @
 * @author: Tuan Nguyen
 * @author: Ove Ranheim
 * @version: $Id: FacesUtil.java,v 1.6 2004/11/03 04:24:51 tuan08 Exp $
 * @email: tuan08@yahoo.com
 * @email: oranheim@users.sourceforge.net
 */
public class FacesUtil {

  static public PortletPreferences getPortletPreferences() {
    PortletRequest request =
      (PortletRequest) FacesContext.getCurrentInstance().
			                 getExternalContext().getRequest() ;
  	return request.getPreferences() ;
  }
  
  static public String  getApplicationRealPath(String s) {
    FacesContext context  = FacesContext.getCurrentInstance() ;
		ExternalContext econtext = context.getExternalContext() ;
		ServletContext scontext = (ServletContext) econtext.getContext() ;
  	return scontext.getRealPath(s) ;
  }
  
  static public ServletContext getServletContext() {
    FacesContext context  = FacesContext.getCurrentInstance() ;
    ExternalContext econtext = context.getExternalContext() ;
    return (ServletContext) econtext.getContext()  ;
  }
  
  // Simple is el expr test
  public static boolean isValueReference(String elexpr) {
		return (elexpr != null && elexpr.startsWith("#{") && elexpr.endsWith("}"));
	}

	// Create valuebinding by UI-field/property for given el-expr
	public static void createValueBinding(FacesContext context,
																				UIComponent uiComp, String key,
																				String elexpr) {
		if (isValueReference(elexpr)) {
			Application app = context.getApplication();
			if (uiComp.getValueBinding(key) == null) {
				uiComp.setValueBinding(key, app.createValueBinding(elexpr));
			}
		}
	}
  
  // Update the value for a UI-field/property
  public static boolean updateBoundValueBinding(FacesContext context,
																								UIComponent uiComp, String key,
																								Object value) {
		boolean b = false;
		ValueBinding vb = uiComp.getValueBinding(key);
		//Map paramMap = context.getExternalContext().getRequestParameterMap();
		if (vb != null) {
			vb.setValue(context, value);
			b = true;
		}
		return b;
	}
  
  // Resolve the placehold el-exprssed value
	public static Object resolveBoundValueBinding(FacesContext context,
																								UIComponent uiComp, String key) {
		Object value = null;
		ValueBinding vb = uiComp.getValueBinding(key); // "text"
		if (vb != null)
			value = vb.getValue(context);
		return value;
	}  
}