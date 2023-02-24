package org.exoplatform.services.jcr.core;

import javax.jcr.Ticket;
import javax.jcr.Workspace;
import javax.jcr.RepositoryException;

/**
 * Created y the eXo platform team
 * User: Benjamin Mestrallet
 * Date: 23 juil. 2004
 */
public interface WorkspaceRegistry {
  public Workspace getWorkspace(Ticket ticket) throws RepositoryException;

  public Workspace getWorkspace(String repositoryName, String workspaceName, Ticket ticket) throws RepositoryException;
}
