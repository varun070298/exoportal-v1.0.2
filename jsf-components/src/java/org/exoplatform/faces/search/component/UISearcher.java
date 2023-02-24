/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.search.component;

import java.util.* ;
import javax.faces.component.UIComponent ;
import org.apache.lucene.document.Document;
import org.exoplatform.faces.core.component.UIExoComponentBase;
import org.exoplatform.faces.core.component.model.SelectItem;
import org.exoplatform.faces.search.component.model.DocumentDataHandler;
import org.exoplatform.services.indexing.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UISearcher.java,v 1.2 2004/10/30 02:29:51 tuan08 Exp $
 */
abstract public class UISearcher extends UIExoComponentBase {
  final static public String SEARCH_SUMMARY_COMPONENT = "UISearchSummary" ;
  
  public UISearcher() throws Exception {
  }
  
  public UISearcher(IndexingService service) throws Exception {
  	init(service) ;
  }
  
  protected void init(IndexingService service) throws Exception {
    setId("UISearcher") ;
    setRendererType("ChildrenRenderer") ;
    List children = getChildren()  ;
    UIComponent uiSearchBar = customizeUISearchBar(service) ;
    children.add(uiSearchBar) ;
    UIComponent uiSearchSummary = customizeUISearchSummary(service) ;
    children.add(uiSearchSummary) ;
    UIComponent uiViewDocument = customizeUIViewDocument(service) ;
    uiViewDocument.setRendered(false) ;
    children.add(uiViewDocument) ;
    UIComponent uiASearch = customizeUIAdvancedSearch(service) ;
    uiASearch.setRendered(false) ;
    children.add(uiASearch) ;
  }
  
  protected UIComponent customizeUISearchBar(IndexingService service) throws Exception {
    List options = new ArrayList() ;
    options.add(new SelectItem("#{UISearchBar.label.search-all}", ""));
    options.add(new SelectItem("#{UISearchBar.label.search-title}",IndexingService.TITLE_FIELD));
    options.add(new SelectItem("#{UISearchBar.label.search-desc}",IndexingService.DESCRIPTION_FIELD));
    options.add(new SelectItem("#{UISearchBar.label.search-document}",IndexingService.DOCUMENT_FIELD));
    options.add(new SelectItem("#{UISearchBar.label.search-author}",IndexingService.AUTHOR_FIELD));
    UISearchBar uiBar =  new UISearchBar();
    uiBar.setSearchOptions(options) ;
    return uiBar ;
  }
  
  protected UIComponent customizeUIAdvancedSearch(IndexingService service) throws Exception {
    return new UIAdvancedSearch(service);
  }
  
  protected UIComponent customizeUISearchSummary(IndexingService service) throws Exception {
    return new UISearchSummary(new DocumentDataHandler()) ;
  }
  
  protected UIComponent customizeUIViewDocument(IndexingService service) throws Exception {
    return new UIViewDocument(service);
  }
  
  public Searcher getSearcher() throws Exception  {
    return null ;
  }
  
  public void showAdvancedSearch() throws Exception {
    setRenderedComponent(UIAdvancedSearch.class) ;
  }
  
  public void quickSearch(String term, List fields) throws Exception  {
    MultipleFieldSearchInput searchInput  =  new MultipleFieldSearchInput(fields)  ;
    searchInput.setTerm(term) ;
    Searcher searcher = getSearcher()  ;
    HitPageList result = searcher.search(searchInput) ;
    UISearchSummary uiSummary = 
      (UISearchSummary)this.getChildComponentOfType(UISearchSummary.class) ;
    uiSummary.setSearchResult(result) ;
    setRenderedComponent(UISearchSummary.class) ;
  }
  
  public void advancedSearch(List searchInputs, List searchModules) throws Exception  {
    Searcher searcher = getSearcher()  ;
    HitPageList result  = searcher.search(searchInputs, searchModules) ;
    UISearchSummary uiSummary = 
      (UISearchSummary)this.getChildComponentOfType(UISearchSummary.class) ;
    uiSummary.setSearchResult(result) ;
    setRenderedComponent(UISearchSummary.class) ;
  }
  
  public void viewDocument(Document doc) throws Exception  {
    UIViewDocument uiViewDocument = 
      (UIViewDocument)this.getChildComponentOfType(UIViewDocument.class) ;
    uiViewDocument.setDocument(doc) ;
    setRenderedComponent(UIViewDocument.class) ;
  }
}