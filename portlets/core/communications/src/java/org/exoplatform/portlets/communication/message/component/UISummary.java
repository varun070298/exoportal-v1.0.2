/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.message.component;

import java.util.* ;
import javax.faces.component.UIComponent;
import org.apache.lucene.document.Document;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.faces.core.component.model.SelectItem;
import org.exoplatform.faces.search.component.UISearchBar;
import org.exoplatform.faces.search.component.UISearcher;
import org.exoplatform.services.communication.message.*;
import org.exoplatform.services.indexing.IndexingService;
import org.exoplatform.services.indexing.Searcher;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UISummary.java,v 1.4 2004/10/30 02:22:19 tuan08 Exp $
 */
public class UISummary extends UISearcher {
  private  MessageIndexerPlugin plugin_ ; 
 
  public UISummary(IndexingService service, 
                   UIMessages uiMessages) throws Exception {
    super(service) ;
  	setId("UISearcher") ;
    plugin_ =  (MessageIndexerPlugin)service.getIndexerPlugin(MessageIndexerPlugin.IDENTIFIER) ;
    getChildren().add(uiMessages) ;
    this.setRenderedComponent(UIMessages.class) ;
  }
  
  public Searcher getSearcher()  throws Exception {
    Searcher searcher = plugin_.getSearcher() ;
    return searcher ;
  }
  
  protected UIComponent customizeUIAdvancedSearch(IndexingService service) throws Exception {
    MessageService mservice =
      (MessageService) PortalContainer.getInstance().getComponentInstanceOfType(MessageService.class) ;
    return new UIAdvancedSearch(mservice, service);
  }
  
  protected UIComponent customizeUISearchBar(IndexingService service) throws Exception {
    UISearchBar uiBar =  new UISearchBar();
    List options = new ArrayList() ;
    options.add(new SelectItem("Search All", ""));
    options.add(new SelectItem("By Subject", MessageIndexerPlugin.SUBJECT_FIELD));
    options.add(new SelectItem("Message Body", MessageIndexerPlugin.BODY_FIELD));
    uiBar.setSearchOptions(options) ;
    return uiBar ;
  }
  
  public void showAdvancedSearch() throws Exception {
    this.setRenderedComponent(UIAdvancedSearch.class) ;
  }
  
  public void viewDocument(Document doc) throws Exception  {
    String massageId = doc.getField(IndexingService.IDENTIFIER_FIELD).stringValue() ;
    Message message  =  (Message)plugin_.getObject(null, massageId);
    UIViewMessage uiView = (UIViewMessage) getSibling(UIViewMessage.class) ;
    uiView.setMessage(message) ;
    setRenderedSibling(UIViewMessage.class) ;
  }
  
  public void changeAccount(UIAccount uiAccount) {
    UIAdvancedSearch uiASearch = 
      (UIAdvancedSearch) getChildComponentOfType(UIAdvancedSearch.class);
    uiASearch.changeAccount(uiAccount) ;
  }
  
  public void changeFolder(Folder folder) throws Exception {
    UIMessages uiMessages = 
      (UIMessages)getChildComponentOfType(UIMessages.class) ;
    uiMessages.changeFolder(folder) ;
    setRenderedComponent(UIMessages.class) ;
  }
  
  public void update() throws Exception {
    UIMessages uiMessages = (UIMessages)getChildComponentOfType(UIMessages.class) ;
    uiMessages.update() ;
  }
}