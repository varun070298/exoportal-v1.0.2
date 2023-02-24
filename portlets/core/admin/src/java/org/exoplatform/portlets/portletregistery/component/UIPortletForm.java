/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.portletregistery.component;

import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.UITextArea;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.portletregistery.Portlet;
import org.exoplatform.services.portletregistery.PortletRegisteryService;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 19 juin 2004
 */
public class UIPortletForm extends UISimpleForm {
  protected UIStringInput portletDisplayName;
  protected UITextArea description;
  private Portlet portlet_ ;
  private PortletRegisteryService portletRegisteryService;

  public UIPortletForm(PortletRegisteryService portletRegisteryService) {
    super("portletForm", "post", null);
    this.portletRegisteryService = portletRegisteryService;
    portletDisplayName = new UIStringInput("name", "");
    description = new UITextArea("description", "");

    setClazz("UIPortletCategoryForm");
    add(new HeaderRow().
        add(new Cell("#{UIPortletForm.header.edit-portlet}").
        addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.portlet-display-name}")).
        add(new ComponentCell(this, portletDisplayName)));
    add(new Row().
        add(new LabelCell("#{UIPortletForm.label.description}")).
        add(new ComponentCell(this, description)));
    add(new Row().
        add(new ListComponentCell().
        add(new FormButton("#{UIPortletForm.button.save}", SAVE_ACTION)).
        add(new FormButton("#{UIPortletForm.button.cancel}", CANCEL_ACTION)).
        addColspan("2").addAlign("center")));

    addActionListener(new UpdateActionListener().setActionToListen(SAVE_ACTION));
    addActionListener(new CancelActionListener().setActionToListen(CANCEL_ACTION));
  }
  
  public void setPortletData(Portlet portlet) { 
  	portlet_ = portlet ;
  	portletDisplayName.setValue(portlet.getDisplayName());
    description.setValue(portlet.getDescription());
  }

  private class UpdateActionListener extends ExoActionListener {
    public UpdateActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
      InformationProvider iprovider = findInformationProvider(event.getComponent()) ;
      String displayName = portletDisplayName.getValue();
      if (displayName == null || "".equals(displayName)) {
        iprovider.addMessage(new ExoFacesMessage("#{UIPortletForm.msg.portlet-name-null}")) ;
        return;
      }
      portlet_.setDisplayName(displayName);
      portlet_.setDescription(description.getValue());    
      portletRegisteryService.updatePortlet(portlet_);
      UIPortletRegistry uiPortlet = (UIPortletRegistry) getParent();
      uiPortlet.setRenderedComponent(UIPortletCategories.class);
    }
  }

  private class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIPortletRegistry uiPortlet = (UIPortletRegistry) getParent();
      uiPortlet.setRenderedComponent(UIPortletCategories.class);
    }
  }
}