/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.log;

import org.exoplatform.commons.utils.PageList ;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 2, 2004
 * @version $Id$
 */
public interface PortalLogService {
  public SessionLogData getSessionLogData(String id) throws Exception ;
  public void  saveSessionLogData(SessionLogData data) throws Exception ;
  public PageList getSessionLogDatas(Query q) throws Exception ;
}
