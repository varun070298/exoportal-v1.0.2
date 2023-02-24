/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component.model;

import java.util.List ;
import org.exoplatform.commons.utils.PageList;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Oct 21, 2004
 * @version $Id: PageListDataHandler.java,v 1.3 2004/11/03 04:24:53 tuan08 Exp $
 */
public class PageListDataHandler extends ListDataHandler {
  private PageList pageList_ = PageList.EMPTY_LIST ;
  
  public void setPageList(PageList pageList) throws Exception { 
    pageList_ = pageList ; 
    setDatas(pageList_.currentPage()) ;
  }
  
  public int getAvailablePage() { return pageList_.getAvailablePage() ; } 
  
  public int getCurrentPage() { return  pageList_.getCurrentPage() ; }  
  
  public int getAvailable() { return pageList_.getAvailable() ; }
  
  public int getFrom() { return pageList_.getFrom() ; }
  
  public int getTo() { return pageList_ .getTo() ; }
  
  public int getCurrentObjectIdndex() { return getFrom() + getCurrentRow() ; }
  
  public Object getObjectInPage(int index) throws Exception {
    return pageList_.currentPage().get(index) ;
  }
  
  public void selectPage(int page) throws Exception {
    setDatas(pageList_.getPage(page)) ;
  }
  
  public List getObjectInCurrentPage() { return getDatas() ; }
  
  public String  getData(String fieldName)   {
    throw new RuntimeException("You need to override this method") ;
  }
  
  public void setCurrentObject(Object o)  {
    
  }
}
