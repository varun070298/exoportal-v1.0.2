/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Jul 28, 2003
 * Time: 10:18:25 PM
 */
package org.exoplatform.services.portletcontainer.impl.portletAPIImp;


import java.util.List;

import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.exoplatform.services.portletcontainer.pci.Output;


public class PortletResponseImp extends HttpServletResponseWrapper
    implements PortletResponse {

  protected List customWindowStates;
  private Output output;  

  public PortletResponseImp(HttpServletResponse httpServletResponse) {
    super(httpServletResponse);
  }

  public void fillPortletResponse(HttpServletResponse response,
                                  Output output,
                                  List customWindowStates) {
    super.setResponse(response);
    this.output = output;
    this.customWindowStates = customWindowStates;
  }

  public void emptyPortletResponse() {
  }

  public Output getOutput() {
    return output;
  }

  public void addProperty(String s, String s1) {
    output.addProperty(s, s1);
  }

  public void setProperty(String s, String s1) {
    output.addProperty(s, s1);
  }

  public String encodeURL(String path) {
    if (!path.startsWith("/") && !path.startsWith("http://") && !path.startsWith("https://") ) {
      throw new IllegalArgumentException("Path must be started with / or http:// or https://") ; 
    }
    return path;
  }
  
}
