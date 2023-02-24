/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.monitor.session.component;

import java.util.ArrayList;
import java.util.List;
import org.exoplatform.faces.core.component.model.SelectItem;
import org.exoplatform.faces.search.component.UISearchBar;
import org.exoplatform.faces.search.component.UISearcher;
import org.exoplatform.services.portal.log.Query;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIContentSearcher.java,v 1.1 2004/10/25 03:48:11 tuan08 Exp $
 */
public class UISessionLogManager extends UISearcher {

  public UISessionLogManager() throws Exception {
    setId("UISearcher") ;
    setRendererType("ChildrenRenderer") ;
    List options = new ArrayList() ;
    options.add(new SelectItem("#{UISearchBar.label.search-all}", ""));
    options.add(new SelectItem("#{UISearchBar.label.session-owner}", "sessionOwner"));
    options.add(new SelectItem("#{UISearchBar.label.remote-user}","remoteUser"));
    options.add(new SelectItem("#{UISearchBar.label.ip-address}", "ipAddress"));
    options.add(new SelectItem("#{UISearchBar.label.client-type}", "clientName"));
    UISearchBar uiBar =  new UISearchBar();
    uiBar.setSearchOptions(options) ;
    getChildren().add(uiBar) ;
    addChild(UIListSessionLog.class) ;
    addChild(UISearchLogForm.class).setRendered(false) ;
    addChild(UIActionHistory.class).setRendered(false) ;   
  }
 
  public void showAdvancedSearch() throws Exception {
    setRenderedComponent(UISearchLogForm.class) ;
  }
  
  public void quickSearch(String term, List fields) throws Exception  {
    Query query = new Query() ;
    if(term != null && term.length() > 0 && fields.size() == 1) {
      String field = (String) fields.get(0) ;
      if("sessionOwner".equals(field)) query.setSessionOwner(term) ;
      else if("remoteUser".equals(field))query.setRemoteUser(term) ;
      else if("ipAddress".equals(field))query.setIPAddress(term) ;
      else if("clientName".equals(field))query.setClientType(term) ;
    }
    UIListSessionLog uiList = 
      (UIListSessionLog) getChildComponentOfType(UIListSessionLog.class) ;
    uiList.update(query) ;
    setRenderedComponent(UIListSessionLog.class) ;
  }
}