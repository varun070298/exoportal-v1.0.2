/**
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail.
 **/
package org.exoplatform.services.portletcontainer.impl.servlet;

import java.io.InputStream;
import java.util.*;
import javax.servlet.*;

import org.apache.commons.logging.Log;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.exoplatform.container.RootContainer;
import org.exoplatform.services.log.LogService;
import org.exoplatform.services.portal.skin.SkinConfigService;
import org.exoplatform.services.portletcontainer.PortletApplicationRegister;
import org.exoplatform.services.portletcontainer.PortletContainerException;
import org.exoplatform.services.portletcontainer.pci.model.PortletApp;
import org.exoplatform.services.portletcontainer.pci.model.XMLParser;
import org.exoplatform.services.xml.resolving.SimpleResolvingService;
/**
 * Created by the Exo Development team.
 * Author : Mestrallet Benjamin
 * benjmestrallet@users.sourceforge.net
 * Date: 10 nov. 2003
 * Time: 12:58:52
 */
public class PortletApplicationListener implements ServletContextListener {
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    RootContainer manager = RootContainer.getInstance();
    Log log = ((LogService)manager.getComponentInstanceOfType(
        LogService.class)).getLog("org.exoplatform.services.portletcontainer");
    ServletContext servletContext = servletContextEvent.getServletContext();
    log.info("DEPLOY PORTLET APPLICATION: " + servletContext.getServletContextName());
    log.debug("Real path : "+ servletContext.getRealPath(""));   
    InputStream is = null;
    try {
      //look for skin-config and register with the Portal Configuration service
      is = servletContext.getResourceAsStream("/WEB-INF/skin-config.xml");
      if (is != null) {
        SkinConfigService pcservice =
            (SkinConfigService) manager.getComponentInstanceOfType(SkinConfigService.class);
        pcservice.addConfiguration(is);
        log.info("Register skin configuration.............................");
      }

      is = servletContext.getResourceAsStream("/WEB-INF/portlet.xml");
      if (is == null) {
        log.info("PORTLET CONFIGURATION IS NOT FOUND, IGNORE THE PACKAGE");
        return;
      }
      PortletApp portletApp = XMLParser.parse(is);
      
      is = servletContext.getResourceAsStream("/WEB-INF/web.xml");
      Collection roles = new ArrayList();
      
      SAXReader reader = new SAXReader();
      reader.setFeature("http://xml.org/sax/features/validation", true);
      reader.setFeature("http://apache.org/xml/features/validation/schema", true);
      reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
      reader.setFeature("http://apache.org/xml/features/validation/dynamic", true);
      
      SimpleResolvingService serviceXML = (SimpleResolvingService) manager.
        getComponentInstanceOfType(SimpleResolvingService.class);
      reader.setEntityResolver(serviceXML.getEntityResolver());
      Document document = reader.read(is);
      List list = document.selectNodes("//web-app/security-role/role-name");
      
      
//      XMLQueryingService xmlQueryingService = (XMLQueryingService) RootContainer.getInstance().
//        getComponentInstanceOfType(XMLQueryingService.class);      
//      SimpleStatementHelper sHelper = xmlQueryingService.createStatementHelper();
//      XMLDataManager dManager = xmlQueryingService.createXMLDataManager();
//      XMLQuery query = xmlQueryingService.createQuery();
//      query.setInputStream(is);
//      query.prepare(sHelper.select("//web-app/security-role/role-name"));
//      query.execute();
//      NodeList nodes = dManager.toFragment(query.getResult()).getAsNodeList();                  
//      for (int i = 0; i < nodes.getLength(); i++) {
//        Node element = (Node) nodes.item(i);
//        roles.add(element.getText());
//      }      
      
      for (Iterator iter = list.iterator(); iter.hasNext();) {
        Node element = (Node) iter.next();
        roles.add(element.getText());
      }
            
      PortletApplicationRegister service = 
        (PortletApplicationRegister) manager.getComponentInstanceOfType(PortletApplicationRegister.class);
      service.registerPortletApplication(servletContext, portletApp, roles); 
    } catch (PortletContainerException e) {
      log.error(e);
    } catch (Exception e) {
      log.error(e);
    }
  }

  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    ServletContext servletContext = servletContextEvent.getServletContext();
    RootContainer manager = RootContainer.getInstance();
    Log log = ((LogService)manager.getComponentInstanceOfType(
        LogService.class)).getLog("org.exoplatform.services.portletcontainer");
    log.info("UNDEPLOY PORTLET APPLICATION: " + servletContext.getServletContextName());    
    try {
      PortletApplicationRegister service = (PortletApplicationRegister) RootContainer.getInstance().
          getComponentInstanceOfType(PortletApplicationRegister.class);
      service.removePortletApplication(servletContext);
    } catch (Exception e) {
      log.error(e);
    }
  }
}