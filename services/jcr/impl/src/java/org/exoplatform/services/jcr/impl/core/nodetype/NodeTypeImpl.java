/***************************************************************************
 * Copyright 2001-2003 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/
package org.exoplatform.services.jcr.impl.core.nodetype;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.exoplatform.services.log.LogUtil;

import javax.jcr.Value;
import javax.jcr.PropertyType;
import javax.jcr.nodetype.*;

/**
 * Created by The eXo Platform SARL        .
 *
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @version $Id: NodeTypeImpl.java,v 1.21 2004/09/10 11:14:47 geaz Exp $
 */
abstract public class NodeTypeImpl implements NodeType {

  protected Log log;

  protected String name;
  protected boolean mixin;
  protected NodeType[] declaredSupertypes;
  protected PropertyDef[] declaredPropertyDefs;
  protected NodeDef[] declaredNodeDefs;

  public NodeTypeImpl() {
    log = LogUtil.getLog("org.exoplatform.services.jcr");
  }


  /**
   * 6.10.5
   * Returns the name of the node type.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns <code>true</code> if this node type is a mixin node type.
   * Returns <code>false</code> if this node type is a primary node type.
   *
   * @return a boolean
   */
  public boolean isMixin() {
    return mixin;
  }

  /**
   * 6.10.5
   * Returns all supertypes of this node in the node type
   * inheritance hierarchy. This consists of all node types
   * encountered as one ascends the hierarchy to the
   * common base type nt:base.
   */
  public NodeType[] getSupertypes() {
    ArrayList stypesList = new ArrayList();

    fillSupertypes(stypesList, this);
    if (stypesList.size() > 0) {
      NodeType[] supertypes = new NodeType[stypesList.size()];
      for (int i = 0; i < stypesList.size(); i++) {
        supertypes[i] = (NodeType) stypesList.get(i);
      }
      return supertypes;
    }
    return new NodeType[0];
  }

  /**
   * 6.10.5
   * Returns the direct supertypes of this node in the node
   * type inheritance hierarchy, that is, those actually
   * declared in this node type. In single-inheritance
   * systems, this will always be an array of size 0 or 1. In
   * systems that support multiple inheritance of node types
   * this array may be of size greater than 1.
   * systems that support multiple inheritance of node types
   * this array may be of size greater than 1.
   */
  public NodeType[] getDeclaredSupertypes() {
    return declaredSupertypes;
  }

  /**
   * 6.10.5
   * Returns an array containing the property definitions of
   * this node type. This includes both those property
   * definitions actually declared in this node type and those
   * inherited from the supertypes of this node type.
   */
  public PropertyDef[] getPropertyDefs() {
    ArrayList propertyDefsList = new ArrayList();

    if (declaredPropertyDefs != null) {
      for (int i = 0; i < declaredPropertyDefs.length; i++) {
        propertyDefsList.add(declaredPropertyDefs[i]);
      }
    }
    NodeType[] supertypes = getSupertypes();
    if (supertypes != null) {
      for (int i = 0; i < supertypes.length; i++) {
        if (supertypes[i].getDeclaredPropertyDefs() != null) {
          for (int j = 0; j < supertypes[i].getDeclaredPropertyDefs().length; j++) {
            if (!propertyDefsList.contains(supertypes[i].getDeclaredPropertyDefs()[j]))
              propertyDefsList.add(supertypes[i].getDeclaredPropertyDefs()[j]);
          }
        }
      }
    }

    if (propertyDefsList.size() > 0) {
      PropertyDef[] propertyDefs = new PropertyDef[propertyDefsList.size()];
      for (int i = 0; i < propertyDefsList.size(); i++) {
        propertyDefs[i] = (PropertyDef) propertyDefsList.get(i);
      }
      return propertyDefs;
    }
    return null;
  }

  /**
   * 6.10.5
   * Returns an array containing the property definitions
   * actually declared in this node type.
   */
  public PropertyDef[] getDeclaredPropertyDefs() {
    return declaredPropertyDefs;
  }

  /**
   * 6.10.5
   * Returns an array containing the child node definitions of
   * this node type. This includes both those child node
   * definitions actually declared in this node type and those
   * inherited from the supertypes of this node type.
   */
  public NodeDef[] getChildNodeDefs() {
    ArrayList nodeDefsList = new ArrayList();

    if (declaredNodeDefs != null)
      for (int i = 0; i < declaredNodeDefs.length; i++)
        nodeDefsList.add(declaredNodeDefs[i]);

    NodeType[] supertypes = getSupertypes();
    if (supertypes != null) {
      for (int i = 0; i < supertypes.length; i++) {
        if (supertypes[i].getDeclaredChildNodeDefs() != null) {
          for (int j = 0; j < supertypes[i].getDeclaredChildNodeDefs().length; j++) {
            if (!nodeDefsList.contains(supertypes[i].getDeclaredChildNodeDefs()[j])) {
              nodeDefsList.add(supertypes[i].getDeclaredChildNodeDefs()[j]);
            }
          }
        }
      }
    }

    if (nodeDefsList.size() > 0) {
      NodeDef[] childNodeDefs = new NodeDef[nodeDefsList.size()];
      for (int i = 0; i < nodeDefsList.size(); i++) {
        childNodeDefs[i] = (NodeDef) nodeDefsList.get(i);
      }
      return childNodeDefs;
    }
    return new NodeDef[0];
  }

  /**
   * 6.10.5
   * Returns an array containing the child node definitions
   * actually declared in this node type.
   */
  public NodeDef[] getDeclaredChildNodeDefs() {
    return declaredNodeDefs;
  }

  /**
   * Returns <code>true</code> if setting <code>propertyName</code> to
   * <code>value</code> is allowed by this node type. Otherwise returns
   * <code>false</code>.
   *
   * @param propertyName The name of the property
   * @param value        A <code>Value</code> object.
   */
  public boolean canSetProperty(String propertyName, Value value) {
    try {
      checkSetProperty(propertyName, value);
      return true;
    } catch (ConstraintViolationException e) {
      return false;
    }
  }

  /**
   * Returns <code>true</code> Checks if adding a child node called
   * <code>childNodeName</code> is allowed by <i>this</i> node type.
   * <p/>
   *
   * @param childNodeName The name of the child node.
   */
  public boolean canAddChildNode(String childNodeName) {
    try {
      checkAddChildNode(childNodeName);
      return true;
    } catch (ConstraintViolationException e) {
      return false;
    }
  }

  /**
   * Returns <code>true</code> if adding a child node called
   * <code>childNodeName</code> of node type <code>nodeTypeName</code> is
   * allowed by <i>this</i> node type. Otherwise returns <code>false</code>.
   * The same as <code>{@link #canAddChildNode(String childNodeName)}</code>
   * except that the type of the child node is explictly specified.
   *
   * @param childNodeName The name of the child node.
   * @param nodeTypeName  The name of the node type of the child node.
   */
  public boolean canAddChildNode(String childNodeName, String nodeTypeName) {
    try {
      checkAddChildNode(childNodeName, nodeTypeName);
      return true;
    } catch (ConstraintViolationException e) {
      return false;
    }
  }

  /**
   * Returns true if removing <code>itemName</code> is allowed by this node type.
   * Otherwise returns <code>false</code>.
   *
   * @param itemName The name of the child item
   */
  public boolean checkRemoveItem(String itemName) {
    try {
      checkRemove(itemName);
      return true;
    } catch (ConstraintViolationException e) {
      return false;
    }
  }

  public PropertyDef getPropertyDef(String name) {
    int residualNb = 0;
    PropertyDef residualProperty = null;
    for (int i = 0; i < getPropertyDefs().length; i++) {
      PropertyDef prop = getPropertyDefs()[i];
      if (prop.getName() != null) {
        if (prop.getName().equals(name))
          return prop;
      } else {
        // residual def
        residualNb++;
        residualProperty = prop;
      }
    }
    if (residualNb == 1)
      return residualProperty;
    return null;
  }


  public NodeDef getChildNodeDef(String name) {
    NodeDef[] nodeDefs = getChildNodeDefs();
    for (int i = 0; i < nodeDefs.length; i++) {
      NodeDef nodeDef = getChildNodeDefs()[i];
      if (nodeDef.getName() != null) {
        if (nodeDef.getName().equals(name))
          return nodeDef;
      }
    }

    // Residual def
    for (int i = 0; i < nodeDefs.length; i++) {
      NodeDef nodeDef = nodeDefs[i];
      if (nodeDef.getName() == null) {
        return nodeDef;
      }
    }
    return null;
  }

  public String toString() {
    return "NodeType: " + name;
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof NodeTypeImpl))
      return false;
    return name == ((NodeType) obj).getName();
  }


  /**
   * 6.10.5
   * Checks if setting propertyName to value is allowed by
   * this node type. If it is not allowed a
   * ConstraintViolationException is thrown.
   */
  public void checkSetProperty(String propertyName, Value value) throws ConstraintViolationException {
    boolean residualFlag = false;
    for (int i = 0; i < getPropertyDefs().length; i++) {
      PropertyDef prop = getPropertyDefs()[i];
      if (prop.getName() == null) {
        residualFlag = true;
      } else if (prop.getName().equals(propertyName)) {
        if (prop.isReadOnly())
          throw new ConstraintViolationException("Could not set Read-Only Property <" + propertyName + "> !");
        if (prop.getRequiredType() != PropertyType.UNDEFINED
            && value.getType() != prop.getRequiredType())
          throw new ConstraintViolationException("Invalid required property type <" +
              prop.getRequiredType() + "> for Property <" + propertyName + "> !");
        if (!checkValueConstraint(prop.getValueConstraint(), value))
          throw new ConstraintViolationException("Value Constraint <" + prop.getValueConstraint()
              + "> violated for Property <" + propertyName + "> !");
        if (prop.getName().indexOf("[") > -1 && !prop.isMultiple())
          throw new ConstraintViolationException("Multiplicity is not supported for Property <"
              + propertyName + "> !");
        return;
      }
    }
    if (residualFlag)
      return;
    else
      throw new ConstraintViolationException("Property <" + propertyName + "> is not allowed!");

  }

  /**
   * 6.10.5
   * Checks if adding a child node called childNodeName of
   * is allowed by this node type. If it is not allowed or if the
   * node type of the child node cannot be unambiguously
   * determined than a ConstraintViolationException is thrown.
   */
  public void checkAddChildNode(String childNodeName) throws ConstraintViolationException {
    checkAddChildNode(childNodeName, null);
  }

  /**
   * 6.10.5
   * Checks if adding a child node called childNodeName of
   * node type nodeTypeName is allowed by this node type.
   * The same as checkAddChildNode(String childNodeName)
   * except that the type of the child node is explicitly
   * specified. If it is not allowed, a
   * ConstraintViolationException is thrown.
   */
  public void checkAddChildNode(String childNodeName, String nodeTypeName) throws ConstraintViolationException {
    if (getChildNodeDefs() == null)
      throw new ConstraintViolationException("Node <" + childNodeName + "> is not allowed!");

    int residual = 0;
    NodeDef residualDef = null;

    for (int i = 0; i < getChildNodeDefs().length; i++) {
      NodeDef def = getChildNodeDefs()[i];
      if (def.getName() == null) {
        residualDef = def;
        residual++;
      } else if (def.getName().equals(childNodeName)) {
        if (def.isReadOnly()) {
          log.debug("NodeType.checkAddChildNode(): Could not add Read-Only Node <" + childNodeName + "> !");
          throw new ConstraintViolationException("NodeType.checkAddChildNode(): Could not add Read-Only Node <" + childNodeName + "> !");
        }

        if (def.getName().indexOf("[") > -1 && !def.allowSameNameSibs()) {
          log.debug("NodeType.checkAddChildNode(): Multiplicity is not supported for Node <" + childNodeName + "> !");
          throw new ConstraintViolationException("NodeType.checkAddChildNode(): Multiplicity is not supported for Node <" + childNodeName + "> !");
        }

        if (nodeTypeName != null) {
          NodeDef[] childNodeDefs = getChildNodeDefs();
          for (int j = 0; j < childNodeDefs.length; j++) {
            NodeDef childNodeDef = childNodeDefs[j];
            NodeType requiredNodeType = childNodeDef.getDefaultPrimaryType();
            if (nodeTypeName.equals(requiredNodeType.getName())) {
              return;
            }
          }
          log.debug("NodeType.checkAddChildNode(): Incompatible node type <" + nodeTypeName + ">");
          throw new ConstraintViolationException("NodeType.checkAddChildNode(): Incompatible node type <" + nodeTypeName + ">");
        }
      }
    }
    if (residual == 1) {
      if (nodeTypeName != null) {
        NodeType[] requiredNodeTypes = residualDef.getRequiredPrimaryTypes();
        for (int i = 0; i < requiredNodeTypes.length; i++) {
          String residualNodeTypeName = requiredNodeTypes[i].getName();

          NodeType testNodeType;
          try {
              testNodeType = NodeTypeManagerImpl.getInstance().getNodeType(nodeTypeName);
           } catch (NoSuchNodeTypeException e) {
              throw new ConstraintViolationException("No such node: "+nodeTypeName);
           }

          if (isSameOrSubType(requiredNodeTypes[i], testNodeType))
            return;
          log.debug("NodeType.checkAddChildNode(): Incompatible node type for residual(1) <" + nodeTypeName + ">");
          throw new ConstraintViolationException("NodeType.checkAddChildNode(): Incompatible node type for residual <" + nodeTypeName + ">");
        }
      }
    } else if (residual > 1) {
      if (nodeTypeName != null) {
        for (int i = 0; i < getChildNodeDefs().length; i++) {
          NodeDef def = getChildNodeDefs()[i];
          if (def.getName() == null) {
            if (nodeTypeName.equals(def.getDeclaringNodeType().getName()))
              return;
          }
        }
      }
      log.debug("NodeType.checkAddChildNode(): Incompatible node type for residual>0 <" + nodeTypeName + ">");
      throw new ConstraintViolationException("NodeType.checkAddChildNode(): Incompatible node type for residual>0 <" + nodeTypeName + ">");
    } else {
      log.debug("NodeType.checkAddChildNode(): Node <" + childNodeName + "> is not allowed!");
      throw new ConstraintViolationException("NodeType.checkAddChildNode(): Node <" + childNodeName + "> is not allowed!");
    }

  }

  /**
   * 6.10.5
   * Checks if removing itemName is allowed by this node
   * type. If it is not allowed a ConstraintViolationException is thrown.
   */
  public void checkRemove(String childNodeName) throws ConstraintViolationException {

    boolean residualFlag = false;

    for (int i = 0; i < getChildNodeDefs().length; i++) {
      NodeDef def = getChildNodeDefs()[i];
      if (def.getName() == null) {
        residualFlag = true;
      } else if (def.getName().equals(childNodeName)) {
        if (def.isReadOnly())
          throw new ConstraintViolationException("Could not remove Read-Only Node <" + childNodeName + "> !");
        if (def.isMandatory())
          throw new ConstraintViolationException("Could not remove Mandatory Node <" + childNodeName + "> !");
      }
    }
    if (residualFlag)
      return;
    else
      throw new ConstraintViolationException("Node <" + childNodeName + "> is not allowed!");

  }

  protected boolean isBase() {
    return getSupertypes().length == 0;
  }

  protected boolean checkValueConstraint(String constraint, Value value) {
    return true;
  }

  protected void fillSupertypes(ArrayList list, NodeType subtype) {
    if (subtype.getDeclaredSupertypes() != null) {
      for (int i = 0; i < subtype.getDeclaredSupertypes().length; i++) {
        list.add(subtype.getDeclaredSupertypes()[i]);
        fillSupertypes(list, subtype.getDeclaredSupertypes()[i]);
      }
    }

  }

  public static boolean isSameOrSubType(NodeType superType, NodeType subType) {
    if (superType.getName().equals(subType.getName()))
      return true;
    else {

      NodeType[] superTypes = subType.getSupertypes();
/*
      try {
        superTypes = NodeTypeManagerImpl.getInstance().getNodeType(typeName).getSupertypes();
      } catch (NoSuchNodeTypeException e) {
//        log.error("NodeTypeImpl.isSameOrSubTypeName() error " + e);
        return false;
      }
*/
      for (int j = 0; j < superTypes.length; j++) {
        NodeType testSuperType = superTypes[j];
        if (testSuperType.getName().equals(superType.getName()))
          return true;
      }
    }
    return false;
  }

}
