/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import org.exoplatform.faces.core.component.UIForm;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.faces.core.validator.EmptyFieldValidator;
import org.exoplatform.services.communication.forum.Category;
import org.exoplatform.services.communication.forum.ForumService;

/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UICategoryForm.java,v 1.7 2004/10/17 13:46:25 tuan08 Exp $
 */
public class UICategoryForm extends UIForm {
  private static String CATEGORY_NAME = "categoryName" , CATEGORY_DESC = "categoryDesc" ,
                        CATEGORY_ORDER = "order";
  
  private ForumService service_ ;
  private Category category_ ;
  
  public UICategoryForm(ForumService service) throws Exception {
    super("category") ;
    setId("UICategoryForm") ;
    service_ = service ;
    addContainer("#{UICategoryForm.header.title}").
      add(new StringField(CATEGORY_NAME, "#{UICategoryForm.label.name}", "")).
      add(new TextAreaField(CATEGORY_DESC, "#{UICategoryForm.label.description}", "")).
      add(new IntegerField(CATEGORY_ORDER, "#{UICategoryForm.label.category-order}", 0));
    addButton(new Button("#{UICategoryForm.button.save}", SAVE_ACTION)) ;
    addButton(new Button("#{UICategoryForm.button.cancel}", CANCEL_ACTION)) ;
    addFieldValidator(EmptyFieldValidator.class) ;
    addActionListener(CancelActionListener.class, CANCEL_ACTION)  ;
    addActionListener(SaveActionListener.class, SAVE_ACTION)  ;
  }
  
  public void setCategory(Category category) {
    category_ = category ;
    if (category == null) {
      resetFields() ;
    } else {
      setStringFieldValue(CATEGORY_NAME, category.getCategoryName()) ;
      setStringFieldValue(CATEGORY_DESC, category.getDescription()) ;
      setIntegerFieldValue(CATEGORY_ORDER, category.getCategoryOrder()) ;
    }
  }
  
  static public class CancelActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UICategoryForm uiForm = (UICategoryForm) event.getComponent() ;
			uiForm.setRenderedSibling(UIAdminViewCategories.class) ;
		}
  }
  
  static public class SaveActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UICategoryForm uiForm = (UICategoryForm) event.getComponent() ;
      Category category = uiForm.category_ ;
       boolean isNew =  false ;
       if(category == null) {
         category = uiForm.service_.createCategoryInstance() ;
         isNew = true ;
       }
       category.setCategoryName(uiForm.getStringFieldValue(CATEGORY_NAME) ) ;
       category.setDescription(uiForm.getStringFieldValue(CATEGORY_DESC)) ;
       category.setCategoryOrder(uiForm.getIntegerFieldValue(CATEGORY_ORDER)) ;
       if(isNew) uiForm.service_.addCategory(category) ;
       else   uiForm.service_.updateCategory(category) ;
       UIAdminViewCategories uiCategories =
       	(UIAdminViewCategories) uiForm.getSibling(UIAdminViewCategories.class) ;
       uiCategories.update() ;
			 uiForm.setRenderedSibling(UIAdminViewCategories.class) ;
		}
  }
}