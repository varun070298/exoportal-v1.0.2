/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.       *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.communication.forum;

import java.util.List;

/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public interface ForumServiceContainer  {
  public ForumService findForumService(String owner) throws Exception;
  public ForumService createForumService(String owner) throws Exception;
  public List getForumOwners() throws Exception  ;
  
  public void addForumEventListener(ForumEventListener listener) ;
}
