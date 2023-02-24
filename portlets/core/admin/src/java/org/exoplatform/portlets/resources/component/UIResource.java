/*******************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved. *
 * Please look at license.txt in info directory for more license detail.           *
 *******************************************************************************/
package org.exoplatform.portlets.resources.component;

import java.util.ArrayList;
import java.util.List;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.resources.ResourceBundleData;
import org.exoplatform.services.resources.ResourceBundleService;
/**
 * Wed, March 22, 2004 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIResource.java,v 1.7 2004/08/17 13:07:01 tuan08 Exp $
 */
public class UIResource extends UISimpleForm {  
  private static List LANGUAGES ;
  static {
    LANGUAGES = new ArrayList(10) ;
    LANGUAGES.add(new SelectItem("Default", "en")) ;
    LANGUAGES.add(new SelectItem("English", "en")) ;
    LANGUAGES.add(new SelectItem("French", "fr")) ;
    LANGUAGES.add(new SelectItem("Vietnamese", "vn")) ;
  }
  
  private UITextArea textInput_ ;
  protected UISelectBox languageInput_ ;
  protected UIStringInput nameInput_ ;
  private boolean adminRole_ ;
  private ResourceBundleData data_ ;
  private ResourceBundleService service_ ;

  public UIResource(ResourceBundleService service) {
    super("resource", "post", null) ;
    setId("UIResource");    
    setClazz("UIResource") ;
    service_ = service ;
    nameInput_ = new UIStringInput("name", "") ;
    languageInput_ = new UISelectBox("language", "en", LANGUAGES) ;
    languageInput_.setClazz("short") ;
    textInput_ = new UITextArea("text" , "") ;
    textInput_.setClazz("large") ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
    add(new Row().
        add(new ComponentCell(this, textInput_)));
    add(new Row().     
        add(new ListComponentCell().
            add("#{UIResource.label.name}").
            add(this, nameInput_).
            add(this, languageInput_)));
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UIResource.button.save}", SAVE_ACTION, EDIT_MODE)).
            add(new FormButton("#{UIResource.button.edit}", EDIT_ACTION, VIEW_MODE)).
            add(new FormButton("#{UIResource.button.preview}", VIEW_ACTION, EDIT_MODE)).
            add(new FormButton("#{UIResource.button.cancel}", CANCEL_ACTION)).
            addColspan("2").addAlign("center"))) ;
    addActionListener(EditActionListener.class, EDIT_ACTION) ;
    addActionListener(PreviewActionListener.class, VIEW_ACTION) ;
    addActionListener(SaveActionListener.class, SAVE_ACTION) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION) ;
  }
  
  public void setResourceBundleData(ResourceBundleData data) {
  	data_ = data ;
    if (data != null) {
    	textInput_.setValue(data.getData()) ;
    	languageInput_.setValue(data.getLanguage()) ;
    	languageInput_.setEditable(false) ;
    	nameInput_.setValue(data.getName()) ;
    	nameInput_.setEditable(false) ; 
    } else {
      textInput_.setValue("") ;   
    	languageInput_.setValue("") ;
    	nameInput_.setValue("") ;
    	nameInput_.setEditable(true) ;
    	languageInput_.setEditable(true) ;  
    }
  }
  
  static public class CancelActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
  		UIResource uiResource = (UIResource) event.getComponent() ;
  		UIResourcesPortlet uiPortlet = (UIResourcesPortlet)uiResource.getParent() ;
      UIListResources uiListResources = 
      	(UIListResources)uiPortlet.getChildComponentOfType(UIListResources.class);
      uiPortlet.setRenderedComponent(uiListResources.getId()) ;
    }
  }
  
  static public class EditActionListener extends ExoActionListener  {
    public EditActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
  		UIResource uiResource = (UIResource) event.getComponent() ;
  		uiResource.setMode(EDIT_MODE) ;
    }
  }
  
  static public class PreviewActionListener extends ExoActionListener  {
  	public void execute(ExoActionEvent event) throws Exception {
  		UIResource uiResource = (UIResource) event.getComponent() ;
      uiResource.setMode(VIEW_MODE) ;
    }
  }
  
  static public class SaveActionListener extends ExoActionListener  {
    public SaveActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
  	public void execute(ExoActionEvent event) throws Exception {
  		UIResource uiResource = (UIResource) event.getComponent() ;
  		InformationProvider iprovider = findInformationProvider(uiResource) ;
  		String name = uiResource.nameInput_.getValue() ;
  		if(name == null || name.length() == 0) {
  			iprovider.addMessage(new ExoFacesMessage("#{UIResource.msg.resource-name-empty}")) ;
  			return ;
  		}
  		
  		if(uiResource.data_ == null) {
        uiResource.data_ = uiResource.service_.createResourceBundleDataInstance() ;
        uiResource.data_.setName(uiResource.nameInput_.getValue()) ;
        uiResource.data_.setLanguage(uiResource.languageInput_.getValue()) ;
      }
      uiResource.data_.setData(uiResource.textInput_.getValue());
      uiResource.service_.saveResourceBundle(uiResource.data_);
      iprovider.addMessage(new ExoFacesMessage("#{UIResource.msg.save-resource-success}")) ;
    }
  }
}