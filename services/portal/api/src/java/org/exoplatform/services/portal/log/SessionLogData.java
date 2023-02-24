/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.log;

import java.util.List;

/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Dec 2, 2004
 * @version $Id$
 */
public interface SessionLogData extends SessionLogDataDescription {
  public List getActionHistory() ;
  public void setActionHistory(List list) ;
}