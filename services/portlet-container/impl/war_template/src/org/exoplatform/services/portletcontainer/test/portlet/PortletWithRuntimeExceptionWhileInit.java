/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by The eXo Platform SARL        .
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: Oct 8, 2003
 * Time: 8:09:40 PM
 */
package org.exoplatform.services.portletcontainer.test.portlet;

import javax.portlet.*;
import java.io.IOException;

public class PortletWithRuntimeExceptionWhileInit implements Portlet{

  public void init(PortletConfig portletConfig) throws PortletException {
    throw new RuntimeException();
  }

  public void processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException, IOException {
  }

  public void render(RenderRequest renderRequest, RenderResponse renderResponse) throws PortletException, IOException {
  }

  public void destroy() {
    //System.out.println("IF YOU SEE THEN THERE IS A BUG");
  }
}