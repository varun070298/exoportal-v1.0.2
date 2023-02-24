/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing.impl;

import java.io.* ;
import java.util.* ;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.commons.utils.io.FileFilterByExtension;
import org.exoplatform.services.indexing.*;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 13, 2004
 * @version $Id: FileIndexerPluginImpl.java,v 1.7 2004/10/25 03:41:16 tuan08 Exp $
 */
public class FileIndexerPluginImpl 
  extends BaseIndexerPlugin implements FileIndexerPlugin {
  
  public FileIndexerPluginImpl(IndexingService iservice) {
    super(iservice) ;
  }
  
  public Document createDocument(File file, String baseDir, String accessRole)  throws Exception {
    String identifier = file.getAbsolutePath() ;
    String title = file.getName() ;
    String textToIndex = IOUtil.getFileContenntAsString(file) ;
    String desc = getContentDescription(textToIndex, 200) ;
    Document doc = createBaseDocument(identifier,"N/A" ,title, desc , textToIndex, accessRole) ;
    doc.add(Field.Keyword(BASE_DIR_FIELD, baseDir)) ;
    return doc ;
  }
  
  public String getPluginIdentifier() { return IDENTIFIER  ; }
  
  public Object getObject(String user, String objectId) throws Exception {
    return null ;
  }
  
  public String getObjectAsText(String user, String objectId) throws Exception {
    String content = IOUtil.getFileContenntAsString(objectId) ;
    return content ;
  }
  
  public String getObjectAsXHTML(String user, String objectId) throws Exception  {
    String content = IOUtil.getFileContenntAsString(objectId) ;
    return content ;
  }
  
  public String getObjectAsXML(String user, String objectId) throws Exception  {
    String content = IOUtil.getFileContenntAsString(objectId) ;
    return content ;
  }
  
  public void reindexDirectory(String directory, String accessRole,
                               String[] acceptExt, boolean recursive) throws Exception {
    Term term = new Term(BASE_DIR_FIELD, directory) ;
    iservice_.queueDeleteDocuments(term) ;
    indexDirectory(directory, accessRole, acceptExt, recursive) ;
  }
  
  public void indexDirectory(String directory, String accessRole, 
                             String[] acceptExt, boolean recursive) throws Exception {
    FileFilterByExtension filter = new FileFilterByExtension(acceptExt, recursive) ;
    File dir = new File(directory) ;
    if(!dir.exists() || dir.isFile()) {
      throw new Exception(directory + " is not a valid directory.") ;
    }
    traverse(dir, filter, directory, accessRole) ;
  }
  
  private void traverse(File file, FileFilter filter, String baseDir, String accessRole) throws Exception {
    File[] files = file.listFiles(filter) ;
    List documents = new ArrayList() ;
    for(int i = 0; i < files.length; i++) {
      if(files[i].isFile()) {
        documents.add(createDocument(files[i], baseDir, accessRole)) ;
      } else {
        traverse(files[i], filter, baseDir, accessRole) ;
      }
    }
    if(documents.size() > 0) {
      iservice_.queueIndexDocuments(documents) ;
    }
  }
}