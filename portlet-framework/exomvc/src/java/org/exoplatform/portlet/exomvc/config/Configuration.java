/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.portlet.exomvc.config;

import java.util.* ;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import org.exoplatform.portlet.exomvc.MVCPortletFramework;
/**
 * @author Tuan Nguyen (tuan08@users.sourceforge.net)
 * @since Nov 7, 2004
 * @version $Id$
 */
public class Configuration {
  private Map viewMode_ ;
  private Map editMode_ ;
  private Map helpMode_ ;
  private Map configMode_ ;
  
  private PageConfig defaultView_ ;
  private PageConfig defaultEdit_ ;
  private PageConfig defaultConfig_ ;
  private PageConfig defaultHelp_ ;
  
  private PortletContext    context_ ;
  private PortletConfig     portletConfig_ ;
  private GroovyResourceManager groovyResourceManager_ ;
  private JSPResourceManager jspResourceManager_ ;
  private VelocityResourceManager velocityResourceManager_ ;
  private String reposistory_ ;
  
  public Configuration(PortletConfig config)  throws Exception {
    context_ = config.getPortletContext() ;
    portletConfig_ = config  ;
    configure() ;
  }
  
  public void configure() throws Exception { 
    defaultView_  = null;
    defaultEdit_  = null ;
    defaultConfig_ = null ;
    defaultHelp_ = null ;
    KeyComparator comparator = new KeyComparator() ;
    viewMode_ = new TreeMap(comparator) ;
    editMode_ = new TreeMap(comparator) ;
    helpMode_ = new TreeMap(comparator) ;
    configMode_ = new TreeMap(comparator) ;
    
    reposistory_ = portletConfig_.getInitParameter("script.reposistory") ;
    if(reposistory_ == null) reposistory_ = "/WEB-INF/script" ;
    String configScript = portletConfig_.getInitParameter("configure.script") ;
    jspResourceManager_  = new JSPResourceManager(context_, reposistory_) ;
    groovyResourceManager_  = new GroovyResourceManager(context_, reposistory_) ;
    Configure configure = 
      (Configure)groovyResourceManager_.getGroovyManager().getObject(configScript) ;
    configure.configure(this) ;
  }
  
  public PageConfig getPageConfig(PortletMode mode, String pageName)  throws Exception {
    PageConfig pconfig = null ;
    if (mode.equals(PortletMode.VIEW)) {
      if(pageName == null) pconfig = defaultView_ ;
      else pconfig = (PageConfig)viewMode_.get(pageName) ;
    } else if (mode.equals(PortletMode.EDIT)) {
      if(pageName == null) pconfig = defaultEdit_ ; 
      else pconfig = (PageConfig)editMode_.get(pageName) ;
    } else if (mode.equals(MVCPortletFramework.CONFIG_MODE)) {
      if(pageName == null) pconfig = defaultConfig_ ; 
      else pconfig = (PageConfig)configMode_.get(pageName) ;
    } else if (mode.equals(PortletMode.HELP)) {
      if(pageName == null) pconfig = defaultHelp_ ; 
      else pconfig = (PageConfig)helpMode_.get(pageName) ; 
    } else {
      throw new PortletException("unknown portlet mode: " + mode);
    }
    return pconfig ;
  }
  
  public PageConfig getDefaultView() { return defaultView_ ; }
  
  public void addViewModePage(PageConfig pconfig) {
    viewMode_.put(pconfig.getPageName(), pconfig) ;
    if(defaultView_ == null) defaultView_ =  pconfig ;
  }
  
  public void addEditModePage(PageConfig pconfig) {
    editMode_.put(pconfig.getPageName(), pconfig) ;
    if(defaultEdit_ == null) defaultEdit_ = pconfig ;
  }
  
  public void addHelpModePage(PageConfig pconfig) {
    helpMode_.put(pconfig.getPageName(), pconfig) ;
    if(defaultHelp_ == null) defaultHelp_ = pconfig ;
  }
  
  public void addConfigModePage(PageConfig pconfig) {
    configMode_.put(pconfig.getPageName(), pconfig) ;
    if(defaultConfig_ == null) defaultConfig_ = pconfig ;
  }
  
  public Map getViewModePages() { return viewMode_ ; }
  
  public PortletConfig getPortletConfig() { return portletConfig_ ; }
  public PortletContext getPortletContext() { return context_ ;}
  
  public GroovyResourceManager getGroovyResourceManager() { return groovyResourceManager_ ; }
  
  public JSPResourceManager getJSPResourceManager() { return jspResourceManager_ ; }
  
  public VelocityResourceManager getVelocityResourceManager() throws Exception {
    if(velocityResourceManager_ == null) {
      synchronized(this) {
        velocityResourceManager_ = new VelocityResourceManager(context_, reposistory_) ;
      }
    }
    return velocityResourceManager_ ;
  }
  
  public void destroy() {
    groovyResourceManager_.destroy() ;
  }
  
  static class KeyComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      String key1 = (String) o1 ;
      String key2 = (String) o2 ;
      return key1.compareTo(key2) ;
     }
  }
}