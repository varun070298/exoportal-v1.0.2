/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portal.impl;

import java.util.List;
import java.io.InputStream;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import org.apache.commons.lang.StringUtils;
import org.exoplatform.services.database.XResources;
import org.exoplatform.services.organization.*;
import org.exoplatform.services.portal.model.Page;
import org.exoplatform.services.portal.model.PageSet;
import org.picocontainer.Startable;
import com.thoughtworks.xstream.XStream;
import org.exoplatform.commons.utils.IOUtil;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ServiceConfiguration;
/**
 * Author : Tuan Nguyen
 *          tuan08@users.sourceforge.net
 * Wed, Feb 18, 2004 @ 21:33 
 */
public class PortalUserEventListenerImpl extends UserEventListener implements Startable {
  private String templateConfig_;
  private String templatePageSet_ ;
  private String templateNavigation_ ;
  
  private NewPortalConfig config_ ;
  private ConfigurationManager cservice_ ;
  private PortalConfigServiceImpl pservice_ ;
  
  public PortalUserEventListenerImpl(ConfigurationManager cservice, 
                                     OrganizationService orgService,
                                     PortalConfigServiceImpl pservice) throws Exception {
    cservice_ = cservice ;
    pservice_ = pservice ;
    ServiceConfiguration sconf = cservice.getServiceConfiguration(getClass()) ;
    config_ = (NewPortalConfig) sconf.getObjectParam("new.portal.configuration").getObject() ;
    String templateUser = config_.getTemplateUser() ;
    String templateLoc = config_.getTemplateLocation() ;
    String xml = 
    	IOUtil.getStreamContentAsString(cservice.getInputStream(config_.getTemplateLocation() + "/templates.xml"));
  	pservice.initPredefinedTemplates(xml) ;
  	
    InputStream is = cservice.getInputStream(templateLoc + "/" + templateUser + "-config.xml") ;
    templateConfig_ =  	IOUtil.getStreamContentAsString(is);
    is = cservice.getInputStream(templateLoc + "/" + templateUser + "-pages.xml") ;
    templatePageSet_ =   	IOUtil.getStreamContentAsString(is);
    is  = cservice.getInputStream(templateLoc + "/" + templateUser + "-navigation.xml") ;
    templateNavigation_ =   IOUtil.getStreamContentAsString(is);
    orgService.addUserEventListener(this) ;
  }
 
  public void preSave(User user, boolean isNew, XResources xresources) throws Exception {
    if(!isNew)  return ;
    Session session = (Session) xresources.getResource(Session.class);
    String owner = user.getUserName();
    String config = null ;
    String pageSet = null ;
    String navigation = null ;
    if(config_.isPredefinedUser(owner)) {
      String location = config_.getTemplateLocation() ;
      config = 
        IOUtil.getStreamContentAsString(cservice_.getInputStream(location + "/" + owner + "-config.xml"));
      pageSet = 
        IOUtil.getStreamContentAsString(cservice_.getInputStream(location + "/" + owner + "-pages.xml"));
      navigation = 
        IOUtil.getStreamContentAsString(cservice_.getInputStream(location + "/" + owner + "-navigation.xml"));
    } else {
      config = StringUtils.replace(templateConfig_, "@owner@", owner);
      pageSet = StringUtils.replace(templatePageSet_, "@owner@", owner);
      navigation = StringUtils.replace(templateNavigation_, "@owner@", owner);
    }
    session.save(new PortalConfigData(config));
    session.save(new NodeNavigationData(navigation));
    XStream xstream = PortalConfigServiceImpl.getXStreamInstance() ;
    PageSet ps = (PageSet) xstream.fromXML(pageSet);
    List pages = ps.getPages() ;
    for (int i = 0 ; i <  pages.size(); i++) {
      Page page = (Page) pages.get(i) ;
      session.save(new PageData(page)) ;
    }
  }

  public void preDelete(User user, XResources xresources) throws Exception {
    Session session = (Session) xresources.getResource(Session.class);
    String owner = user.getUserName();
    session.delete(PortalConfigServiceImpl.queryPageDataByOwner, owner, Hibernate.STRING);
    session.delete(session.get(PortalConfigData.class, owner)) ;
    session.delete(session.get(NodeNavigationData.class, owner)) ;
    pservice_.invalidateCache(user.getUserName()) ;
  }
  
  public void start() { }
  public void stop() {} 
    
}