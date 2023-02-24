/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.portlets.wsrp.component;

import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.portlets.wsrp.component.model.ProducerData;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 7 juin 2004
 */
public class UIProducerInfo extends UIExoComponentBase {

	private ProducerData producerData ;

  public UIProducerInfo() {
    setRendererType("ProducerInfoRenderer");
	}

  public String getFamily() {
    return "org.exoplatform.portlets.wsrp.component.UIProducerInfo";
  }

  public ProducerData getProducerData() {
    if(producerData == null)
      return (ProducerData) ((UIProducerMenu)((UIExoComponentBase)getParent()).
          getChildComponentOfType(UIProducerMenu.class)).getProducers().iterator().next();
    return producerData;
  }

  public void setProducerData(ProducerData producerData) {
    this.producerData = producerData;
  }

}
