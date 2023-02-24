/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.display.component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.SessionContainer;
import org.exoplatform.portal.session.PortalResources;
import org.exoplatform.portlets.content.display.component.model.ContentConfig;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.portletcontainer.ExoPortletContext;
import org.exoplatform.services.portletcontainer.ExoPortletRequest;

/**
 * @author benjaminmestrallet
 */
public class UIStaticContentTab extends UIContentTab {

  private ExoCache cache_;
  
  public UIStaticContentTab(ContentConfig config) throws Exception {
    super(config);
    CacheService cacheS = (CacheService) PortalContainer.getComponent(CacheService.class);
    cache_ = cacheS.getCacheInstance(getClass().getName());
  }

  public void encodeChildren(FacesContext context) throws IOException {
    ResponseWriter w = context.getResponseWriter();
    w.write("<div class='UIContentTab'>");
    PortalResources appres = (PortalResources) SessionContainer
        .getComponent(PortalResources.class);
    Locale locale = appres.getLocale();
    String uri = config_.getUri();
    String content = null;
    if (uri == null) {
      content = "";
    } else if (uri.endsWith(".html")) {
      try {
        content = resolveContent(uri);
      } catch (Exception e) {             
      }
    } else {
      String tmpUri = uri + "_" + locale.getLanguage() + ".html";
      try {
        content = resolveContent(tmpUri);
      } catch (Exception ex) {
        tmpUri = uri + "_en.html";
        try {
          content = resolveContent(tmpUri);
        } catch (Exception ex2) {          
        }
      }
    }
    if (content == null) {
      w.write("No configuration for the content");
    } else {
      w.write(content);
    }
    w.write("</div>");
  }

  private String resolveContent(String uri) throws Exception {
    String content = (String) cache_.get(uri);
    if(content != null)
      return  content;
    if (uri.startsWith("war:")) {
      String path = uri.substring(4, uri.length());
      String[] pathElements = StringUtils.split(path, "/");
      InputStream is = null;
      ExternalContext eContext = FacesContext.getCurrentInstance()
          .getExternalContext();
      if ("content".equals(pathElements[0])) {
        path = path.substring(2 + pathElements[0].length());
        is = eContext.getResourceAsStream(path);
      } else {
        ServletContext context = ((ExoPortletContext) (((ExoPortletRequest) eContext
            .getRequest()).getPortletSession().getPortletContext()))
            .getWrappedServletContext();
        context = context.getContext("/" + pathElements[0]);
        path = path.substring(2 + pathElements[0].length());
        is = context.getResourceAsStream(path);
      }
      if(is == null)
        throw new IOException();
      byte[] buf = new byte[is.available()];
      is.read(buf);
      content = new String(buf, config_.getEncoding());
    } else if (uri.startsWith("file://")) {
      String path = uri.substring(7, uri.length());
      FileInputStream is = new FileInputStream(path);
      if(is == null)
        throw new IOException();
      byte[] buf = new byte[is.available()];
      is.read(buf);
      content = new String(buf, config_.getEncoding());
    }
    cache_.put(uri, content);
    return content;
  }

}