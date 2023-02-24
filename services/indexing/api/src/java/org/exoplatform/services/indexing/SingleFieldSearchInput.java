/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 12, 2004
 * @version $Id: SingleFieldSearchInput.java,v 1.2 2004/10/14 23:27:57 tuan08 Exp $
 */
public class SingleFieldSearchInput implements SearchInput {
  
  private String field_ ;
  private String term_ ;
  private boolean required_ = true ;
  
  public SingleFieldSearchInput(String field) {
    field_ = field ;
  }
  
  public SingleFieldSearchInput(String field, String term) {
    field_ = field ;
    term_ = term ;
  }
  
  public boolean isRequired() { return required_ ; }
  public void setRequired(boolean b) { required_ = b ; }
  
  public String getField() { return field_ ; }
  
  public boolean hasTerm() { 
    return term_ != null  && term_.length() > 0;
  }
  
  public String getTerm() { return term_ ; }
  public void   setTerm(String term) { term_ = term ; }
  
  public Query getQuery(Analyzer analyzer) throws Exception {
    return QueryParser.parse(term_ , field_, analyzer) ;
  }
}