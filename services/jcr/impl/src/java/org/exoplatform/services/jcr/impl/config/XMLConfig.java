/**
 **************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.jcr.impl.config;

import org.exoplatform.services.jcr.config.*;
import org.exoplatform.services.xml.querying.Statement;
import org.exoplatform.services.xml.querying.XMLQuery;
import org.exoplatform.services.xml.querying.XMLQueryingService;
import org.exoplatform.services.xml.querying.helper.SimpleStatementHelper;
import org.exoplatform.services.xml.querying.helper.XMLDataManager;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.Properties;
import java.util.ArrayList;
import org.exoplatform.container.configuration.ServiceConfiguration;
import org.exoplatform.container.configuration.ConfigurationManager;
import org.exoplatform.container.configuration.ValueParam;

/**
 * Created by The eXo Platform SARL     
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: XMLConfig.java,v 1.11 2004/09/22 13:54:54 geaz Exp $
 */
public class XMLConfig implements RepositoryServiceConfig {

  public static final String CONF_FILE = "conf/portal/exo-jcr-config.xml";

  private WorkspaceEntry[] wes;
  private ContainerEntry[] ces;
  private RepositoryEntry[] res;
  private RepositoryManagerEntry[] mes;
  private String defaultRepositoryName;

  private XMLQueryingService xmlQueryingService;
  private SimpleStatementHelper sHelper;
  private XMLDataManager dManager;
  private XMLQuery query;

  public XMLConfig(XMLQueryingService xmlQueryingService) throws RepositoryConfigurationException {
    this.xmlQueryingService = xmlQueryingService;

    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    InputStream config = cl.getResourceAsStream(CONF_FILE);

    if (config == null)
      throw new RepositoryConfigurationException("XML config data not found! Check if '" + CONF_FILE + "' file exists.");

    init(config);
  }

  public XMLConfig(XMLQueryingService xmlQueryingService, ConfigurationManager configurationService) throws RepositoryConfigurationException {

    this.xmlQueryingService = xmlQueryingService;
    try {

        ServiceConfiguration conf =
             configurationService.getServiceConfiguration(XMLConfig.class);
        ValueParam param = conf.getValueParam("conf-path");
        InputStream is = configurationService.getInputStream((String)param.getValue());
        init(is);

    } catch (Exception e){
      throw new RepositoryConfigurationException("XML config data not found! Reason: "+e );
    }

  }


  public XMLConfig(XMLQueryingService xmlQueryingService, InputStream source) throws RepositoryConfigurationException {
    this.xmlQueryingService = xmlQueryingService;
    init(source);
  }

  public String getDefaultRepositoryName() {
    return defaultRepositoryName;
  }


  public RepositoryEntry[] getRepositoryEntries() {
    return res;
  }

  public WorkspaceEntry[] getWorkspaceEntries() {
    return wes;
  }

  public WorkspaceEntry getWorkspaceEntry(String repositoryName, String name) {
    for (int i = 0; i < wes.length; i++) {
      if (wes[i].getRepositoryName().equals(repositoryName) && wes[i].getName().equals(name))
        return wes[i];
    }
    return null;
  }

  public ContainerEntry[] getSupportedContainerEntries() {
    return ces;
  }

  public ContainerEntry getContainerEntry(String name) {
    for (int i = 0; i < ces.length; i++) {
      if (ces[i].getName().equals(name))
        return ces[i];
    }
    return null;
  }

  public RepositoryManagerEntry[] getSupportedRepositoryManagerEntries() {
    return mes;
  }

  public RepositoryManagerEntry getRepositoryManagerEntry(String name) {
    for (int i = 0; i < mes.length; i++) {
      if (mes[i].getName().equals(name))
        return mes[i];
    }
    return null;
  }

  public RepositoryEntry getRepositoryEntry(String name) {
    for (int i = 0; i < res.length; i++) {
      if (res[i].getName().equals(name))
        return res[i];
    }
    return null;
  }

  private void init(InputStream source) throws RepositoryConfigurationException {

    try {

      sHelper = xmlQueryingService.createStatementHelper();
      dManager = xmlQueryingService.createXMLDataManager();
      query = xmlQueryingService.createQuery();
      query.setInputStream(source);

      // default-repository
      Element defNode = (Element)selectNodes("repositories-config/default-repository").item(0);
      if(defNode == null)
        throw new RepositoryConfigurationException("Default node name not found in configuration."); 
//      Element node = (Element) reps.item(0);
      defaultRepositoryName = defNode.getAttribute("name");


      // repositories
      NodeList reps = selectNodes("repositories-config/repositories/*[name()='repository']");
      res = new RepositoryEntry[reps.getLength()];
      for (int i = 0; i < res.length; i++) {
        Element node = (Element) reps.item(i);
        String manager = node.getAttribute("manager");
        String name = node.getAttribute("name");

//        if (alias.equals(""))
//          alias = cAlias;
        res[i] = new RepositoryEntry(name, manager);

      }


      ArrayList wsList = new ArrayList();
      for (int j = 0; j < res.length; j++) {

         String repositoryName = res[j].getName();

         // workspaces
         NodeList workspaces = selectNodes("repositories-config/repositories/repository[@name='" + repositoryName + "']/workspaces/*[name()='workspace']");
         for (int i = 0; i < workspaces.getLength(); i++) {
           Element node = (Element) workspaces.item(i);
           String name = node.getAttribute("name");
           String container = node.getAttribute("container");
           String defaultWorkspace = node.getAttribute("default");
           if ("true".equals(defaultWorkspace))
             wsList.add(new WorkspaceEntry(name, repositoryName, true, container));
           else if ("false".equals(defaultWorkspace))
             wsList.add(new WorkspaceEntry(name, repositoryName, false, container));
           else
             wsList.add(new WorkspaceEntry(name, repositoryName, container));
         }
      }

      wes = new WorkspaceEntry[wsList.size()];
      for(int i=0; i<wsList.size(); i++)
        wes[i] = (WorkspaceEntry)wsList.get(i);

      // ws containers
      reps = selectNodes("repositories-config/containers/*");
      ces = new ContainerEntry[reps.getLength()];
      for (int i = 0; i < ces.length; i++) {
        Properties props = new Properties();
        Element node = (Element) reps.item(i);
        String name = node.getAttribute("name");
        Class clazz = null;
        try {
          clazz = Class.forName(node.getAttribute("class"));
        } catch (Exception e) {
        }

        // ws containers' properties
        NodeList ps = selectNodes("repositories-config/containers/container[@name='" +
            name + "']/properties/*");
        for (int j = 0; j < ps.getLength(); j++) {
          Element p = (Element) ps.item(j);
          props.setProperty(p.getAttribute("name"), p.getAttribute("value"));
        }
        ces[i] = new ContainerEntry(name, clazz, props);
      }

      // repository managers
      reps = selectNodes("repositories-config/managers/*");
      mes = new RepositoryManagerEntry[reps.getLength()];
      for (int i = 0; i < mes.length; i++) {
        Properties props = new Properties();
        Element node = (Element) reps.item(i);
        String name = node.getAttribute("name");
        Class clazz = null;
        try {
          clazz = Class.forName(node.getAttribute("class"));
        } catch (Exception e) {
        }

        // repository managers' properties
        NodeList ps = selectNodes("repositories-config/managers/manager[@name='" +
                                  name + "']/properties/*");
        for (int j = 0; j < ps.getLength(); j++) {
          Element p = (Element) ps.item(j);
          props.setProperty(p.getAttribute("name"), p.getAttribute("value"));
        }
        mes[i] = new RepositoryManagerEntry(name, clazz, props);
      }

    } catch (Exception e) {
      throw new RepositoryConfigurationException("XMLConfig failed. Reason " + e);
    }
  }

  private NodeList selectNodes(String xpath) throws Exception {
      Statement stat = sHelper.select(xpath);

      query.prepare(stat);
      query.execute();
      return dManager.toFragment(query.getResult()).getAsNodeList();
  }
}
