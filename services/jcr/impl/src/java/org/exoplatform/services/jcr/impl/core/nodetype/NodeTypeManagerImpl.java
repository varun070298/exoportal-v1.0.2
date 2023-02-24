/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.nodetype;

import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import java.util.Collections;


import java.util.Collection;


import java.util.ArrayList;
import java.util.HashMap;
import javax.jcr.RepositoryException;
import org.exoplatform.services.jcr.impl.Constants;
import org.exoplatform.services.jcr.impl.util.EntityCollection;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeTypeManagerImpl.java,v 1.7 2004/08/05 15:55:15 benjmestrallet Exp $
 */

/**
 * 6.10.4
 * Discovering which node types are available in a particular
 * implementation begins with the NodeTypeManager object, which is
 * acquired via the Workspace:
 */

public class NodeTypeManagerImpl implements NodeTypeManager {

  private static NodeTypeManager instance;
  private HashMap types;
  private HashMap mixTypes;

  private NodeTypeManagerImpl() {

    types = new HashMap();
    mixTypes = new HashMap();

    types.put("nt:base", new org.exoplatform.services.jcr.impl.core.nodetype.nt.Base());
    types.put("nt:default", new org.exoplatform.services.jcr.impl.core.nodetype.nt.Default());
    types.put("nt:hierarchyItem", new org.exoplatform.services.jcr.impl.core.nodetype.nt.HierarchyItem());
    types.put("nt:content", new org.exoplatform.services.jcr.impl.core.nodetype.nt.Content());
    types.put("nt:file", new org.exoplatform.services.jcr.impl.core.nodetype.nt.File());
    types.put("nt:folder", new org.exoplatform.services.jcr.impl.core.nodetype.nt.Folder());
    types.put("nt:hierarchyNode", new org.exoplatform.services.jcr.impl.core.nodetype.nt.HierarchyNode());
    types.put("nt:nodeType", new org.exoplatform.services.jcr.impl.core.nodetype.nt.NodeType());
    types.put("nt:propertyDef", new org.exoplatform.services.jcr.impl.core.nodetype.nt.PropertyDefNT());
    types.put("nt:childNodeDef", new org.exoplatform.services.jcr.impl.core.nodetype.nt.ChildNodeDef());
    types.put("nt:versionHistory", new org.exoplatform.services.jcr.impl.core.nodetype.nt.VersionHistory());
    types.put("nt:version", new org.exoplatform.services.jcr.impl.core.nodetype.nt.Version());
    types.put("nt:query", new org.exoplatform.services.jcr.impl.core.nodetype.nt.Query());
    types.put("nt:mimeResource", new org.exoplatform.services.jcr.impl.core.nodetype.nt.MimeResource());

    types.put("exo:jcrdocfile", new org.exoplatform.services.jcr.impl.core.nodetype.exo.JcrDocFile());
    types.put("exo:mockNodeType", new org.exoplatform.services.jcr.impl.core.nodetype.exo.MockNodeType());

    mixTypes.put("mix:referenceable", new org.exoplatform.services.jcr.impl.core.nodetype.mix.Referenceable());
  }

  public static NodeTypeManager getInstance() {
    if (instance == null)
      instance = new NodeTypeManagerImpl();
    return instance;
  }


  /**
   * 6.10.4
   * Returns all available node types.
   */
  public NodeTypeIterator getAllNodeTypes() {
    EntityCollection ec = new EntityCollection(new ArrayList(types.values()));
    ec.addAll(new ArrayList(mixTypes.values()));
    return ec;
  }

  /**
   * 6.10.4
   * Returns the NodeType specified by nodeTypeName. If no
   * node type by that name is registered, a NoSuchNodeTypeException is thrown.
   */
  public NodeType getNodeType(String nodeTypeName) throws NoSuchNodeTypeException {
    NodeType nt = (NodeType) types.get(nodeTypeName);
    if (nt == null)
      nt = (NodeType) mixTypes.get(nodeTypeName);

    if (nt == null)
      throw new NoSuchNodeTypeException("NodeTypeManager.getNodeType(): NodeType '" + nodeTypeName + "' not found.");
    else
      return nt;
  }

  /**
   * Returns an iterator over all available primary node types.
   *
   * @return An <code>NodeTypeIterator</code>.
   * @throws RepositoryException if an error occurs.
   */
  public NodeTypeIterator getPrimaryNodeTypes() throws RepositoryException {
    return new EntityCollection(new ArrayList(types.values()));
  }

  /**
   * Returns an iterator over all available mixin node types.
   *
   * @return An <code>NodeTypeIterator</code>.
   * @throws RepositoryException if an error occurs.
   */
  public NodeTypeIterator getMixinNodeTypes() throws RepositoryException {
    return new EntityCollection(new ArrayList(mixTypes.values()));
  }

  public void addPrimaryNodeType(NodeTypeImpl type) throws NoSuchNodeTypeException {
    types.put(type.getName(), type);
  }

  public void addMixinNodeType(NodeTypeImpl type) throws NoSuchNodeTypeException {
    mixTypes.put(type.getName(), type);
  }

}
