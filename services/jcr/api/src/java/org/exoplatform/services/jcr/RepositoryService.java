/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import java.util.Properties;


import java.util.Collection;
import javax.jcr.Workspace;
import org.exoplatform.services.jcr.config.RepositoryServiceConfig;
import org.exoplatform.services.jcr.storage.WorkspaceContainer;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:benjamin.mestrallet@exoplatform.com">Benjamin Mestrallet</a>
 * @version $Id: RepositoryService.java,v 1.7 2004/08/23 11:11:17 geaz Exp $
 */

public interface RepositoryService {
  Repository getRepository() throws RepositoryException;

  Repository getRepository(String name) throws RepositoryException;

  RepositoryServiceConfig getConfig();
}
