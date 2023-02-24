/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

import java.util.Date ; 
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateField;
import org.apache.lucene.index.Term ;
import org.apache.lucene.search.* ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 12, 2004
 * @version $Id: RangeFieldSearchInput.java,v 1.1 2004/10/14 23:27:57 tuan08 Exp $
 */
public class RangeFieldSearchInput implements SearchInput {
  
  private String field_ ;
  private String lowerTerm_ ;
  private String upperTerm_ ;
  private boolean inclusive_ = true ;
  
  public RangeFieldSearchInput(String field, Date lowerTerm, Date upperTerm) {
    field_ = field ;
    if(lowerTerm != null) 
      lowerTerm_ = DateField.dateToString(lowerTerm) ;
    if(upperTerm != null)
      upperTerm_ = DateField.dateToString(upperTerm) ;
  }
  
  public RangeFieldSearchInput(String field, String lowerTerm, String upperTerm) {
    field_ = field ;
    lowerTerm_ = lowerTerm ;
    upperTerm_ = upperTerm ;
  }
  
  public String getField() { return field_ ; }
  
  public void setInclusive(boolean b) { inclusive_ = b ; }
  
  public boolean hasTerm() { 
    return (upperTerm_ != null  && upperTerm_.length() > 0) || lowerTerm_ != null  && lowerTerm_.length() > 0;
  }
  
  public Query getQuery(Analyzer analyzer) throws Exception {
    Term lower = null ;
    if(lowerTerm_ != null)lower = new  Term(field_, lowerTerm_) ;
    Term upper = null ;
    if(upperTerm_ != null) upper = new  Term(field_, upperTerm_) ;
    return  new RangeQuery(lower, upper, inclusive_) ;
  }
}