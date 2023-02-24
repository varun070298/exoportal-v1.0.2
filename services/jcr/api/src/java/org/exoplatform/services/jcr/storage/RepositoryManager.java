/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.storage;


import java.util.Calendar;
import java.util.Set;

import javax.jcr.PathNotFoundException;
import javax.jcr.ItemExistsException;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.core.ReferenceableNodeLocation;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: RepositoryManager.java,v 1.5 2004/11/02 18:29:51 geaz Exp $
 */

public interface RepositoryManager {

  Calendar getCurrentTime();

  String generateUUID(NodeData context);

  ReferenceableNodeLocation getLocationByUUID(String workspaceContainerName, String UUID) throws PathNotFoundException;

  ReferenceableNodeLocation getLocationByPath(String workspaceContainerName, String path) throws PathNotFoundException;

  void addLocation(String workspaceContainerName, String UUID, String path, boolean isNew) throws PathNotFoundException, ItemExistsException;

  void deleteLocationByPath(String workspaceContainerName, String path);

  void deleteLocationByUUID(String workspaceContainerName, String UUID);

  void addWorkspaceContainer(String workspaceContainerName);
}
