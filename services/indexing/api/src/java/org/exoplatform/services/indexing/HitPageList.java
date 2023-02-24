/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.search.Hits;
import org.apache.lucene.document.Document;
import org.exoplatform.commons.utils.PageList;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 21, 2004
 * @version $Id: HitPageList.java,v 1.4 2004/11/01 21:06:50 tuan08 Exp $
 */
public class HitPageList extends PageList {
 
  private Hits hits_  ;
  private Searcher searcher_ ;
  private float[] scores_ ;
  
  public HitPageList(Searcher searcher, int pageSize) throws Exception {
    super(pageSize) ;
    scores_ = new float[pageSize] ;
    searcher_ = searcher ;
    hits_ = searcher.getLastSearchResult() ;
    setAvailablePage(hits_.length()) ;
  }
  
  public HitPageList(Searcher searcher) throws Exception {
    this(searcher, 15) ;
  }
  
  public void setPageSize(int pageSize) {
    scores_ = new float[pageSize] ;
    super.setPageSize(pageSize) ;
  }
  
  protected void populateCurrentPage(int page) throws Exception  {
    int from = getFrom() ;
    int to = getTo() ;
    currentListPage_ = new ArrayList(to - from) ;
    for(int i = from ; i < to ; i++ ) {
      currentListPage_.add(hits_.doc(i)) ;
      scores_[i - from] = hits_.score(i) ;
    }
  }
  
  public List getAll() throws Exception  {
    int available = getAvailable() ;
    List list = new ArrayList(available) ;
    for(int i = 0; i < available ; i++ ) {
      list.add(hits_.doc(i)) ;
    }
    return list ;
  }
  
  public Document getDocumentInPage(int idx)  {
    return (Document)currentListPage_.get(idx) ;
  }
  
  public float getScoreOfDocumentInPage(int idx) { return scores_[idx] ; }
  
  public Document getDocument(int idx) throws Exception {
    return hits_.doc(idx) ;
  }
  
  public Searcher getSearcher()  { return searcher_ ;  }
}