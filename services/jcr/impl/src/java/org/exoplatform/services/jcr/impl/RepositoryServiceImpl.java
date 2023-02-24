/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl;

import java.util.*;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;

import org.exoplatform.container.PortalContainer; 
import org.exoplatform.container.RootContainer;
import org.exoplatform.container.configuration.PortalContainerInfo;

import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.impl.core.RepositoryImpl;
import org.exoplatform.services.jcr.storage.RepositoryManager;
import org.exoplatform.services.jcr.storage.WorkspaceContainer;
import org.exoplatform.services.jcr.config.*;

import org.picocontainer.Parameter;
import org.picocontainer.defaults.ComponentParameter;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.DefaultComponentAdapterFactory;
import org.picocontainer.ComponentAdapter ;
/**
 * Created by The eXo Platform SARL.
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: RepositoryServiceImpl.java,v 1.10 2004/09/22 13:54:55 geaz Exp $
 */

public class RepositoryServiceImpl implements RepositoryService {

  RepositoryServiceConfig config;    
  private PortalContainerInfo containerInfo;

  public RepositoryServiceImpl(PortalContainerInfo containerInfo, 
                               RepositoryServiceConfig config) {
    this.config = config;
    this.containerInfo = containerInfo;
  }

  public Repository getRepository() throws RepositoryException {
    return getRepository(config.getDefaultRepositoryName());
  }

  public Repository getRepository(String name) throws RepositoryException {

    PortalContainer portalContainer = 
      RootContainer.getInstance().getPortalContainer(containerInfo.getContainerName());

    RepositoryImpl repository = (RepositoryImpl) portalContainer.getComponentInstance(name);
    if (repository == null){
      init(portalContainer);
      repository = (RepositoryImpl) portalContainer.getComponentInstance(name);

      if(repository == null)
        throw new RepositoryException("Repository '"+name+"' not found.");

      HashMap workspaceContainers = new HashMap();

      try {
           String managerName = config.getRepositoryEntry(name).getManager();

           repository.setRepositoryManager( (RepositoryManager)PortalContainer.getInstance().
                getComponentInstance(managerName) );

            WorkspaceEntry[] wes = config.getWorkspaceEntries();
            for (int i = 0; i < wes.length; i++) {
                if (wes[i].getRepositoryName().equals(name)) {
                    workspaceContainers.put(wes[i].getName(),
                        (WorkspaceContainer)PortalContainer.getInstance().
                        getComponentInstance(config.getWorkspaceEntry(name,
                        wes[i].getName()).getContainerName()));

                    if(wes[i].isBase())
                        repository.setDefaultWorkspaceName( wes[i].getName() );

                    repository.getRepositoryManager().addWorkspaceContainer(wes[i].getContainerName());
                }
            }
       } catch (RepositoryConfigurationException e) {
            throw new RepositoryException("RepositoryServiceImpl.getRepository() failed ", e);
       }

       repository.setWorkspaceContainers(workspaceContainers);

    }
    return repository;
  }

  public RepositoryServiceConfig getConfig() {
    return config;
  }
  
  private void init(PortalContainer portalContainer) {    
    DefaultComponentAdapterFactory factory = new DefaultComponentAdapterFactory() ;
    RepositoryManagerEntry[] mEntries = config.getSupportedRepositoryManagerEntries();
    for (int i = 0; i < mEntries.length; i++) {
      Parameter[] params = new Parameter[mEntries[i].getParameters().size()+1];
      params[0] = new ConstantParameter(mEntries[i].getName());
      int j = 1;
      for (Enumeration e = mEntries[i].getParameters().propertyNames(); e.hasMoreElements(); j++)
        params[j] = new ConstantParameter(mEntries[i].getParameters().getProperty((String) e.nextElement()));
      ComponentAdapter adapter = 
        factory.createComponentAdapter(mEntries[i].getName(), mEntries[i].getType(), params) ;
      portalContainer.registerComponent(adapter) ;
    }

    ContainerEntry[] cEntries = config.getSupportedContainerEntries();
    for (int i = 0; i < cEntries.length; i++) {
      Parameter[] params = new Parameter[cEntries[i].getParameters().size()+1];
      params[0] = new ConstantParameter(cEntries[i].getName());
      int j = 1;
      for (Enumeration e = cEntries[i].getParameters().propertyNames(); e.hasMoreElements(); j++)
        params[j] = new ConstantParameter(cEntries[i].getParameters().getProperty((String) e.nextElement()));
      ComponentAdapter adapter = 
        factory.createComponentAdapter(cEntries[i].getName(), cEntries[i].getType(), params) ;
      portalContainer.registerComponent(adapter) ;
    }

    RepositoryEntry[] rEntries = config.getRepositoryEntries();
    for (int i = 0; i < rEntries.length; i++) {
      Parameter[] params = new Parameter[2];
      params[0] = new ConstantParameter(rEntries[i].getName());
      params[1] = new ComponentParameter(); // SecurityService
      ComponentAdapter adapter = 
        factory.createComponentAdapter(rEntries[i].getName(), RepositoryImpl.class, params) ;
      portalContainer.registerComponent(adapter) ;
    }
  }
}
