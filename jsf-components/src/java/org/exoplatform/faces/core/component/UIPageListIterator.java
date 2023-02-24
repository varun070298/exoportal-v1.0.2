/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.faces.core.component;

import java.util.Map;
import java.util.List ;
import javax.faces.context.FacesContext;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.faces.core.component.model.PageListDataHandler;
/**
 * Wed, Dec 22, 2003 @ 23:14 
 * @author: Tuan Nguyen
 * @email:   tuan08@users.sourceforge.net
 * @version: $Id: UIPageListIterator.java,v 1.2 2004/11/03 04:24:53 tuan08 Exp $
 */
public class UIPageListIterator extends UIExoComponentBase {
  public static final String  COMPONENT_FAMILY = "org.exoplatform.faces.core.component.UIPageListIterator" ;
  public static final String  PAGE_PARAM = "page" ;
  private String cache_ ;
  private PageListDataHandler dataHandler_ ;
  
  public UIPageListIterator(PageListDataHandler dataHandler) {
    dataHandler_ = dataHandler ;
    cache_ = null ;
    setClazz("UIPageIterator") ;
    setId("UIPageListIterator");
    setRendererType("PageListIteratorRenderer") ;
    setRendered(true);
  }
  
  public int getAvailablePage() { return dataHandler_.getAvailablePage() ; } 
  
  public int getCurrentPage() { return  dataHandler_.getCurrentPage() ; }  
  
  public int getAvailable() { return dataHandler_.getAvailable() ; }
  
  public int getFrom() { return dataHandler_.getFrom() ; }
  public int getTo() { return dataHandler_.getTo() ; }
  
  public String getCache() { return cache_ ; } 
  public void setCache(String cache)  { cache_ = cache ; }
  
 
  public PageListDataHandler getPageListDataHandler()  { return dataHandler_ ; }
  public void  setPageListDataHandler(PageListDataHandler dh)  { 
    dataHandler_ = dh;
    cache_ = null ;
  }
  
  public void  setPageList(PageList l)  throws Exception { 
    dataHandler_.setPageList(l) ;
    cache_ = null ;
  }
  
  public List getObjectInCurrentPage() { 
    return dataHandler_.getObjectInCurrentPage()  ; 
  }
  
  public String getFamily() {  return COMPONENT_FAMILY ; }
  
  public Object getObjectInPage(int index) throws Exception {
    return dataHandler_.getObjectInPage(index) ;
  }
  
  public void decode(FacesContext context) {
    Map paramMap = context.getExternalContext().getRequestParameterMap() ;
    String comp = (String) paramMap.get(UICOMPONENT) ;
    if(getId().equals(comp)) {
      try{
        String  page = (String) paramMap.get(PAGE_PARAM) ;
        int selectPage = Integer.parseInt(page) ;
        dataHandler_.selectPage(selectPage) ;
        cache_ = null ;
        context.renderResponse() ;
      } catch (Exception ex) {
        ex.printStackTrace() ;
      }
    }
  }
}