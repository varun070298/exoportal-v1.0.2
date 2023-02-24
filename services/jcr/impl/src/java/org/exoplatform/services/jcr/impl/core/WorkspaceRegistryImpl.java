/*
 * Copyright 2001-2003 The eXo platform SARL All rights reserved.
 * Please look at license.txt in info directory for more license detail. 
 */

package org.exoplatform.services.jcr.impl.core;


import javax.jcr.RepositoryException;
import javax.jcr.Ticket;
import javax.jcr.Workspace;

import org.exoplatform.services.jcr.config.RepositoryServiceConfig;
import org.exoplatform.services.jcr.config.WorkspaceEntry;
import org.exoplatform.services.jcr.core.WorkspaceRegistry;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 23 juil. 2004
 */
public class WorkspaceRegistryImpl implements WorkspaceRegistry {

  private RepositoryServiceConfig repositoryServiceConfig;

  public WorkspaceRegistryImpl(RepositoryServiceConfig repositoryServiceConfig) {
    this.repositoryServiceConfig = repositoryServiceConfig;
  }

  public Workspace getWorkspace(Ticket ticket) throws RepositoryException {
    WorkspaceEntry[] workspaceEntries = repositoryServiceConfig.getWorkspaceEntries();
    for (int i = 0; i < workspaceEntries.length; i++) {
      WorkspaceEntry workspaceEntry = workspaceEntries[i];
      if(workspaceEntry.isBase())
        return createWorkspace(workspaceEntry.getName(), ticket);
    }
    return null;
  }

  public Workspace getWorkspace(String repositoryName, String workspaceName, Ticket ticket) throws RepositoryException {
    WorkspaceEntry workspaceEntry = repositoryServiceConfig.getWorkspaceEntry(repositoryName, workspaceName);
    if(workspaceEntry == null)
      return getWorkspace(ticket);
    else
      return createWorkspace(workspaceName, ticket);
  }

   private Workspace createWorkspace(String workspaceName, Ticket ticket) throws RepositoryException {
    return new WorkspaceImpl(workspaceName, ticket);
  }


}
