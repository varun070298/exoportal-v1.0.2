/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.portal.impl;

import java.util.*;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import org.exoplatform.commons.utils.PageList;
import org.exoplatform.services.cache.CacheService;
import org.exoplatform.services.cache.ExoCache;
import org.exoplatform.services.database.DBObjectPageList;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.ObjectQuery;
import org.exoplatform.services.portal.Interceptor;
import org.exoplatform.services.portal.PortalACL;
import org.exoplatform.services.portal.PortalConfigService;
import org.exoplatform.services.portal.Query;
import org.exoplatform.services.portal.impl.converter.*;
import org.exoplatform.services.portal.model.*;
import org.exoplatform.services.portletcontainer.pci.model.ExoPortletPreferences;
import org.exoplatform.services.portletcontainer.pci.model.Preference;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;
/**
 * Created by The eXo Platform SARL        .
 * Author : Tuan Nguyen
 *          tuan08@owners.sourceforge.net
 * Date: Jun 14, 2003
 * Time: 1:12:22 PM
 */
public class PortalConfigServiceImpl implements PortalConfigService {
	
  static private String[] MAPPING =
    { "org/exoplatform/services/portal/impl/PortalConfigData.hbm.xml",
      "org/exoplatform/services/portal/impl/PageData.hbm.xml" ,
      "org/exoplatform/services/portal/impl/NodeNavigationData.hbm.xml" } ;
  
  public static final String queryPageDataByOwner =
    "from pd in class org.exoplatform.services.portal.impl.PageData where pd.owner = ? ";
  public static final String queryNodeNavigationData =
    "from nv in class org.exoplatform.services.portal.impl.NodeNavigationData where nv.owner = ? ";
    
  static private XStream xstream_ ; 
  
  private PortalACL portalACL_ ;
  private HibernateService hservice_ ;
  private Map predefinedTemplates_ ;
  private Interceptor interceptor_ ;
  private ExoCache portalConfigCache_ ;
  private ExoCache pageConfigCache_ ;
  private ExoCache nodeNavigationCache_ ;
  
  public PortalConfigServiceImpl(Interceptor interceptor, 
                                 PortalACL portalACL,
                                 CacheService cacheService ,
                                 HibernateService hservice) throws Exception {
    hservice_ = hservice ;
    interceptor_ = interceptor ;
    hservice.addMappingFiles(MAPPING) ;
    portalACL_ = portalACL ;
    portalConfigCache_ = cacheService.getCacheInstance(PortalConfig.class.getName()) ;
    portalConfigCache_.setMaxSize(1000) ;
    pageConfigCache_ = cacheService.getCacheInstance(Page.class.getName()) ;
    pageConfigCache_.setMaxSize(1000) ;
    nodeNavigationCache_ = cacheService.getCacheInstance(NodeImpl.class.getName()) ;
    nodeNavigationCache_.setMaxSize(1000) ;
  }
  
  public void initPredefinedTemplates(String xml) throws Exception {
  	predefinedTemplates_ = new HashMap() ;
    XStream xstream = getXStreamInstance() ;
    PageSet ps = (PageSet) xstream.fromXML(xml);
    List pages = ps.getPages() ;
    for (int i = 0 ; i <  pages.size(); i++) {
      Page page = (Page) pages.get(i) ;
      predefinedTemplates_.put(page.getOwner() + ":" + page.getName(), page) ;
    }
  }
  
  public void savePortalConfig(String owner, String xml)  throws Exception {
  	hservice_.save(new PortalConfigData(xml)) ;
    portalConfigCache_.remove(owner) ;
  }
  
  public void savePortalConfig(PortalConfig config)  throws Exception {
    portalConfigCache_.remove(config.getOwner()) ;
  	interceptor_.savePortalConfig(config) ;
  }
  
  public PortalConfig getPortalConfig(String owner) throws Exception {
    PortalConfig config = (PortalConfig) portalConfigCache_.get(owner) ;
    if(config != null) return config ;
    config =   interceptor_.getPortalConfig(owner);
    if(config != null) portalConfigCache_.put(owner, config) ;
    return config ;
  }
 
  public String getPortalConfigAsXmlString(String owner) throws Exception {
  	PortalConfigData  data = 
  		(PortalConfigData) hservice_.findOne(PortalConfigData.class, owner);
  	return data.getData() ;
  }

  public PageList findAllPortalConfigDescriptions(Query q) throws Exception {
  	String owner = q.getOwner() ;
  	if (owner == null || owner.length() == 0) {
  		owner = "%" ;
  	}
  	owner = owner.replace('*', '%') ;
    String viewPermission = q.getViewPermission() ; 
    String editPermission = q.getEditPermission() ; 
    ObjectQuery oq = new ObjectQuery(PortalConfigDescriptionData.class);
    oq.addLIKE("id", owner) ;
    if(viewPermission != null && viewPermission.length() > 0) {
      oq.addEQ("viewPermission", viewPermission) ;
    }
    if(editPermission != null && editPermission.length() > 0) {
      oq.addEQ("editPermission", editPermission) ;
    }
    return new DBObjectPageList(hservice_, oq) ;
  }
  
  public void removePortalConfig(String owner) throws Exception {
    hservice_.remove(PortalConfigData.class, owner) ;
    portalConfigCache_.remove(owner) ;
  }
  
  public void savePage(String xml)  throws Exception { 
    PageData data = new PageData(xml) ;
    hservice_.save(data) ;
    pageConfigCache_.remove(data.getId()) ;
  }

  public void savePage(Page page) throws Exception { 
    interceptor_.savePage(page) ;  
    pageConfigCache_.remove(PageData.getId(page)) ;
  }
  
  public Page getPage(String pageId) throws Exception {
    Page page = (Page)pageConfigCache_.get(pageId) ;
    if(page != null) return page ;
    page = interceptor_.getPage(pageId);
    pageConfigCache_.put(pageId, page) ;
    return page ;
  }

  public String getPageAsXmlString(String refId) throws Exception {
    PageData data = (PageData) hservice_.findOne(PageData.class, refId);
    return data.getData();
  }

  public void removePage(String refId) throws Exception {
    hservice_.remove(PageData.class, refId) ;
    pageConfigCache_.remove(refId) ;
  }

  public void removePageOfOwner(String owner) throws Exception {
  	Session session = hservice_.openSession();
    session.delete(queryPageDataByOwner, owner, Hibernate.STRING);
  	session.flush() ;
    pageConfigCache_.clear() ;
  }
  
  public PageList findAllPageDescriptions(Query q) throws Exception {
  	String owner = q.getOwner() ;
  	if (owner == null || owner.length() == 0) {
  		owner = "%" ;
  	}
  	owner = owner.replace('*', '%') ;
    String viewPermission = q.getViewPermission() ; 
    String editPermission = q.getEditPermission() ; 
    ObjectQuery oq = new ObjectQuery(PageDescriptionData.class);
    oq.addLIKE("owner", owner) ;
    if(viewPermission != null && viewPermission.length() > 0) {
      oq.addEQ("viewPermission", viewPermission) ;
    }
    if(editPermission != null && editPermission.length() > 0) {
      oq.addEQ("editPermission", editPermission) ;
    }
    return new DBObjectPageList(hservice_, oq) ;
  }
  //=============================================================================
  public void saveNodeNavigation(String owner, Node node) throws Exception {
    interceptor_.saveNodeNavigation(owner, node) ;
    nodeNavigationCache_.remove(owner) ;
  }
  
  public void removeNodeNavigation(String owner) throws Exception  {
    hservice_.remove(NodeNavigationData.class, owner) ;
    nodeNavigationCache_.remove(owner) ;
  }
  
  public Node getNodeNavigation(String owner) throws Exception {
    Node node = (Node) nodeNavigationCache_.get(owner) ;
    if(node == null) {
      node =  interceptor_.getNodeNavigation(owner);
      nodeNavigationCache_.put(owner, node) ;
    }
    if(node == null) return null ;
    //clone the node
    return new NodeImpl(node, null, 0) ;
  }
  
  public Node createNodeInstance() { return new NodeImpl(); }
  //=============================================================================
  
  public PortalACL getPortalACL() { return portalACL_ ; }
  
  public List getPredefinedTemplates(String owner) {
  	ArrayList list = new ArrayList(predefinedTemplates_.size()) ;
  	Iterator i = predefinedTemplates_.values().iterator() ;
  	while(i.hasNext()) {
  		Page page = (Page)i.next() ;
  		if(owner.equals(page.getOwner())) {
  			list.add(page) ;
  		}
  	}
  	return list ;
  }
  
  public Page getPredefinedTemplate(String owner, String name) throws  Exception {
  	Page page = (Page)	predefinedTemplates_.get(owner + ":" + name)  ;
  	if (page != null) page = (Page)page.softCloneObject() ;
    return page ;
  }
  
  void invalidateCache(String owner) throws Exception {
    portalConfigCache_.remove(owner) ;
    pageConfigCache_.clear() ;
    nodeNavigationCache_.remove(owner) ;
  }
  
 static XStream getXStreamInstance() {
 	if (xstream_ == null ) {
      xstream_ = new XStream(new XppDriver());
      xstream_.alias("user-portal-config", Backup.class);
      xstream_.alias("portal-config", PortalConfig.class);
      xstream_.alias("body", Body.class);
      xstream_.alias("node", PageNode.class);
      xstream_.alias("page", Page.class);
      xstream_.alias("page-set", PageSet.class);
      xstream_.alias("container", Container.class);
      xstream_.alias("portlet", Portlet.class);
      xstream_.alias("portlet-preferences", ExoPortletPreferences.class);
      xstream_.alias("preference", Preference.class);
      xstream_.alias("node-navigation", NodeNavigation.class);
      xstream_.registerConverter(new PortletConverter());
      xstream_.registerConverter(new ExoPortletPreferencesConverter());
      xstream_.registerConverter(new PreferenceConverter());
      xstream_.registerConverter(new ContainerConverter());
      xstream_.registerConverter(new PageConverter());
      xstream_.registerConverter(new PageSetConverter());
      xstream_.registerConverter(new NodeConfigConverter());
    }
    return xstream_ ;
  }
}