/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.picocontainer.Startable;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 12, 2004
 * @version $Id: BaseIndexerPlugin.java,v 1.6 2004/10/14 23:27:57 tuan08 Exp $
 */
abstract public class BaseIndexerPlugin implements IndexerPlugin, Startable   {
  final static public String[] MANDATORY_FIELDS = 
    {IndexingService.IDENTIFIER_FIELD,  IndexingService.MODULE_FIELD, 
     IndexingService.TITLE_FIELD, IndexingService.DOCUMENT_FIELD};
  
  private String[] indexField_ ;
  private Searcher searcher_  ;
  protected IndexingService iservice_ ;
  
  public BaseIndexerPlugin(IndexingService iservice) {
    iservice.addIndexerPlugin(this) ;
    iservice_ = iservice ;
  }
  
  
  abstract public String getPluginIdentifier()  ;
  
  final public String[] getMandatoryIndexFields() { return MANDATORY_FIELDS ; }  
  public String[] getCustomizedIndexFields() { return indexField_ ; }  
  
  synchronized public Searcher getSearcher() throws Exception {
    if(searcher_ == null) {
      IndexSearcher isearcher = new IndexSearcher(iservice_.getIndexDatabaseLocation()) ;
      searcher_ = new Searcher(isearcher, iservice_.getAnalyzer());
      searcher_.setQueryModules(new TermQuery(new Term(IndexingService.MODULE_FIELD, getPluginIdentifier()))) ;
    }
    return searcher_ ;
  }
  
  synchronized public void resetSearcher() {
    searcher_ = null ;
  }
  
  protected  Document createBaseDocument(String identifier, String author, 
                                         String title,  String description, 
                                         String textToIndex, String viewRole) {
    Document doc = new Document();
    if(description == null) {
      if(textToIndex.length() < 50 ) description = textToIndex ;
      else description = textToIndex.substring(0, 50) ;
    }
    if(viewRole == null)  viewRole = "owner" ;
    doc.add(Field.Keyword(IndexingService.IDENTIFIER_FIELD, identifier)) ;
    doc.add(Field.Keyword(IndexingService.MODULE_FIELD, getPluginIdentifier())) ;
    doc.add(Field.Keyword(IndexingService.AUTHOR_FIELD, author)) ;
    doc.add(Field.Text(IndexingService.TITLE_FIELD, title)) ;
    doc.add(Field.Text(IndexingService.DESCRIPTION_FIELD, description)) ;
    doc.add(Field.UnStored(IndexingService.DOCUMENT_FIELD, textToIndex)) ;
    doc.add(Field.UnStored(IndexingService.DOCUMENT_ACCESS_ROLE, viewRole)) ;
    return doc ;
  }
  
  public void removeIndex() throws Exception {
    Term term = new Term(IndexingService.MODULE_FIELD, getPluginIdentifier()) ;
    iservice_.queueDeleteDocuments(term) ;
  }
  
  public void reindex() throws Exception {
    removeIndex() ;
  }
  
  protected String getContentDescription(String text, int size) {
    char[] chars = text.toCharArray() ;
    StringBuffer b = new StringBuffer() ;
    int counter = 0 ;
    for(int i = 0 ; i < chars.length && counter < size; i++ ) {
      if(chars[i] == '<') {
        while(chars[i] != '>' && i < chars.length) {
          i++ ;
        }
        b.append(" - ") ;
      } else {
        b.append(chars[i]) ;
        counter++ ;
      }
    }
    return b.toString() ;
  }
  
  public void start() {}
   
  public void stop() {} 
}