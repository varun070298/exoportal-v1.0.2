/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.indexing;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 12, 2004
 * @version $Id: FileIndexerPlugin.java,v 1.3 2004/09/17 16:59:42 tuan08 Exp $
 */
public interface FileIndexerPlugin extends IndexerPlugin {
  final static public String IDENTIFIER= "FileIndexerPlugin"   ;
  final static public String BASE_DIR_FIELD= "basedir"   ;
  
  public void indexDirectory(String directory, String acessRole, 
                             String[] acceptExt, boolean recursive) throws Exception ;
  public void reindexDirectory(String directory, String accessRole, 
                               String[] acceptExt, boolean recursive) throws Exception ;

}