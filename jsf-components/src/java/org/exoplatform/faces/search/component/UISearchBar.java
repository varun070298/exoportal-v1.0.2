/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.search.component;

import java.util.*;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UISearchBar.java,v 1.2 2004/10/30 02:29:51 tuan08 Exp $
 */
public class UISearchBar extends UISimpleForm {
  static final public String SEARCH_ACTION = "search" ;
  static final public String ADVANCED_SEARCH_ACTION = "advancedSearch" ;
  
  private UIStringInput termInput_ ;
  private UISelectBox   searchFieldInput_;
  private List searchOptions_ ;
  
  public UISearchBar() {
    super("quickSearchForm", "post", null) ;
  	setId("UISearchBar") ;
    setClazz("UISearchBar");
    
    termInput_ = new UIStringInput("term", "")  ;
    searchFieldInput_= new UISelectBox("searchField", "", null );
    add(new Row().
    	  add(new ListComponentCell().
    	      add(this, termInput_ ).
            add(this, searchFieldInput_ ).
    	      add(new FormButton("#{UISearchBar.button.search}", SEARCH_ACTION))).
        add(new ListComponentCell().
            add(new FormButton("#{UISearchBar.button.advanced-search}", ADVANCED_SEARCH_ACTION)).
            addAlign("right"))) ;
    
    addActionListener(AdvancedSearchActionListener.class, ADVANCED_SEARCH_ACTION) ;
    addActionListener(SearchActionListener.class, SEARCH_ACTION) ;
  }
  
  public boolean isRendered() { return true ; }
  
  public void setSearchOptions(List options) {
    searchOptions_ = options ;
    searchFieldInput_.setOptions(options) ;
  }
  
  static public class SearchActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UISearchBar uiForm =  (UISearchBar) event.getSource();
      String term  = uiForm.termInput_.getValue() ;
      if(term == null || term.length() == 0) return ;
      String select = uiForm.searchFieldInput_.getValue() ;
      List fields = new ArrayList() ;
      if("".equals(select)) {
        for(int i = 0; i < uiForm.searchOptions_.size(); i++) {
          SelectItem item = (SelectItem) uiForm.searchOptions_.get(i) ;
          if(item.value_.length() > 0) {
            fields.add(item.value_) ;
          }
        }
      } else {
        fields.add(select) ;
      }
      UISearcher uiSearcher = (UISearcher) uiForm.getParent() ;
      uiSearcher.quickSearch(term, fields) ;
    }
  } 
  
  static public class AdvancedSearchActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UISearchBar uiSearch =  (UISearchBar) event.getSource();
      UISearcher uiSearcher = (UISearcher) uiSearch.getParent() ;
      uiSearcher.showAdvancedSearch() ;
    }
  } 
}