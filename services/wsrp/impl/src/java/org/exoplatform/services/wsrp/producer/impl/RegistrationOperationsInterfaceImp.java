/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 14 janv. 2004
 */
package org.exoplatform.services.wsrp.producer.impl;

import java.rmi.RemoteException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.exoplatform.commons.utils.IdentifierUtil;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.wsrp.exceptions.Exception2Fault;
import org.exoplatform.services.wsrp.exceptions.Faults;
import org.exoplatform.services.wsrp.exceptions.WSRPException;
import org.exoplatform.services.wsrp.producer.PersistentStateManager;
import org.exoplatform.services.wsrp.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp.type.RegistrationContext;
import org.exoplatform.services.wsrp.type.RegistrationData;
import org.exoplatform.services.wsrp.type.RegistrationState;
import org.exoplatform.services.wsrp.type.ReturnAny;


/**
 * @author Mestrallet Benjamin
 *         benjmestrallet@users.sourceforge.net
 */
public class RegistrationOperationsInterfaceImp
    implements RegistrationOperationsInterface {

  private Log log;

  private PersistentStateManager stateManager;

  public RegistrationOperationsInterfaceImp(PersistentStateManager stateManager,
                                            LogService logService) {
    this.stateManager = stateManager;
    this.log = logService.getLog("org.exoplatform.services.wsrp");
  }

  public RegistrationContext register(RegistrationData data)
      throws RemoteException {
    log.debug("Register method entered");
    String registrationHandle = null;
    byte[] registrationState = null;
    try {
      validateRegistrationDatas(data);
      registrationHandle = IdentifierUtil.generateUUID(data);
      registrationState = stateManager.register(registrationHandle, data);//may be null
    } catch (WSRPException e) {
      log.debug("Registration failed", e);
      Exception2Fault.handleException(e);
    }
    RegistrationContext rC = new RegistrationContext();
    rC.setRegistrationHandle(registrationHandle);
    rC.setRegistrationState(registrationState);
    log.debug("Registration done with handle : " + registrationHandle);
    return rC;
  }

  public RegistrationState modifyRegistration(RegistrationContext registrationContext,
                                              RegistrationData data)
      throws RemoteException {
    log.debug("Modify registrion method entered");
    try {
      if (!stateManager.isRegistered(registrationContext)) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }

    String registrationHandle = registrationContext.getRegistrationHandle();
    try {
      validateRegistrationDatas(data);
      stateManager.register(registrationHandle, data);
    } catch (WSRPException e) {
      log.debug("Registration failed", e);
      Exception2Fault.handleException(e);
    }

    return new RegistrationState();//the state is kept in the producer (not send to the consumer)
  }

  public ReturnAny deregister(RegistrationContext registrationContext)
      throws RemoteException {
    log.debug("Deregister method entered");
    try {
      if (!stateManager.isRegistered(registrationContext)) {
        Exception2Fault.handleException(new WSRPException(Faults.INVALID_REGISTRATION_FAULT));
      }
    } catch (WSRPException e) {
      Exception2Fault.handleException(e);
    }
    try {
      stateManager.deregister(registrationContext);
    } catch (WSRPException e) {
      log.debug("Registration failed", e);
      Exception2Fault.handleException(e);
    }
    return new ReturnAny();
  }

  private void validateRegistrationDatas(RegistrationData data)
      throws WSRPException {
    String consumerAgent = data.getConsumerAgent();
    String[] members = StringUtils.split(consumerAgent, ".");
    if (!StringUtils.isNumeric(members[1])) {
      throw new WSRPException(Faults.MISSING_PARAMETERS_FAULT);
    }
    if (!StringUtils.isNumeric(members[2])) {
      throw new WSRPException(Faults.MISSING_PARAMETERS_FAULT);
    }
  }

}
