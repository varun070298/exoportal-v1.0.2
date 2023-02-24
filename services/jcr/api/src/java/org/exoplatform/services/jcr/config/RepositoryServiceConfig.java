/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.config;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: RepositoryServiceConfig.java,v 1.6 2004/09/22 13:54:55 geaz Exp $
 */

public interface RepositoryServiceConfig {

  WorkspaceEntry[] getWorkspaceEntries();

  WorkspaceEntry getWorkspaceEntry(String repositoryName, String name);

  ContainerEntry[] getSupportedContainerEntries();

  ContainerEntry getContainerEntry(String name) throws RepositoryConfigurationException;

  RepositoryEntry[] getRepositoryEntries();

  RepositoryEntry getRepositoryEntry(String name) throws RepositoryConfigurationException;

  RepositoryManagerEntry[] getSupportedRepositoryManagerEntries();

  RepositoryManagerEntry getRepositoryManagerEntry(String name) throws RepositoryConfigurationException;

  String getDefaultRepositoryName();
}
