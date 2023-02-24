/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.content.search.component;

import java.util.*;
import org.exoplatform.faces.search.component.UISearchSummary;
import org.exoplatform.faces.search.component.UISearcher;
import org.exoplatform.services.indexing.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIContentSearcher.java,v 1.1 2004/10/25 03:48:11 tuan08 Exp $
 */
public class UIContentSearcher extends UISearcher {
  private IndexingService service_ ;
  private List modules_ ;
  public UIContentSearcher(IndexingService service) throws Exception {
    super(service) ;
  	setId("UISearcher") ;
    service_ = service ;
    modules_ = new ArrayList() ;
    Iterator i = service.getIndexerPlugins().iterator();
    while(i.hasNext()) {
      IndexerPlugin plugin =(IndexerPlugin) i.next() ;
      modules_.add(plugin.getPluginIdentifier());
    }
  }
  
  public Searcher getSearcher()  throws Exception {
    Searcher searcher = service_.getSearcher() ;
    return searcher ;
  }
  
  public void quickSearch(String term, List fields) throws Exception  {
    MultipleFieldSearchInput searchInput  =  new MultipleFieldSearchInput(fields)  ;
    searchInput.setTerm(term) ;
    Searcher searcher = getSearcher()  ;
    HitPageList result = searcher.search(searchInput, modules_) ;
    UISearchSummary uiSummary = 
      (UISearchSummary)this.getChildComponentOfType(UISearchSummary.class) ;
    uiSummary.setSearchResult(result) ;
    this.setRenderedComponent(UISearchSummary.class) ;
  }
}