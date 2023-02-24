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
import org.exoplatform.portal.faces.component.model.PortletCategoryData;
import org.exoplatform.services.portletregistery.PortletCategory;
import org.exoplatform.services.portletregistery.PortletRegisteryService;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 16 juin 2004
 */
public class UIPortletCategoryForm extends UISimpleForm {
	private PortletCategoryData categoryData_ ;
  protected UIStringInput name_;
  protected UITextArea description_;
  private PortletRegisteryService service_;

  public UIPortletCategoryForm(PortletRegisteryService service) {
    super("portletCategoryForm", "post", null);
    service_ = service ;
    name_ = new UIStringInput("name", "");
    description_ = new UITextArea("description", "");
    setClazz("UIPortletCategoryForm");
    add(new HeaderRow().
        add(new Cell("#{UIPortletCategoryForm.header.create-category}").
        addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIPortletCategoryForm.label.portlet-category-name}")).
        add(new ComponentCell(this, name_)));
    add(new Row().
        add(new LabelCell("#{UIPortletCategoryForm.label.description}")).
        add(new ComponentCell(this, description_)));
    add(new Row().
        add(new ListComponentCell().
        add(new FormButton("#{UIPortletCategoryForm.button.save}", SAVE_ACTION)).
        add(new FormButton("#{UIPortletCategoryForm.button.cancel}", CANCEL_ACTION)).
        addColspan("2").addAlign("center")));
    addFacesListener(new SaveActionListener().setActionToListen(SAVE_ACTION));
    addFacesListener(new CancelActionListener().setActionToListen(CANCEL_ACTION));
  }
  
  public void setPortletCategoryData(PortletCategoryData category) {
  	categoryData_ = category ;
  	if(category == null) {
  		description_.setValue("") ;
  		name_.setValue("") ;
  	} else {
  		name_.setValue(category.getPortletCategoryName()) ;
  		description_.setValue(category.getPortletCategory().getDescription()) ;
  	}
  }
  
  public class SaveActionListener extends ExoActionListener {
    public SaveActionListener() {
      addInterceptor(new CheckRoleInterceptor(CheckRoleInterceptor.ADMIN)) ;  
    }
    
    public void execute(ExoActionEvent event) throws Exception {
    	UIPortletCategoryForm uiForm = (UIPortletCategoryForm) event.getComponent() ;
      InformationProvider iprovider = findInformationProvider(event.getComponent()) ;
      String categoryName = name_.getValue() ;
      if (categoryName == null || "".equals(categoryName)) {
        iprovider.addMessage(new ExoFacesMessage("#{UIPortletCategoryForm.msg.category-name-null}"));
        return;
      }
      if(categoryData_ == null) {
      	PortletCategory category = service_.createPortletCategoryInstance();
      	category.setPortletCategoryName(categoryName);
      	category.setDescription(description_.getValue());
      	service_.addPortletCategory(category);
      } else {
      	PortletCategory category = categoryData_.getPortletCategory() ;
      	category.setPortletCategoryName(categoryName);
      	category.setDescription(description_.getValue());
      	service_.updatePortletCategory(category);
      }
      UIPortletCategories uiCategories = 
    		(UIPortletCategories) uiForm.getSibling(UIPortletCategories.class) ;
      uiCategories.reset() ;
      uiForm.setRenderedSibling(UIPortletCategories.class) ;
    }
  }
  
  public class CancelActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
    	UIPortletCategoryForm uiForm = (UIPortletCategoryForm) event.getComponent() ;
      uiForm.setRenderedSibling(UIPortletCategories.class) ;
    }
  }
}