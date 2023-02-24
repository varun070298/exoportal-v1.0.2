/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import java.util.List ;
import org.exoplatform.services.portal.model.*;
/**
 * Jun 4, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $ID$
 **/
public class Backup {
	private PortalConfig portalConfig;
	private List pages ;
  private NodeNavigation nodeNavigation ;
	
	public Backup(PortalConfig config, List pages, NodeNavigation nv) {
		portalConfig = config ;
		this.pages = pages ;
    nodeNavigation = nv ;
	}
	
	public PortalConfig getPortalConfig() { return portalConfig ; }
	
	public List getPages() { return pages ; }
  
  public NodeNavigation getNodeNavigation() { return nodeNavigation ; }
}