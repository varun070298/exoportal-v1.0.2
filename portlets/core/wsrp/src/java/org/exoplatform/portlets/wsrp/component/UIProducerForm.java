/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.wsrp.component;

import java.util.*;
import javax.faces.context.FacesContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.Node;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.UITextArea;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.portlet.faces.context.ExternalContextImpl;
import org.exoplatform.services.portletcontainer.PortletContainerService;
import org.exoplatform.services.wsrp.consumer.ConsumerEnvironment;
import org.exoplatform.services.wsrp.consumer.Producer;
import org.exoplatform.services.wsrp.consumer.ProducerRegistry;
import org.exoplatform.services.wsrp.type.RegistrationData;
/**
 * Sun, Feb 22, 2004 @ 16:07 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIProducerForm.java,v 1.11 2004/08/17 13:07:02 tuan08 Exp $
 */
public class UIProducerForm extends UISimpleForm implements Node {
  protected static Log log_ = getLog("org.exoplatform.portlets.wsrp") ;
  public static final String COMPONENT_TYPE = "UIProducerForm";
  public static final String VIEW_ID = "producer-form";
  
  private String tabTitle_ ;
  private UIStringInput producerName_ ;
  private UIStringInput producerURL_ ;
  private UIStringInput markupIntfEndPoint_ ;
  private UIStringInput pmIntfEndPoint_ ;
  private UIStringInput registrationIntfEndPoint_ ;
  private UIStringInput serviceDescIntfEndPoint_ ;
  private UITextArea    description_ ;
  private String[] consumerModes_ ; 
  private String[] consumerStates_ ; 
  private String   consumerName_ ; 
  private String   consumerAgent_ ; 

  private String registerProducerFailMesg_ ;
  private String registerProducerSucessMesg_ ;
  private ConsumerEnvironment consumerEnvironment_ ; 

  public UIProducerForm(ResourceBundle res, ConsumerEnvironment consumerEnvironment) {
    super("producerForm", "post", null) ;
    setId(VIEW_ID);
    tabTitle_ = "Create Producer" ;
    consumerEnvironment_ = consumerEnvironment ;

    registerProducerFailMesg_  = res.getString("UIProducerForm.msg.register-producer-fail");
    registerProducerSucessMesg_ = res.getString("UIProducerForm.msg.register-producer-success");

    PortletContainerService portletContainer =
      (PortletContainerService) PortalContainer.getInstance().
                                getComponentInstanceOfType(PortletContainerService.class);
    Collection modes  = portletContainer.getSupportedPortletModes();
    Collection states = portletContainer.getSupportedWindowStates();
    consumerModes_ = new String[modes.size()] ;
    Iterator iterator= modes.iterator() ;
    int j = 0 ;
    while (iterator.hasNext()) {
      PortletMode mode = (PortletMode) iterator.next() ;
      consumerModes_[j] = "wsrp:" + mode.toString() ;
      j++ ;
    }

    consumerStates_ = new String[states.size()] ;
    iterator= states.iterator() ;
    j= 0 ;
    while (iterator.hasNext()) {
      WindowState state = (WindowState) iterator.next() ;
      consumerStates_[j] = "wsrp:" + state.toString() ;
      j++ ;
    }
    ExternalContextImpl eContext =
      (ExternalContextImpl) FacesContext.getCurrentInstance().getExternalContext() ;
    PortletConfig config = eContext.getConfig() ;
    consumerName_ = config.getInitParameter("consumer-name") ;
    consumerAgent_ = config.getInitParameter("consumer-agent") ;

    String saveButton =  "#{UIProducerForm.button.save}" ;
    String resetButton =  "#{UIProducerForm.button.reset}" ;
    producerName_ = new UIStringInput("producerName", "");
    producerURL_ = new UIStringInput("producerURL", "");
    markupIntfEndPoint_ = new UIStringInput("markupIntfEndPoint", "") ;
    pmIntfEndPoint_ = new UIStringInput("pmIntfEndPoint", "") ;
    registrationIntfEndPoint_ = new UIStringInput("registrationIntfEndPoint", "");
    serviceDescIntfEndPoint_ = new UIStringInput("serviceDescIntfEndPoint", "") ;
    description_ = new UITextArea("description", "");    
    
    add(new HeaderRow().
        add(new Cell("#{UIProducerForm.header.new-producer}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIProducerForm.label.producer-name}")).
        add(new ComponentCell(this, producerName_)));
    add(new Row().
        add(new LabelCell("#{UIProducerForm.label.producer-url}")).
        add(new ComponentCell(this, producerURL_)));
    add(new Row().
        add(new LabelCell("#{UIProducerForm.label.markup-interface-end-point}")).
        add(new ComponentCell(this, markupIntfEndPoint_ )));
    add(new Row().
        add(new LabelCell("#{UIProducerForm.label.portlet-management-interface-end-point}")).
        add(new ComponentCell(this, pmIntfEndPoint_ )));
    add(new Row().
        add(new LabelCell("#{UIProducerForm.label.registration-interface-end-point}")).
        add(new ComponentCell(this, pmIntfEndPoint_ )));
    add(new Row().
        add(new LabelCell("#{UIProducerForm.label.service-description-end-point}")).
        add(new ComponentCell(this, serviceDescIntfEndPoint_ )));
    add(new Row().
        add(new LabelCell("#{UIProducerForm.label.description}")).
        add(new ComponentCell(this, description_ )));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton(saveButton, SAVE_ACTION)).
            add(new FormButton(resetButton, "reset")).            
            addColspan("2").addAlign("center"))) ;
    reset() ;
  }
  
  private void reset()  {
    producerName_.setValue("exo producer") ;
    producerURL_.setValue("http://localhost:8080/wsrp/services/") ; 
    markupIntfEndPoint_.setValue("WSRPBaseService") ; ;
    pmIntfEndPoint_.setValue("WSRPPortletManagementService") ; 
    registrationIntfEndPoint_.setValue("WSRPRegistrationService") ;
    serviceDescIntfEndPoint_.setValue("WSRPServiceDescriptionService") ; 
    description_.setValue("") ; 
  }

  public String getComponentType() { return COMPONENT_TYPE; }
  
  public void decode(FacesContext context) {
    Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    String action = (String) paramMap.get(FacesConstants.ACTION) ;
    if(CANCEL_ACTION.equals(action)) {
      return ;
    }

    if("reset".equals(action)) {
      reset() ;
      context.renderResponse() ;
      return ;
    }

    if(SAVE_ACTION.equals(action)) {
      if(hasError()) {
      } else {
        try {
          String pURL = producerURL_.getValue() ;
          String producerId = "producer" + Integer.toString(pURL.hashCode()) ;
          ProducerRegistry pregistry = consumerEnvironment_.getProducerRegistry() ;
          Producer producer = pregistry.createProducerInstance() ;
          producer.setID(producerId);
          producer.setName(producerName_.getValue());
          producer.setMarkupInterfaceEndpoint(pURL +markupIntfEndPoint_.getValue());
          producer.setPortletManagementInterfaceEndpoint(pURL + pmIntfEndPoint_.getValue());
          producer.setRegistrationInterfaceEndpoint(pURL + registrationIntfEndPoint_.getValue());
          producer.setServiceDescriptionInterfaceEndpoint(pURL + serviceDescIntfEndPoint_.getValue());
          producer.setDescription(description_.getValue());
          producer.setDesiredLocales(new String[] {"en"});
          if (producer.isRegistrationRequired()) {
            String[] CONSUMER_SCOPES = {"chunk_data"};
            String[] CONSUMER_CUSTOM_PROFILES = {"what_more"};
            RegistrationData registrationData =  new RegistrationData();
            registrationData.setConsumerName(consumerName_);
            registrationData.setConsumerAgent(consumerAgent_);
            registrationData.setMethodGetSupported(false);
            registrationData.setConsumerModes(consumerModes_);
            registrationData.setConsumerWindowStates(consumerStates_);
            registrationData.setConsumerUserScopes(CONSUMER_SCOPES);
            registrationData.setCustomUserProfileData(CONSUMER_CUSTOM_PROFILES);
            producer.register(registrationData);
          }
          pregistry.addProducer(producer) ;
        } catch (Exception ex) {
          log_.error("Error: ", ex) ;
        }
      }
    }
  }

  public String getName() {
    return tabTitle_;
  }

  public String getIcon() {
    return "no-icon" ;
  }

  public String getDescription() {
    return "no description";
  }


}