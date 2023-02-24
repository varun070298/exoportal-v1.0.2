/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import java.util.List;
import org.exoplatform.faces.FacesConstants;
import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.forum.Category;
import org.exoplatform.services.communication.forum.Forum;
import org.exoplatform.services.communication.forum.ForumService;

/**
 * Sat, Jan 03, 2004 @ 11:16 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIAdminViewCategories.java,v 1.7 2004/10/17 13:46:24 tuan08 Exp $
 */
public class UIAdminViewCategories extends UIGrid {
  private static Parameter ADD_CATEGORY_PARAM = new Parameter(FacesConstants.ACTION, "addCategory") ;
  private static Parameter DELETE_CATEGORY_PARAM = new Parameter(FacesConstants.ACTION, "deleteCategory") ;
  private static Parameter EDIT_CATEGORY_PARAM = new Parameter(FacesConstants.ACTION, "editCategory") ;

  private static Parameter ADD_FORUM_PARAM = new Parameter(FacesConstants.ACTION, "addForum") ;
  private static Parameter DELETE_FORUM_PARAM = new Parameter(FacesConstants.ACTION, "deleteForum") ;
  private static Parameter EDIT_FORUM_PARAM = new Parameter(FacesConstants.ACTION, "editForum") ;
       
  private ForumService service_  ;
   
  public UIAdminViewCategories(ForumService service) throws Exception {
    super() ;
    setId("UIAdminViewCategories") ;
    setClazz("UIAdminViewCategories");
    service_ = service ;
    addActionListener(AddCategoryActionListener.class, "addCategory")  ;
    addActionListener(EditCategoryActionListener.class, "editCategory")  ;
    addActionListener(DeleteCategoryActionListener.class, "deleteCategory")  ;
    addActionListener(AddForumActionListener.class, "addForum")  ;
    addActionListener(EditForumActionListener.class, "editForum")  ;
    addActionListener(DeleteForumActionListener.class, "deleteForum")  ;
    buildGrid() ;
  }
  
  public void  buildGrid() throws Exception {
    Parameter categoryIdParam = new Parameter("categoryId", "") ;
    Parameter forumIdParam = new Parameter("forumId", "") ;
    Parameter[] addCategoryParams = { ADD_CATEGORY_PARAM } ;
    Parameter[] delCategoryParams = {DELETE_CATEGORY_PARAM, categoryIdParam } ;
    Parameter[] editCategoryParams = {EDIT_CATEGORY_PARAM, categoryIdParam } ;
    Parameter[] addForumParams = {ADD_FORUM_PARAM, categoryIdParam } ;
    Parameter[] delForumParams = {DELETE_FORUM_PARAM, forumIdParam } ;
    Parameter[] editForumParams = {EDIT_FORUM_PARAM, forumIdParam } ;
    
    List categories = service_.getCategories();
    add(new HeaderRow().
        add(new Cell("#{UIAdminViewCategories.header.name}").
            addAlign("left").addColspan("2").addWidth("*")).
        add(new Cell("#{UIAdminViewCategories.header.modified-by}").addWidth("100")).
        add(new Cell("#{UIAdminViewCategories.header.modified-date}").addWidth("200")).
        add(new Cell("#{UIAdminViewCategories.header.actions}").addWidth("150"))) ;
    for (int i = 0; i < categories.size(); i++) {
      Category category = (Category) categories.get(i) ;
      String categoryId = category.getId() ;
      categoryIdParam.setValue(categoryId) ;
      List forums = service_.getForums(category.getId()) ;
      add(new Row().
          add(new Cell(category.getCategoryName()).
              addAlign("left").addColspan("2").addHeight("35").addStyle("font-weight: bold")).
          add(new Cell(category.getModifiedBy()).addAlign("center")).
          add(new Cell(category.getModifiedDate())).
          add(new ListComponentCell().
                  add(new Button("#{UIAdminViewCategories.button.add}", addForumParams)).
                  add(new Button("#{UIAdminViewCategories.button.edit}", editCategoryParams)).
                  add(new Button("#{UIAdminViewCategories.button.delete}",delCategoryParams)).
              addAlign("center"))) ;
      for(int j = 0 ; j < forums.size(); j++) {
        Forum forum = (Forum) forums.get(j) ;
        String forumId = forum.getId() ;
        forumIdParam.setValue(forumId) ;
        add(new Row().
            add(new Cell("#{UIAdminViewCategories.img.forum-icon}").addWidth("30")).
            add(new Cell(forum.getForumName()).addStyle("font-weight:bold")).
            add(new Cell(forum.getModifiedBy()).addAlign("center")).
            add(new Cell(forum.getModifiedDate())).
            add(new ListComponentCell().
                add(new Button("#{UIAdminViewCategories.button.add}", addForumParams)).
                add(new Button("#{UIAdminViewCategories.button.edit}", editForumParams)).
                add(new Button("#{UIAdminViewCategories.button.delete}",delForumParams)).
                addAlign("center")));
      }
    }
    Button button = new Button("#{UIAdminViewCategories.button.new-category}", addCategoryParams) ;
    add(new Row().
        add(new ListComponentCell().add(button).
            addHeight("30").addAlign("center").addColspan("5")));
  }
  
  public void update() throws Exception {
    clear() ;
    buildGrid() ; 
  }

  static public class AddCategoryActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIAdminViewCategories uiCategories = (UIAdminViewCategories) event.getComponent() ;
			UICategoryForm uiForm = (UICategoryForm) uiCategories.getSibling(UICategoryForm.class) ;
			uiForm.setCategory(null) ; 
			uiCategories.setRenderedSibling(UICategoryForm.class) ;
		}
  }
  
  static public class EditCategoryActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIAdminViewCategories uiCategories = (UIAdminViewCategories) event.getComponent() ;
			UICategoryForm uiForm = (UICategoryForm) uiCategories.getSibling(UICategoryForm.class) ;
			String categoryId = event.getParameter("categoryId") ;
      Category category = uiCategories.service_.getCategory(categoryId) ;
      uiForm.setCategory(category) ; 
			uiCategories.setRenderedSibling(UICategoryForm.class) ;
		}
  }
  
  static public class DeleteCategoryActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIAdminViewCategories uiCategories = (UIAdminViewCategories) event.getComponent() ;
			String categoryId = event.getParameter("categoryId") ;
      uiCategories.service_.removeCategory(categoryId) ;
      uiCategories.update();
		}
  }
  
  static public class AddForumActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIAdminViewCategories uiCategories = (UIAdminViewCategories) event.getComponent() ;
			UIForumForm uiForm = (UIForumForm) uiCategories.getSibling(UIForumForm.class) ;
			String categoryId = event.getParameter("categoryId") ;
			uiForm.setForum(categoryId, null) ; 
			uiCategories.setRenderedSibling(UIForumForm.class) ;
		}
  }
  
  static public class EditForumActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIAdminViewCategories uiCategories = (UIAdminViewCategories) event.getComponent() ;
			UIForumForm uiForm = (UIForumForm) uiCategories.getSibling(UIForumForm.class) ;
			String forumId = event.getParameter("forumId") ;
      Forum forum = uiCategories.service_.getForum(forumId) ;
			uiForm.setForum(null, forum) ; 
			uiCategories.setRenderedSibling(UIForumForm.class) ;
		}
  }
  
  static public class DeleteForumActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UIAdminViewCategories uiCategories = (UIAdminViewCategories) event.getComponent() ;
			String forumId = event.getParameter("forumId") ;
      uiCategories.service_.removeForum(forumId) ;
      uiCategories.update();
		}
  }
}