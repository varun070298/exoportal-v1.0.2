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
import org.exoplatform.services.wsrp.WSRPConstants;
import org.exoplatform.services.wsrp.consumer.*;
/*
 * A part of the business logic of this portlet was taken from the WSRP4J project
 * @author  Tuan Nguyne
 *          tuan08@users.sourceforge.net
 * Tue, Feb 24, 2004 @ 14:35
 */
public class UIProducers {
  private Log log_ ;
  private List uiProducers_ ;
  
  public UIProducers(ProducerRegistry pregistry, Log log) throws Exception {
    log_ = log ;
    Iterator i = pregistry.getAllProducers() ;
    uiProducers_ = new ArrayList() ;
    boolean visible = true ;
    while (i.hasNext()) {
      Producer producer = (Producer) i.next() ;
      UIProducer uiProducer = new UIProducer(producer, log) ;
      uiProducers_.add(uiProducer) ;
      uiProducer.setVisible(visible) ;
      visible = false ;
    }
  }

  public void render(RenderRequest request, RenderResponse response, ResourceBundle res) throws Exception {
    for (int i  = 0 ; i < uiProducers_.size(); i++) {
      UIProducer uiProducer = (UIProducer) uiProducers_.get(i) ;
      uiProducer.render(request, response, res) ;
    }
  }

  public void processAction(ActionRequest request, ActionResponse response) throws Exception {
    String action = request.getParameter("action") ;
    String producerId = request.getParameter("producerId") ;
    if ("selectProducer".equals(action)) {
      for (int i  = 0 ; i < uiProducers_.size(); i++) {
        UIProducer uiProducer = (UIProducer) uiProducers_.get(i) ;
        if(uiProducer.getId().equals(producerId)) {
          uiProducer.setVisible(!uiProducer.isVisible()) ;
        }
      }
    }

    if ("selectPortlet".equals(action)) {
      String portletHandle = request.getParameter("portletHandle") ;
      PortletPreferences prefs = request.getPreferences() ;
      prefs.setValue(WSRPConstants.WSRP_PRODUCER_ID , producerId) ;
      prefs.setValue(WSRPConstants.WSRP_PORTLET_HANDLE , portletHandle) ;
      prefs.setValue(WSRPConstants.WSRP_PARENT_HANDLE , portletHandle) ;
      prefs.store() ;
    }
  }
}
