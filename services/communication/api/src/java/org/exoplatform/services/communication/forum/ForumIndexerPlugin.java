/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum;

import org.exoplatform.services.indexing.IndexerPlugin;
import org.exoplatform.services.indexing.IndexingService;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Sep 12, 2004
 * @version $Id: ForumIndexerPlugin.java,v 1.2 2004/10/16 21:27:02 tuan08 Exp $
 */
public interface ForumIndexerPlugin extends IndexerPlugin {
  final static public String IDENTIFIER= "ForumIndexerPlugin"   ;
  final static public String AUTHOR_FIELD = IndexingService.AUTHOR_FIELD ;
  final static public String SUBJECT_FIELD = IndexingService.TITLE_FIELD  ;
  final static public String BODY_FIELD = IndexingService.DOCUMENT_FIELD  ;
  final static public String CATEGORY_ID_FIELD = "categoryId"   ;
  final static public String FORUM_ID_FIELD = "forumId"   ;
  final static public String TOPIC_ID_FIELD = "topicId"   ;
  final static public String POST_ID_FIELD = "postId"   ;
  final static public String DATE_FIELD = "date" ;
}