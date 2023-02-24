/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.portletregistery.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.Group;
import org.exoplatform.services.portletregistery.Portlet;
import org.exoplatform.services.portletregistery.PortletRegisteryService;
import org.exoplatform.services.portletregistery.PortletRole;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 19 juin 2004
 */
public class UIPortletRole extends UIExoCommand {
  private PortletRegisteryService portletRegisteryService;
  private OrganizationService orgServvice_;
  private Portlet portlet_ ;

  public static final String ADD_ROLE = "addRole";
  public static final String REMOVE_ROLE = "removeRole";

  public static final String AVAILABLE_ROLE_SELECT = "availableRolesSelect";
  public static final String CURRENT_ROLE_SELECT = "currentRoleSelect";

  private Collection currentRoles;
  private Collection availableRoles = new ArrayList();

  public UIPortletRole(OrganizationService organizationService,
                       PortletRegisteryService portletRegisteryService) throws Exception {
    setRendererType("PortletRoleRenderer");
    setId("UIPortletRole");
    this.orgServvice_ = organizationService;
    this.portletRegisteryService = portletRegisteryService;

    addFacesListener(new UpdateActionListener().setActionToListen(SAVE_ACTION));
    addFacesListener(new CancelActionListener().setActionToListen(CANCEL_ACTION));
    addFacesListener(new AddRoleActionListener().setActionToListen(ADD_ROLE));
    addFacesListener(new RemoveRoleActionListener().setActionToListen(REMOVE_ROLE));
  }
  
  public String getFamily() {
    return "org.exoplatform.portlets.portletregistery.component.UIPortletRole";
  }


  public void setCurrentPortlet(Portlet portlet) throws Exception {
    portlet_ = portlet ;
    Collection initialRoleList = orgServvice_.findGroups(null);
    currentRoles = new ArrayList();
    Collection temp = portletRegisteryService.getPortletRoles(portlet.getId());
    for (Iterator i = temp.iterator(); i.hasNext();) {
      PortletRole portletRole = (PortletRole) i.next();
      currentRoles.add(portletRole.getPortletRoleName());
    }
    availableRoles = new ArrayList();
    for (Iterator i = initialRoleList.iterator(); i.hasNext();) {
      Group availableRole = (Group) i.next();
      if(!currentRoles.contains(availableRole.getGroupName())){
        availableRoles.add(availableRole.getGroupName());
      }
    }
  }

  public Collection getAvailableRoles() {
    return availableRoles;
  }

  public Collection getCurrentRoles() {
    return currentRoles;
  }

  private class AddRoleActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      Map parameters = FacesContext.getCurrentInstance().getExternalContext().
          getRequestParameterValuesMap();
      String[] rolesSelected = (String[]) parameters.get(AVAILABLE_ROLE_SELECT);
      if(rolesSelected == null)
        return;
      for (int i = 0; i < rolesSelected.length; i++) {
        String roleToAdd = rolesSelected[i];
        currentRoles.add(roleToAdd);
        availableRoles.remove(roleToAdd);
      }
    }
  }

  private class RemoveRoleActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      Map parameters = FacesContext.getCurrentInstance().getExternalContext().
          getRequestParameterValuesMap();
      String[] rolesSelected = (String[]) parameters.get(CURRENT_ROLE_SELECT);
      if(rolesSelected == null) return;
      for (int i = 0; i < rolesSelected.length; i++) {
        String roleToRemove = rolesSelected[i];
        currentRoles.remove(roleToRemove);
        availableRoles.add(roleToRemove);
      }
    }
  }

  private class UpdateActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      portletRegisteryService.updatePortletRoles(portlet_.getId(), currentRoles);
      setRenderedSibling(UIPortletCategories.class);
    }
  }

  private class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
    	setRenderedSibling(UIPortletCategories.class);
    }
  }
}