/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.faces.component;

import java.util.*;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.portal.faces.listener.page.ShowCurrentPageActionListener;
import org.exoplatform.services.portal.model.PortalConfig;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portal.skin.model.Decorator;
import org.exoplatform.services.portal.skin.model.Style;
/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPortalForm.java,v 1.9 2004/09/29 17:44:51 benjmestrallet Exp $
 */
public class UIPortalForm extends UISimpleForm {
  
  private UIStringInput localeInput_ ;
  private UIStringInput viewPermissionInput_ ;
  private UIStringInput editPermissionInput_ ;
  private UISelectBox rendererInput_ ;
  //private UISelectBox decoratorInput_;
  private List styleOptions_;
  private List rendererOptions_;

  public UIPortalForm(SkinConfigService service) {
    super("portalForm", "post", null) ;
    setId("UIPortalForm") ;
    localeInput_ = new UIStringInput("defaultLocale", "") ;
    viewPermissionInput_ = new UIStringInput("viewPermission", "") ;
    editPermissionInput_ = new UIStringInput("editPermission", "") ;
    
    rendererOptions_ = new ArrayList();
    styleOptions_ = new ArrayList();
    Iterator i = service.getPortalDecorators().iterator();
    while(i.hasNext()) {
      Decorator decorator = (Decorator) i.next();
      String rendererType = decorator.getRendererType();
      rendererOptions_.add(new SelectItem(rendererType, rendererType));
    }
    
   
    Decorator currentDecorator =  service.getPortalDecorator("default");
    List styles = currentDecorator.getStyles();
    for(int j= 0; j < styles.size(); j++) {
      Style style = (Style)styles.get(j);
      String name = style.getName();
      styleOptions_.add(new SelectItem(name, name));
    }
    
    rendererInput_ = new UISelectBox("renderer", "", rendererOptions_);
    rendererInput_.setUpdateOnChangeAction("updateStyles");
    rendererInput_.setValue("");
    
    String saveButton = "#{UIPortalForm.link.save}";
    String cancelButton = "#{UIPortalForm.link.cancel}";
    add(new HeaderRow().
        add(new Cell("#{UIPortalForm.header.edit-portal-config}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIPortalForm.label.default-locale}")).
        add(new ComponentCell(this, localeInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortalForm.label.view-permission}")).
        add(new ComponentCell(this, viewPermissionInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortalForm.label.edit-permission}")).
        add(new ComponentCell(this, editPermissionInput_)));
    add(new Row().
        add(new LabelCell("#{UIPortalForm.label.renderer}")).
        add(new ComponentCell(this, rendererInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton(saveButton, SAVE_ACTION)).
            add(new FormButton(cancelButton, CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    addActionListener(ShowCurrentPageActionListener.class, CANCEL_ACTION) ;
  }
   
  public void setUIPortal(UIPortal uiPortal) {
  	PortalConfig model = uiPortal.getPortalConfigModel() ;
    localeInput_.setValue(model.getLocale()) ;
    viewPermissionInput_.setValue(model.getViewPermission()) ;
    editPermissionInput_.setValue(model.getEditPermission()) ;
    rendererInput_.setValue(model.getRenderer()) ;
  }
   
 
  static public class SaveActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
      UIPortalForm uiForm = (UIPortalForm) event.getSource() ;
	    UIPortal uiPortal = (UIPortal) uiForm.getAncestorOfType(UIPortal.class) ;
			PortalConfig config = uiPortal.getEditablePortalConfigModel() ;
      config.setLocale(uiForm.localeInput_.getValue()) ;
      config.setViewPermission(uiForm.viewPermissionInput_.getValue()) ;
      config.setEditPermission(uiForm.editPermissionInput_.getValue()) ;
      config.setRenderer(uiForm.rendererInput_.getValue()) ;
      uiPortal.setBodyComponent(uiPortal.getCurrentUIPage()) ;
      uiPortal.setComponentModified(true) ;
		}
	}
}	