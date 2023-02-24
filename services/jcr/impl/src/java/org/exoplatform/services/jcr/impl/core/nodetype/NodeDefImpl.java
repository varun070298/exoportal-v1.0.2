/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.jcr.impl.core.nodetype;

import javax.jcr.nodetype.NodeDef;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.version.OnParentVersionAction;

//import javax.jcr.version.OnVersionPropertyAction;
//import javax.jcr.version.OnVersionNodeAction;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeDefImpl.java,v 1.4 2004/07/30 11:05:22 benjmestrallet Exp $
 */

public class NodeDefImpl implements NodeDef {

  private String name;
  private String defaultNodeType;
  private String[] requiredNodeTypes;
  private String[] defaultMixinTypes;
  private String[] requiredMixinTypes;
  private boolean autoCreate;
  private int onVersion;
  private boolean readOnly;
  private boolean primaryItem;
  private boolean multiple;
  private boolean mandatory;

  public NodeDefImpl(String name, String[] requiredNodeTypes, String defaultNodeType,
                     String[] requiredMixinTypes, String[] defaultMixinTypes,
                     boolean autoCreate, boolean mandatory, int onVersion, boolean readOnly, boolean primaryItem,
                     boolean multiple) {

    this.name = name;
    this.defaultNodeType = defaultNodeType;
    this.requiredNodeTypes = requiredNodeTypes;
    this.defaultMixinTypes = defaultMixinTypes;
    this.requiredMixinTypes = requiredMixinTypes;
    this.autoCreate = autoCreate;
    this.onVersion = onVersion;
    this.readOnly = readOnly;
    this.primaryItem = primaryItem;
    this.multiple = multiple;
    this.mandatory = mandatory;

  }

  // Residual definition
  public NodeDefImpl() {
    this.name = null;
    this.defaultNodeType = null;
    this.requiredNodeTypes = null;
    this.defaultMixinTypes = null;
    this.requiredMixinTypes = null;
    this.autoCreate = false;
    // ??
    this.onVersion = OnParentVersionAction.IGNORE;
    this.readOnly = false;
    this.primaryItem = false;
    this.multiple = true;
    this.mandatory = false;

  }

  /**
   * Gets the node type that contains the declaration of <i>this</i>
   * <code>NodeDef</code>.
   *
   * @return a <code>NodeType</code> object.
   */
  public NodeType getDeclaringNodeType() {
    try {
      return NodeTypeManagerImpl.getInstance().getNodeType(name);
    } catch (NoSuchNodeTypeException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  /**
   * 6.10.7
   * Gets the name of the child node. If null, this NodeDef
   * defines a residual set of child nodes. That is, it defines
   * the characteristics of all those child nodes with names
   * apart from the names explicitly used in other property or
   * child node definitions.
   */
  public String getName() {
    return name;
  }

  /**
   * 6.10.7
   */
  public NodeType[] getRequiredPrimaryTypes() {
    NodeType[] types = new NodeType[requiredNodeTypes.length];
    NodeTypeManager manager = NodeTypeManagerImpl.getInstance();
    try {
      for (int i = 0; i < types.length; i++)
        types[i] = manager.getNodeType(requiredNodeTypes[i]);
    } catch (NoSuchNodeTypeException e) {
      throw new RuntimeException(e.getMessage());
    }

    return types;
  }

  /**
   * 6.10.7
   */
  public NodeType getDefaultPrimaryType() /* throws NoSuchNodeTypeException*/ {
    try {
      return NodeTypeManagerImpl.getInstance().getNodeType(defaultNodeType);
    } catch (NoSuchNodeTypeException e) {
      throw new RuntimeException(e.getMessage());
    }

  }

  /**
   * Gets the minimum set of mixin node types that the child node must have.
   *
   * @return an array of <code>NodeType</code> objects.
   */
  public NodeType[] getRequiredMixinTypes() {
    NodeType[] types = new NodeType[requiredMixinTypes.length];
    NodeTypeManager manager = NodeTypeManagerImpl.getInstance();
    try {
      for (int i = 0; i < types.length; i++)
        types[i] = manager.getNodeType(requiredMixinTypes[i]);

    } catch (NoSuchNodeTypeException e) {
      throw new RuntimeException(e.getMessage());
    }

    return types;

//        throw new RuntimeException("MIXIN types does not supported yet!");
  }

  /**
   * Gets the default set of mixin node types that will be assigned to the child
   * node upon creation. This set of mixin node types must include <i>at least
   * those</i>, or subtypes of those, returned by
   * <code>getRequiredMixinTypes</code>.
   *
   * @return an array of <code>NodeType</code> objects.
   */
  public NodeType[] getDefaultMixinTypes() {
    NodeType[] types = new NodeType[defaultMixinTypes.length];
    NodeTypeManager manager = NodeTypeManagerImpl.getInstance();
    try {
      for (int i = 0; i < types.length; i++)
        types[i] = manager.getNodeType(defaultMixinTypes[i]);

    } catch (NoSuchNodeTypeException e) {
      throw new RuntimeException(e.getMessage());
    }

    return types;

//        throw new RuntimeException("MIXIN types does not supported yet!");
  }


  /**
   * 6.10.7
   */

  public boolean isAutoCreate() {
    return autoCreate;
  }

  /**
   * 6.10.7
   */
  public int getOnParentVersion() {
    return onVersion;
  }

  /**
   * 6.10.7
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * 6.10.7
   */
  public boolean isPrimaryItem() {
    return primaryItem;
  }

  /**
   * 6.10.7
   */
//    public boolean isMultiple() {
//        return multiple;
//    }

  /**
   * Reports whether this child node can have same-name siblings. In other
   * words, whether the parent node can have more than one child node of this
   * name.
   *
   * @return a boolean.
   */
  public boolean allowSameNameSibs() {
    return multiple;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public String toString() {
    return "NodeDefImpl: " + name;
  }

  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!(obj instanceof NodeDefImpl))
      return false;
    if (this.getName() == null)
      return ((NodeDefImpl) obj).getName() == null;
    return this.getName().equals(((NodeDefImpl) obj).getName());
  }

}

