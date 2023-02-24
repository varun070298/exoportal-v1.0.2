/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlets.communication.forum.component;

import java.util.ArrayList;
import java.util.List;
import javax.faces.component.UIComponent;
import org.exoplatform.faces.core.component.model.SelectItem;
import org.exoplatform.faces.search.component.UISearchBar;
import org.exoplatform.faces.search.component.UISearcher;
import org.exoplatform.services.communication.forum.ForumIndexerPlugin;
import org.exoplatform.services.communication.forum.ForumService;
import org.exoplatform.services.indexing.IndexingService;
import org.exoplatform.services.indexing.Searcher;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UIContentSearcher.java,v 1.1 2004/10/25 03:48:11 tuan08 Exp $
 */
public class UIForumSearcher extends UISearcher {
  private ForumIndexerPlugin plugin_ ;
  private ForumService fservice_ ;
  
  public UIForumSearcher(ForumService fservice ,
                         IndexingService service) throws Exception {
  	setId("UISearcher") ;
    plugin_ = (ForumIndexerPlugin)service.getIndexerPlugin(ForumIndexerPlugin.IDENTIFIER) ;
    fservice_ = fservice ;
    init(service) ;
  }
  
  public Searcher getSearcher()  throws Exception {
    Searcher searcher = plugin_.getSearcher();
    return searcher ;
  }
  
  protected UIComponent customizeUISearchBar(IndexingService service ) throws Exception {
    UISearchBar uiBar =  new UISearchBar();
    List options = new ArrayList() ;
    options.add(new SelectItem("Search All", ""));
    options.add(new SelectItem("By Subject", ForumIndexerPlugin.SUBJECT_FIELD));
    options.add(new SelectItem("By Post", ForumIndexerPlugin.BODY_FIELD));
    options.add(new SelectItem("By Author", ForumIndexerPlugin.AUTHOR_FIELD));
    uiBar.setSearchOptions(options) ;
    return uiBar ;
  }
  
  public void showAdvancedSearch() throws Exception {
    this.setRenderedComponent(UIAdvancedSearch.class) ;
  }
  
  protected UIComponent customizeUIAdvancedSearch(IndexingService service) throws Exception {
    return new UIAdvancedSearch(fservice_)  ;
  }
}