package org.exoplatform.portal.portlet.struts;

import java.util.HashMap;
import org.exoplatform.services.portletcontainer.helper.URLEncoder;


public class StrutsURLEncoder implements URLEncoder {
  private static String STATIC_RESOURCE = "static";
  private static HashMap staticResource_;

  static {
    staticResource_ = new HashMap();
    staticResource_.put("gif", STATIC_RESOURCE);
    staticResource_.put("jpg", STATIC_RESOURCE);
    staticResource_.put("png", STATIC_RESOURCE);
    staticResource_.put("bmp", STATIC_RESOURCE);
    staticResource_.put("txt", STATIC_RESOURCE);
    staticResource_.put("html", STATIC_RESOURCE);
    staticResource_.put("xml", STATIC_RESOURCE);
    staticResource_.put("xsl", STATIC_RESOURCE);
  }

  private String contextPath_;
  private String portalRequestUrl_;

  public StrutsURLEncoder(String contextPath, String portalUrl) {
    contextPath_ = contextPath;
    portalRequestUrl_ = portalUrl;
  }

  public String encodeURL(String url) {
    if (url.startsWith("http://")) return url;
        
    int ml_index = url.indexOf("/", 1);
    String ml_ext = url.substring(ml_index);
    url = contextPath_ + ml_ext;

    int index = url.lastIndexOf(".");
    String ext = null;
    if (index > 0) {
      ext = url.substring(index + 1, url.length());
    }
    if (isStaticResource(ext)) {
      return url;
    } else {
      url = url.replace('?', '&');
      return portalRequestUrl_ + "&requestUri=" + url;
    }


  }

  private boolean isStaticResource(String ext) {
    if (ext == null) return false;
    Object o = staticResource_.get(ext);
    return o != null;
  }
}
