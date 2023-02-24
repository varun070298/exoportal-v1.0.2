/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import java.util.*;
import org.exoplatform.faces.core.component.*;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.communication.message.*;
import org.exoplatform.services.indexing.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIAdvancedSearch.java,v 1.7 2004/10/30 02:22:18 tuan08 Exp $
 */
public class UIAdvancedSearch extends  UISimpleForm {
  static final public String SEARCH_ACTION = "search" ;
  
  private UIStringInput subjectInput_ ;
  private UIStringInput fromInput_ ;
  private UIStringInput toInput_ ;
  private UIStringInput bodyInput_ ;
  private List uiFolderInput_ ; 
  private UICheckBox  uiSearchByFlagInput_ ;
  private UISelectBox uiFlagInput_ ;
  private UICheckBox  uiSearchByDateRangeInput_ ;
  private UIDateInput uiFromDateImput_ ;
  private UIDateInput uiToDateImput_ ;
  
  public UIAdvancedSearch(MessageService mservice, 
                          IndexingService iservice) throws Exception {
    super("advancedSearchForm", "post", null) ;
  	setId("UIAdvancedSearch") ;
    setClazz("UIAdvancedSearch");
    setRendererType("SimpleFormButtonRenderer") ;
    uiFolderInput_ = new ArrayList() ;
    subjectInput_ = new UIStringInput("subject", "") ;
    fromInput_ = new UIStringInput("from", "") ;
    toInput_ = new UIStringInput("to", "") ;
    bodyInput_ = new UIStringInput("body", "") ;
    String[] flag = mservice.getSupportedFlags() ;
    List options = new ArrayList() ;
    for(int i = 0; i < flag.length ; i++) {
      options.add(new SelectItem(flag[i], flag[i]));
    }
    uiSearchByFlagInput_ = new UICheckBox("searchByFlag", "true"); 
    uiFlagInput_= new UISelectBox("flag", "", options);
    uiFromDateImput_ = new UIDateInput("fromDate", new GregorianCalendar(2004, 0, 1).getTime() ) ;
    uiSearchByDateRangeInput_ = new UICheckBox("searchByDate", "true"); 
    uiToDateImput_ = new UIDateInput("toDate", new Date()) ;
    addActionListener(SearchActionListener.class, SEARCH_ACTION) ;
  }
  
  public void changeAccount(UIAccount uiAccount) {
    getChildren().clear() ;
    getRows().clear() ;
    uiFolderInput_.clear() ;
    add(new HeaderRow().
        add(new Cell("#{UIAdvancedSearch.header.search-terms}").
            addColspan("2")));
    add(new Row().
        add(new LabelCell("#{UIAdvancedSearch.label.subject}")).
        add(new ComponentCell(this, subjectInput_)));
    add(new Row().
        add(new LabelCell("#{UIAdvancedSearch.label.from}")).
        add(new ComponentCell(this, fromInput_)));
    add(new Row().
        add(new LabelCell("#{UIAdvancedSearch.label.to}")).
        add(new ComponentCell(this, toInput_)));
    add(new Row().
        add(new LabelCell("#{UIAdvancedSearch.label.message-body}")).
        add(new ComponentCell(this, bodyInput_)));
    add(new HeaderRow().
        add(new Cell("#{UIAdvancedSearch.header.search-options}").
            addColspan("2")));
    add(new Row().
        add(new Cell("#{UIAdvancedSearch.label.search-in}").
            addColspan("2")));
    List folders = uiAccount.getFolders() ;
    for(int i = 0; i < folders.size(); i++) {
      Folder folder =(Folder) folders.get(i) ;
      UICheckBox uiFolder = new UICheckBox(folder.getName(), folder.getId());
      uiFolder.setSelect(true) ;
      add(new Row().
          add(new ListComponentCell().
              add(this, uiFolder).         
              add(folder.getLabel()).
              addColspan("2").addClazz("indent-2"))) ;
      uiFolderInput_.add(uiFolder) ;
    }
    
    add(new Row().
        add(new ListComponentCell().
            add(this, uiSearchByFlagInput_).
            add("#{UIAdvancedSearch.label.show-message-with-flag}").
            add(this , uiFlagInput_).addClazz("indent-2").
            addColspan("2"))) ;
    
    add(new Row().
        add(new ListComponentCell().
            add(this, uiSearchByDateRangeInput_).
            add("#{UIAdvancedSearch.label.received-sent-date-range}").
            addColspan("2").addClazz("indent-2"))) ;
    
    add(new Row().
        add(new ListComponentCell().
            add("#{UIAdvancedSearch.label.from-date}").
            add(this, uiFromDateImput_).
            addColspan("2").addClazz("indent-2"))) ;
    add(new Row().
        add(new ListComponentCell().
            add("#{UIAdvancedSearch.label.to-date}").
            add(this, uiToDateImput_).
            addColspan("2").addClazz("indent-2"))) ;
    add(new Row().
        add(new ListComponentCell().
            add("<div style='float: right'>").
            add(new FormButton("#{UIAdvancedSearch.button.search}", SEARCH_ACTION)).
            add("</div>").
            addColspan("2"))) ;
  }
  
  static public class SearchActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UIAdvancedSearch uiForm =  (UIAdvancedSearch) event.getSource();
      List inputs = new ArrayList() ;
      StringBuffer folderTerm = new StringBuffer() ;
      for(int i = 0; i < uiForm.uiFolderInput_.size() ; i++) {
        UICheckBox uiCheckBox = (UICheckBox) uiForm.uiFolderInput_.get(i) ;
        if(uiCheckBox.getSelect()) {
          if(folderTerm.length() > 0) folderTerm.append(" OR ") ;
          folderTerm.append(uiCheckBox.getValue()) ;
        }
      }
      if(folderTerm.length() == 0) {
        //should choose at least one box
        return ;
      }
      inputs.add(new SingleFieldSearchInput(MessageIndexerPlugin.FOLDER_ID_FIELD, folderTerm.toString())) ;
      String value = uiForm.subjectInput_.getValue() ;
      if(value.length() > 0) {
        inputs.add(new SingleFieldSearchInput(MessageIndexerPlugin.SUBJECT_FIELD, value)) ;
      }
      value = uiForm.fromInput_.getValue() ;
      if(value.length() > 0) {
        inputs.add(new SingleFieldSearchInput(MessageIndexerPlugin.FROM_FIELD, value)) ;
      }
      value = uiForm.toInput_.getValue() ;
      if(value.length() > 0) {
        inputs.add(new SingleFieldSearchInput(MessageIndexerPlugin.TO_FIELD, value)) ;
      }
      value = uiForm.bodyInput_.getValue() ;
      if(value.length() > 0) {
        inputs.add(new SingleFieldSearchInput(MessageIndexerPlugin.BODY_FIELD, value)) ;
      }
      
      if(uiForm.uiSearchByDateRangeInput_.getSelect()) {
        Date from = uiForm.uiFromDateImput_.getValue() ;
        Date to = uiForm.uiToDateImput_.getValue() ;
        inputs.add(new RangeFieldSearchInput(MessageIndexerPlugin.RECEIVED_DATE_FIELD, from, to)) ;
      }
      
      UISummary uiSummary = (UISummary) uiForm.getAncestorOfType(UISummary.class) ;
      uiSummary.advancedSearch(inputs, null) ;
    }
  } 
}