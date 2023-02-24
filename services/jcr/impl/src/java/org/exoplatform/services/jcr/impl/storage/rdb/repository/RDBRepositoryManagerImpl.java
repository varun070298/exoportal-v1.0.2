/****************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.jcr.impl.storage.rdb.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.ItemExistsException;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import net.sf.hibernate.Session;
import net.sf.hibernate.type.StringType;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.core.ReferenceableNodeLocation;
import org.exoplatform.services.jcr.impl.storage.BaseRepositoryManager;
import org.exoplatform.services.jcr.impl.storage.rdb.repository.data.ContainerRecord;
import org.exoplatform.services.jcr.impl.storage.rdb.repository.data.PathReference;
import org.exoplatform.services.jcr.impl.storage.rdb.repository.data.UUIDReference;
import org.exoplatform.services.log.LogUtil;
import org.exoplatform.services.database.HibernateServiceContainer;
import org.exoplatform.services.database.HibernateService;
import org.exoplatform.services.database.DatabaseService;

/**
 * Created by The eXo Platform SARL        .
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: RDBRepositoryManagerImpl.java,v 1.1 2004/11/02 18:34:38 geaz Exp $
 */
public class RDBRepositoryManagerImpl extends BaseRepositoryManager {

   static private String[] MAPPINGS =
   {
     "org/exoplatform/services/jcr/impl/storage/rdb/repository/data/ContainerRecord.hbm.xml" ,
     "org/exoplatform/services/jcr/impl/storage/rdb/repository/data/UUIDReference.hbm.xml" ,
     "org/exoplatform/services/jcr/impl/storage/rdb/repository/data/PathReference.hbm.xml" ,
   } ;


    private static String FIND_LOCATION_BY_UUID = "from UUIDReference as ref where ref.uuid=? and ref.container.name=?";
    private static String FIND_LOCATION_BY_PATH = "from PathReference as ref where ref.path=? and ref.uuidRef.container.name=?";
    private static String FIND_CONTAINER = "from ContainerRecord as container where container.name=?";

    private static boolean initialized = false;

    protected Log log;
    private HibernateService hibernateService;
//    private Session hibernateSession;
    private String name;

    // ?
    private static long id = System.currentTimeMillis();

    public RDBRepositoryManagerImpl(String name, String dsName)  throws RepositoryException {

        log = LogUtil.getLog("org.exoplatform.services.jcr");
        this.name = name;
        log.debug("RDBRepositoryManagerImpl (String name, String dsName, HibernateServiceContainer hibernateContainer) instantiated. DS name ="+dsName);

        try {

           HibernateServiceContainer hibernateContainer = (HibernateServiceContainer)PortalContainer.
                       getInstance().getComponentInstanceOfType(HibernateServiceContainer.class);

           this.hibernateService = hibernateContainer.getHibernateService(dsName);

           hibernateService.addMappingFiles(MAPPINGS);

//           this.hibernateSession = hibernateService.openSession();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryException("RDBRepositoryManagerImpl(String name, String dsName) failed Reason: "+e);
        }

    }


    synchronized public String generateUUID(NodeData context) {
        // Temporary... Use hibernate for generate UUID ?
        return String.valueOf(++id);
    }

    synchronized public ReferenceableNodeLocation getLocationByUUID(String workspaceContainerName, String UUID)
        throws PathNotFoundException {
            log.debug("RDBRepositoryManagerImpl is finding location by UUID " + UUID);
            List refs;
            try {
                Session hibernateSession = hibernateService.openSession();
                Object[] values = { UUID, workspaceContainerName};
                StringType[] types = { new StringType(), new StringType() };
                refs = hibernateSession.find(FIND_LOCATION_BY_UUID, values, types);

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Hibernate throws Exception " + e);
            }
            if (refs.isEmpty())
                throw new PathNotFoundException("UUID " + UUID + " not found in " + workspaceContainerName);
            UUIDReference ref = (UUIDReference)refs.get(0);
            ReferenceableNodeLocation loc = new ReferenceableNodeLocation(ref.getRealPath(), UUID, ref.getRealPath(), ref.getRefPaths());
            log.debug("RDBRepositoryManagerImpl found location by UUID " + UUID + " " + loc);

            return loc;
    }

    synchronized public ReferenceableNodeLocation getLocationByPath(String workspaceContainerName, String path)
        throws PathNotFoundException {
            List refs;
            try {
                Session hibernateSession = hibernateService.openSession();

                log.debug("RDBRepositoryManagerImpl is finding location by path " + path+" in workspace "+workspaceContainerName+" session: "+hibernateSession);
                Object[] values = { path, workspaceContainerName };
                StringType[] types = { new StringType(), new StringType() };
                refs = hibernateSession.find(FIND_LOCATION_BY_PATH, values, types);

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Hibernate throws Exception " + e);
            }
            if (refs.isEmpty())
                throw new PathNotFoundException("Path " + path + " not found in " + workspaceContainerName);
            PathReference ref = (PathReference)refs.get(0);
            ReferenceableNodeLocation loc = new ReferenceableNodeLocation(path, ref.getUuidRef().getUuid(), ref.getUuidRef().getRealPath(),
                ref.getUuidRef().getRefPaths());
            log.debug("RDBRepositoryManagerImpl found location by path " + path + " " + loc);
            return loc;
    }

    synchronized public void addLocation(String workspaceContainerName, String UUID, String path, boolean isNew)
        throws PathNotFoundException, ItemExistsException {
            log.debug("RDBRepositoryManagerImpl is adding reference " + path + " isNew= " + isNew + " to UUID: " + UUID +
                      " containerName: "+workspaceContainerName);
            try {
                Session hibernateSession = hibernateService.openSession();
                ContainerRecord containerRecord = new ContainerRecord();
                containerRecord.setName(workspaceContainerName);
                List containerRecords = hibernateSession.find(FIND_CONTAINER, workspaceContainerName, new StringType());
                if (!containerRecords.isEmpty())
                    containerRecord = (ContainerRecord)containerRecords.get(0);
                else {
                    throw new PathNotFoundException("RDBRepositoryManager.addLocation() Container "+workspaceContainerName+" not found");
//                    session.save(containerRecord);
//                    log.debug("NEW ContainerRecord "+containerRecord.getId()+" "+workspaceContainerName);
                }
                UUIDReference uuidRef;
                Object[] values = { UUID, workspaceContainerName};
                StringType[] types = { new StringType(), new StringType() };
                List uuidRefs = hibernateSession.find(FIND_LOCATION_BY_UUID, values, types);
                if (isNew) {
                    if (!uuidRefs.isEmpty())
                        throw new ItemExistsException("UUID " + UUID + " already exists in " + workspaceContainerName);
                    uuidRef = new UUIDReference();
                    uuidRef.setUuid(UUID);
                    uuidRef.setRealPath(path);
                    uuidRef.setContainer(containerRecord);

                    PathReference pathRef = new PathReference();
                    pathRef.setPath(path);

                    Set pathRefs = new HashSet();
                    pathRefs.add(pathRef);
                    uuidRef.setReferences(pathRefs);
                    hibernateSession.save(uuidRef);

                    pathRef.setUuidRef(uuidRef);
                    hibernateSession.save(pathRef);

                    hibernateSession.flush();
                } else {
                    if (uuidRefs.isEmpty())
                        throw new PathNotFoundException("UUID " + UUID + " not found in " + workspaceContainerName);
                    uuidRef = (UUIDReference)uuidRefs.get(0);

//                    Object[] values1 = {path, workspaceContainerName};
//                    List pathRefs = session.find(FIND_LOCATION_BY_PATH, values1, types);
//                    if (pathRefs.isEmpty())
//                        throw new ItemExistsException("Path " + path + " already exists in " + workspaceContainerName);
                    PathReference pathRef = new PathReference();
                    pathRef.setUuidRef(uuidRef);
                    pathRef.setPath(path);
                    hibernateSession.save(pathRef);

                    Set refs = uuidRef.getReferences();
                    refs.add(pathRef);
                    uuidRef.setReferences(refs);

                    hibernateSession.flush();
                }
            } catch (PathNotFoundException e) {
                throw e;
            } catch (ItemExistsException e) {
                throw e;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Hibernate throws Exception " + e);
            }
            log.debug("RDBRepositoryManagerImpl added reference " + path + " isNew= " + isNew + " to " + UUID);
    }

    synchronized public void deleteLocationByPath(String workspaceContainerName, String path) {
        log.debug("RDBRepositoryManagerImpl is deleting location by path " + path);
        try {
            ReferenceableNodeLocation loc = this.getLocationByPath(workspaceContainerName, path);
            deleteLocationByUUID(workspaceContainerName, loc.getUUID());
        } catch (PathNotFoundException e) {
            //        log.debug("RDBRepositoryManagerImpl is deleting location by path "+path);
        }
    }

    synchronized public void deleteLocationByUUID(String workspaceContainerName, String UUID) {
        log.debug("RDBRepositoryManagerImpl is deleting location by UUID " + UUID);
        try {
            Session hibernateSession = hibernateService.openSession();
            Object[] values = { UUID, workspaceContainerName};
            StringType[] types = { new StringType(), new StringType() };
            List uuidRefs = hibernateSession.find(FIND_LOCATION_BY_UUID, values, types);
            if (uuidRefs.isEmpty())
                return;
            else
                hibernateSession.delete((UUIDReference)uuidRefs.get(0));

//        } catch (PathNotFoundException e) {
        } catch (Exception e) {
            throw new RuntimeException("Hibernate throws Exception " + e);
        }
    }

  synchronized public void addWorkspaceContainer(String workspaceContainerName) {
    try {
        Session hibernateSession = hibernateService.openSession();
        ContainerRecord containerRecord = new ContainerRecord();
        containerRecord.setName(workspaceContainerName);
        List containerRecords = hibernateSession.find(FIND_CONTAINER, workspaceContainerName, new StringType());
        if (!containerRecords.isEmpty()) {
           containerRecord = (ContainerRecord)containerRecords.get(0);
           log.debug("RDBRepositoryManagerImpl.addWorkspaceContainer() "+workspaceContainerName+" already exists");
        } else {
           hibernateSession.save(containerRecord);
           hibernateSession.flush();
           log.debug("RDBRepositoryManagerImpl.addWorkspaceContainer() "+containerRecord.getId()+" "+workspaceContainerName);
        }

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Hibernate throws Exception " + e);
    }
  }

}
