package org.exoplatform.portlet.faces.taglib;

import java.io.IOException;
import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.*;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentBodyTag;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.Config;
import javax.portlet.PortletSession ;
import javax.portlet.RenderResponse ;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogUtil;

import com.sun.faces.util.Util;


public class ExoViewTag extends UIComponentBodyTag {
	private static Log log_ = LogUtil.getLog("org.exoplatform.portlet.faces.faces");
	
  protected String locale;

  public void setLocale(String newLocale) { locale = newLocale; }

  public ExoViewTag() { locale = null; }

  protected UIComponent findComponent(FacesContext context) throws JspException {
    UIComponent comp = super.findComponent(context) ;
    return comp ;
  }
  
  protected int getDoStartValue() throws JspException {
    return 2;
  }

  public int doStartTag() throws JspException {
    int rc = 0;
    try {
      rc = super.doStartTag();
    } catch(JspException e) {
      throw e;
    } catch(Throwable t) {
      throw new JspException(t);
    }
    return rc;
  }

  public void doInitBody() throws javax.servlet.jsp.JspException {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ResponseWriter facesWriter = facesContext.getResponseWriter();
    facesWriter = facesWriter.cloneWithWriter(getBodyContent()) ;
    facesContext.setResponseWriter(facesWriter) ;
    super.doInitBody() ;
  }
  
  public int doAfterBody() throws JspException {
    String content = null;
    try {
      if(null == (bodyContent = getBodyContent())) {
        Object params[] = { getClass().getName() };
        throw new JspException("Body content is nulll" + params[0].toString());
      }
      content = bodyContent.getString();
      getPreviousOut().write(content);
    } catch(IOException iox) {
      Object params[] = { "session", iox.getMessage() };
      throw new JspException("SAVING STATE ERROR" + params[0].toString());
    }
    return 6;
  }

  public int doEndTag() throws JspException {
    int rc = super.doEndTag();
    FacesContext context = FacesContext.getCurrentInstance();
    /*
    ResponseWriter writer = context.getResponseWriter();
    try {
      writer.endDocument();
    } catch(IOException e) {
      throw new JspException(e.getMessage());
    }
    */
    PortletSession session = null;
    if(null != (session = (PortletSession)context.getExternalContext().getSession(false))) {
      RenderResponse response = (RenderResponse)context.getExternalContext().getResponse();
      session.setAttribute("javax.faces.request.charset", response.getCharacterEncoding());
    }
    return rc;
  }

  public String getComponentType() {
    throw new IllegalStateException();
  }

  public String getRendererType() {
    return null;
  }

  protected int getDoEndValue() throws JspException {
    return 6;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    Locale viewLocale = null;
    ValueBinding vb = null;
    if(null != locale) {
      if(UIComponentTag.isValueReference(locale)) {
        component.setValueBinding("locale", vb = Util.getValueBinding(locale));
        Object resultLocale = vb.getValue(FacesContext.getCurrentInstance());
        if(resultLocale instanceof Locale)
          viewLocale = (Locale)resultLocale;
        else
          if(resultLocale instanceof String)
            viewLocale = getLocaleFromString((String)resultLocale);
      } else {
        viewLocale = getLocaleFromString(locale);
      }
      ((UIViewRoot)component).setLocale(viewLocale);
      Config.set((ServletRequest)getFacesContext().getExternalContext().getRequest(), "javax.servlet.jsp.jstl.fmt.locale", viewLocale);
    }
  }

  protected Locale getLocaleFromString(String localeExpr) {
    Locale result = Locale.getDefault();
    if(localeExpr.indexOf("_") == -1 && localeExpr.indexOf("-") == -1) {
      if(localeExpr.length() == 2)
        result = new Locale(localeExpr, "");
    } else
      if(localeExpr.length() == 5) {
        String language = localeExpr.substring(0, 2);
        String country = localeExpr.substring(3, localeExpr.length());
        result = new Locale(language, country);
      }
    return result;
  }
}
