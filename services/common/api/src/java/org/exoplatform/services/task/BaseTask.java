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
abstract public class BaseTask  implements Task {
  private PortalContainer pcontainer_ ;
  
  public BaseTask () {
    pcontainer_  = PortalContainer.getInstance() ;
  }
  
  public PortalContainer getPortalContainer() { return pcontainer_ ; }
}