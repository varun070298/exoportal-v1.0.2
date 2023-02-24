/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 17, 2004
 * @version $Id: SearchInput.java,v 1.5 2004/10/14 23:27:57 tuan08 Exp $
 */
public interface SearchInput {
  //public boolean isRequired() ;
  public boolean hasTerm() ;
  public Query getQuery(Analyzer analyzer) throws Exception ;
}
