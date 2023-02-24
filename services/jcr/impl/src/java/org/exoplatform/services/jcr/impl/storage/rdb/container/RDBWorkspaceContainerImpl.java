/**
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.jcr.impl.storage.rdb.container;

import java.util.*;

import javax.jcr.*;

import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import net.sf.hibernate.Session;
import net.sf.hibernate.type.LongType;
import net.sf.hibernate.type.StringType;

import org.apache.commons.logging.Log;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.core.ItemLocation;
import org.exoplatform.services.jcr.core.NodeData;
import org.exoplatform.services.jcr.impl.core.NodeImpl;
import org.exoplatform.services.jcr.impl.core.PropertyImpl;
//import org.exoplatform.services.jcr.impl.storage.rdb.HibernateProviderImpl;
import org.exoplatform.services.jcr.storage.WorkspaceContainer;
//import org.exoplatform.services.jcr.storage.rdb.DataSourceProvider;
//import org.exoplatform.services.jcr.storage.rdb.HibernateProvider;
import org.exoplatform.services.log.LogUtil;

import org.exoplatform.services.jcr.impl.storage.rdb.container.data.*;
//import org.exoplatform.services.jcr.impl.storage.rdb.HibernateServiceAdapter;
import org.exoplatform.services.database.HibernateServiceContainer;
import org.exoplatform.services.database.HibernateService;

//import org.exoplatform.services.jcr.impl.storage.rdb.HibernateServiceAdapter;



/**
 * Created by The eXo Platform SARL        .
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: RDBWorkspaceContainerImpl.java,v 1.1 2004/11/02 18:34:38 geaz Exp $
 */
public class RDBWorkspaceContainerImpl implements WorkspaceContainer {

   static private String[] MAPPINGS =
   {
     "org/exoplatform/services/jcr/impl/storage/rdb/container/data/ValueRecord.hbm.xml" ,
     "org/exoplatform/services/jcr/impl/storage/rdb/container/data/PropertyRecord.hbm.xml",
     "org/exoplatform/services/jcr/impl/storage/rdb/container/data/NodeRecord.hbm.xml",
   } ;


    private static String FIND_NODE = "from NodeRecord as node where node.path=?";
    private static String FIND_CHILDREN = "from NodeRecord as node where node.parentId=?";

    private static boolean initialized = false;

    //    private static long ROOT_ID = Long.MIN_VALUE;
    private Log log;
    private String name;
    private Long rootId;
    private HibernateService hibernateService;

    public RDBWorkspaceContainerImpl(String name, String dsName, String rootNodeType)  throws RepositoryException {

        log = LogUtil.getLog("org.exoplatform.services.jcr");

        this.name = name;

        try {


           HibernateServiceContainer hibernateContainer = (HibernateServiceContainer)PortalContainer.
                       getInstance().getComponentInstanceOfType(HibernateServiceContainer.class);

           log.debug("RDBWorkspacecontainerImpl (String name, String dsName, String rootNodeType) hibernate container ="+hibernateContainer);

           this.hibernateService = hibernateContainer.getHibernateService(dsName);

           hibernateService.addMappingFiles(MAPPINGS);

          init(name, rootNodeType);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryException("RDBWorkspaceContainerImpl() failed Reason: "+e);
        }

    }

    public RDBWorkspaceContainerImpl(String name, String dsName) throws RepositoryException {
        this(name, dsName, "nt:default");
    }

    private void init(String name, String rootNodeType) throws RepositoryException {

            List nodes;
            Session hibernateSession;
            try {
               hibernateSession = hibernateService.openSession();
               nodes = hibernateSession.find(FIND_NODE, ROOT_PATH, new StringType());
            } catch (Exception e) {
                throw new RepositoryException("Hibernate failed: " + e);
            }

            if (nodes.isEmpty()) {
                NodeRecord nodeRecord = new NodeRecord();
                nodeRecord.setPath(ROOT_PATH);
                nodeRecord.setParentId(new Long(0l));
                HashSet rootProps = new HashSet();
                PropertyImpl rootProp = new PropertyImpl("/jcr:primaryType",
                    new StringValue(rootNodeType), PropertyType.STRING);
                PropertyRecord rootPropRecord = getPropertyRecord(rootProp);
                rootProps.add(rootPropRecord);
                nodeRecord.setProperties(rootProps);
                try {
//                   hibernateSession.beginTransaction();
                   rootId = (Long)hibernateSession.save(nodeRecord);
                   hibernateSession.save(rootPropRecord);
                   hibernateSession.save((StringValueRecord)rootPropRecord.getValues().iterator().next());
                   hibernateSession.flush();
                } catch (Exception e) {
                   throw new RepositoryException("Hibernate failed: " + e);
                }
                log.debug("RDB ContainerImpl(), created root node " + rootId);
            } else {
                rootId = ((NodeRecord)nodes.get(0)).getId();
            }

    }

    public String getName() {
        return name;
    }

    public NodeData getRootNode() throws RepositoryException {
        return getNodeByPath(ROOT_PATH);
    }

    public NodeData getNodeByPath(String absPath) throws RepositoryException {
        log.debug("RDB Container looks up node : " + absPath + " in workspace container: " + name);
        try {
            Session hibernateSession = hibernateService.openSession();
            List nodes = hibernateSession.find(FIND_NODE, absPath, new StringType());
            if (nodes.isEmpty())
                return null;
            NodeRecord nodeRecord = (NodeRecord)nodes.get(0);
            List properties = new ArrayList();
            Iterator props = nodeRecord.getProperties().iterator();
            while (props.hasNext()) {
                PropertyRecord propertyRecord = (PropertyRecord)props.next();
                properties.add(getProperty(nodeRecord.getPath() + "/" + propertyRecord.getName(), propertyRecord));
            }
            return new NodeImpl(nodeRecord.getPath(), properties);
        } catch (Exception e) {
            throw new RepositoryException("RDB Container.getNodeByPath() failed: <" + absPath + "> " + e.getMessage());
        }
    }

    public List getChildren(String path) throws RepositoryException {
        try {
            Session hibernateSession = hibernateService.openSession();
            List childrenPaths = new ArrayList();
            List nodes = hibernateSession.find(FIND_NODE, path, new StringType());
            if (nodes.isEmpty())
                return childrenPaths;
            Long parentId = ((NodeRecord)nodes.get(0)).getId();
            List children = hibernateSession.find(FIND_CHILDREN, parentId, new LongType());
            for (int i = 0; i < children.size(); i++) {
                NodeRecord nodeRecord = (NodeRecord)children.get(i);
                childrenPaths.add(nodeRecord.getPath());
            }
            return childrenPaths;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryException("RDB Container.getChildren() failed: " + e.getMessage());
        }
    }

    public QueryResult getQueryResult(Query query) {
        return null;
    }

    synchronized public void add(Node node) throws RepositoryException, ItemExistsException {      
        if (node.getPath().equals("/"))
            return;
        log.debug("RDB Container is adding " + node + " to workspace container: " + name);
        try {
            Session hibernateSession = hibernateService.openSession();
            String parentPath = new ItemLocation(node.getPath()).getParentPath();
            List parentNodes = hibernateSession.find(FIND_NODE, parentPath, new StringType());
            if (parentNodes.isEmpty()) {
                log.warn("RDBContainer add() failed. Parent for " + node.getPath() + " not found. NodesModificationManager should proccess this!");
                return;
            }
//                throw new RepositoryException("RDBContainer add() failed. Parent for " + node.getPath() + " not found.");
            Long parentId = ((NodeRecord)parentNodes.get(0)).getId();
            NodeImpl nodeImpl = (NodeImpl)node;

            NodeRecord nodeRecord = new NodeRecord();
            nodeRecord.setPath(node.getPath());
            nodeRecord.setParentId(parentId);
            hibernateSession.save(nodeRecord);

            List props = nodeImpl.getPermanentProperties();
            Set propertyRecords = new HashSet();
            for (int i = 0; i < props.size(); i++) {
                PropertyImpl prop = (PropertyImpl)props.get(i);
                PropertyRecord propertyRecord = getPropertyRecord(prop);

                propertyRecords.add(propertyRecord);
                hibernateSession.save(propertyRecord);

                Iterator valueRecords = propertyRecord.getValues().iterator();
                while(valueRecords.hasNext()) {
                    hibernateSession.save((ValueRecord) valueRecords.next());
                }
            }

            nodeRecord.setProperties(propertyRecords);
            hibernateSession.update(nodeRecord);

            hibernateSession.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryException("RDB Container.add() failed:  " + e.getMessage());
        }
        log.debug("RDB Container added " + node + " to workspace container: " + name);        
    }

    synchronized public void delete(String absPath) throws RepositoryException {
        try {
            Session hibernateSession = hibernateService.openSession();
            hibernateSession.delete(FIND_NODE, absPath, new StringType());
//            hibernateSession.flush();
        } catch (Exception e) {
            throw new RepositoryException("RDB Container.delete() failed: " + e.getMessage());
        }
        log.debug("RDB Container removed " + absPath);
    }

    synchronized public void update(Node node) throws RepositoryException {
        try {
            Session hibernateSession = hibernateService.openSession();
            List nodes = hibernateSession.find(FIND_NODE, node.getPath(), new StringType());
            if (nodes.isEmpty())
                throw new RepositoryException("RDB Container could not find node for update " + node);
            NodeRecord nodeRecord = (NodeRecord)nodes.get(0);
            NodeImpl nodeImpl = (NodeImpl)node;
            List props = nodeImpl.getPermanentProperties();
            Set propertyRecords = new HashSet();
            for (int i = 0; i < props.size(); i++) {
                PropertyImpl prop = (PropertyImpl)props.get(i);
                propertyRecords.add(getPropertyRecord(prop));
            }
            nodeRecord.setProperties(propertyRecords);
            hibernateSession.update(nodeRecord);
            hibernateSession.flush();
        } catch (Exception e) {
            throw new RepositoryException("RDB Container.update() failed: " + e.getMessage());
        }
        log.debug("RDB Container updated " + node);
    }

    private PropertyImpl getProperty(String path, PropertyRecord propertyRecord) throws RepositoryException, ValueFormatException {
        Value[] values = new Value[propertyRecord.getValues().size()];
        Iterator valueRecords = propertyRecord.getValues().iterator();
        int i = 0;
        while (valueRecords.hasNext()) {
            ValueRecord valueRecord = (ValueRecord)valueRecords.next();
            if (valueRecord instanceof StringValueRecord)
                values[i++] = new StringValue(((StringValueRecord)valueRecord).getValue());
            else if (valueRecord instanceof DateValueRecord)
                values[i++] = new DateValue(((DateValueRecord)valueRecord).getValue());
            else if (valueRecord instanceof BooleanValueRecord)
                values[i++] = new BooleanValue(((BooleanValueRecord)valueRecord).getValue());
            else if (valueRecord instanceof LongValueRecord)
                values[i++] = new LongValue(((LongValueRecord)valueRecord).getValue());
            else if (valueRecord instanceof DoubleValueRecord)
                values[i++] = new DoubleValue(((DoubleValueRecord)valueRecord).getValue());
            else if (valueRecord instanceof ReferenceValueRecord)
                values[i++] = new ReferenceValue(((ReferenceValueRecord)valueRecord).getValue());
            else if (valueRecord instanceof SoftLinkValueRecord)
                values[i++] = new SoftLinkValue(((SoftLinkValueRecord)valueRecord).getValue());
            else if (valueRecord instanceof BinaryValueRecord)
                values[i++] = new BinaryValue(((BinaryValueRecord)valueRecord).getValue());
            else
                throw new ValueFormatException("Unknown Value record " + valueRecord.getClass());
        }
        return new PropertyImpl(path, values, values[0].getType());
    }

    private PropertyRecord getPropertyRecord(PropertyImpl property) throws RepositoryException, ValueFormatException {
        Value[] values = property.getValues();
        Set valueRecords = new HashSet();
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof StringValue) {
                try {
                   valueRecords.add(new StringValueRecord(values[i].getString()));
                } catch (ValueFormatException e) {
                   log.warn("RDBWorkspaceContainerImpl.getPropertyRecord() -- Value format exception for "+property+" Presume NULL value.");
                   valueRecords.add(new StringValueRecord());
                }
            } else if (values[i] instanceof DateValue) {
                try{
                  valueRecords.add(new DateValueRecord(values[i].getDate()));
                } catch (ValueFormatException e) {
                   log.warn("RDBWorkspaceContainerImpl.getPropertyRecord() -- Value format exception for "+property+" Presume NULL value.");
                   valueRecords.add(new DateValueRecord());
                }
            } else if (values[i] instanceof BooleanValue) {
                try {
                   valueRecords.add(new BooleanValueRecord(new Boolean(values[i].getBoolean())));
                } catch (ValueFormatException e) {
                   log.warn("RDBWorkspaceContainerImpl.getPropertyRecord() -- Value format exception for "+property+" Presume NULL value.");
                   valueRecords.add(new BooleanValueRecord());
                }
            } else if (values[i] instanceof LongValue) {
                try {
                   valueRecords.add(new LongValueRecord(new Long(values[i].getLong())));
                } catch (ValueFormatException e) {
                   log.warn("RDBWorkspaceContainerImpl.getPropertyRecord() -- Value format exception for "+property+" Presume NULL value.");
                   valueRecords.add(new StringValueRecord());
                }
            } else if (values[i] instanceof DoubleValue) {
                try {
                   valueRecords.add(new DoubleValueRecord(new Double(values[i].getDouble())));
                } catch (ValueFormatException e) {
                   log.warn("RDBWorkspaceContainerImpl.getPropertyRecord() -- Value format exception for "+property+" Presume NULL value.");
                   valueRecords.add(new DoubleValueRecord());
                }
            } else if (values[i] instanceof ReferenceValue) {
                try {
                   valueRecords.add(new ReferenceValueRecord(values[i].getString()));
                } catch (ValueFormatException e) {
                   log.warn("RDBWorkspaceContainerImpl.getPropertyRecord() -- Value format exception for "+property+" Presume NULL value.");
                   valueRecords.add(new ReferenceValueRecord());
                }
            } else if (values[i] instanceof SoftLinkValue) {
                try {
                   valueRecords.add(new SoftLinkValueRecord(values[i].getString()));
                } catch (ValueFormatException e) {
                   log.warn("RDBWorkspaceContainerImpl.getPropertyRecord() -- Value format exception for "+property+" Presume NULL value.");
                   valueRecords.add(new SoftLinkValueRecord());
                }
            } else if (values[i] instanceof BinaryValue) {
                try {
                   valueRecords.add(new BinaryValueRecord(values[i].getString().getBytes()));
                } catch (ValueFormatException e) {
                   log.warn("RDBWorkspaceContainerImpl.getPropertyRecord() -- Value format exception for "+property+" Presume NULL value.");
                   valueRecords.add(new BinaryValueRecord());
                }
            } else
//                 log.error("Unknown Value record for " + property);
                throw new ValueFormatException("Unknown Value record for " + property);
        }
        PropertyRecord propertyRecord = new PropertyRecord();
        propertyRecord.setName(property.getName());
        propertyRecord.setValues(valueRecords);
        return propertyRecord;
    }
}
