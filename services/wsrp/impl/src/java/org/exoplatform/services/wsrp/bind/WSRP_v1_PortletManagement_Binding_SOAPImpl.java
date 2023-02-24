/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 *  
 * Created on 15 janv. 2004
 */
package org.exoplatform.services.wsrp.bind;

import java.rmi.RemoteException;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.wsrp.intf.WSRP_v1_PortletManagement_PortType;
import org.exoplatform.services.wsrp.producer.PortletManagementOperationsInterface;
import org.exoplatform.services.wsrp.type.*;


/**
 * @author  Mestrallet Benjamin
 *          benjmestrallet@users.sourceforge.net
 */
public class WSRP_v1_PortletManagement_Binding_SOAPImpl 
    implements WSRP_v1_PortletManagement_PortType{

  private PortletManagementOperationsInterface portletManagementOperationsInterface;
  
  public WSRP_v1_PortletManagement_Binding_SOAPImpl() {
    PortalContainer manager = PortalContainer.getInstance();
    portletManagementOperationsInterface = (PortletManagementOperationsInterface)manager.
        getComponentInstanceOfType(PortletManagementOperationsInterface.class);
  }

  public PortletDescriptionResponse getPortletDescription(PortletDescriptionRequest getPortletDescription)
    throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, 
           InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, 
           AccessDeniedFault, InvalidHandleFault {
    return portletManagementOperationsInterface.getPortletDescription(getPortletDescription.getRegistrationContext(),
                                                                      getPortletDescription.getPortletContext(),
                                                                      getPortletDescription.getUserContext(),
                                                                      getPortletDescription.getDesiredLocales());
  }

  public PortletContext clonePortlet(ClonePortletRequest clonePortlet)
    throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, 
           InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, 
           AccessDeniedFault, InvalidHandleFault {
    return portletManagementOperationsInterface.clonePortlet(clonePortlet.getRegistrationContext(),
                                                             clonePortlet.getPortletContext(),
                                                             clonePortlet.getUserContext());
  }

  public DestroyPortletsResponse destroyPortlets(DestroyPortletsRequest destroyPortlets)
    throws RemoteException, InconsistentParametersFault, InvalidRegistrationFault, 
           OperationFailedFault, MissingParametersFault {
    return portletManagementOperationsInterface.destroyPortlets(destroyPortlets.getRegistrationContext(),
                                                                destroyPortlets.getPortletHandles());
  }

  public PortletContext setPortletProperties(SetPortletPropertiesRequest setPortletProperties)
    throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, 
           InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, 
           AccessDeniedFault, InvalidHandleFault {
    return portletManagementOperationsInterface.setPortletProperties(setPortletProperties.getRegistrationContext(),
                                                                     setPortletProperties.getPortletContext(),
                                                                     setPortletProperties.getUserContext(),
                                                                     setPortletProperties.getPropertyList());
  }

  public PropertyList getPortletProperties(GetPortletPropertiesRequest getPortletProperties)
    throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, 
           InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, 
           AccessDeniedFault, InvalidHandleFault {
    return portletManagementOperationsInterface.getPortletProperties(getPortletProperties.getRegistrationContext(),
                                                                     getPortletProperties.getPortletContext(),
                                                                     getPortletProperties.getUserContext(),
                                                                     getPortletProperties.getNames());
  }

  public PortletPropertyDescriptionResponse getPortletPropertyDescription(PortletPropertyDescriptionRequest getPortletPropertyDescription)
    throws RemoteException, InvalidUserCategoryFault, InconsistentParametersFault, 
           InvalidRegistrationFault, OperationFailedFault, MissingParametersFault, 
           AccessDeniedFault, InvalidHandleFault {
    return portletManagementOperationsInterface.getPortletPropertyDescription(getPortletPropertyDescription.getRegistrationContext(),
                                                                              getPortletPropertyDescription.getPortletContext(),
                                                                              getPortletPropertyDescription.getUserContext(),
                                                                              getPortletPropertyDescription.getDesiredLocales());
  }

}
