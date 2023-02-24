/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.search.component;

import java.util.List;
import org.apache.lucene.document.Document;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.faces.core.component.UIExoCommand;
import org.exoplatform.faces.core.component.UIPageListIterator;
import org.exoplatform.faces.core.component.model.Parameter;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.faces.search.component.model.DocumentDataHandler;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Aug 27, 2004
 * @version $Id: UISearchSummary.java,v 1.1 2004/10/25 03:34:07 tuan08 Exp $
 */
public class UISearchSummary extends UIExoCommand {
  final static public String COMPONENT_FAMILY = "org.exoplatform.faces.search.component.UISearchSummary" ;
  
  public final int PAGE_SIZE = 15 ;
  public final static String VIEW_ACTION = "view" ;
  public final static String DOCUMENT_INDEX = "index" ;
  public static Parameter viewParam_ = new Parameter(ACTION, VIEW_ACTION) ;
  
  private UIPageListIterator uiPageIterator_ ;
  private DocumentDataHandler dataHandler_ ;
  
  public UISearchSummary(DocumentDataHandler dataHandler) {
  	setId("UISearchSummary") ;
    setClazz("UISearchResult");
    setRendererType("SearchSummaryRenderer") ;
    dataHandler_ = dataHandler ;
    uiPageIterator_ = new UIPageListIterator(dataHandler) ;
    List children = getChildren() ;
    children.add(uiPageIterator_);
    addActionListener(ViewActionListener.class, VIEW_ACTION) ;
  }
  
  public UIPageListIterator getUIPageIterator() { return uiPageIterator_ ; }
  
  public void  setSearchResult(PageList result) throws Exception {
    uiPageIterator_.setPageList(result) ;
  }
  
  public String getFamily() { return COMPONENT_FAMILY ; }
  
  static public class ViewActionListener extends ExoActionListener {
    public void execute(ExoActionEvent event) throws Exception {
      UISearchSummary uiSummary =  (UISearchSummary) event.getSource();
      String indexStr = event.getParameter(DOCUMENT_INDEX) ;
      int idx = Integer.parseInt(indexStr) ;
      Document doc =  (Document)uiSummary.dataHandler_.getObjectInPage(idx) ;
      UISearcher uiSearcher = (UISearcher) uiSummary.getParent();
      uiSearcher.viewDocument(doc) ;
    }
  } 
}