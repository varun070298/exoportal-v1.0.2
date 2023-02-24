/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.filesystem;


import org.apache.commons.codec.binary.Base64;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.core.ReferenceableNodeLocation;
import org.exoplatform.services.jcr.impl.storage.BaseRepositoryManager;
import javax.jcr.PathNotFoundException;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: SimpleFsRepositoryManagerImpl.java,v 1.6 2004/11/02 18:34:39 geaz Exp $
 */

public class SimpleFsRepositoryManagerImpl extends BaseRepositoryManager {

  private static long id = System.currentTimeMillis();


    public SimpleFsRepositoryManagerImpl(String name) {
    }

  synchronized public String generateUUID(NodeData context) {
    try {
      String uuid = new String(Base64.encodeBase64(context.getPath().getBytes()));
      return uuid;
    } catch (Exception e) {
      throw new RuntimeException("Unexpected IO Exception " + e.getMessage());
    }
  }

  synchronized public ReferenceableNodeLocation getLocationByUUID(String workspaceContainerName, String UUID) throws PathNotFoundException {
    String path = new String(Base64.decodeBase64(UUID.getBytes()));
    return new ReferenceableNodeLocation(path, UUID, path);
  }

  synchronized public ReferenceableNodeLocation getLocationByPath(String workspaceContainerName, String path) throws PathNotFoundException {
    String uuid = new String(Base64.encodeBase64(path.getBytes()));
    return new ReferenceableNodeLocation(path, uuid, path);
  }

  synchronized public void addLocation(String workspaceContainerName, String UUID, String path, boolean isNew) {
  }

  synchronized public void deleteLocationByPath(String workspaceContainerName, String path) {
  }

  synchronized public void deleteLocationByUUID(String workspaceContainerName, String UUID) {
  }

  synchronized public void addWorkspaceContainer(String workspaceContainerName) {
  }

/*
  public String getPath(String workspaceContainerName, String UUID) {
     return new String(Base64.decodeBase64(UUID.getBytes()))+"/jcr:content";
  }

  public void addPath(String workspaceContainerName, String UUID, String path) {
  }

  public void deletePath(String workspaceContainerName, String UUID) {
  }


  public Set getReferencedPaths(String workspaceContainerName, String refUUID) throws PathNotFoundException {
    Set paths = new HashSet();
    paths.add(Base64.encodeBase64(refUUID.getBytes())+"/jcr:content");
    return paths;
  }

  synchronized public void addReference(String workspaceContainerName, String relatedUUID, String path) {
  }

  synchronized public void deleteReference(String workspaceContainerName, String refPath) {
  }

  public String getRelatedUUID(String workspaceContainerName, String refPath) {
    return new String(Base64.encodeBase64(refPath.getBytes())+"/jcr:content");
  }
*/
}
