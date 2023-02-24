/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import java.util.List;
import org.exoplatform.faces.application.ExoFacesMessage;
import org.exoplatform.faces.core.component.InformationProvider;
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
 * @version: $Id: UICategory.java,v 1.15 2004/10/20 18:46:31 benjmestrallet Exp $
 */
public class UICategory extends UIGrid {
  private static String FORUM_ID_PARAM = "forum" ;
  private static Parameter VIEW_TOPICS =   new Parameter(ACTION, "viewTopics")  ;
  private static Parameter[] selectCategoryParams = { new Parameter(ACTION, "selectCategory")} ;
  
  private ForumService service_ ;
  
  public UICategory(ForumService service, Category category) throws Exception {
    super() ;
    String id = "category_" + category.getId() ;
    setId(id) ;
    setClazz("UICategory");
    service_ = service ;
    List forums = service.getForums(category.getId()) ;
    String forumIcon = "#{UICategory.img.forum-icon}" ;
    Parameter forumIdParam = new Parameter(FORUM_ID_PARAM, "") ;
    Parameter[] viewTopicsParams = {VIEW_TOPICS, forumIdParam} ;
    add(new Row().
        add(new ListComponentCell().
            add(new Button(category.getCategoryName(), selectCategoryParams)).
            addHeight("30").addColspan("5").addClazz("left-indent"))) ;
    add(new HeaderRow().
        add(new Cell("#{UICategory.header.forum}").
            addWidth("*").addColspan("2")).
        add(new Cell("#{UICategory.header.topics}").addWidth("75")).
        add(new Cell("#{UICategory.header.posts}").addWidth("75")).
        add(new Cell("#{UICategory.header.last-post}").addWidth("200")));
    
    for(int j = 0 ; j < forums.size(); j++) {
      Forum forum = (Forum) forums.get(j)  ;
      forumIdParam.setValue(forum.getId()) ;
      String lastPostBy = forum.getLastPostBy() ;
      if(lastPostBy == null) lastPostBy = "guest" ;
      add(new Row().
          add(new Cell(forumIcon).addWidth("30")).
          add(new ListComponentCell().
              add(new Button(forum.getForumName(), viewTopicsParams)).
              add("<br/>").add(forum.getDescription()).addClazz("left-indent")).
          add(new Cell(forum.getTopicCount())).
          add(new Cell(forum.getPostCount())).
          add(new Cell(forum.getLastPostDate() + "<br/>" + lastPostBy)));
    }
    addActionListener(SelectCategoryActionListener.class, "selectCategory")  ;
    addActionListener(ViewTopicsActionListener.class, "viewTopics")  ;
  }
  
  static public class SelectCategoryActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UICategory uiCategory = (UICategory) event.getComponent() ;
			List rows = uiCategory.getRows() ;
	    for (int i = 1 ; i < rows.size(); i++) {
	    	Row row = (Row)rows.get(i);
	      row.setVisible(!row.isVisible()) ;
	    } 
		}
  }
  
  static public class ViewTopicsActionListener extends ExoActionListener {
		public void execute(ExoActionEvent event) throws Exception {
			UICategory uiCategory = (UICategory) event.getComponent() ;
			String forumId = event.getParameter(FORUM_ID_PARAM) ;
      Forum forum = uiCategory.service_.getForum(forumId) ;
      UIForumPortlet uiController = 
      	(UIForumPortlet)uiCategory.getAncestorOfType(UIForumPortlet.class) ;
      UITopics uiTopics = (UITopics) uiController.getChildComponentOfType(UITopics.class) ;
      if(uiTopics.setForum(forum)) {
        uiController.addHistoryElement(uiCategory.getParent());
        uiController.setRenderedComponent(UITopics.class) ;   
        ((UIToolbarPanel)uiController.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
      } else {
      	InformationProvider iprovider = findInformationProvider(uiCategory) ;
        iprovider.addMessage(new ExoFacesMessage("#{UICategory.msg.no-view-forum-role}"));
      }                                  
		}
  }
}