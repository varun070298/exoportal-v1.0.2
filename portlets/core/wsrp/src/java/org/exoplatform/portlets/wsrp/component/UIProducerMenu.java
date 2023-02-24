/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.wsrp.component;


import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.UINode;
import org.exoplatform.portlets.wsrp.component.model.ProducerData;
import org.exoplatform.services.wsrp.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp.type.PortletDescription;
import java.util.*;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 6 juin 2004
 */
public class UIProducerMenu extends UINode{
  private ConsumerEnvironment consumerEnvironment_;
  private HashMap producers;
  private long lastAccessTime_;

  public UIProducerMenu(ConsumerEnvironment  consumerEnvironment) throws Exception {
    setRendererType("ProducerMenuRenderer") ;
    consumerEnvironment_ = consumerEnvironment ;
    producers = new HashMap() ;
    init();
  }

  public String getFamily() {
    return "org.exoplatform.portlets.wsrp.component.UIProducerMenu" ;
  }

  private void init() throws Exception {
    producers.clear();
    ProducerRegistry pregistry = consumerEnvironment_.getProducerRegistry() ;
    Iterator i = pregistry.getAllProducers() ;
    while (i.hasNext()) {
      Producer producer = (Producer) i.next() ;
      ProducerData producerData = new ProducerData(producer);
      producers.put(producer.getName(), producerData);
    }
    lastAccessTime_ = consumerEnvironment_.getProducerRegistry().getLastModifiedTime() ;
  }

  public Collection getProducers() {
    if(lastAccessTime_ !=  consumerEnvironment_.getProducerRegistry().getLastModifiedTime()){
      try {
        init();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return producers.values() ;
  }

  public void decode(FacesContext context) {
    Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    String action = (String) paramMap.get(FacesConstants.ACTION) ;
    if("selectProducer".equals(action)) {
    	String producerName = (String) paramMap.get("producerName") ;
    	ProducerData producerData = (ProducerData) producers.get(producerName) ;
    	producerData.setSelect(!producerData.isSelect()) ;
    	context.renderResponse() ;
    } else if("showProducer".equals(action)) {
    	String producerName = (String) paramMap.get("producerName") ;
    	ProducerData producerData = (ProducerData) producers.get(producerName) ;
    	producerData.setSelect(true) ;
    	UIProducerNode uiParent = (UIProducerNode) getParent() ;
    	UIProducerInfo uiProducerInfo = (UIProducerInfo) uiParent.getChildComponentOfType(UIProducerInfo.class) ;
    	uiProducerInfo.setProducerData(producerData) ;
    	uiProducerInfo.setRendered(true);
      ((UIComponent)uiParent.getChildComponentOfType(UIOfferedPortlet.class)).setRendered(false); ;
    	context.renderResponse() ;
    } else if("showPortlet".equals(action)) {
    	String producerName = (String) paramMap.get("producerName") ;
      String portletName = (String) paramMap.get("portletName") ;
    	ProducerData producerData = (ProducerData) producers.get(producerName) ;
      PortletDescription portletDescription = producerData.getOfferedPortlet(portletName) ;
      UIProducerNode uiParent = (UIProducerNode) getParent() ;
      UIOfferedPortlet uiOfferedPortlet = (UIOfferedPortlet) uiParent.
          getChildComponentOfType(UIOfferedPortlet.class) ;
      uiOfferedPortlet.populate(portletDescription) ;
      uiOfferedPortlet.setRendered(true);
      ((UIComponent)uiParent.getChildComponentOfType(UIProducerInfo.class)).setRendered(false); ;
      context.renderResponse() ;
    }
  }
}
