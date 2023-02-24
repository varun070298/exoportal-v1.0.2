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
import org.exoplatform.services.indexing.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIAdvancedSearch.java,v 1.1 2004/10/25 03:34:07 tuan08 Exp $
 */
public class UIAdvancedSearch extends  UISimpleForm {
  static final public String SEARCH_ACTION = "search"  ;
  
  private UIStringInput documentInput_ ;
  private UIStringInput titleInput_ ;
  private UIStringInput descriptionInput_ ;
  private UIStringInput authorInput_ ;
  private List uiModulesInput_ ; 
  
  public UIAdvancedSearch(IndexingService iservice) throws Exception {
    super("aSearchForm", "post", null) ;
  	setId("UIAdvancedSearch") ;
    setClazz("UIAdvancedSearch");
    setRendererType("SimpleFormButtonRenderer") ;
    titleInput_ = new UIStringInput("title", "") ;
    authorInput_ = new UIStringInput("author", "") ;
    descriptionInput_ = new UIStringInput("description", "") ;
    documentInput_ = new UIStringInput("document", "") ;
   
    add(new HeaderRow().
        add(new Cell("#{UIAdvancedSearch.header.search-terms}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIAdvancedSearch.label.document}")).
        add(new ComponentCell(this, documentInput_)));
    add(new Row().
        add(new LabelCell("#{UIAdvancedSearch.label.title}")).
        add(new ComponentCell(this, titleInput_)));
    add(new Row().
        add(new LabelCell("#{UIAdvancedSearch.label.description}")).
        add(new ComponentCell(this, descriptionInput_)));
    add(new Row().
        add(new LabelCell("#{UIAdvancedSearch.label.author}")).
        add(new ComponentCell(this, authorInput_)));
    add(new HeaderRow().
        add(new Cell("#{UIAdvancedSearch.header.search-options}").
            addColspan("2")));
    
    uiModulesInput_ = new ArrayList() ;
    Iterator i = iservice.getIndexerPlugins().iterator();
    while(i.hasNext()) {
      IndexerPlugin plugin =(IndexerPlugin) i.next() ;
      UICheckBox uiModule = new UICheckBox(plugin.getPluginIdentifier(), "true");
      uiModule.setSelect(true) ;
      add(new Row().
          add(new ListComponentCell().
              add(this, uiModule).         
              add("#{UIAdvancedSearch." +plugin.getPluginIdentifier() + ".module}").
              addColspan("2").addClazz("indent-2"))) ;
      uiModulesInput_.add(uiModule) ;
    }
    add(new Row().
        add(new ListComponentCell().
            add("<div style='float: right'>").
            add(new FormButton("#{UIAdvancedSearch.button.search}", SEARCH_ACTION)).
            add("</div>").
            addColspan("2"))) ;
    
    addActionListener(SearchActionListener.class, SEARCH_ACTION) ;
  }
  
  static public class SearchActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAdvancedSearch uiForm =  (UIAdvancedSearch) event.getSource();
      List selectModules = new ArrayList() ;
      for(int i = 0; i < uiForm.uiModulesInput_.size(); i++) {
        UICheckBox uiBox = (UICheckBox) uiForm.uiModulesInput_.get(i) ;
        if(uiBox.getSelect()) {
          selectModules.add(uiBox.getName()) ;
        }
      }
      
      List inputs = new ArrayList() ;
      String value = uiForm.titleInput_.getValue() ;
      if(value.length() > 0) {
        inputs.add(new SingleFieldSearchInput(IndexingService.TITLE_FIELD, value)) ;
      }
      value = uiForm.authorInput_.getValue() ;
      if(value.length() > 0) {
        inputs.add(new SingleFieldSearchInput(IndexingService.AUTHOR_FIELD, value)) ;
      }
      value = uiForm.documentInput_.getValue() ;
      if(value.length() > 0) {
        inputs.add(new SingleFieldSearchInput(IndexingService.DOCUMENT_FIELD, value)) ;
      }
      UISearcher uiSearcher = (UISearcher) uiForm.getParent() ;
      uiSearcher.advancedSearch(inputs, selectModules) ;
    }
  }
}