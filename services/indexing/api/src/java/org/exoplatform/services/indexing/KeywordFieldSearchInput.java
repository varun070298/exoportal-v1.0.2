/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term ;
import org.apache.lucene.search.* ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 12, 2004
 * @version $Id: KeywordFieldSearchInput.java,v 1.1 2004/09/17 23:38:59 tuan08 Exp $
 */
public class KeywordFieldSearchInput implements SearchInput {
  
  private String field_ ;
  private String term_ ;
  
  public KeywordFieldSearchInput(String field) {
    field_ = field ;
  }
  
  public KeywordFieldSearchInput(String field, String term) {
    field_ = field ;
    term_ = term ;
  }
  
  public String getField() { return field_ ; }
  
  public boolean hasTerm() { 
    return term_ != null  && term_.length() > 0;
  }
  
  public String getTerm() { return term_ ; }
  public void   setTerm(String term) { term_ = term ; }
  
  public Query getQuery(Analyzer analyzer) throws Exception {
    return  new TermQuery(new Term(field_, term_)) ;
  }
}