/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.model;

import java.util.List ;
/**
 * May 13, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Layout.java,v 1.1 2004/05/16 20:15:48 tuan08 Exp $
 **/
public class Layout {
  public List containers_ ;
  
  public List getContainers() { return containers_ ; }
  public void setContainers(List list) { containers_ = list ; }
}
