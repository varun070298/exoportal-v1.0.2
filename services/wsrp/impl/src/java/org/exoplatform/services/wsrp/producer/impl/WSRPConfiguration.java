package org.exoplatform.services.wsrp.producer.impl;

import java.util.Collection;
import java.util.Iterator;
import org.exoplatform.services.portletcontainer.impl.PortletContainerConf;

import org.exoplatform.commons.utils.ExoProperties;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.PropertiesParam;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.container.configuration.ValuesParam;
/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/

/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 * Date: 23 janv. 2004
 * Time: 12:16:47
 */
public class WSRPConfiguration {

  private boolean hasUserSpecificState;
  private boolean doesUrlTemplateProcessing;
  private boolean templatesStoredInSession;
  private boolean userContextStoredInSession;
  private boolean usesMethodGet;
  private boolean requiresRegistration;
  private boolean blockingInteractionOptimized;
  private boolean saveRegistrationStateOnConsumer;
  private boolean savePortletStateOnConsumer;


  public WSRPConfiguration(ConfigurationManager cService) {
    try {           
      ServiceConfiguration sconf = cService.getServiceConfiguration(WSRPConfiguration.class) ;                                     
      PropertiesParam param = sconf.getPropertiesParam("wsrp-conf");      
      init(param.getProperties());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void init(ExoProperties props) {
  	hasUserSpecificState = 
  		props.getProperty("wsrp.has.user.specific.state").equals("true") ;
    doesUrlTemplateProcessing = 
    	props.getProperty("wsrp.does.url.template.processing").equals("true") ;
    templatesStoredInSession = 
      props.getProperty("wsrp.templates.stored.in.session").equals("true") ;
    userContextStoredInSession = 
      props.getProperty("wsrp.user.context.stored.in.session").equals("true") ;
    usesMethodGet =
      props.getProperty("wsrp.uses.method.get").equals("true") ;
    requiresRegistration = 
      props.getProperty("wsrp.requires.registration").equals("true") ;
    blockingInteractionOptimized = 
      props.getProperty("wsrp.perform.blocking.interaction.optimized").equals("true") ;
    saveRegistrationStateOnConsumer = 
      props.getProperty("wsrp.save.registration.state.on.consumer").equals("true") ;
    savePortletStateOnConsumer = 
      props.getProperty("wsrp.save.portlet.state.on.consumer").equals("true") ;
  }

  public boolean isHasUserSpecificState() {
    return hasUserSpecificState;
  }

  public boolean isDoesUrlTemplateProcessing() {
    return doesUrlTemplateProcessing;
  }

  public boolean isTemplatesStoredInSession() {
    return templatesStoredInSession;
  }

  public boolean isUserContextStoredInSession() {
    return userContextStoredInSession;
  }

  public boolean isUsesMethodGet() {
    return usesMethodGet;
  }

  public boolean isRegistrationRequired() {
    return requiresRegistration;
  }

  public boolean isBlockingInteractionOptimized() {
    return blockingInteractionOptimized;
  }

  public boolean isSaveRegistrationStateOnConsumer() {
    return saveRegistrationStateOnConsumer;
  }

  public boolean isSavePortletStateOnConsumer() {
    return savePortletStateOnConsumer;
  }
}