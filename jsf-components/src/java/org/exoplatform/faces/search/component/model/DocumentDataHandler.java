/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.search.component.model;

import org.apache.lucene.document.Document ;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.faces.core.component.model.PageListDataHandler;
import org.exoplatform.services.indexing.HitPageList;
import org.exoplatform.services.indexing.IndexingService;


/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 23, 2004
 * @version $Id: DocumentDataHandler.java,v 1.3 2004/11/01 20:15:12 tuan08 Exp $
 */
public class DocumentDataHandler  extends PageListDataHandler {
  private Document doc_  ;
  private float score_ ;
  private HitPageList hits_ ;
  
  public String  getData(String fieldName)   {
    return "N/A" ;
  }
  
  public void setPageList(PageList pageList) throws Exception { 
    hits_ = (HitPageList)pageList ;
    super.setPageList(pageList) ;
  }
  
  public String getTitle() { return doc_.get(IndexingService.TITLE_FIELD) ; }
  public String getDescription() { return doc_.get(IndexingService.DESCRIPTION_FIELD) ; }
  public String getAuthor() { return doc_.get(IndexingService.AUTHOR_FIELD) ; }
  public String getDocumentId() { return doc_.get(IndexingService.IDENTIFIER_FIELD) ; }
  public String getModule() { return doc_.get(IndexingService.MODULE_FIELD) ; }
  public float getScore() { return score_ ; }
  
  public void setCurrentObject(Object o) { 
    doc_ = (Document) o; 
    score_ = hits_.getScoreOfDocumentInPage(getCurrentRow()) ;
  }
}