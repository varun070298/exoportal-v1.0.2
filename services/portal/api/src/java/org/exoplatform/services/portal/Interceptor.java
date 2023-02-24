/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal;

import org.exoplatform.services.portal.model.*;

/**
 * Jul 15, 2004 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: Interceptor.java,v 1.2 2004/07/20 12:41:07 tuan08 Exp $
 */
public interface Interceptor {
  public PortalConfig getPortalConfig(String owner) throws Exception ;
  public void savePortalConfig(PortalConfig config) throws Exception  ;
  
  public Page getPage(String pageId) throws Exception ;
  public void  savePage(Page page) throws Exception  ;
  
  public void saveNodeNavigation(String owner , Node node) throws Exception ;
  public Node getNodeNavigation(String owner) throws Exception ;
}