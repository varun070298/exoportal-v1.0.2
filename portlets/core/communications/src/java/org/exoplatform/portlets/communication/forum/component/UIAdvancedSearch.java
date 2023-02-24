/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import java.util.*;
import org.exoplatform.faces.core.component.UIDateInput;
import org.exoplatform.faces.core.component.UISelectBox;
import org.exoplatform.faces.core.component.UISimpleForm;
import org.exoplatform.faces.core.component.UIStringInput;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.forum.Category;
import org.exoplatform.services.communication.forum.Forum;
import org.exoplatform.services.communication.forum.ForumIndexerPlugin;
import org.exoplatform.services.communication.forum.ForumService;
import org.exoplatform.services.indexing.RangeFieldSearchInput;
import org.exoplatform.services.indexing.SingleFieldSearchInput;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UISearch.java,v 1.4 2004/10/20 18:46:31 benjmestrallet Exp $
 */
public class UIAdvancedSearch extends  UISimpleForm {
  static final public String SEARCH_ACTION = "search" ;
  
  private UISelectBox uiTargetInput_ ;
  private UIStringInput subjectInput_ ;
  private UIStringInput bodyInput_ ;
  private UIStringInput authorInput_ ;
  private UIDateInput uiFromDateImput_ ;
  private UIDateInput uiToDateImput_ ;
  
  public UIAdvancedSearch(ForumService fservice) throws Exception {
    super("searchForm", "post", null) ;
  	setId("UIAdvancedSearch") ;
    setClazz("UIAdvancedSearch");
    
    List categories = fservice.getCategories();
    List options = new ArrayList() ;
    options.add(new SelectItem("All Categories", ""));
    StringBuffer b = new StringBuffer() ;
    for(int i = 0; i < categories.size() ; i++) {
      b.setLength(0) ;
      Category category = (Category) categories.get(i) ;
      b.append("category:").append(category.getId()) ;
      options.add(new SelectItem("<<<< " + category.getCategoryName() + " >>>>", b.toString()));
      List forums = fservice.getForums(category.getId()) ;
      for(int j = 0 ; j < forums.size(); j++) {
        b.setLength(0) ;
        Forum forum = (Forum) forums.get(j) ;
        b.append("forum:").append(forum.getId()) ;
        options.add(new SelectItem(forum.getForumName(), b.toString()));
      }
    } 
    uiTargetInput_ = new UISelectBox("target", "", options);
    subjectInput_ = new UIStringInput("subject", "") ;
    bodyInput_ = new UIStringInput("body", "") ;
    authorInput_ = new UIStringInput("author", "") ;
    uiFromDateImput_ = new UIDateInput("fromDate", new GregorianCalendar(2004, 0, 1).getTime() ) ; 
    uiToDateImput_ = new UIDateInput("toDate", new Date()) ;
    
    addActionListener(SearchActionListener.class, SEARCH_ACTION) ;
    
    add(new HeaderRow().
        add(new Cell("#{UISearch.header.search}").addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UISearch.label.search-in}")).
        add(new ComponentCell(this, uiTargetInput_).addClazz("search-in")));
    add(new Row().
        add(new LabelCell("#{UISearch.label.search-subject}")).
        add(new ComponentCell(this, subjectInput_)));
    add(new Row().
        add(new LabelCell("#{UISearch.label.search-body}")).
        add(new ComponentCell(this, bodyInput_)));
    add(new Row().
        add(new LabelCell("#{UISearch.label.search-author}")).
        add(new ComponentCell(this, authorInput_)));
    add(new Row().
        add(new LabelCell("#{UISearch.label.search-date}")).
        add(new ListComponentCell().
            add(this, uiFromDateImput_).
            add("#{UISearch.label.to}").
            add(this, uiToDateImput_))) ;
    
    add(new Row().
        add(new ListComponentCell().
            add(new FormButton("#{UISearch.button.search}", SEARCH_ACTION)).
            addColspan("2").addAlign("center"))) ;
  }
  
  static public class SearchActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAdvancedSearch uiForm =  (UIAdvancedSearch) event.getSource();
      List inputs = new ArrayList() ;
      String value = uiForm.uiTargetInput_.getValue() ;
      if(value.length() > 0) {
        if(value.startsWith("category:")) {
          value = value.substring("category:".length() , value.length()) ;
          inputs.add(new SingleFieldSearchInput(ForumIndexerPlugin.CATEGORY_ID_FIELD, value)) ;
        } else if(value.startsWith("forum:")) {
          value = value.substring("forum:".length() , value.length()) ;
          inputs.add(new SingleFieldSearchInput(ForumIndexerPlugin.FORUM_ID_FIELD, value)) ;
        }
      }     
      value = uiForm.subjectInput_.getValue() ;
      if(value.length() > 0) {
        inputs.add(new SingleFieldSearchInput(ForumIndexerPlugin.SUBJECT_FIELD, value)) ;
      }
      
      value = uiForm.bodyInput_.getValue() ;
      if(value.length() > 0) {
        inputs.add(new SingleFieldSearchInput(ForumIndexerPlugin.BODY_FIELD, value)) ;
      }     
      value = uiForm.authorInput_.getValue() ;
      if(value.length() > 0) {
        inputs.add(new SingleFieldSearchInput(ForumIndexerPlugin.AUTHOR_FIELD, value)) ;
      }
      
      Date from = uiForm.uiFromDateImput_.getValue() ;
      Date to = uiForm.uiToDateImput_.getValue() ;
      inputs.add(new RangeFieldSearchInput(ForumIndexerPlugin.DATE_FIELD, from, to)) ;  
      
      UIForumSearcher uiSearcher = (UIForumSearcher) uiForm.getAncestorOfType(UIForumSearcher.class) ;
      uiSearcher.advancedSearch(inputs, null) ;
    }
  } 
}