/**
 **************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.jcr.impl.core;

import java.util.HashMap;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Ticket;

import org.apache.commons.logging.Log;
import org.exoplatform.services.jcr.storage.RepositoryManager;
import org.exoplatform.services.jcr.storage.WorkspaceContainer;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.organization.OrganizationService;

/**
 * Created by The eXo Platform SARL .
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov </a>
 * @version $Id: RepositoryImpl.java,v 1.12 2004/11/02 18:36:33 geaz Exp $
 */
public class RepositoryImpl implements Repository {

  private HashMap workspaceContainers;

  private RepositoryManager repositoryManager;

  private String defaultWorkspaceName;

  protected Log log;

  private String name;

  private OrganizationService organizationService;

  public RepositoryImpl(String name, OrganizationService organizationService)
      throws RepositoryException {
    this.name = name;
    this.organizationService = organizationService;
    log = LogUtil.getLog("org.exoplatform.services.jcr");
  }

  /**
   * 6.1.1 Login, creating a new ticket for the given credentials and specified
   * workspace. If login fails, a <code>LoginException</code> is thrown and no
   * valid ticket is generated. How the repository treats an attempt to connect
   * with <code>null</code> <code>credentials</code> and or a
   * <code>null</code> <code>workspaceName</code> is a matter of
   * implementation. Possibilities include allowing an anonymous connection in
   * cases of <code>null credentials</code> and connecting to a default
   * workspace in cases of a <code>null workspaceName</code>.<p/><b>Level
   * 2: </b> <p/>Returns a <code>{@link javax.jcr.xa.XATicket}</code> object
   * in order to support transactions. <p/>
   * 
   * @param credentials
   *          The credentials of the user
   * @param workspaceName
   *          the name of a workspace.
   * @return a valid ticket for the user to access the repository.
   * @throws LoginException
   *           If the login authentication fails.
   * @throws NoSuchWorkspaceException
   *           If the specified <code>workspaceName</code> is not recognized.
   */
  public Ticket login(Credentials credentials, String workspaceName)
      throws LoginException, NoSuchWorkspaceException {
    log.debug("login");
    if (workspaceName == null) {
      if (defaultWorkspaceName == null)
        throw new NoSuchWorkspaceException(
            "Both workspace and default-workspace name are null! ");
      else
        workspaceName = defaultWorkspaceName;
    }

    if (credentials != null) {
      if (workspaceContainers.get(workspaceName) == null)
        throw new NoSuchWorkspaceException("Workspace '" + workspaceName
            + "' not found ");
      
      boolean success = false;
      try {
        success = organizationService.authenticate(credentials
            .getUserId(), new String(credentials.getPassword()));
      } catch (Exception e1) {
        throw new LoginException("Can not authorize the user : "
            + credentials.getUserId(), e1);        
      }
      if (!success)
        throw new LoginException("Can not authorize the user : "
            + credentials.getUserId());
    }
    try {
      return new TicketImpl(this, credentials, workspaceName);
    } catch (RepositoryException e) {
      throw new LoginException("Unexpected problems with Ticket creation.", e);
    }
  }

  public WorkspaceContainer getContainer(String workspaceName)
      throws RepositoryException {
    WorkspaceContainer container = (WorkspaceContainer) workspaceContainers
        .get(workspaceName);
    if (container == null)
      throw new RepositoryException("No container found for workspace '"
          + workspaceName + "'!");
    return container;
  }

  public RepositoryManager getRepositoryManager() {
    return repositoryManager;
  }

  public void setRepositoryManager(RepositoryManager repositoryManager) {
    this.repositoryManager = repositoryManager;
  }

  public void setWorkspaceContainers(HashMap containers) {
    this.workspaceContainers = containers;
  }

  public void setDefaultWorkspaceName(String name) {
    this.defaultWorkspaceName = name;
  }

  public String getName() {
    return name;
  }
}