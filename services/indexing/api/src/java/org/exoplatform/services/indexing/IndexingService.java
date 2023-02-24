/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

import java.util.* ;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Sept 14, 2004
 * Time: 1:12:22 PM
 */
public interface IndexingService  {
  final static public String PREFIX = "document-" ;
  final static public String IDENTIFIER_FIELD =  PREFIX + "identifier" ;
  final static public String AUTHOR_FIELD =  PREFIX + "author" ;
  final static public String MODULE_FIELD = PREFIX +  "module" ;
  final static public String TITLE_FIELD =    PREFIX + "title" ;
  final static public String DESCRIPTION_FIELD =    PREFIX + "description" ;
  final static public String DOCUMENT_FIELD = PREFIX +  "body" ;
  final static public String DOCUMENT_ACCESS_ROLE = PREFIX +  "accessRole" ;
  
  public Analyzer getAnalyzer() ;
  
  public String getIndexDatabaseLocation() ;
  
  public void addIndexerPlugin(IndexerPlugin plugin) ;
  public Collection getIndexerPlugins() throws Exception ;
  public IndexerPlugin getIndexerPlugin(String identifer) throws Exception ;
 
  public void queueUpdateDocument(Document document) throws Exception ;
  public void queueUpdateDocuments(List documents) throws Exception ;
  
  public void queueIndexDocument(Document document) throws Exception ;
  public void queueIndexDocuments(List documents) throws Exception ;
  
  public void queueDeleteDocuments(Term queryTerm) throws Exception ;
  
  public Searcher getSearcher() throws Exception ;
  
  public void optimizeDatabase() throws Exception ;
}