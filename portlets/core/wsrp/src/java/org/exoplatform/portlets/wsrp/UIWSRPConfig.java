/*
* Copyright 2001-2004 The eXo platform SARL All rights reserved.
* Please look at license.txt in info directory for more license detail.
*/

package org.exoplatform.portlets.wsrp;

import java.util.*;
import java.io.IOException;
import java.io.Writer   ;
import javax.portlet.* ;
import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.exoplatform.services.wsrp.consumer.*;
/*
 * A part of the business logic of this portlet was taken from the WSRP4J project
 * @author  Tuan Nguyne
 *          tuan08@users.sourceforge.net
 * Tue, Feb 24, 2004 @ 14:35
 */
public class UIWSRPConfig {
  private Log log_ ;
  private UIProducers uiProducers_ ;
  
  public UIWSRPConfig(ConsumerEnvironment env, Log log) throws Exception {
    log_ = log ;
    ProducerRegistry pregistry = env.getProducerRegistry() ;
    uiProducers_ = new UIProducers(pregistry, log) ;
  }

  public void render(RenderRequest request, RenderResponse response, ResourceBundle res) throws Exception {
    uiProducers_.render(request, response, res) ;
  }

  public void processAction(ActionRequest request, ActionResponse response) throws Exception {
    uiProducers_.processAction(request, response) ;
  }
}
