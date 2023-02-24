/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */
package org.exoplatform.portlets.console.component;

import org.exoplatform.faces.core.component.UIGrid;
import org.exoplatform.faces.core.component.model.*;
import org.exoplatform.faces.core.event.CheckRoleInterceptor;
import org.exoplatform.faces.core.event.ExoActionEvent;
import org.exoplatform.faces.core.event.ExoActionListener;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 22 juin 2004
 */
public class UIListCache  extends UIGrid  {
  final static public String CLEAR_ACTION = "clear" ;
  final static public String REFRESH_ACTION = "refresh" ;
  private static Parameter[] CLEAR_PARAMS = {new Parameter(ACTION , CLEAR_ACTION) } ;
  private static Parameter[] REFRESH_PARAMS = {new Parameter(ACTION , REFRESH_ACTION) } ;

  private ExoCacheDataHandler dataHandler_ ;
  private CacheService service_ ;
  private boolean adminRole_ ;

  public UIListCache(CacheService cservice) throws Exception {
    setId("UIListCache") ;
    service_ = cservice ;
    dataHandler_ = new ExoCacheDataHandler() ;
    dataHandler_.setDatas(service_.getAllCacheInstances()) ;
    adminRole_ = hasRole(CheckRoleInterceptor.ADMIN) ;
    add(new Rows(dataHandler_, "even", "odd").
        add(new Column("#{UIListCache.header.name}", "name").setCellClass("left-indent")).
        add(new Column("#{UIListCache.header.cache-size}", "cacheSize")).
        add(new Column("#{UIListCache.header.max-size}",   "maxSize")).
        add(new Column("#{UIListCache.header.cache-hit}", "cacheHit")).
        add(new Column("#{UIListCache.header.cache-miss}", "cacheMiss")).
        add(new ActionColumn("#{UIListCache.header.action}", "name").
            add(adminRole_ ,new Button("#{UIListCache.button.clear}", CLEAR_PARAMS)))) ;
    add(new Row().setClazz("footer").
        add(new ActionCell().
            add(adminRole_, new Button("#{UIListCache.button.refresh}", REFRESH_PARAMS)).
            addColspan("6").addAlign("center")));
    addActionListener(ClearActionListener.class, CLEAR_ACTION) ;
    addActionListener(RefreshActionListener.class, REFRESH_ACTION) ;
  }

  static public class ExoCacheDataHandler extends CollectionDataHandler {
  	private ExoCache instance ;
    
  	public String  getData(String fieldName)   {
      if("name".equals(fieldName)) return instance.getName() ;
      if("cacheHit".equals(fieldName)) return Integer.toString(instance.getCacheHit()) ;
      if("cacheMiss".equals(fieldName)) return Integer.toString(instance.getCacheMiss()) ;
      if("cacheSize".equals(fieldName)) return Integer.toString(instance.getCacheSize()) ;
      if("maxSize".equals(fieldName)) return Integer.toString(instance.getMaxSize()) ;
  		return "unknown" ;
  	}
  	
  	public void setCurrentObject(Object o) {  instance = (ExoCache) o;  }
  }
  
  static public class ClearActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIListCache uiComp  = (UIListCache) event.getSource() ;
      String name = event.getParameter(OBJECTID) ;
      ExoCache cache = uiComp.service_.getCacheInstance(name) ;
      cache.clear() ;
    }
  }
  
  static public class RefreshActionListener extends ExoActionListener  {
    public void execute(ExoActionEvent event) throws Exception {
      UIListCache uiComp  = (UIListCache) event.getSource() ;
      uiComp.dataHandler_.setDatas(uiComp.service_.getAllCacheInstances()) ;
    }
  }
}