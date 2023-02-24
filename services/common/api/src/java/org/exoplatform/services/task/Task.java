/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.task;

import org.exoplatform.container.PortalContainer; 
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 30, 2004
 * @version $Id$
 */
public interface Task {
  public void execute() throws Exception ;
  public String getName() ;
  public String getDescription() ;
  public PortalContainer getPortalContainer() ;
}