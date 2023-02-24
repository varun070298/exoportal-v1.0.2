/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.message;

import org.exoplatform.services.indexing.IndexerPlugin;
import org.exoplatform.services.indexing.IndexingService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 12, 2004
 * @version $Id: MessageIndexerPlugin.java,v 1.2 2004/10/14 23:29:21 tuan08 Exp $
 */
public interface MessageIndexerPlugin extends IndexerPlugin {
  final static public String IDENTIFIER= "MessageIndexerPlugin"   ;
  final static public String SUBJECT_FIELD = IndexingService.TITLE_FIELD  ;
  final static public String BODY_FIELD = IndexingService.DOCUMENT_FIELD  ;
  final static public String FOLDER_ID_FIELD = "folderId"   ;
  final static public String ACCOUNT_ID_FIELD = "accountId"   ;
  final static public String TO_FIELD = "to" ;
  final static public String FROM_FIELD = "from" ;
  final static public String RECEIVED_DATE_FIELD = "receivedDate" ;
  final static public String FLAG_FIELD = "flag" ;
}