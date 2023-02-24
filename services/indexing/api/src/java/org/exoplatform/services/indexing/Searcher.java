/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 13, 2004
 * @version $Id: Searcher.java,v 1.9 2004/10/30 02:29:14 tuan08 Exp $
 */
public class Searcher {
  private org.apache.lucene.search.Searcher searcher_ ;
  private Query modules_ ;
  private Analyzer analyzer_ ;
  private Hits lastSearchResult_ ;
  
  public Searcher(IndexSearcher searcher,  
                  Analyzer analyzer) throws Exception {
    searcher_ = searcher;
    analyzer_ = analyzer ;
  }
  
  public void setQueryModules(Query query) {
    modules_ = query ;
  }
  
  public Hits getLastSearchResult() { return lastSearchResult_ ; }
  
  public HitPageList search(SearchInput input) throws Exception {
    BooleanQuery query = new BooleanQuery() ;
    if(modules_ != null) {
      query.add(modules_, true, false) ;
    }
    
    Query subQuery = input.getQuery(analyzer_) ;
    query.add(subQuery, true, false) ;
    lastSearchResult_ = searcher_.search(query)   ;
    lastSearchResult_ = searcher_.search(query)   ;
    return new HitPageList(this) ;
  }
  
  public HitPageList search(List inputs) throws Exception {
    BooleanQuery query = new BooleanQuery() ;
    if(modules_ != null) {
      query.add(modules_, true, false) ;
    }
    for(int i = 0; i < inputs.size(); i++) {
      SearchInput input = (SearchInput) inputs.get(i) ;
      if(input.hasTerm()) {
        Query subQuery = input.getQuery(analyzer_) ;
        query.add(subQuery, true, false) ;
      }
    }
    lastSearchResult_ = searcher_.search(query)   ;
    return new HitPageList(this) ;
  }
  
  public HitPageList search(SearchInput input, List modules) throws Exception {
    BooleanQuery query = new BooleanQuery() ;
    if(modules != null  &&  modules.size() > 0) {
      Query modulesQuery = createModuleQuery(modules);
      query.add( modulesQuery, true, false) ;
    }
    
    Query subQuery = input.getQuery(analyzer_) ;
    query.add(subQuery, true, false) ;
    lastSearchResult_ = searcher_.search(query)   ;
    return new HitPageList(this) ;
  }
  
  
  public HitPageList search(List inputs, List modules) throws Exception {
    BooleanQuery query = new BooleanQuery() ;
    if(modules != null  &&  modules.size() > 0) {
      Query modulesQuery = createModuleQuery(modules);
      query.add( modulesQuery, true, false) ;
    }
    
    for(int i = 0; i < inputs.size(); i++) {
      SearchInput input = (SearchInput) inputs.get(i) ;
      if(input.hasTerm()) {
        Query subQuery = input.getQuery(analyzer_) ;
        query.add(subQuery, true, false) ;
      }
    }
    lastSearchResult_ = searcher_.search(query)   ;
    return new HitPageList(this) ;
  }
  
  private Query createModuleQuery(List modules) {
    BooleanQuery modulesQuery = new BooleanQuery() ;
    for(int i = 0 ; i < modules.size(); i++) {
      String module = (String) modules.get(i) ;
      Query moduleQuery = new TermQuery(new Term(IndexingService.MODULE_FIELD, module)) ;
      modulesQuery.add(moduleQuery, false, false) ;
    }
    return modulesQuery; 
  }
  
  protected void finalize() {
    try {
      searcher_.close() ;
    } catch (Exception ex) {
      ex.printStackTrace() ;
    }
  }
}