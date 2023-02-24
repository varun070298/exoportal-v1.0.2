/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.user.component;

import java.util.ArrayList;
import java.util.List;
import org.exoplatform.faces.core.component.model.SelectItem;
import org.exoplatform.faces.search.component.UISearchBar;
import org.exoplatform.faces.search.component.UISearcher;
import org.exoplatform.services.organization.Query;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIContentSearcher.java,v 1.1 2004/10/25 03:48:11 tuan08 Exp $
 */
public class UIUserManager extends UISearcher {

  public UIUserManager() throws Exception {
    setId("UISearcher") ;
    setRendererType("ChildrenRenderer") ;
    List options = new ArrayList() ;
    options.add(new SelectItem("#{UISearchBar.label.search-all}", ""));
    options.add(new SelectItem("#{UISearchBar.label.username}", "username"));
    options.add(new SelectItem("#{UISearchBar.label.first-name}","firstname"));
    options.add(new SelectItem("#{UISearchBar.label.last-name}", "lastname"));
    options.add(new SelectItem("#{UISearchBar.label.email}", "email"));
    UISearchBar uiBar =  new UISearchBar();
    uiBar.setSearchOptions(options) ;
    getChildren().add(uiBar) ;
    addChild(UIListUser.class) ;
    addChild(UISearchUserForm.class).setRendered(false) ;
    addChild(UIUserProfileSummary.class).setRendered(false) ;
    addChild(UIUserInfo.class).setRendered(false) ;
  }
 
  public void showAdvancedSearch() throws Exception {
    setRenderedComponent(UISearchUserForm.class) ;
  }
  
  public void quickSearch(String term, List fields) throws Exception  {
    Query query = new Query() ;
    if(term != null && term.length() > 0 && fields.size() == 1) {
      String field = (String) fields.get(0) ;
      if("username".equals(field)) query.setUserName(term) ;
      else if("lastname".equals(field))query.setFirstName(term) ;
      else if("firstname".equals(field))query.setLastName(term) ;
      else if("email".equals(field))query.setEmail(term) ;
    }
    UIListUser uiListUser = (UIListUser) getChildComponentOfType(UIListUser.class) ;
    uiListUser.search(query) ;
    setRenderedComponent(UIListUser.class) ;
  }
}