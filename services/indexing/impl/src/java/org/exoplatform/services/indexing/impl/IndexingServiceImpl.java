/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing.impl;

import java.util.* ;
import java.io.File;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.exoplatform.container.configuration.*;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.apache.commons.logging.Log ;
import org.exoplatform.container.PortalContainer ;
import org.exoplatform.services.indexing.*;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.task.TaskService ;
import org.exoplatform.services.task.Task ;
import org.picocontainer.Startable;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Sept 14, 2004
 * Time: 1:12:22 PM
 */
public class IndexingServiceImpl  implements IndexingService , Startable, Task {
  final static private String UPDATE_ACTION = "update" ;
  final static private String ADD_ACTION = "add" ;
  final static private String DELETE_ACTION = "delete" ;
  
  
  private Analyzer analyzer_ ;
  private Map plugins_ ;
  private List queues_ ;
  private String indexDBLocation_ ;
  private Searcher searcher_ ;
  private Log log_ ;
  private int counter_ ;
  
  public IndexingServiceImpl(ConfigurationManager confService,
                             LogService lservice, 
                             TaskService tservice,
                             Analyzer analyzer) throws Exception {
    log_ = lservice.getLog(getClass()) ;
    analyzer_  = analyzer ;
    plugins_ = new HashMap() ;
    queues_ = new ArrayList(200) ;
    ServiceConfiguration sconf  = confService.getServiceConfiguration(IndexingService.class) ;
    ValueParam param = (ValueParam)sconf.getParameter("index.database.dir") ;
    indexDBLocation_ = param.getValue() ;
    if(confService.isDefault(indexDBLocation_)) {
      indexDBLocation_ = System.getProperty("java.io.tmpdir") + "/lucenedb" ;
    }
    File dir = new File(indexDBLocation_) ;
    if(dir.isFile()) {
      throw new Exception("Expect a directory, but " + indexDBLocation_ + " is a file") ;
    }
    
    if(!dir.exists())    dir.mkdir()  ;
    File segments = new File(indexDBLocation_ + "/segments") ;
    if(!segments.exists()) {
      IndexWriter writer = new IndexWriter(indexDBLocation_ , analyzer_, true);
      writer.optimize();
      writer.close();
    }
    tservice.queueRepeatTask(this) ;
  }
  
  public Analyzer getAnalyzer()  {  return analyzer_ ; }
  
  public String getIndexDatabaseLocation()  { return indexDBLocation_  ; }
  
  public Collection getIndexerPlugins() throws Exception {
    return plugins_.values() ;
  }
  
  public void addIndexerPlugin(IndexerPlugin plugin) {
    plugins_.put(plugin.getPluginIdentifier(), plugin) ;
  }
  
  public IndexerPlugin getIndexerPlugin(String identifier) throws Exception {
    IndexerPlugin plugin = (IndexerPlugin) plugins_.get(identifier) ;
    if(plugin == null) throw new Exception("Cannot find the plugin: "  + identifier) ;
    return plugin ;
  }
 
  synchronized public Searcher getSearcher() throws Exception {
    if(searcher_ == null) {
      IndexSearcher isearcher = new IndexSearcher(getIndexDatabaseLocation()) ;
      searcher_ = new Searcher(isearcher, analyzer_);
    }
    return searcher_ ;
  }
  
  synchronized public void queueUpdateDocument(Document document) throws Exception  {
    Term deleteQuery = 
      new Term(IDENTIFIER_FIELD, document.getField(IDENTIFIER_FIELD).stringValue());
    queues_.add(new Command(UPDATE_ACTION, deleteQuery, document)) ;
    activateIndexerThread() ;
  }
  
  synchronized public void queueUpdateDocuments(List documents) throws Exception  {
    for(int i = 0 ; i < documents.size(); i++) {
      Document document = (Document) documents.get(i) ;
      Term deleteQuery = 
        new Term(IDENTIFIER_FIELD, document.getField(IDENTIFIER_FIELD).stringValue());
      queues_.add(new Command(UPDATE_ACTION, deleteQuery, document)) ;
    }
    activateIndexerThread()  ;
  }
  
  synchronized public void queueIndexDocument(Document document) throws Exception  {
    queues_.add(new Command(ADD_ACTION, null, document)) ;
    activateIndexerThread() ;
  }
  
  synchronized public void queueIndexDocuments(List documents) throws Exception {
    for(int i = 0 ; i < documents.size(); i++) {
      Document document = (Document) documents.get(i) ;
      queues_.add(new Command(ADD_ACTION, null, document)) ;
    }
    activateIndexerThread() ;
  }
  
  synchronized public void queueDeleteDocuments(Term queryTerm) throws Exception  {
    queues_.add(new Command(DELETE_ACTION, queryTerm, null)) ;
    activateIndexerThread() ;
  }
  
  synchronized private List dequeue() throws Exception  {
    List tmp = queues_ ;
    queues_ = new ArrayList(200) ;
    return tmp ;
  }
  
  private void activateIndexerThread() {
  }
  
  private void runBatchCommand(List commands) throws Exception  {
    if(commands.size() == 0) return ;
    IndexReader reader = null ;
    //delete the existed documents in the index db
    for(int i = 0; i < commands.size(); i++) {
      Command entry = (Command) commands.get(i) ;
      if(entry.command_ ==  UPDATE_ACTION || entry.command_ == DELETE_ACTION) {
        if(reader == null) {
          reader = IndexReader.open(indexDBLocation_);
        }
        reader.delete(entry.deleteQuery_) ;
      }
    }
    if(reader  != null)  reader.close() ;
    //add indexed documents
    IndexWriter writer = new IndexWriter(indexDBLocation_ , analyzer_, false);
    for(int i = 0; i < commands.size(); i++) {
      Command entry = (Command) commands.get(i) ;
      if(entry.command_ ==  UPDATE_ACTION || entry.command_ == ADD_ACTION) {
        writer.addDocument(entry.document_);
      }
      counter_++ ;
    }
    writer.optimize();
    writer.close();
  }
  
  public void optimizeDatabase() throws Exception { 
    IndexWriter writer = new IndexWriter(indexDBLocation_ , analyzer_, false);
    writer.optimize();
    writer.close();
  }
  
  public void execute() {
    try {
      List queues = dequeue() ;
      while(queues.size() > 0) {
        runBatchCommand(queues) ;
        queues = dequeue() ;
      }
      Iterator i = plugins_.values().iterator() ;
      //ask the plugin update it searcher 
      while(i.hasNext()) {
        IndexerPlugin plugin = (IndexerPlugin) i.next() ;
        plugin.resetSearcher() ;
      }
      searcher_ = null ;
    } catch (Exception ex) {
      log_.error("Error while indexing the new document entries: ", ex) ;
    }
  }
  
  public String getName()  { return "IndexingServiceImpl"  ; }
  public String getDescription() { 
    return "index the data, there are " + queues_.size() + " in the queue";
  }
  
  public PortalContainer getPortalContainer() { return null ; }
  
  public void start() { }
  public void stop() { }
  
  private class Command {
    String command_ ;
    Term deleteQuery_ ;
    Document document_ ;
    
    public Command(String action, Term deleteQuery, Document doc) {
      command_ = action ;
      deleteQuery_ = deleteQuery ;
      document_ = doc ;
    }
  }
}
