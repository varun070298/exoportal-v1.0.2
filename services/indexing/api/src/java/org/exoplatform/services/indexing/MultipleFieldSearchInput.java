/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

import  java.util.*  ;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser ;
import org.apache.lucene.search.* ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 12, 2004
 * @version $Id: MultipleFieldSearchInput.java,v 1.2 2004/09/18 19:20:55 tuan08 Exp $
 */
public class MultipleFieldSearchInput implements SearchInput {
  private List field_ ;
  private String term_ ;
  private boolean matchAllFields_ = false ;
  
  public MultipleFieldSearchInput(List field) {
    field_ = field ;
  }
  
  public List getField() { return field_ ; }
  public void   setField(List field) { field_ = field ; } 
  
  public String getTerm() { return term_ ; }
  public void   setTerm(String term) { term_ = term ; }
  
  public boolean isMatchAllFields() { return matchAllFields_ ; }
  public void    setMatchAllFields(boolean b) { matchAllFields_ = b ; }
  
  public boolean hasTerm() { 
    return term_ != null  && term_.length() > 0;
  }
  
  
  public Query getQuery(Analyzer analyzer) throws Exception {
    BooleanQuery bquery = new BooleanQuery() ;
    for(int i = 0; i < field_.size(); i++) {
      String field = (String)field_.get(i) ;
      Query fieldquery =  QueryParser.parse(term_ , field , analyzer) ;
      bquery.add(fieldquery, matchAllFields_, false) ;
    }
    return bquery ;
  }
}