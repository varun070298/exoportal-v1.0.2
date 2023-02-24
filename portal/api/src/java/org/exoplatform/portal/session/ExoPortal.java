/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portal.session;

import javax.faces.event.FacesEvent;
import org.exoplatform.services.portal.model.Node;
/**
 * Apr 26, 2004
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: PortalInfo.java,v 1.5 2004/09/28 18:19:20 tuan08 Exp $
 **/
public interface ExoPortal {
  final static public String XHTML_MIME_TYPE = "text/xhtml" ;
  final static public String XHTMLMP_MIME_TYPE = "application/vnd.wap.xhtml+xml" ;
  
  final static public int PORTAL_VIEW_MODE = 0 ;
  final static public int PORTAL_EDIT_PAGE_MODE = 1 ;
  final static public int PORTAL_EDIT_MODE = 2 ;
  final static public int PORTAL_EDIT_NAVIGATION_MODE = 3 ;
  
  public Node getRootNode()  ;
  public Node getSelectedNode()   ;
  public void setSelectedNode(Node node) ;
  
  public boolean hasEditPortalCapability() ;
  public boolean hasEditNavigationCapability() ;
  
  public int   getPortalMode() ;
  public void  setPortalMode(int mode) ; 
  
  public void queueEvent(FacesEvent event) ;
}