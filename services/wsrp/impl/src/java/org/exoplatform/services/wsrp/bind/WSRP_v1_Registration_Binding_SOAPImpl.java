/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 15 janv. 2004
 */
package org.exoplatform.services.wsrp.bind;

import java.rmi.RemoteException;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.wsrp.intf.WSRP_v1_Registration_PortType;
import org.exoplatform.services.wsrp.producer.RegistrationOperationsInterface;
import org.exoplatform.services.wsrp.type.*;



/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRP_v1_Registration_Binding_SOAPImpl implements WSRP_v1_Registration_PortType{

  private RegistrationOperationsInterface registrationOperationsInterface;

  public WSRP_v1_Registration_Binding_SOAPImpl() {
    PortalContainer manager = PortalContainer.getInstance();
    registrationOperationsInterface = (RegistrationOperationsInterface)manager.
        getComponentInstanceOfType(RegistrationOperationsInterface.class);
  }

  public RegistrationContext register(RegistrationData data) throws RemoteException, OperationFailedFault, MissingParametersFault {
    return registrationOperationsInterface.register(data);
  }

  public ReturnAny deregister(RegistrationContext context) throws RemoteException, InvalidRegistrationFault, OperationFailedFault {
    return registrationOperationsInterface.deregister(context);    
  }

  public RegistrationState modifyRegistration(ModifyRegistrationRequest modifyRegistration) throws RemoteException, InvalidRegistrationFault, OperationFailedFault, MissingParametersFault {
    return registrationOperationsInterface.modifyRegistration(modifyRegistration.getRegistrationContext(),
                                                              modifyRegistration.getRegistrationData());
  }

}
