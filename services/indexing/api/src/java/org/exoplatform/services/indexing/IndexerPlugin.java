/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 12, 2004
 * @version $Id: IndexerPlugin.java,v 1.7 2004/11/01 15:07:51 tuan08 Exp $
 */
public interface IndexerPlugin {
  final static public String[] MANDATORY_FIELDS = 
    {IndexingService.IDENTIFIER_FIELD,  IndexingService.MODULE_FIELD, 
     IndexingService.TITLE_FIELD, IndexingService.DOCUMENT_FIELD};
  
  final static public String UNSYNCHRONIZED_DATABASE = 
    "You usually have this message when your database and lucence indexed database are not " +
    "synchrnonized. The problem is occured when you use a new database or other db tool to delete " +
    "a record from database while you still using the old lucence indexed database. To solve " +
    "the problem , you should login as the admin and reindex the one all the services";
  
  public String getPluginIdentifier()  ;
  
  public String[] getMandatoryIndexFields() ;  
  public String[] getCustomizedIndexFields()  ;  
  
  public Searcher getSearcher() throws Exception  ;
  
  public void resetSearcher() ;
  
  public Object getObject(String user, String objectId) throws Exception ;
  public String getObjectAsText(String user, String objectId) throws Exception ;
  public String getObjectAsXHTML(String user, String objectId) throws Exception ;
  public String getObjectAsXML(String user, String objectId) throws Exception ;
  
  
  public void removeIndex() throws Exception ;
  
  public void reindex() throws Exception  ; 
}