/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing.test;

import org.apache.lucene.document.Document;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.indexing.FileIndexerPlugin;
import org.exoplatform.services.indexing.IndexingService;
import org.exoplatform.services.indexing.Searcher;
import org.exoplatform.services.indexing.SingleFieldSearchInput;
import org.exoplatform.services.indexing.impl.FileIndexerPluginImpl;
import org.exoplatform.test.BasicTestCase;

/**
 * Thu, May 15, 2003 @   
 * @author: Tuan Nguyen
 * @version: $Id: TestIndexingService.java,v 1.7 2004/10/25 03:41:16 tuan08 Exp $
 * @since: 0.0
 * @email: tuan08@yahoo.com
 */
public class TestIndexingService  extends BasicTestCase {
  static private IndexingService service_ ;
  public TestIndexingService(String name) {
    super(name);
  }

  public void setUp() throws Exception {
    if(service_ == null) {
      PortalContainer pcontainer = PortalContainer.getInstance() ;
      service_ = (IndexingService) pcontainer.getComponentInstanceOfType(IndexingService.class) ;
    }
  }
 
  public void testIndexingService() throws Exception {
    FileIndexerPluginImpl plugin = 
      (FileIndexerPluginImpl)service_.getIndexerPlugin(FileIndexerPlugin.IDENTIFIER) ;
    String[] acceptExt = { "java" , "xml"} ;
    plugin.indexDirectory("src","guest", acceptExt , true) ;
    // wait to make sure that indexer thread finish its job 
    Thread.sleep(3500) ;
    Searcher searcher = plugin.getSearcher() ;
    SingleFieldSearchInput documentSearchInput = 
      new SingleFieldSearchInput(IndexingService.DOCUMENT_FIELD) ;
    documentSearchInput.setTerm("Tuan") ;
    //SingleFieldSearchInput[] inputs = { documentSearchInput } ;
    java.util.List hits =  searcher.search(documentSearchInput).getAll() ;
    Document foundDoc = null ; 
    for(int i = 0; i < hits.size(); i ++) {
      foundDoc = (Document )hits.get(i) ;
      System.out.println("F O U N D : " +  foundDoc.getField(IndexingService.IDENTIFIER_FIELD).stringValue()) ;
    }
    if(foundDoc != null ) {
      String objectId =  foundDoc.getField(IndexingService.IDENTIFIER_FIELD).stringValue() ;
      System.out.println(plugin.getObjectAsText(null, objectId)) ;
    }
    
    plugin.removeIndex() ;
    Thread.sleep(3500) ;
    searcher = plugin.getSearcher() ;
    hits =  searcher.search(documentSearchInput).getAll() ;
    assertEquals("exect no result is found", 0 , hits.size()) ;
    service_.optimizeDatabase() ;
  }
}