 /***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.UIToolbar;
import org.exoplatform.faces.core.component.model.Button;
import org.exoplatform.faces.core.component.model.ComponentCell;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.component.model.Row;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
/**
 * @author Benjamin Mestrallet
 * benjamin.mestrallet@exoplatform.com
 */
public class UIToolbarPanel extends UIGrid { 
  
  private static final String SEARCH = "search";    
  private static final String VIEW_FORUMS = "viewForums";  
  private static final String BACK = "back";    
  
  public static Parameter[] backParams = { new Parameter(ACTION , BACK)} ;  
  public static Parameter[] searchParams = { new Parameter(ACTION , SEARCH)} ;  
  public static Parameter[] viewForumsParams = { new Parameter(ACTION , VIEW_FORUMS)} ;  
  
  public UIToolbarPanel() {
    setId("UIToolbarPanel");
    setClazz("UIToolbarPanel");
    UIToolbar toolbar = new UIToolbar() ;
    toolbar.setRendered(true) ;
    toolbar.addLeftButton(new Button("#{UIForumPortlet.button.home}", viewForumsParams));
    toolbar.addLeftButton(new Button("#{UIForumPortlet.button.search}", searchParams));   
    toolbar.addRightButton(new Button("#{UIForumPortlet.button.back}", backParams));
    getChildren().add(toolbar) ; 
    
    add(new Row().add(new ComponentCell(this, toolbar))) ;
    
    addActionListener(ViewForumsActionListener.class, VIEW_FORUMS)  ;   
    addActionListener(SearchActionListener.class, SEARCH) ;
    addActionListener(BackActionListener.class, BACK) ;       
    
  }

  
  static public class ViewForumsActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIToolbarPanel panel = (UIToolbarPanel) event.getComponent() ;      
      UIForumPortlet forumPortlet = (UIForumPortlet)panel.getAncestorOfType(UIForumPortlet.class);      
      forumPortlet.addHistoryElement(forumPortlet.getRenderedComponent());      
      forumPortlet.setRenderedComponent(UIViewCategories.class); 
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
    }
  }  
  
  static public class SearchActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIToolbarPanel panel = (UIToolbarPanel) event.getSource() ;                   
      UIForumPortlet forumPortlet = (UIForumPortlet)panel.getAncestorOfType(UIForumPortlet.class);
      forumPortlet.addHistoryElement(forumPortlet.getRenderedComponent());      
      forumPortlet.setRenderedComponent(UIForumSearcher.class);  
      ((UIToolbarPanel)forumPortlet.getChildComponentOfType(UIToolbarPanel.class)).setRendered(true);
    }
  } 
  
  static public class BackActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIToolbarPanel panel = (UIToolbarPanel) event.getSource() ; 
      ((UIForumPortlet)panel.getAncestorOfType(UIForumPortlet.class)).undo();
      panel.setRendered(true);
    }
  }      
  
}
