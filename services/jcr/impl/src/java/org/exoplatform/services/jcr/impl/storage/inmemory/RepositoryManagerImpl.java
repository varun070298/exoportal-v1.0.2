/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.storage.inmemory;


import javax.jcr.PathNotFoundException;
import javax.jcr.ItemExistsException;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.codec.binary.Base64;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.core.ReferenceableNodeLocation;
import org.exoplatform.services.jcr.impl.storage.BaseRepositoryManager;
import org.exoplatform.services.log.LogUtil;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: RepositoryManagerImpl.java,v 1.5 2004/11/02 18:34:39 geaz Exp $
 */

public class RepositoryManagerImpl extends BaseRepositoryManager {

  protected Log log;

  private static long id = System.currentTimeMillis();

  private Map workspaceContainers;
  private Map referenceTables;

  public RepositoryManagerImpl(String name) {
    log = LogUtil.getLog("org.exoplatform.services.jcr");
    workspaceContainers = new HashMap();
    referenceTables = new HashMap();
  }

  synchronized public String generateUUID(NodeData context) {
    return String.valueOf(++id);
  }

  synchronized public ReferenceableNodeLocation getLocationByUUID(String workspaceContainerName, String UUID)  throws PathNotFoundException {

    log.debug("RepositoryManagerImpl is finding location by UUID "+UUID);

    Map container = getWorkspaceContainer(workspaceContainerName);
    if(container == null)
      throw new PathNotFoundException("Workspace container '"+workspaceContainerName+"' not found.");

    ReferenceableNodeLocation loc = (ReferenceableNodeLocation)container.get(UUID);
    log.debug("RepositoryManagerImpl found location by UUID "+UUID+" "+loc);
    if (loc == null)
        throw new PathNotFoundException("UUID '"+UUID+"' not found.");

    return loc;

  }

  synchronized public ReferenceableNodeLocation getLocationByPath(String workspaceContainerName, String path)  throws PathNotFoundException {

    log.debug("RepositoryManagerImpl is finding location by path "+path);

     Map refTable = getReferenceTable(workspaceContainerName);
     if(refTable == null) {
        refTable = new HashMap();
     }

    String uuid = (String) refTable.get(path);

    if ( uuid == null)
        throw new PathNotFoundException("Path '"+path+"' not found.");

    return getLocationByUUID(workspaceContainerName, uuid);

  }

  synchronized public void addLocation(String workspaceContainerName, String UUID, String path, boolean isNew) throws PathNotFoundException, ItemExistsException {

    Map container = getWorkspaceContainer(workspaceContainerName);
    if(container == null) {
        throw new PathNotFoundException("RepositoryManagerImpl.addLocation() Container "+workspaceContainerName+" not found");
//      container = new HashMap();
//      workspaceContainers.put(workspaceContainerName, container);
    }

    Map refTable = getReferenceTable(workspaceContainerName);
    if(refTable == null) {
        throw new PathNotFoundException("RepositoryManagerImpl.addLocation() Container "+workspaceContainerName+" not found");

//      refTable = new HashMap();
//      referenceTables.put(workspaceContainerName, refTable);
    }

    String realPath;
    refTable.put(path, UUID);
    if(isNew) {
      container.put(UUID, new ReferenceableNodeLocation(path, UUID, path));
    } else {
      ReferenceableNodeLocation loc = (ReferenceableNodeLocation)container.get(UUID);
      loc.addReferencedPath(path);
    }

    log.debug("RepositoryManagerImpl added reference "+path+" isNew= "+isNew+" to "+UUID+" size  "+refTable.size());

  }

  synchronized public void deleteLocationByPath(String workspaceContainerName, String path) {

    Map refTable = getReferenceTable(workspaceContainerName);
    if(refTable == null)
        return;

    String uuid = (String)refTable.get(path);
    if(uuid == null)
        return;

    log.debug("RepositoryManagerImpl is deleting location by path "+path);

    deleteLocationByUUID(workspaceContainerName, uuid);

  }

  synchronized public void deleteLocationByUUID(String workspaceContainerName, String UUID) {

    Map refTable = getReferenceTable(workspaceContainerName);
    if(refTable == null)
        return;

    log.debug("RepositoryManagerImpl is deleting location by UUID "+UUID);

    Iterator entries = refTable.keySet().iterator();
    ArrayList paths = new ArrayList();
    while (entries.hasNext()) {
      String testPath = (String) entries.next();
      String testUUID = (String) refTable.get(testPath);
      if(testUUID.equals(UUID))
        paths.add(testPath);
//        refTable.remove(testPath);
    }

    for(int i=0; i<paths.size(); i++) {
        log.debug("RepositoryManagerImpl deleted path "+paths.get(i));
        refTable.remove(paths.get(i));
    }

    Map container = getWorkspaceContainer(workspaceContainerName);
    if(container != null)
        container.remove(UUID);

  }

  synchronized public void addWorkspaceContainer(String workspaceContainerName) {

    Map container = getWorkspaceContainer(workspaceContainerName);
    if(container == null) {
      container = new HashMap();
      workspaceContainers.put(workspaceContainerName, container);
      log.debug("RepositoryManagerImpl.addWorkspaceContainer() Container "+workspaceContainerName+" added");
    }  else {
      log.debug("RepositoryManagerImpl.addWorkspaceContainer() Container "+workspaceContainerName+" already exists");
    }

    Map refTable = getReferenceTable(workspaceContainerName);
    if(refTable == null) {
      refTable = new HashMap();
      referenceTables.put(workspaceContainerName, refTable);
      log.debug("RepositoryManagerImpl.addWorkspaceContainer() Ref table "+workspaceContainerName+" added");
    }  else {
      log.debug("RepositoryManagerImpl.addWorkspaceContainer() Ref table "+workspaceContainerName+" already exists");
    }

  }


  private Map getWorkspaceContainer(String workspaceContainerName) {
    return (Map) workspaceContainers.get(workspaceContainerName);
  }

  private Map getReferenceTable(String workspaceContainerName) {
    return (Map) referenceTables.get(workspaceContainerName);
  }

}
