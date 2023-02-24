/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.wsrp.component.model;

import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.type.PortletDescription;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 6 juin 2004
 */
public class ProducerData {

	private boolean select_ ;
  private Producer producer;


  public ProducerData(Producer producer) {
    select_ = false ;
    this.producer = producer;
  }

  public String getProducerName() { return producer.getName() ; }

  public boolean isSelect() {return select_ ;}
  public void    setSelect(boolean b) { select_ = b ; }

  public Producer getProducer() {
    return producer ;
  }

  public PortletDescription getOfferedPortlet(String portletName) {
    PortletDescription[] array = getOfferedPortlets();
    if(array == null)
      return null;
    for (int i = 0; i < array.length; i++) {
      PortletDescription portletDescription = array[i];
      if(portletDescription.getPortletHandle().equals(portletName))
        return portletDescription;
    }
    return null;
  }

  public PortletDescription[] getOfferedPortlets() {
    try {
      return  producer.getServiceDescription().getOfferedPortlets() ;
    } catch (WSRPException e) {
      e.printStackTrace();
      return null;
    }
  }

}
